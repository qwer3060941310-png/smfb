# Print compile diagnostics, skipping the noisy strictfp warnings (ASCII only).
param([int]$Max = 15)

# Portable: the build output is resolved through the shared bootstrap. The old "parent of the
# source root" rule resolves to D:\ under the junction layout (D:\desertstormfront\源代码 ->
# D:\源代码), so the log was looked for at D:\appbuild\classes\compile.log and never found.
. (Join-Path $PSScriptRoot '_env.ps1')
$log = Join-Path (Get-DsfClassesDir) 'compile.log'

if (-not (Test-Path $log)) {
    Write-Host 'no compile log found'
    exit 0
}

$lines = Get-Content -Path $log
$shown = 0
foreach ($line in $lines) {
    if ($line -match 'strictfp') { continue }
    if ($line -notmatch '\.java') { continue }
    Write-Host $line
    $shown = $shown + 1
    if ($shown -ge $Max) { break }
}
Write-Host "shown=$shown"
