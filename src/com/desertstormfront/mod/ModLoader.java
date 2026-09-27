/*
 * Mod loader: data-driven unit overrides (mod/units.json) and new units (mod/new_units.json).
 *
 * Design notes:
 *  - Uses reflection for overrides and the public constructor for new units, so existing
 *    game logic stays untouched.
 *  - Missing/invalid mod files are a strict no-op (built-in behaviour is preserved).
 *  - Deliberately does NOT use OsfLog: this runs from MapDefinition's static initializer,
 *    i.e. before the libGDX application exists (Gdx.app would be null and crash startup).
 *  - JSON keys may be written semantically ("health", "speed", "damage", ...). The confirmed set
 *    is the ALIASES table below; evidence is in 17_UnitType字段与方法语义映射.md. The legacy
 *    single-letter keys keep working, so existing mod files load unchanged - the legacy key is
 *    an explicit column of ALIASES, not an assumption that the field is still called "g".
 *  - New units are appended with id == list.size() so that id and index stay aligned for the
 *    cost matrix, which is then rebuilt and re-applied to every unit.
 *  - All JSON reads go through type-safe helpers: a value of an unexpected type falls back to
 *    the default and is reported, instead of throwing and breaking startup.
 */
package com.desertstormfront.mod;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.desertstormfront.game.model.AmmoType;
import com.desertstormfront.game.model.Domain;
import com.desertstormfront.game.model.Layer;
import com.desertstormfront.game.model.UnitType;
import com.desertstormfront.game.model.UnitTypeList;
import com.desertstormfront.map.MapDefinition;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class ModLoader {

    private static final String MOD_FILE = "mod" + File.separator + "units.json";
    private static final String NEW_UNITS_FILE = "mod" + File.separator + "new_units.json";
    private static final String RULESET_FILE = "mod" + File.separator + "ruleset.json";

    /** UnitType field that holds the health, written directly for the "health" JSON key. */
    private static final String FIELD_HEALTH = "health";

    /**
     * JSON key (semantic or legacy) -> the UnitType field it stands for.
     *
     * Columns: {semantic key, Java field name, legacy JSON key}.
     *
     * The UnitType fields are now semantic themselves (batch "UnitType fields"), so the middle
     * column repeats the first for the confirmed set. It stays explicit on purpose: that is the
     * exact name reflection needs, and the third column is what keeps mod files written with the
     * old single letters ("g" = health, "m" = speed) loading unchanged.
     *
     * Only fields whose meaning was confirmed in 17_UnitType字段与方法语义映射.md are listed:
     * a guessed alias would silently write the wrong field, which is worse than no alias at all.
     *
     * Matching is case-SENSITIVE throughout. "D" (sight range) and "d" (name) are different
     * fields, and libGDX JsonValue.get() is case-insensitive, so legacy candidates go through
     * exact() and the upper-case constructor keys carry a trailing underscore ("D_", "H_").
     */
    private static final String[][] ALIASES = {
        {"name", "name", "d"},
        {"key", "key", "e"},
        {"code", "code", "f"},
        {"health", "health", "g"},
        {"building", "building", "h"},
        {"layer", "layer", "i"},
        {"domain", "domain", "j"},
        {"size", "size", "k"},
        {"moveFactor", "moveFactor", "l"},
        {"speed", "speed", "m"},        // field m is the speed, NOT l (see doc 17)
        {"flyHeight", "flyHeight", "n"},
        {"ammo", "ammo", "r"},
        {"damage", "damage", "s"},
        {"range", "range", "t"},
        {"sightRange", "sightRange", "D,D_"},
        {"capacity", "capacity", "H,H_"},
        // 2026-09-26 batch: UnitType fields renamed per 17_UnitType字段与方法语义映射.md.
        // The legacy single-letter JSON keys (and the upper-case trailing-underscore spellings used
        // by buildUnit) keep resolving to the new field names, so existing mod files load unchanged.
        {"allUnitTypes", "allUnitTypes", "a"},
        {"damageMatrix", "damageMatrix", "b"},
        {"id", "id", "c"},
        {"verticalOffset", "verticalOffset", "o"},
        {"requiresFacingTarget", "requiresFacingTarget", "q"},
        {"salvo", "salvo", "u"},
        {"reloadTime", "reloadTime", "v"},
        {"isGeneral", "isGeneral", "w"},
        {"canCapture", "canCapture", "x"},
        {"capturable", "capturable", "y"},
        {"canRepair", "canRepair", "z"},
        {"rangeInTiles", "rangeInTiles", "C,C_"},
        {"producer", "producer", "E"},
        {"producedBy", "producedBy", "F"},
        {"canProduce", "canProduce", "G"},
        {"canCarryAircraft", "canCarryAircraft", "I,I_"},
        {"sortWeight", "sortWeight", "J"}
    };

    /**
     * Resolves a JSON key to the UnitType field reflection has to write. Every spelling resolves:
     * the semantic one and each legacy one, which matters for the "fields" override block where
     * the key comes straight from the mod file. Unknown keys are returned unchanged.
     */
    static String fieldName(String key) {
        for (String[] alias : ALIASES) {
            if (alias[0].equals(key) || isLegacyKey(alias, key)) {
                return alias[1];
            }
        }
        return key;
    }

    /**
     * The upper-case fields have TWO legacy spellings: the field name as it was before the rename
     * ("D" in a "fields" override block) and the constructor key with the trailing underscore
     * ("D_"), which exists because libGDX JsonValue.get() is case-insensitive and "D" would
     * otherwise read the "d" (name) entry. Both must keep working, hence the comma-separated list.
     */
    private static boolean isLegacyKey(String[] alias, String key) {
        if (alias[2] == null) {
            return false;
        }
        for (String legacy : alias[2].split(",")) {
            if (legacy.trim().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Semantic-first lookup: the requested key wins (case-insensitively, as before), then the
     * legacy spelling for it. Unknown keys behave exactly like {@code parent.get(key)}.
     */
    private static JsonValue get(JsonValue parent, String key) {
        JsonValue value = parent.get(key);
        if (value != null) {
            return value;
        }
        for (String[] alias : ALIASES) {
            if (!alias[0].equals(key)) {
                continue;
            }
            String candidates = alias[2] != null ? alias[2] : alias[1];
            for (String legacy : candidates.split(",")) {
                JsonValue found = exact(parent, legacy.trim());
                if (found != null) {
                    return found;
                }
            }
            return null;
        }
        return null;
    }

    /**
     * Strict lookup used for the legacy candidate keys only: the stored key must match exactly.
     * Being case-insensitive here would let "D" silently read the "d" (name) entry.
     */
    private static JsonValue exact(JsonValue parent, String name) {
        JsonValue value = parent.get(name);
        if (value == null) {
            return null;
        }
        if (value.name == null || !value.name.equals(name)) {
            log("ignoring '" + value.name + "' while looking for the key '" + name + "'");
            return null;
        }
        return value;
    }

    /** Field names holding the unit key, tried in order (survives member renaming). */
    private static final String[] KEY_FIELD_CANDIDATES = {"e", "key"};

    /** Rulesets already processed for new units, so reloads stay idempotent. */
    private static final java.util.Set<UnitTypeList> ENHANCED = new java.util.HashSet<UnitTypeList>();

    /** Built-in ruleset ids, used when re-applying mods on reload. */
    private static final String[] BUILTIN_RULESETS = {"tsf", "dsf"};

    private static JsonValue cached = null;
    private static boolean loaded = false;
    private static JsonValue cachedNew = null;
    private static boolean loadedNew = false;

    private ModLoader() {
    }

    /** Entry point: called once per built-in ruleset, right after it is registered. */
    public static void apply(MapDefinition definition) {
        if (isDisabled()) {
            return;
        }
        try {
            UnitTypeList list = definition.getUnitTypes();
            if (list == null) {
                return;
            }
            addNewUnits(definition, list);
            applyOverrides(list);
        } catch (Throwable t) {
            log("failed to apply mods: " + t);
        }
    }

    private static final String PLUGINS_FILE = "mod" + File.separator + "plugins.txt";

    static {
        // v6: external listeners must be registered before any event is posted.
        if (!isDisabled()) {
            loadPlugins();
        }
    }

    /**
     * Global kill-switch: when mod/DISABLED exists, no mod processing happens at all.
     * Useful to tell mod-caused problems apart from baseline problems.
     */
    private static boolean isDisabled() {
        try {
            return new java.io.File("mod" + File.separator + "DISABLED").exists();
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * v6 hook mechanism: instantiate ModEventListener classes listed in mod/plugins.txt.
     * (A real script engine is unavailable: JDK 17 removed Nashorn and this build is offline.)
     */
    private static void loadPlugins() {
        try {
            java.io.File file = new java.io.File(PLUGINS_FILE);
            if (!file.exists()) {
                return;
            }
            for (String line : java.nio.file.Files.readAllLines(file.toPath())) {
                String name = line.trim();
                if (name.isEmpty() || name.startsWith("#")) {
                    continue;
                }
                try {
                    Object instance = Class.forName(name).getDeclaredConstructor().newInstance();
                    if (instance instanceof ModEventListener) {
                        ModEvents.subscribe((ModEventListener) instance);
                        log("plugin loaded: " + name);
                    } else {
                        log("plugin ignored (not a ModEventListener): " + name);
                    }
                } catch (Throwable t) {
                    log("plugin failed: " + name + " -> " + t);
                }
            }
        } catch (Throwable t) {
            log("cannot read plugins: " + t);
        }
    }

    // ------------------------------------------------------------------ v3: new units

    private static void addNewUnits(MapDefinition definition, UnitTypeList list) {
        // Guard: new units are appended, so applying twice would duplicate them (e.g. on reload).
        if (!ENHANCED.add(list)) {
            return;
        }
        JsonValue root = loadNewUnits();
        if (root == null) {
            return;
        }
        JsonValue units = root.get("units");
        if (units == null) {
            return;
        }
        int added = 0;
        for (JsonValue u = units.child; u != null; u = u.next) {
            try {
                // id == current size keeps id and list index aligned for the cost matrix
                int id = list.size();
                UnitType unitType = buildUnit(u, id);
                list.add(unitType);
                ++added;
                log("added unit id=" + id + " key=" + keyOf(unitType));
            } catch (Throwable t) {
                log("failed to add unit: " + t);
            }
        }
        if (added > 0) {
            rebuildCostMatrix(definition, list);
            ModEvents.post(ModEvents.UNITS_ADDED, Integer.valueOf(added));
        }
    }

    /**
     * The cost matrix is sized by the original unit count; appending units requires a larger
     * matrix, otherwise lookups by unit id would go out of bounds.
     */
    private static void rebuildCostMatrix(MapDefinition definition, UnitTypeList list) {
        try {
            int[][] old = definition.getCostMatrix();
            int oldSize = old == null ? 0 : old.length;
            int size = list.size();
            int[][] matrix = new int[size][size];
            if (old != null) {
                for (int i = 0; i < old.length && i < size; ++i) {
                    if (old[i] == null) {
                        continue;
                    }
                    for (int j = 0; j < old[i].length && j < size; ++j) {
                        matrix[i][j] = old[i][j];
                    }
                }
            }
            for (int i = 0; i < size; ++i) {
                Object entry = list.get(i);
                if (entry instanceof UnitType) {
                    ((UnitType) entry).initialize(list, matrix);
                }
            }
            log("cost matrix rebuilt: " + oldSize + " -> " + size);
            ModEvents.post(ModEvents.MATRIX_REBUILT, Integer.valueOf(size));
        } catch (Throwable t) {
            log("failed to rebuild cost matrix: " + t);
        }
    }

    /**
     * Build a UnitType from a JSON unit definition (shared by v3 new units and v4 rulesets).
     * Package-private so ModFieldAliasTest can assert the semantic keys against the legacy ones.
     */
    static UnitType buildUnit(JsonValue u, int id) {
        // Confirmed fields are read by their semantic name (the legacy letter still works);
        // fields whose meaning is not established yet are left on their legacy key on purpose.
        String name = str(u, "name", "Custom" + id + "[i18n]: Custom " + id);
        String key = str(u, "key", "CUSTOM_" + id);
        return new UnitType(
                id,
                name,
                key,
                charOf(u),
                lng(u, "health", 100L),
                bool(u, "building", false),
                layerOf(u),
                domainOf(u),
                flt(u, "size", 0.3),
                flt(u, "moveFactor", 0.17),
                flt(u, "speed", 0.2),
                flt(u, "flyHeight", 0.0),
                bool(u, "o", false),
                bool(u, "p", false),
                ammoOf(u),
                flt(u, "damage", 0.2),
                flt(u, "range", 0.3),
                i32(u, "u", 0),
                flt(u, "v", 1.2),
                bool(u, "w", false),
                bool(u, "x", false),
                bool(u, "y", false),
                bool(u, "z", false),
                // Upper-case fields carry a trailing underscore: libGDX JsonValue lookup is
                // case-insensitive and would otherwise collide with d/h/i/...
                bool(u, "A_", false),
                i32(u, "B_", 1),
                i32(u, "C_", 2),
                flt(u, "sightRange", 4.0),
                new UnitTypeList(),
                new UnitTypeList(),
                i32(u, "capacity", 0),
                bool(u, "I_", false));
    }

    /** v4: build and register a custom ruleset declared in mod/ruleset.json. */
    public static void registerCustomRulesets() {
        if (isDisabled()) {
            return;
        }
        try {
            JsonValue root = read(RULESET_FILE);
            if (root == null) {
                return;
            }
            String name = root.getString("name", "mod");
            MapDefinition delegate = MapDefinition.getRuleset("tsf");
            if (delegate == null) {
                log("cannot create custom ruleset: built-in 'tsf' ruleset not found");
                return;
            }
            List<UnitType> units = new ArrayList<UnitType>();
            JsonValue array = root.get("units");
            if (array != null) {
                int id = 0;
                for (JsonValue u = array.child; u != null; u = u.next) {
                    units.add(buildUnit(u, id));
                    ++id;
                }
            }
            if (units.isEmpty()) {
                log("custom ruleset '" + name + "' declares no units, skipping");
                return;
            }
            ModRuleset ruleset = new ModRuleset(delegate, root, name, units);
            MapDefinition.register(ruleset);
            log("registered custom ruleset '" + name + "' with " + units.size() + " units");
            ModEvents.post(ModEvents.RULESET_REGISTERED, name);
        } catch (Throwable t) {
            log("failed to register custom ruleset: " + t);
        }
    }

    // ------------------------------------------------------------- v1/v2: overrides

    private static void applyOverrides(UnitTypeList list) throws Exception {
        JsonValue root = load();
        if (root == null) {
            return;
        }
        for (int i = 0; i < list.size(); ++i) {
            Object entry = list.get(i);
            if (!(entry instanceof UnitType)) {
                continue;
            }
            UnitType unitType = (UnitType) entry;
            // Read the key by field, not by the getter: method renaming (see members.tsv)
            // would otherwise break this call.
            String key = keyOf(unitType);
            if (key == null) {
                continue;
            }
            JsonValue override = root.get(key);
            if (override == null) {
                continue;
            }
            applyTo(unitType, key, override);
        }
        ModEvents.post(ModEvents.OVERRIDES_APPLIED, list);
    }

    private static void applyTo(UnitType unitType, String key, JsonValue override) throws Exception {
        if (override.has("health")) {
            set(unitType, FIELD_HEALTH, override.get("health"));
            log(key + " health=" + override.get("health").asLong());
        }
        JsonValue fields = override.get("fields");
        if (fields != null) {
            for (JsonValue child = fields.child; child != null; child = child.next) {
                // Semantic keys are accepted here too: {"fields": {"speed": 0.5}} == {"fields": {"m": 0.5}}
                set(unitType, fieldName(child.name()), child);
                log(key + " " + child.name() + "=" + child.asString());
            }
        }
    }

    /** Unit key, resolved by field so it keeps working before and after member renaming. */
    private static String keyOf(UnitType unitType) {
        for (String name : KEY_FIELD_CANDIDATES) {
            try {
                Field field = UnitType.class.getDeclaredField(name);
                field.setAccessible(true);
                Object value = field.get(unitType);
                if (value != null) {
                    return value.toString();
                }
            } catch (Throwable ignored) {
                // try the next candidate
            }
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void set(UnitType unitType, String fieldName, JsonValue value) throws Exception {
        Field field = UnitType.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        Class<?> type = field.getType();
        Object converted;
        if (type == long.class) {
            converted = Long.valueOf(value.asLong());
        } else if (type == float.class) {
            converted = Float.valueOf((float) value.asDouble());
        } else if (type == int.class) {
            converted = Integer.valueOf(value.asInt());
        } else if (type == boolean.class) {
            converted = Boolean.valueOf(value.asBoolean());
        } else if (type == String.class) {
            converted = value.asString();
        } else if (type.isEnum()) {
            converted = Enum.valueOf((Class<Enum>) type, value.asString());
        } else {
            log("unsupported field type for '" + fieldName + "': " + type.getName());
            return;
        }
        field.set(unitType, converted);
    }

    /**
     * v7: re-read all mod files and re-apply them without restarting the game.
     * New units are guarded by ENHANCED, so repeated reloads never duplicate units.
     */
    public static void reload() {
        try {
            loaded = false;
            cached = null;
            loadedNew = false;
            cachedNew = null;
            for (String id : BUILTIN_RULESETS) {
                MapDefinition definition = MapDefinition.getRuleset(id);
                if (definition != null) {
                    apply(definition);
                }
            }
            registerCustomRulesets();
            log("reloaded mods");
            ModEvents.post(ModEvents.MOD_RELOADED, null);
        } catch (Throwable t) {
            log("reload failed: " + t);
        }
    }

    // --------------------------------------------------------- type-safe JSON reads

    private static String str(JsonValue parent, String key, String fallback) {
        JsonValue v = get(parent, key);
        if (v == null) {
            return fallback;
        }
        if (!v.isString()) {
            log("field '" + key + "' is not a string (" + v.asString() + "), using default");
            return fallback;
        }
        return v.asString();
    }

    private static long lng(JsonValue parent, String key, long fallback) {
        JsonValue v = get(parent, key);
        if (v == null) {
            return fallback;
        }
        if (!v.isNumber()) {
            log("field '" + key + "' is not a number (" + v.asString() + "), using default " + fallback);
            return fallback;
        }
        return v.asLong();
    }

    private static float flt(JsonValue parent, String key, double fallback) {
        JsonValue v = get(parent, key);
        if (v == null) {
            return (float) fallback;
        }
        if (!v.isNumber()) {
            log("field '" + key + "' is not a number (" + v.asString() + "), using default " + fallback);
            return (float) fallback;
        }
        return (float) v.asDouble();
    }

    private static int i32(JsonValue parent, String key, int fallback) {
        return (int) lng(parent, key, fallback);
    }

    private static boolean bool(JsonValue parent, String key, boolean fallback) {
        JsonValue v = get(parent, key);
        if (v == null) {
            return fallback;
        }
        if (v.isBoolean()) {
            return v.asBoolean();
        }
        if (v.isNumber()) {
            return v.asInt() != 0;
        }
        if (v.isString()) {
            return Boolean.parseBoolean(v.asString());
        }
        return fallback;
    }

    private static char charOf(JsonValue u) {
        String value = str(u, "code", "Z");
        return value.isEmpty() ? 'Z' : value.charAt(0);
    }

    private static Layer layerOf(JsonValue u) {
        try {
            return Layer.valueOf(str(u, "layer", "Base"));
        } catch (Throwable t) {
            log("unknown Layer '" + str(u, "layer", "?") + "', using Base");
            return Layer.Base;
        }
    }

    private static Domain domainOf(JsonValue u) {
        try {
            return Domain.valueOf(str(u, "domain", "Ground"));
        } catch (Throwable t) {
            log("unknown Domain '" + str(u, "domain", "?") + "', using Ground");
            return Domain.Ground;
        }
    }

    private static AmmoType ammoOf(JsonValue u) {
        JsonValue v = get(u, "ammo");
        if (v == null || !v.isString()) {
            return null;
        }
        try {
            return AmmoType.valueOf(v.asString());
        } catch (Throwable t) {
            log("unknown AmmoType '" + v.asString() + "', using null");
            return null;
        }
    }

    // --------------------------------------------------------------------- loading

    private static JsonValue load() {
        if (loaded) {
            return cached;
        }
        loaded = true;
        cached = read(MOD_FILE);
        return cached;
    }

    private static JsonValue loadNewUnits() {
        if (loadedNew) {
            return cachedNew;
        }
        loadedNew = true;
        cachedNew = read(NEW_UNITS_FILE);
        return cachedNew;
    }

    private static JsonValue read(String relativePath) {
        try {
            File file = new File(relativePath);
            if (!file.exists()) {
                log("no mod file at " + file.getAbsolutePath() + " (using built-in values)");
                return null;
            }
            FileReader reader = new FileReader(file);
            try {
                JsonValue parsed = new JsonReader().parse(reader);
                log("loaded " + file.getAbsolutePath());
                return parsed;
            } finally {
                reader.close();
            }
        } catch (Throwable t) {
            log("cannot read " + relativePath + ": " + t);
            return null;
        }
    }

    private static void log(String message) {
        System.out.println("[ModLoader] " + message);
    }
}
