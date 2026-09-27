# Rename no-argument static/instance methods across the whole source tree (ASCII only).
#
# Scope (deliberately narrow so it stays safe):
#   1. qualified calls:      <Class>.<old>()  ->  <Class>.<new>()
#   2. declarations in <Class>.java:  <modifiers> <type> <old>() {  ->  ... <new>() {
#
# Only zero-argument methods are handled, which removes overload ambiguity: a call
# written as <Class>.x() can only bind to the no-arg overload x(). Overloaded
# setters (x(int) / x(float) / x(boolean) / ...) are left untouched on purpose.
#
# Encoding is written without BOM (Set-Content -Encoding UTF8 would add one and can
# upset javac on files that did not have it).
#
# Usage: powershell -File run\rename-methods.ps1 -Class UserConfig -MapFile run\map-usercfg-getters.tsv
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

# Case sensitive on purpose: obfuscators use both 'a' and 'A' as distinct method
# names, and a PowerShell hashtable is case INsensitive (a/A would collide).
$map = New-Object 'System.Collections.Generic.Dictionary[string,string]'
foreach ($line in Get-Content $mapPath) {
    if ($line -match '^\s*#' -or $line.Trim() -eq '') { continue }
    $parts = $line -split "`t"
    if ($parts.Count -ge 2) {
        $old = $parts[0].Trim()
        if ($map.ContainsKey($old)) {
            Write-Host "WARNING duplicate mapping for '$old' - keeping the first one"
            continue
        }
        $map[$old] = $parts[1].Trim()
    }
}
if ($map.Count -eq 0) { throw "no entries in $mapPath" }

$enc = New-Object System.Text.UTF8Encoding($false)
$files = Get-ChildItem -Path $src -Recurse -Filter '*.java'
$changedFiles = 0
$changedCalls = 0

foreach ($f in $files) {
    $text = [System.IO.File]::ReadAllText($f.FullName)
    $orig = $text
    foreach ($k in $map.Keys) {
        # qualified call, no arguments
        $pattern = [regex]::Escape($Class) + '\.' + [regex]::Escape($k) + '\(\)'
        $text = [regex]::Replace($text, $pattern, "$Class.$($map[$k])()")
    }
    if ($text -ne $orig) {
        $changedFiles++
        $changedCalls += ([regex]::Matches($orig, [regex]::Escape($Class) + '\.')).Count
        [System.IO.File]::WriteAllText($f.FullName, $text, $enc)
    }
}

# declarations inside the owning class
$owner = Get-ChildItem -Path $src -Recurse -Filter "$Class.java" | Select-Object -First 1
if ($owner) {
    $text = [System.IO.File]::ReadAllText($owner.FullName)
    $orig = $text
    foreach ($k in $map.Keys) {
        # e.g. "    public static float d()" -> "    public static float getAudioVolume()"
        $text = [regex]::Replace($text,
            '(?m)^(\s+(?:public|protected|private)\s+(?:static\s+|final\s+|abstract\s+|synchronized\s+)*[\w\.\<>\[\]]+[\s\.<>\[\]]+)' + [regex]::Escape($k) + '\(\)',
            "`${1}$($map[$k])()")
    }
    if ($text -ne $orig) {
        [System.IO.File]::WriteAllText($owner.FullName, $text, $enc)
        Write-Host "decl updated: $($owner.FullName)"
    }
}

Write-Host "RENAME_DONE class=$Class entries=$($map.Count) files=$changedFiles"
Write-Host 'next: run\dev.ps1 build, then run\dev.ps1 test'
