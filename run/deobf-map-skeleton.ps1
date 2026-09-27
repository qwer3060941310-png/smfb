# Emit a rename-map skeleton, straight from javap - for one class, or for a whole package.
#
# Authoring a batch is the slow part of deobfuscation: every entry needs
#   owner <TAB> kind <TAB> old <TAB> descriptor <TAB> semantic name
# and the descriptor must be exact or the symbol-resolved renamer will not match
# (see tools\deobf\RenameAst.java). This script produces that table with the correct
# descriptors and leaves only the last column to be filled in by hand.
#
# Only short (1-2 letter) names are listed by default: those are the obfuscator pattern.
# Use -All to dump every method of the class, e.g. to re-check a finished batch.
#
# -Package is the mode to use when a package is nearly done and what is left is one or two
# methods per class (typically interface implementations such as the command factories or the
# screen listeners): it walks every compiled class under the package and merges their rows into
# ONE map file, instead of one file per class that then has to be concatenated by hand.
#
# Usage:
#   powershell -File run\deobf-map-skeleton.ps1 -Class com.desertstormfront.command.UnitCommander
#   powershell -File run\deobf-map-skeleton.ps1 -Package com.desertstormfront.command -Out run\map-command-remain.tsv
#   powershell -File run\deobf-map-skeleton.ps1 -Class com.foo.Bar -Out run\map-bar.tsv -All
param(
    [string]$Class = '',
    [string]$Package = '',
    [string]$Out = '',
    [switch]$All,
    [switch]$Fields
)
if (($Class -eq '') -eq ($Package -eq '')) {
    throw 'pass exactly one of -Class <fqn> or -Package <fqn>'
}

# Portable: resolve the source tree, the JDK and the build output through the shared bootstrap.
# The previous version assumed the checkout sits directly under the deploy root (Split-Path of the
# source root); with the junction layout (D:\desertstormfront\源代码 -> D:\源代码) that yields D:\
# and javap is looked for at D:\jdk\bin\javap.exe, so the script failed outright. Other scripts were
# migrated to _env.ps1 by the T06 batch; this one was missed.
. (Join-Path $PSScriptRoot '_env.ps1')
$srcRoot = Get-DsfSrcRoot

$javap = Get-DsfTool 'javap'
if (-not $javap) { throw 'missing javap (set DSF_JAVA_HOME or JAVA_HOME)' }

$classesRoot = Get-DsfClassesDir
if (-not (Test-Path $classesRoot)) { throw "not compiled yet: $classesRoot (run run\build.ps1 first)" }

