/*
 * Symbol-resolved, AST-based method renamer for the deobfuscation batches.
 *
 * Why this exists
 * ---------------
 * run\rename-methods.ps1 and run\rename-unit-noarg.ps1 rewrite source text with regexes.
 * That approach has three structural defects, and together they broke the build once
 * (Unit no-arg batch, 2026-09-18: 100+ errors, had to be reverted):
 *
 *   1. Receiver-blind : only <Class>., this. and unit. receivers were rewritten, so calls
 *                       through other variables (unit2.k(), carrier.C(), ...) kept the old name.
 *   2. Hierarchy-blind: renaming Unit.z() to getProgress() left the abstract declaration
 *                       UnitPosition.z() untouched, so Unit stopped implementing its supertype.
 *   3. Scope-blind    : the "this.<old>()" pattern was applied in EVERY file, so an unrelated
 *                       DateTime.c() could be rewritten even though it is not in the batch.
 *
 * This tool resolves real symbols through javac instead:
 *   - a map entry is matched against the DECLARATION (owner + name + zero args),
 *   - the whole override family (supertype declaration + every override) is renamed together,
 *   - a reference is rewritten only when it resolves to one of those exact elements,
 *   - same-named methods on unrelated classes are never touched.
 *
 * It is DRY-RUN by default and writes nothing unless -apply is given. Always follow an -apply
 * run with run\build.ps1 and run\test.ps1.
 *
 * Usage (JDK 11+ single-file source launch: no build step, no third-party jars):
 *   java --add-modules jdk.compiler tools\deobf\RenameAst.java ^
 *        -src src ^
 *        -map run\map-unit-noarg.tsv ^
 *        -owner com.desertstormfront.game.model.Unit ^
 *        -cp "..\lib\*;..\getdown-client-1.2.jar;..\getdown-runner.jar" ^
 *        [-apply] [-report build\rename-report.tsv]
 *
 * Map file: "oldName<TAB>newName" lines, # comments allowed. A three-column line uses the
 * first column as the owner FQN for that entry, otherwise -owner applies to the whole file.
 *
 * The canonical layout is the five-column one produced by run\deobf-map-skeleton.ps1:
 *
 *     owner <TAB> kind <TAB> old <TAB> descriptor <TAB> new
 *
 * where kind is "M" (method) or "F" (field), and a descriptor pins the overload or the field
 * type. Method descriptors always start with "(" and field descriptors never do, so maps without
 * a kind column are still classified correctly. A row whose last column is empty is a documented
 * "leave this one obfuscated" entry and is skipped (it must keep its trailing tab).
 *
 * Fields (kind F) are rewritten like methods - declaration plus every resolved reference - with
 * two differences that matter:
 *   - there is no override family, so instead a field that HIDES a supertype field is reported;
 *   - a BARE reference (no receiver) would start binding to a local variable or parameter that
 *     happens to carry the new name, so those sites are qualified ("this.x" / "Owner.x") instead.
 */
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.TreeScanner;
import com.sun.source.util.Trees;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class RenameAst {

    private static final class Edit {
        final int start;
        final int end;
        final String replacement;
        final String kind;

        Edit(int start, int end, String replacement, String kind) {
            this.start = start;
            this.end = end;
            this.replacement = replacement;
            this.kind = kind;
        }
    }

    private static final class Entry {
        final String owner;
        final String oldName;
        /**
         * For a method: the JVM descriptor of the parameters, e.g. "(Lcom/x/World;)V"; null means
         * "no arguments". For a field: the JVM type descriptor, e.g. "J" or "Lcom/x/UnitType;";
         * null means "match by name whatever the type".
         */
        final String descriptor;
        final String newName;
        /** "M" for a method, "F" for a field or an enum constant. */
        final String kind;

        Entry(String owner, String oldName, String descriptor, String newName, String kind) {
            this.owner = owner;
            this.oldName = oldName;
            this.descriptor = descriptor;
            this.newName = newName;
            this.kind = kind;
        }

        boolean isField() {
            return "F".equals(kind);
        }
    }

    private Path srcDir;
    private Path reportPath;
    private boolean apply;
    private final List<Entry> entries = new ArrayList<Entry>();

    public static void main(String[] args) throws Exception {
        RenameAst tool = new RenameAst();
        Path mapPath = null;
        String owner = null;
        String classpath = null;

        for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case "-src":
                    tool.srcDir = Paths.get(args[++i]);
                    break;
                case "-map":
                    mapPath = Paths.get(args[++i]);
                    break;
                case "-owner":
                    owner = args[++i];
                    break;
                case "-cp":
                    classpath = args[++i];
                    break;
                case "-report":
                    tool.reportPath = Paths.get(args[++i]);
                    break;
                case "-apply":
                    tool.apply = true;
                    break;
                default:
                    throw new IllegalArgumentException("unknown option: " + args[i]);
            }
        }
        if (tool.srcDir == null || mapPath == null) {
            throw new IllegalArgumentException("required: -src <dir> -map <tsv> [-owner <fqn>] [-cp <cp>]");
        }
        tool.run(mapPath, owner, classpath);
    }

    private void readMap(Path mapPath, String defaultOwner) throws IOException {
        String text = new String(Files.readAllBytes(mapPath), StandardCharsets.UTF_8);
        if (text.startsWith("\uFEFF")) {
            text = text.substring(1);   // tolerate a BOM written by PowerShell 5.1
        }
        int blankRows = 0;
        for (String line : text.split("\r?\n")) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }
            // split(..., -1) keeps trailing empty fields: a row whose last column is blank means
            // "deliberately left obfuscated" (see map-unitcommander-methods.tsv). Java's default
            // split() would drop that column and silently shift the remaining ones.
            String[] parts = line.split("\t", -1);
            if (parts.length < 2) {
                // Editors sometimes turn the separator into spaces; accept both.
                parts = trimmed.split("\\s+");
            }

            String owner = defaultOwner;
            String old;
            String descriptor = null;
            String newName;
            String kind = null;
            if (parts.length >= 5) {
                // members.tsv layout: owner | kind | old | descriptor | new
                owner = fqn(parts[0].trim());
                kind = parts[1].trim().toUpperCase(java.util.Locale.ROOT);
                old = parts[2].trim();
                descriptor = parts[3].trim();
                newName = parts[4].trim();
            } else if (parts.length == 4 && parts[2].trim().startsWith("(")) {
                // owner | old | descriptor | new
                owner = fqn(parts[0].trim());
                old = parts[1].trim();
                descriptor = parts[2].trim();
                newName = parts[3].trim();
            } else if (parts.length == 4) {
                // owner | old | new | (trailing note) - no descriptor column
                owner = fqn(parts[0].trim());
                old = parts[1].trim();
                newName = parts[2].trim();
            } else if (parts.length == 3) {
                if (parts[0].indexOf('/') >= 0 || parts[0].indexOf('.') >= 0) {
                    owner = fqn(parts[0].trim());       // owner | old | new
                    old = parts[1].trim();
                    newName = parts[2].trim();
                } else {
                    old = parts[0].trim();              // old | descriptor | new
                    descriptor = parts[1].trim();
                    newName = parts[2].trim();
                }
            } else {
                old = parts[0].trim();                  // old | new (zero-arg only)
                newName = parts[1].trim();
            }
            if (owner == null) {
                throw new IllegalArgumentException("map needs -owner or an owner column: " + trimmed);
            }
            if (kind == null) {
                // No kind column: a field descriptor never starts with "(" while a method descriptor
                // always does, so the descriptor alone is enough to tell the two apart.
                kind = descriptor != null && !descriptor.startsWith("(") ? "F" : "M";
            }
            if (!"M".equals(kind) && !"F".equals(kind)) {
                throw new IllegalArgumentException("kind must be M or F, got '" + kind + "': " + trimmed);
            }
            // A malformed map must fail loudly. Two rows accidentally joined by a space produce a
            // "name" such as "isImmobile com.x.Y", which used to be written into the source as an
            // invalid identifier and only surfaced as a confusing javac error later.
            if (parts.length > 5) {
                throw new IllegalArgumentException("row has " + parts.length
                        + " columns, expected at most 5 (are two rows on one line?): " + trimmed);
            }
            if (!isIdentifier(old) || (!newName.isEmpty() && !isIdentifier(newName))) {
                throw new IllegalArgumentException("not a valid Java identifier in row: " + trimmed);
            }
            if (old.isEmpty() || newName.isEmpty()) {
                // A blank target name is the documented way to keep an entry obfuscated on
                // purpose (duplicate body / unconfirmed meaning). Never rename to "".
                blankRows++;
                continue;
            }
            entries.add(new Entry(owner, old, descriptor, newName, kind));
        }
        if (blankRows > 0) {
            System.out.println("skipped " + blankRows + " row(s) with a blank target name");
        }
        if (entries.isEmpty()) {
            throw new IllegalArgumentException("no map entries in " + mapPath);
        }
    }

    private void run(Path mapPath, String defaultOwner, String classpath) throws IOException {
        readMap(mapPath, defaultOwner);

        List<Path> sources;
        try (Stream<Path> walk = Files.walk(srcDir)) {
            sources = walk.filter(p -> p.toString().endsWith(".java")).collect(Collectors.toList());
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("no system compiler found: run with a JDK, not a JRE");
        }
        StandardJavaFileManager fileManager =
                compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8);
        List<String> options = new ArrayList<String>();
        options.add("-proc:none");
        options.add("-nowarn");
        options.add("-encoding");
        options.add("UTF-8");
        if (classpath != null && !classpath.isEmpty()) {
            options.add("-classpath");
            options.add(classpath);
        }

        Iterable<? extends JavaFileObject> objects = fileManager.getJavaFileObjectsFromPaths(sources);
        JavacTask task = (JavacTask) compiler.getTask(null, fileManager, null, options, null, objects);

        List<CompilationUnitTree> parsed = new ArrayList<CompilationUnitTree>();
        for (CompilationUnitTree unit : task.parse()) {
            parsed.add(unit);
        }
        task.analyze();

        Trees trees = Trees.instance(task);
        Elements elements = task.getElements();

        // pass 1: every zero-arg method declaration in the project
        DeclarationCollector collector = new DeclarationCollector(trees);
        for (CompilationUnitTree unit : parsed) {
            collector.scan(unit, null);
        }

        Map<ExecutableElement, String> renames =
                resolveRenames(collector.declarations, elements, task.getTypes());
        Map<VariableElement, String> fieldRenames =
                resolveFieldRenames(collector.fields, elements, task.getTypes());
        if (renames.isEmpty() && fieldRenames.isEmpty()) {
            System.out.println("NO TARGETS: nothing matched " + mapPath.getFileName());
            return;
        }

        // pass 2: rewrite every reference that resolves to a renamed element
        Rewriter rewriter = new Rewriter(trees, renames, fieldRenames);
        for (CompilationUnitTree unit : parsed) {
            rewriter.scan(unit, null);
        }

        summarize(mapPath, renames, fieldRenames, rewriter);
    }

    // ------------------------------------------------------------------ map -> symbol resolution

    private Map<ExecutableElement, String> resolveRenames(
            List<ExecutableElement> declarations, Elements elements, Types types) {
        Map<ExecutableElement, String> renames = new LinkedHashMap<ExecutableElement, String>();
        int unmatched = 0;
        for (Entry entry : entries) {
            if (entry.isField()) {
                continue;   // field entries are resolved by resolveFieldRenames
            }
            int hits = 0;
            for (ExecutableElement method : declarations) {
                if (!entry.owner.equals(ownerOf(method))
                        || !method.getSimpleName().contentEquals(entry.oldName)) {
                    continue;
                }
                // A descriptor entry pins the overload; without one only a zero-arg method can match.
                if (entry.descriptor == null) {
                    if (!method.getParameters().isEmpty()) {
                        continue;
                    }
                } else if (!parameterKey(entry.descriptor)
                        .equals(parameterKey(method, types))) {
                    continue;
                }
                renames.put(method, entry.newName);
                hits++;
            }
            if (hits == 0) {
                unmatched++;
                System.out.println("UNMATCHED: " + entry.owner + "." + entry.oldName
                        + (entry.descriptor == null ? "()" : entry.descriptor) + " -> " + entry.newName);
            }
        }
        if (renames.isEmpty()) {
            return renames;
        }

        // Widen to the override family: renaming a subtype override without its supertype
        // declaration leaves the subtype failing to implement the abstract method.
        boolean changed = true;
        while (changed) {
            changed = false;
            for (ExecutableElement method : declarations) {
                if (renames.containsKey(method)) {
                    continue;
                }
                for (Map.Entry<ExecutableElement, String> target : renames.entrySet()) {
                    if (!sameSignature(method, target.getKey(), types)) {
                        continue;
                    }
                    if (overrides(method, target.getKey(), elements)
                            || overrides(target.getKey(), method, elements)) {
                        renames.put(method, target.getValue());
                        changed = true;
                        break;
                    }
                }
            }
        }

        // Report the declarations that came in through the override family rather than the map:
        // these are exactly the supertype/interface declarations the regex tool used to miss.
        for (Map.Entry<ExecutableElement, String> rename : renames.entrySet()) {
            boolean direct = false;
            String owner = ownerOf(rename.getKey());
            for (Entry entry : entries) {
                if (!entry.isField()
                        && entry.owner.equals(owner)
                        && rename.getKey().getSimpleName().contentEquals(entry.oldName)) {
                    direct = true;
                    break;
                }
            }
            if (!direct) {
                System.out.println("  + family: " + owner + "." + rename.getKey().getSimpleName()
                        + " -> " + rename.getValue());
            }
        }

        // Drop entries whose new name collides with an existing member of the same arity:
        // that would silently merge two different methods.
        List<ExecutableElement> collisions = new ArrayList<ExecutableElement>();
        for (Map.Entry<ExecutableElement, String> rename : renames.entrySet()) {
            Element enclosing = rename.getKey().getEnclosingElement();
            if (!(enclosing instanceof TypeElement)) {
                continue;
            }
            for (Element member : elements.getAllMembers((TypeElement) enclosing)) {
                if (member.getKind() == ElementKind.METHOD
                        && member.getSimpleName().contentEquals(rename.getValue())
                        && !member.equals(rename.getKey())
                        && parameterKey((ExecutableElement) member, types)
                                .equals(parameterKey(rename.getKey(), types))) {
                    collisions.add(rename.getKey());
                    break;
                }
            }
        }
        for (ExecutableElement collision : collisions) {
            System.out.println("SKIP (name collision): " + ownerOf(collision) + "."
                    + collision.getSimpleName() + " -> " + renames.get(collision));
            renames.remove(collision);
        }
        return renames;
    }

    // ------------------------------------------------------------------ field resolution

    /**
     * Field counterpart of {@link #resolveRenames}.
     *
     * Fields have no override family, so the widening pass is replaced by two other checks:
     *   - HIDING: a field in a subtype can hide a same-named supertype field; renaming only one of
     *     them would silently change what an inherited reference binds to, so it is reported.
     *   - COLLISION: two members of one type must not end up sharing a field name.
     */
    private Map<VariableElement, String> resolveFieldRenames(
            List<VariableElement> fields, Elements elements, Types types) {
        Map<VariableElement, String> renames = new LinkedHashMap<VariableElement, String>();
        for (Entry entry : entries) {
            if (!entry.isField()) {
                continue;
            }
            int hits = 0;
            for (VariableElement field : fields) {
                if (!entry.owner.equals(ownerOf(field))
                        || !field.getSimpleName().contentEquals(entry.oldName)) {
                    continue;
                }
                // The descriptor pins the type; without one the name alone decides.
                if (entry.descriptor != null
                        && !fieldTypeKey(entry.descriptor).equals(typeKey(field.asType(), types))) {
                    continue;
                }
                renames.put(field, entry.newName);
                hits++;
            }
            if (hits == 0) {
                System.out.println("UNMATCHED: " + entry.owner + "." + entry.oldName
                        + (entry.descriptor == null ? "" : " :" + entry.descriptor)
                        + " -> " + entry.newName);
            }
        }
        if (renames.isEmpty()) {
            return renames;
        }

        // Report fields that hide, or are hidden by, something in a supertype/subtype: the map
        // only names one of the two, so a reference through the other type would keep the old name.
        for (Map.Entry<VariableElement, String> rename : renames.entrySet()) {
            Element enclosing = rename.getKey().getEnclosingElement();
            if (!(enclosing instanceof TypeElement)) {
                continue;
            }
            for (Element member : elements.getAllMembers((TypeElement) enclosing)) {
                if (isFieldLike(member)
                        && member.getSimpleName().contentEquals(rename.getKey().getSimpleName())
                        && !member.equals(rename.getKey())) {
                    System.out.println("  ! hidden field (check the other declaration manually): "
                            + ownerOf((VariableElement) member) + "."
                            + member.getSimpleName());
                }
            }
        }

        List<VariableElement> collisions = new ArrayList<VariableElement>();
        for (Map.Entry<VariableElement, String> rename : renames.entrySet()) {
            Element enclosing = rename.getKey().getEnclosingElement();
            if (!(enclosing instanceof TypeElement)) {
                continue;
            }
            for (Element member : elements.getAllMembers((TypeElement) enclosing)) {
                // Enum constants count here too: renaming one onto an existing constant's name
                // would merge two distinct constants.
                if (isFieldLike(member)
                        && member.getSimpleName().contentEquals(rename.getValue())
                        && !member.equals(rename.getKey())) {
                    collisions.add(rename.getKey());
                    break;
                }
            }
        }
        for (VariableElement collision : collisions) {
            System.out.println("SKIP (name collision): " + ownerOf(collision) + "."
                    + collision.getSimpleName() + " -> " + renames.get(collision));
            renames.remove(collision);
        }
        return renames;
    }

    /**
     * A plain field or an enum constant. Both are variable declarations that a map row of kind "F"
     * may target; locals and parameters are not.
     */
    private static boolean isFieldLike(Element element) {
        return element.getKind() == ElementKind.FIELD
                || element.getKind() == ElementKind.ENUM_CONSTANT;
    }

    /** A field type descriptor ("J", "Ljava/lang/String;", "[[I") in the erased key format. */
    private static String fieldTypeKey(String descriptor) {
        return parameterKey("(" + descriptor + ")V");
    }

    private static String ownerOf(VariableElement field) {
        Element enclosing = field.getEnclosingElement();
        return enclosing instanceof TypeElement
                ? ((TypeElement) enclosing).getQualifiedName().toString()
                : null;
    }

    private static boolean overrides(ExecutableElement sub, ExecutableElement sup, Elements elements) {
        Element enclosing = sub.getEnclosingElement();
        if (!(enclosing instanceof TypeElement)) {
            return false;
        }
        try {
            return elements.overrides(sub, sup, (TypeElement) enclosing);
        } catch (RuntimeException ignored) {
            return false;
        }
    }

    private static boolean sameSignature(ExecutableElement a, ExecutableElement b, Types types) {
        return a.getSimpleName().contentEquals(b.getSimpleName())
                && parameterKey(a, types).equals(parameterKey(b, types));
    }

    // ------------------------------------------------------------------ descriptor / parameter keys

    /** Dotted FQN from either a JVM internal name ("com/a/B") or an already dotted name. */
    private static String fqn(String name) {
        String dotted = name.indexOf('/') >= 0 ? name.replace('/', '.') : name;
        // javap prints nested classes with the JVM binary separator ("Outer$Inner") while javac
        // reports the canonical dotted name ("Outer.Inner"), so a member typed with an inner class
        // failed to match its own descriptor ("UNMATCHED" with no hits, first hit on
        // StreamDataReader$UncheckedDataInputStream). Both sides are normalised here - the source
        // side already arrives dotted - which keeps the comparison exact for every other type.
        return dotted.replace('$', '.');
    }

    /** Java identifier check: a malformed map must never write garbage into the source. */
    private static boolean isIdentifier(String name) {
        if (name.isEmpty() || !Character.isJavaIdentifierStart(name.charAt(0))) {
            return false;
        }
        for (int i = 1; i < name.length(); ++i) {
            if (!Character.isJavaIdentifierPart(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parameters of a JVM descriptor as a comma-joined list of erased type keys, e.g.
     * "(Lcom/x/World;I)V" -> "com.x.World,int". Compared against {@link #parameterKey(ExecutableElement, Types)},
     * so neither side needs a fully formed descriptor.
     */
    private static String parameterKey(String descriptor) {
        StringBuilder sb = new StringBuilder();
        int i = descriptor.indexOf('(');
        if (i < 0) {
            return sb.toString();
        }
        i++;
        boolean first = true;
        while (i < descriptor.length() && descriptor.charAt(i) != ')') {
            int dims = 0;
            while (i < descriptor.length() && descriptor.charAt(i) == '[') {
                dims++;
                i++;
            }
            if (i >= descriptor.length()) {
                break;
            }
            char c = descriptor.charAt(i);
            String base;
            if (c == 'L') {
                int end = descriptor.indexOf(';', i);
                if (end < 0) {
                    break;
                }
                // fqn(), not a bare replace('/','.'): a javap descriptor writes a nested type as
                // "Outer$Inner" while the source side reports "Outer.Inner", and a mismatch makes
                // the field row silently UNMATCHED.
                base = fqn(descriptor.substring(i + 1, end));
                i = end + 1;
            } else {
                base = primitiveName(c);
                i++;
            }
            for (int d = 0; d < dims; ++d) {
                base = base + "[]";
            }
            if (!first) {
                sb.append(',');
            }
            sb.append(base);
            first = false;
        }
        return sb.toString();
    }

    /** Erased parameter types of a resolved method, in the same format as {@link #parameterKey(String)}. */
    private static String parameterKey(ExecutableElement method, Types types) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (VariableElement parameter : method.getParameters()) {
            if (!first) {
                sb.append(',');
            }
            sb.append(typeKey(parameter.asType(), types));
            first = false;
        }
        return sb.toString();
    }

    private static String typeKey(TypeMirror type, Types types) {
        if (type.getKind() == TypeKind.ARRAY) {
            return typeKey(((javax.lang.model.type.ArrayType) type).getComponentType(), types) + "[]";
        }
        if (type.getKind().isPrimitive()) {
            return type.getKind().toString().toLowerCase(java.util.Locale.ROOT);
        }
        if (type.getKind() == TypeKind.DECLARED) {
            Element element = types.asElement(type);
            if (element instanceof TypeElement) {
                return ((TypeElement) element).getQualifiedName().toString();
            }
        }
        return type.toString();
    }

    private static String primitiveName(char c) {
        switch (c) {
            case 'I':
                return "int";
            case 'J':
                return "long";
            case 'S':
                return "short";
            case 'B':
                return "byte";
            case 'C':
                return "char";
            case 'F':
                return "float";
            case 'D':
                return "double";
            case 'Z':
                return "boolean";
            case 'V':
                return "void";
            default:
                return String.valueOf(c);
        }
    }

    private static String ownerOf(ExecutableElement method) {
        Element enclosing = method.getEnclosingElement();
        return enclosing instanceof TypeElement
                ? ((TypeElement) enclosing).getQualifiedName().toString()
                : null;
    }

    // ---------------------------------------------------------------------------- tree scanners

    private static final class DeclarationCollector extends TreePathScanner<Void, Void> {
        private final Trees trees;
        final List<ExecutableElement> declarations = new ArrayList<ExecutableElement>();
        final List<VariableElement> fields = new ArrayList<VariableElement>();

        DeclarationCollector(Trees trees) {
            this.trees = trees;
        }

        @Override
        public Void visitMethod(MethodTree tree, Void unused) {
            Element element = trees.getElement(getCurrentPath());
            if (element instanceof ExecutableElement && element.getKind() == ElementKind.METHOD) {
                declarations.add((ExecutableElement) element);
            }
            return super.visitMethod(tree, unused);
        }

        @Override
        public Void visitVariable(VariableTree tree, Void unused) {
            Element element = trees.getElement(getCurrentPath());
            // ElementKind.FIELD excludes locals and parameters, which have their own kinds.
            // ENUM_CONSTANT is included on purpose: an enum constant is a variable declaration too,
            // and excluding it made whole enums (ui.KeyCode = 31 single letter constants) unreachable
            // for symbol renaming. Constants have no override family and are renamed exactly like
            // fields: declaration plus every resolved reference.
            if (element instanceof VariableElement
                    && (element.getKind() == ElementKind.FIELD
                        || element.getKind() == ElementKind.ENUM_CONSTANT)) {
                fields.add((VariableElement) element);
            }
            return super.visitVariable(tree, unused);
        }
    }

    /** Declared local and parameter names of one method, used only for the shadowing check. */
    /**
     * Collects the variable/parameter names declared inside a method, so a field rename can tell
     * whether the new name would be shadowed at that point.
     *
     * A plain TreeScanner is used on purpose: NameCollector is started from a detached tree (the
     * enclosing MethodTree) rather than from a live visit position, and TreePathScanner.scan(tree, p)
     * builds a new TreePath from the CURRENT path - which is null here and used to throw a
     * NullPointerException for bare ("unqualified") references to the renamed field.
     */
    private static final class NameCollector extends TreeScanner<Void, Void> {
        final Set<String> names = new HashSet<String>();

        @Override
        public Void visitVariable(VariableTree tree, Void unused) {
            names.add(tree.getName().toString());
            return super.visitVariable(tree, unused);
        }
    }

    private static MethodTree enclosingMethod(TreePath path) {
        for (TreePath current = path; current != null; current = current.getParentPath()) {
            if (current.getLeaf() instanceof MethodTree) {
                return (MethodTree) current.getLeaf();
            }
        }
        return null;
    }

    private static final class Rewriter extends TreePathScanner<Void, Void> {
        private final Trees trees;
        private final SourcePositions positions;
        private final Map<ExecutableElement, String> renames;
        private final Map<VariableElement, String> fieldRenames;
        private final Map<CompilationUnitTree, Map<Integer, Edit>> edits =
                new LinkedHashMap<CompilationUnitTree, Map<Integer, Edit>>();
        private final Map<CompilationUnitTree, String> sources =
                new LinkedHashMap<CompilationUnitTree, String>();
        private int declarations;
        private int calls;
        private int references;
        private int skipped;
        private int fieldDeclarations;
        private int fieldRefs;
        private int qualified;

        Rewriter(Trees trees, Map<ExecutableElement, String> renames,
                 Map<VariableElement, String> fieldRenames) {
            this.trees = trees;
            this.positions = trees.getSourcePositions();
            this.renames = renames;
            this.fieldRenames = fieldRenames;
        }

        @Override
        public Void visitMethodInvocation(MethodInvocationTree tree, Void unused) {
            addReference(tree.getMethodSelect());
            return super.visitMethodInvocation(tree, unused);
        }

        @Override
        public Void visitMemberReference(MemberReferenceTree tree, Void unused) {
            addReference(tree);
            return super.visitMemberReference(tree, unused);
        }

        @Override
        public Void visitIdentifier(IdentifierTree tree, Void unused) {
            // An identifier is either a local/field variable or a method used without a receiver,
            // so the element kind decides which renamer claims it.
            Element element = trees.getElement(getCurrentPath());
            if (element instanceof VariableElement) {
                addFieldReference(tree);
            } else {
                addReference(tree);
            }
            return super.visitIdentifier(tree, unused);
        }

        @Override
        public Void visitMemberSelect(MemberSelectTree tree, Void unused) {
            addFieldReference(tree);
            return super.visitMemberSelect(tree, unused);
        }

        @Override
        public Void visitVariable(VariableTree tree, Void unused) {
            Element element = trees.getElement(getCurrentPath());
            if (element instanceof VariableElement) {
                String newName = fieldRenames.get(element);
                if (newName != null) {
                    addFieldDeclaration(tree, element.getSimpleName().toString(), newName);
                }
            }
            return super.visitVariable(tree, unused);
        }

        @Override
        public Void visitMethod(MethodTree tree, Void unused) {
            Element element = trees.getElement(getCurrentPath());
            if (element instanceof ExecutableElement) {
                ExecutableElement method = (ExecutableElement) element;
                String newName = renames.get(method);
                if (newName != null) {
                    addDeclaration(tree, method.getSimpleName().toString(), newName);
                }
            }
            return super.visitMethod(tree, unused);
        }

        private void addReference(Tree tree) {
            Element element = trees.getElement(getCurrentPath());
            if (!(element instanceof ExecutableElement)) {
                return;
            }
            String newName = renames.get(element);
            if (newName == null) {
                return;
            }
            String oldName = element.getSimpleName().toString();
            CompilationUnitTree unit = getCurrentPath().getCompilationUnit();
            long start = positions.getStartPosition(unit, tree);
            long end = positions.getEndPosition(unit, tree);
            if (start < 0 || end < 0) {
                skipped++;
                return;
            }
            int nameEnd;
            int nameStart;
            if (tree instanceof IdentifierTree) {
                nameStart = (int) start;
                nameEnd = (int) end;
            } else {
                // a member select spans "<receiver>.<name>"; the name is the last token
                nameEnd = (int) end;
                nameStart = nameEnd - oldName.length();
            }
            // Safety net: never splice at a guessed offset. If the text under the computed range
            // is not exactly the old name, skip the site instead of corrupting the source.
            String text = source(unit);
            if (text == null || nameStart < 0 || nameEnd > text.length()
                    || !oldName.contentEquals(text.subSequence(nameStart, nameEnd))) {
                skipped++;
                return;
            }
            put(unit, nameStart, nameEnd, newName,
                    tree instanceof MemberReferenceTree ? "reference" : "call");
        }

        /**
         * A field reference: "obj.field", "Owner.field" or a bare "field". Only the NAME token is
         * replaced, so the receiver is untouched.
         */
        private void addFieldReference(Tree tree) {
            Element element = trees.getElement(getCurrentPath());
            if (!(element instanceof VariableElement)) {
                return;
            }
            String newName = fieldRenames.get(element);
            if (newName == null) {
                return;
            }
            String oldName = element.getSimpleName().toString();
            CompilationUnitTree unit = getCurrentPath().getCompilationUnit();
            long start = positions.getStartPosition(unit, tree);
            long end = positions.getEndPosition(unit, tree);
            if (start < 0 || end < 0) {
                skipped++;
                return;
            }
            int nameStart;
            int nameEnd;
            if (tree instanceof IdentifierTree) {
                nameStart = (int) start;
                nameEnd = (int) end;
            } else {
                nameEnd = (int) end;
                nameStart = nameEnd - oldName.length();
            }
            String text = source(unit);
            if (text == null || nameStart < 0 || nameEnd > text.length()
                    || !oldName.contentEquals(text.subSequence(nameStart, nameEnd))) {
                skipped++;
                return;
            }
            // Shadowing check. A qualified reference ("this.x", "obj.x") always keeps its meaning,
            // but a BARE "x" would bind to a local variable or parameter that happens to carry the
            // new name. When one does, the reference is qualified instead of renamed, which is the
            // only rewrite that preserves behaviour.
            String replacement = newName;
            if (tree instanceof IdentifierTree && isShadowed((VariableElement) element, newName)) {
                replacement = qualifier(element) + "." + newName;
                qualified++;
            }
            put(unit, nameStart, nameEnd, replacement, "field");
        }

        /**
         * A field declaration is one VariableTree covering modifiers, type, name and initialiser,
         * so the name cannot be taken from the tree start. It is located as the first occurrence of
         * the old name AFTER the declared type, which also skips a C-style "int a[]" declarator.
         */
        private void addFieldDeclaration(VariableTree tree, String oldName, String newName) {
            CompilationUnitTree unit = getCurrentPath().getCompilationUnit();
            String text = source(unit);
            if (text == null) {
                skipped++;
                return;
            }
            // The anchor is normally the end of the written type, so that a name repeated inside
            // the initialiser is not matched. An enum constant has no written type - javac reports
            // no position for tree.getType() - so anchor on the start of the declaration instead,
            // which is the constant's own name token.
            long anchor = tree.getType() == null
                    ? -1
                    : positions.getEndPosition(unit, tree.getType());
            if (anchor < 0) {
                anchor = positions.getStartPosition(unit, tree);
            }
            if (anchor < 0 || anchor > text.length()) {
                skipped++;
                return;
            }
            Matcher matcher = Pattern
                    .compile("(?<![\\w$.])" + Pattern.quote(oldName) + "(?![\\w$])")
                    .matcher(text);
            if (!matcher.find((int) anchor)) {
                skipped++;
                return;
            }
            int nameStart = matcher.start();
            int nameEnd = matcher.end();
            if (!oldName.contentEquals(text.subSequence(nameStart, nameEnd))) {
                skipped++;
                return;
            }
            put(unit, nameStart, nameEnd, newName, "fieldDeclaration");
        }

        private boolean isShadowed(VariableElement field, String newName) {
            MethodTree method = enclosingMethod(getCurrentPath());
            if (method == null) {
                return false;   // in a field initialiser nothing can shadow it
            }
            NameCollector collector = new NameCollector();
            collector.scan(method, null);
            return collector.names.contains(newName);
        }

        /** "this" for an instance field, the owning type's simple name for a static one. */
        private static String qualifier(Element field) {
            if (field.getModifiers().contains(Modifier.STATIC)) {
                Element owner = field.getEnclosingElement();
                if (owner instanceof TypeElement) {
                    return ((TypeElement) owner).getSimpleName().toString();
                }
            }
            return "this";
        }

        private void addDeclaration(MethodTree tree, String oldName, String newName) {
            CompilationUnitTree unit = getCurrentPath().getCompilationUnit();
            long start = positions.getStartPosition(unit, tree);
            Tree body = tree.getBody();
            long end = body != null
                    ? positions.getStartPosition(unit, body)
                    : positions.getEndPosition(unit, tree);
            String text = source(unit);
            if (start < 0 || end < 0 || text == null || end > text.length()) {
                skipped++;
                return;
            }
            // The name is the last identifier followed by "(" in the declaration header; the
            // parameter list comes after it, so neither a parameter nor a return type matches.
            String header = text.substring((int) start, (int) end);
            Matcher matcher = Pattern
                    .compile("(?<![\\w$.])" + Pattern.quote(oldName) + "(?=\\s*\\()")
                    .matcher(header);
            int offset = -1;
            while (matcher.find()) {
                offset = matcher.start();
            }
            if (offset < 0) {
                skipped++;
                return;
            }
            put(unit, (int) start + offset, (int) start + offset + oldName.length(), newName,
                    "declaration");
        }

        /** Records one edit; returns false when that offset already had one. */
        private boolean put(CompilationUnitTree unit, int start, int end, String replacement,
                String kind) {
            Map<Integer, Edit> perUnit = edits.get(unit);
            if (perUnit == null) {
                perUnit = new TreeMap<Integer, Edit>();
                edits.put(unit, perUnit);
            }
            if (perUnit.containsKey(start)) {
                return false;
            }
            perUnit.put(start, new Edit(start, end, replacement, kind));
            if ("declaration".equals(kind)) {
                declarations++;
            } else if ("reference".equals(kind)) {
                references++;
            } else if ("field".equals(kind)) {
                fieldRefs++;
            } else if ("fieldDeclaration".equals(kind)) {
                fieldDeclarations++;
            } else {
                calls++;
            }
            return true;
        }

        private String source(CompilationUnitTree unit) {
            if (sources.containsKey(unit)) {
                return sources.get(unit);
            }
            String text = null;
            try {
                text = new String(Files.readAllBytes(Paths.get(unit.getSourceFile().toUri())),
                        StandardCharsets.UTF_8);
            } catch (IOException | RuntimeException ignored) {
                // reported as skipped
            }
            sources.put(unit, text);
            return text;
        }

        int filesChanged() {
            return edits.size();
        }

        int totalEdits() {
            return declarations + calls + references + fieldDeclarations + fieldRefs;
        }
    }

    // --------------------------------------------------------------------------------- output

    private void summarize(Path mapPath, Map<ExecutableElement, String> renames,
            Map<VariableElement, String> fieldRenames, Rewriter rewriter)
            throws IOException {
        Path srcRoot = srcDir.toAbsolutePath().normalize();
        Map<String, Integer> perFile = new LinkedHashMap<String, Integer>();
        for (Map.Entry<CompilationUnitTree, Map<Integer, Edit>> entry : rewriter.edits.entrySet()) {
            String path = Paths.get(entry.getKey().getSourceFile().toUri()).toAbsolutePath().normalize()
                    .toString();
            perFile.put(path, entry.getValue().size());
        }

        System.out.println("map                : " + mapPath + " (" + entries.size() + " entries)");
        System.out.println("renamed methods    : " + renames.size()
                + " (declarations incl. override family, after collision check)");
        if (!fieldRenames.isEmpty()) {
            System.out.println("renamed fields     : " + fieldRenames.size() + " after collision check");
        }
        System.out.println("edits              : " + rewriter.totalEdits()
                + " (method declarations " + rewriter.declarations
                + ", calls " + rewriter.calls
                + ", method refs " + rewriter.references
                + "; field declarations " + rewriter.fieldDeclarations
                + ", field refs " + rewriter.fieldRefs + ")");
        if (rewriter.qualified > 0) {
            System.out.println("qualified          : " + rewriter.qualified
                    + " bare reference(s) would have been shadowed by a local, so they were "
                    + "rewritten as this.x / Owner.x instead");
        }
        System.out.println("files touched      : " + rewriter.filesChanged());
        if (rewriter.skipped > 0) {
            System.out.println("skipped (no position): " + rewriter.skipped);
        }

        List<Map.Entry<String, Integer>> top = new ArrayList<Map.Entry<String, Integer>>(
                perFile.entrySet());
        top.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        for (int i = 0; i < Math.min(15, top.size()); ++i) {
            String relative;
            try {
                relative = srcRoot.relativize(Paths.get(top.get(i).getKey())).toString();
            } catch (RuntimeException e) {
                relative = top.get(i).getKey();
            }
            System.out.println("   " + top.get(i).getValue() + "\t" + relative);
        }

        if (reportPath != null) {
            Files.createDirectories(reportPath.toAbsolutePath().getParent());
            try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(reportPath,
                    StandardCharsets.UTF_8))) {
                out.println("file\toffset\tkind\told\tnew");
                for (Map.Entry<CompilationUnitTree, Map<Integer, Edit>> entry
                        : rewriter.edits.entrySet()) {
                    String path = Paths.get(entry.getKey().getSourceFile().toUri())
                            .toAbsolutePath().normalize().toString();
                    String text = new String(Files.readAllBytes(Paths.get(path)),
                            StandardCharsets.UTF_8);
                    for (Edit edit : entry.getValue().values()) {
                        out.println(path + "\t" + edit.start + "\t" + edit.kind + "\t"
                                + text.substring(edit.start, edit.end) + "\t" + edit.replacement);
                    }
                }
            }
            System.out.println("report             : " + reportPath);
        }

        if (!apply) {
            System.out.println("DRY-RUN: nothing written. Re-run with -apply, then run\\build.ps1 "
                    + "and run\\test.ps1");
            return;
        }

        int written = 0;
        for (Map.Entry<CompilationUnitTree, Map<Integer, Edit>> entry : rewriter.edits.entrySet()) {
            Path path = Paths.get(entry.getKey().getSourceFile().toUri()).toAbsolutePath().normalize();
            String text = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            List<Edit> ordered = new ArrayList<Edit>(entry.getValue().values());
            ordered.sort(Comparator.comparingInt((Edit e) -> e.start).reversed());
            for (Edit edit : ordered) {
                text = text.substring(0, edit.start) + edit.replacement + text.substring(edit.end);
            }
            Files.write(path, text.getBytes(StandardCharsets.UTF_8));
            written++;
        }
        System.out.println("APPLIED to " + written + " files. Run run\\build.ps1 then run\\test.ps1.");
    }
}
