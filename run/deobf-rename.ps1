# Symbol-resolved AST renamer wrapper (dry-run unless -Apply is given).
#
# Replaces the regex-based renamers (rename-methods.ps1 / rename-unit-noarg.ps1), which were
# receiver-blind, hierarchy-blind and scope-blind and broke the build on 2026-09-18.
# See tools\deobf\RenameAst.java for the full rationale.
#
# Workflow for every batch:
#   1. powershell -File run\deobf-rename.ps1 -Map run\map-<class>-noarg.tsv -Owner <fqn>
#      -> read the dry-run summary: renamed symbols, edit count, file list
#   2. same command with -Apply
#   3. powershell -File run\dev.ps1 test      (must be 10/10)
#   4. powershell -File run\deobf-audit.ps1   (record the drop in obfuscated methods)
#   5. commit, so the batch can be reverted as a unit
#
# Dry-run output is also written to build\rename-report.tsv for review.
#
# Usage:
#   powershell -File run\deobf-rename.ps1 -Map run\map-unittype-methods.tsv [-Owner <fqn>] [-Source src] [-Apply]
#   -Owner is optional when the map carries an owner column (5/4/3-column members.tsv layout).
param(
    [Parameter(Mandatory = $true)][string]$Map,
    [string]$Owner = '',
    [string]$Source = 'src',
    [switch]$Apply
)

# Portable: derive the deploy root from this script's location (run\ -> <src>\run; its parent holds lib/, jdk/, data/).
# Override with the DSF_ROOT environment variable when the layout differs.
. (Join-Path $PSScriptRoot '_env.ps1')
$srcRoot = Get-DsfSrcRoot

$mapPath = Join-Path $srcRoot $Map
if (-not (Test-Path $mapPath)) { throw "missing map file: $mapPath" }

$java = Get-DsfTool 'java'
if (-not $java) { throw 'missing JDK (set DSF_JAVA_HOME or JAVA_HOME)' }

$tool = Join-Path $srcRoot 'tools\deobf\RenameAst.java'
if (-not (Test-Path $tool)) { throw "missing tool: $tool" }

# --- map guards ---------------------------------------------------------------------------------
# Two failure modes that reach javac as errors and cost a full rollback (both hit on 2026-09-18):
#
#  1. Two members of the same class renamed onto the same signature. The tool only checks a new
#     name against members that ALREADY exist, so a duplicate INSIDE the batch slips through:
#     Unit's private d(UnitList) and public b(UnitList) were both mapped to setGuardFollowers and
#     javac reported "method setGuardFollowers(UnitList) is already defined".
#  2. A skeleton row (owner | kind | old | descriptor | new) written with a BLANK target name but
#     without its trailing empty column. It then has 4 columns and is parsed as the unrelated
#     "owner | old | descriptor | new" layout, producing bogus entries.
#
# Checking the map here is cheaper than another build/rollback cycle.
$mapRows = Get-Content -LiteralPath $mapPath -Encoding UTF8 |
    Where-Object { $_ -notmatch '^\s*#' -and $_.Trim() -ne '' }
$seenTargets = @{}
foreach ($line in $mapRows) {
    $c = $line -split "`t", -1
    if ($c.Count -eq 4 -and $c[1].Trim() -match '^[MF]$') {
        throw "map guard: '$($line.Trim())' looks like a skeleton row whose target name was blanked; keep the trailing empty column so it stays 5-column"
    }
    if ($c.Count -ge 5) {
        if ($c[4].Trim() -eq '') { continue }            # intentionally unnamed row
        $signature = "$($c[0].Trim())|$($c[4].Trim())|$($c[3].Trim())"
    } elseif ($c.Count -eq 4) {
        $signature = "$($c[0].Trim())|$($c[3].Trim())|$($c[2].Trim())"
    } elseif ($c.Count -eq 3) {
        $signature = "$($c[0].Trim())|$($c[2].Trim())|$($c[1].Trim())"
    } elseif ($c.Count -eq 2) {
        $signature = "$Owner|$($c[1].Trim())|"
    } else {
        continue
    }
    if ($seenTargets.ContainsKey($signature)) {
        throw "map guard: two members renamed onto the same signature '$signature' ($($seenTargets[$signature]))"
    }
    $seenTargets[$signature] = $line.Trim()
}
Write-Host "map guard: $($seenTargets.Count) distinct target signature(s)"

# Absolute classpath, expanded by us: javac's own wildcard handling is unreliable for the
# files passed through the compiler API.
$jars = [System.Collections.Generic.List[string]]::new()
foreach ($r in (Get-DsfRoots)) {
    $found = Get-ChildItem -Path (Join-Path $r 'lib\*.jar') -ErrorAction SilentlyContinue
    foreach ($j in $found) { $jars.Add($j.FullName) }
}
if ($jars.Count -eq 0) { throw 'cannot locate dependency jars (set DSF_ROOT)' }
$cp = (($jars.ToArray()) + @(Get-DsfGetdownJars)) -join ';'

$cmdArgs = @(
    '--add-modules', 'jdk.compiler',
    $tool,
    '-src', (Join-Path $srcRoot $Source),
    '-map', $mapPath,
    '-cp', $cp,
    '-report', (Join-Path $srcRoot 'build\rename-report.tsv')
)
if ($Owner -ne '') { $cmdArgs += @('-owner', $Owner) }
if ($Apply) { $cmdArgs += '-apply' }

& $java '-Duser.language=en' '-Duser.country=US' @cmdArgs
if ($LASTEXITCODE -ne 0) { throw "rename tool failed with exit=$LASTEXITCODE" }

if ($Apply) {
    Write-Host 'next: run\dev.ps1 test  (must be 10/10), then run\deobf-audit.ps1'
} else {
    Write-Host 'DRY-RUN only. Re-run with -Apply to write, then run\dev.ps1 test.'
}