# Build the target list. Nested classes (Foo$Bar) are skipped: they are addressed through their
# owner in the source, and a '$' in the owner name would not match the renamer's dotted FQNs.
$targets = New-Object System.Collections.Generic.List[string]
if ($Class -ne '') {
    $targets.Add($Class)
    if ($Out -eq '') { $Out = 'run\map-' + ($Class.Split('.')[-1]).ToLower() + '-skeleton.tsv' }
} else {
    $pkgDir = Join-Path $classesRoot ($Package.Replace('.', '\'))
    if (-not (Test-Path $pkgDir)) { throw "no compiled classes for package: $Package" }
    Get-ChildItem $pkgDir -Recurse -Filter *.class |
        Where-Object { $_.Name -notmatch '\$' } |
        ForEach-Object {
            $rel = $_.FullName.Substring($classesRoot.Length + 1)
            $targets.Add(($rel.Substring(0, $rel.Length - 6)).Replace('\', '.'))
        }
    if ($Out -eq '') { $Out = 'run\map-' + ($Package.Split('.')[-1]).toLower() + '-skeleton.tsv' }
}
$outPath = Join-Path $srcRoot $Out

$rows = New-Object System.Collections.Generic.List[string]
# HashSet[string] compares ordinal (case-SENSITIVE). A plain @{} hashtable would treat "C" and "c"
# as the same key and silently drop the second one - which matters a lot here, because the
# obfuscator's names differ only by case across the board (a/A, c/C, ...).
$seen = New-Object 'System.Collections.Generic.HashSet[string]'
$skipped = 0

foreach ($cls in $targets) {
    $classFile = Join-Path $classesRoot ($cls.Replace('.', '\') + '.class')
    if (-not (Test-Path $classFile)) {
        $classFile = Join-Path (Join-Path $srcRoot 'build') ($cls.Replace('.', '\') + '.class')
    }
    if (-not (Test-Path $classFile)) { continue }

    $lines = & $javap -p -s $classFile
    $simple = $cls.Split('.')[-1]

    for ($i = 0; $i -lt $lines.Count; $i++) {
        $line = $lines[$i]
        if ($line -notmatch '^\s') { continue }
        if ($line -match '\bclass\b' -or $line -match '\binterface\b' -or $line -match '\benum\b') { continue }
        $paren = $line.IndexOf('(')
        if ($paren -lt 0) {
            # A field line ("private long g;") has no parentheses. -Fields emits those rows with
            # kind F; the renamer classifies any descriptor that does not start with "(" as a field
            # type, so the two modes never collide.
            if (-not $Fields) { continue }
            $raw = $line.Trim()
            # A field declaration always ends with ";" and has no comma or brace. That excludes the
            # javap metadata lines ("descriptor: I", "flags: ACC_PRIVATE") and the wrapped
            # continuation line of a signature that javap decided to split.
            if (-not $raw.EndsWith(';') -or $raw -match '^[A-Za-z_]\w*\s*:' -or $raw -match '[(),{}]') {
                continue
            }
            $head = $raw.TrimEnd(';').Trim()
            if ($head -eq '') { continue }
            $name = (($head -split '\s+')[-1]) -replace '\[\]$', ''
            if ($name -eq $simple) { continue }
            $descriptor = ''
            for ($j = $i + 1; $j -lt [Math]::Min($i + 3, $lines.Count); $j++) {
                if ($lines[$j] -match '^\s*descriptor:\s*(\S+)') { $descriptor = $Matches[1]; break }
            }
            if ($descriptor -eq '') { continue }
            $isShortField = ($name -cmatch '^[A-Za-z]{1,2}$')
            if (-not $All -and -not $isShortField) { $skipped++; continue }
            $key = "$cls|$name|$descriptor"
            if (-not $seen.Add($key)) { continue }
            $rows.Add("$cls`tF`t$name`t$descriptor`t")
            continue
        }
        if ($Fields) { continue }        # -Fields lists fields only
        $head = $line.Substring(0, $paren).Trim()
        if ($head -eq '') { continue }
        $name = ($head -split '\s+')[-1]
        if ($name -eq $simple -or $name -eq '<init>' -or $name -eq '<clinit>') { continue }

        # the descriptor is on the following line and is what makes the entry unambiguous
        $descriptor = ''
        for ($j = $i + 1; $j -lt [Math]::Min($i + 3, $lines.Count); $j++) {
            if ($lines[$j] -match '^\s*descriptor:\s*(\S+)') { $descriptor = $Matches[1]; break }
        }
        if ($descriptor -eq '') { continue }

        $isShort = ($name -cmatch '^[A-Za-z]{1,2}$')
        if (-not $All -and -not $isShort) { $skipped++; continue }

        $key = "$cls|$name|$descriptor"
        if (-not $seen.Add($key)) { continue }                 # javap can repeat a bridge method
        $rows.Add("$cls`tM`t$name`t$descriptor`t")
    }
}

if ($rows.Count -eq 0) {
    throw "no rows produced for $(if ($Class -ne '') { $Class } else { $Package }) (already renamed? use -All to check)"
}

$header = New-Object System.Collections.Generic.List[string]
$header.Add("# Skeleton for $(if ($Class -ne '') { $Class } else { "$Package (package-wide)" }) - fill in the LAST column (semantic name), then:")
$header.Add("#   powershell -File run\deobf-rename.ps1 -Map $Out            # dry-run (also validates the map)")
$header.Add("#   powershell -File run\deobf-rename.ps1 -Map $Out -Apply    # write")
$header.Add("# Descriptors come from javap: do not edit them, the renamer matches on them.")
$header.Add("# A row with a BLANK last column must keep its trailing tab, or it loses a column and is")
$header.Add("# misparsed as a different layout (see 26, pitfall 10).")
if (-not $All) { $header.Add("# $skipped already-semantic method(s) omitted; use -All to include them.") }
$header.Add("# Columns: owner <TAB> kind <TAB> old <TAB> descriptor <TAB> new")

$text = (($header + $rows) -join "`r`n") + "`r`n"
[System.IO.File]::WriteAllText($outPath, $text, (New-Object System.Text.UTF8Encoding($false)))
Write-Output "SKELETON_DONE scope=$(if ($Class -ne '') { $Class } else { $Package }) classes=$($targets.Count) rows=$($rows.Count) out=$outPath"
