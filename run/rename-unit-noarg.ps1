# Rename NO-ARG methods of a class safely across the source tree.
#
# Safety model:
#   * Only empty-paren methods: lookahead (?=\s*\(\s*\)) so a(int) is never
#     touched when renaming a().
#   * Declarations + all no-arg calls inside the owning class are renamed with
#     \bOLDNAME\b(?=\s*\(\s*\))  -> also catches cast / method-return receivers
#     like ((Unit)x).G() or this.d.c().G().
#   * Cross-file call sites renamed only when the receiver is Unit. / this. /
#     unit.  -> same-named single-letter methods on OTHER obfuscated classes
#     (e.g. unitType.k()) are never touched.
#
# Usage: powershell -File run\rename-unit-noarg.ps1 -Class Unit -MapFile run\map-unit-noarg.tsv
param(
    [Parameter(Mandatory = $true)][string]$Class,
    [Parameter(Mandatory = $true)][string]$MapFile
)

# Portable: derive the deploy root from this script's location (run\ -> <src>\run; its parent holds lib/, jdk/, data/).
# Override with the DSF_ROOT environment variable when the layout differs.
. (Join-Path $PSScriptRoot '_env.ps1')
$srcRoot = Get-DsfSrcRoot
$root    = if ($env:DSF_ROOT) { $env:DSF_ROOT } else { (Get-DsfRoots)[0] }
$src = Join-Path $srcRoot 'src'
$mapPath = Join-Path $srcRoot $MapFile
if (-not (Test-Path $mapPath)) { throw "missing map file: $mapPath" }

$map = New-Object 'System.Collections.Generic.Dictionary[string,string]'
foreach ($line in Get-Content $mapPath) {
    if ($line -match '^\s*#' -or $line.Trim() -eq '') { continue }
    $parts = $line -split "`t"
    if ($parts.Count -ge 2) {
        $old = $parts[0].Trim()
        if ($map.ContainsKey($old)) { Write-Host "WARNING dup $old"; continue }
        $map[$old] = $parts[1].Trim()
    }
}
if ($map.Count -eq 0) { throw "no entries in $mapPath" }

$enc = New-Object System.Text.UTF8Encoding($false)
$files = Get-ChildItem -Path $src -Recurse -Filter '*.java'
$changedFiles = 0

foreach ($f in $files) {
    $text = [System.IO.File]::ReadAllText($f.FullName)
    $orig = $text
    $isOwner = ($f.Name -eq "$Class.java")
    foreach ($k in $map.Keys) {
        $new = $map[$k]
        # cross-file class-qualified + this. + unit. receivers (safe)
        $text = [regex]::Replace($text, [regex]::Escape($Class) + '\.' + [regex]::Escape($k) + '(?=\s*\(\s*\))', "$Class.$new")
        $text = [regex]::Replace($text, '(?<![\w])(this|unit)\.' + [regex]::Escape($k) + '(?=\s*\(\s*\))', '$1.' + $new)
        # owning class: also catch cast / method-return receivers (e.g. ((Unit)x).G())
        if ($isOwner) {
            $text = [regex]::Replace($text, '\b' + [regex]::Escape($k) + '\b(?=\s*\(\s*\))', $new)
        }
    }
    if ($text -ne $orig) {
        [System.IO.File]::WriteAllText($f.FullName, $text, $enc)
        $changedFiles++
    }
}

Write-Host "RENAME_DONE class=$Class entries=$($map.Count) files=$changedFiles"
Write-Host 'next: run\build.ps1 then run\test.ps1'
