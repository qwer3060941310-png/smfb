# Comment coverage audit (ASCII only).
#
# P3 (readability) needs a metric before it can be improved: this measures how much of the
# source is documented and which files are worst, so batches can be targeted instead of guessed.
#
# Counting rule: a line is a comment when it starts (after indentation) with //, * or /*.
# That is the same rule as the DoD table in 26, so the number is comparable across runs.
#
# Usage:
#   powershell -File run\comment-audit.ps1            # measure and report
#   powershell -File run\comment-audit.ps1 -Floor 3.5 # fail (exit 1) below a coverage floor
#   powershell -File run\comment-audit.ps1 -Top 20    # list more undocumented files
param(
    [double]$Floor = 0.0,
    [int]$Top = 10
)

# Portable bootstrap: the source tree is resolved by the shared helper, which prefers the tree
# that owns this script (a copied run\ folder must never measure another checkout).
. (Join-Path $PSScriptRoot '_env.ps1')
$srcRoot = Get-DsfSrcRoot
$src = Join-Path $srcRoot 'src'
if (-not (Test-Path $src)) { throw "missing source tree: $src" }

$files = Get-ChildItem -Recurse -Filter *.java -Path $src
$total = 0
$comments = 0
$undocumented = New-Object System.Collections.Generic.List[object]
$worst = New-Object System.Collections.Generic.List[object]

foreach ($f in $files) {
    $lines = Get-Content -LiteralPath $f.FullName
    $n = $lines.Count
    $c = ($lines | Where-Object { $_ -match '^\s*(//|\*|/\*)' }).Count
    $total += $n
    $comments += $c
    $rel = $f.FullName.Substring($srcRoot.Length + 1)
    if ($c -eq 0) {
        $undocumented.Add([pscustomobject]@{ Lines = $n; File = $rel })
    }
    # Every restored file carries the CFR header, so "no comment at all" is rare. What matters
    # for P3 is where the ratio is lowest: those are the files a documentation batch must hit.
    if ($n -ge 50) {
        $pctFile = [math]::Round(100.0 * $c / $n, 1)
        $worst.Add([pscustomobject]@{ Pct = $pctFile; Lines = $n; File = $rel })
    }
}

$pct = if ($total -gt 0) { [math]::Round(100.0 * $comments / $total, 1) } else { 0 }
Write-Output "COMMENT_AUDIT_DONE files=$($files.Count) lines=$total comments=$comments pct=$pct floor=$Floor"

Write-Output "--- lowest comment ratio, files >= 50 lines (P3 targets, top $Top):"
$worst | Sort-Object -Property @{Expression = 'Pct'}, @{Expression = 'Lines'; Descending = $true} |
    Select-Object -First $Top |
    ForEach-Object { Write-Output ("  {0,5}%  {1,6} lines  {2}" -f $_.Pct, $_.Lines, $_.File) }
Write-Output "undocumentedFiles=$($undocumented.Count)"

if ($Floor -gt 0 -and $pct -lt $Floor) {
    Write-Output "COMMENT_AUDIT FAIL pct=$pct is below floor=$Floor"
    exit 1
}
exit 0
