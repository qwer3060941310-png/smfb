# Offline CI entry (ASCII only).
#
# Runs the whole offline pipeline in the order a CI job needs it and fails (exit 1) if any step
# fails, so a single command gates a commit:
#   1. build   - javac (asserted by test.ps1's build.exitCode gate as well)
#   2. test    - the full regression gate suite (run\test.ps1), 19+ gates
#   3. package - dist\DesertStormfront.jar + runtime deps + data (skippable)
#
# No network, no Gradle/Maven: everything uses the deployed JDK + lib\ jars (see run\_env.ps1).
# Each step runs in its own process so exit codes propagate reliably.
#
# Usage:
#   powershell -File run\ci.ps1
#   powershell -File run\ci.ps1 -SkipPackage
param(
    [switch]$SkipPackage
)

$runDir = $PSScriptRoot
$pwsh   = (Get-Command powershell -ErrorAction SilentlyContinue)
if (-not $pwsh) { $pwsh = (Get-Command pwsh -ErrorAction SilentlyContinue) }
if (-not $pwsh) { throw 'cannot find a PowerShell host' }
$pwshPath = $pwsh.Source

$failed = New-Object System.Collections.Generic.List[string]

function Invoke-Step([string]$name, [string]$script, [string[]]$extra) {
    Write-Host ("=== CI: {0} ===" -f $name)
    # Echo the child's output to the host so the CI log is complete, then return only its code.
    & $pwshPath -NoProfile -File (Join-Path $runDir $script) @extra 2>&1 |
        ForEach-Object { Write-Host $_ }
    $code = $LASTEXITCODE
    if ($code -ne 0) {
        Write-Host ("CI step '{0}' FAILED (exit {1})" -f $name, $code)
        $failed.Add($name)
    }
    return $code
}

Invoke-Step 'build' 'build.ps1' @() | Out-Null
# test.ps1 skips its own build because the build step above already ran.
Invoke-Step 'test'  'test.ps1'  @('-SkipBuild') | Out-Null
if (-not $SkipPackage) {
    Invoke-Step 'package' 'package.ps1' @() | Out-Null
}

if ($failed.Count -gt 0) {
    Write-Host ("CI_FAILED steps=" + ($failed -join ','))
    exit 1
}
Write-Host 'CI_PASSED'
exit 0
