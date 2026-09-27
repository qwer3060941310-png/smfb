# Unified dev task runner (offline build system; ASCII only).
# Usage: powershell -File run\dev.ps1 <task> [-TimeoutMs N]
# Tasks: build | run | smoke | test | screen | package | exe | clean | map | javadoc | env | audit | comments | probe
param(
    [Parameter(Position = 0)][string]$Task = 'build',
    [int]$TimeoutMs = 20000
)

# Portable: derive the deploy root from this script's location (run\ -> <src>\run; its parent holds lib/, jdk/, data/).
# Override with the DSF_ROOT environment variable when the layout differs.
. (Join-Path $PSScriptRoot '_env.ps1')
$srcRoot = Get-DsfSrcRoot
# Working directory of the game: the deploy root that owns data/ (libGDX resolves data/
# relative to the CWD), NOT merely the parent of the source tree.
$root    = Get-DsfDataRoot

$runDir  = Join-Path $srcRoot 'run'
$classes = Get-DsfClassesDir
$java    = Get-DsfTool 'java'

function Invoke-Script($name, $extraArgs) {
    $p = Join-Path $runDir $name
    if (-not (Test-Path $p)) { throw "missing script: $p" }
    if ($extraArgs) { & $p @extraArgs } else { & $p }
}

switch ($Task.ToLower()) {
    'build'   { Invoke-Script 'build.ps1' }
    'package' { Invoke-Script 'package.ps1' }
    'exe'     { Invoke-Script 'package-exe.ps1' }
    'smoke'   { & (Join-Path $runDir 'smoke.ps1') -TimeoutMs $TimeoutMs }
    'test'    { & (Join-Path $runDir 'test.ps1') }
    'screen'  { & (Join-Path $runDir 'screen-check.ps1') -WaitMs $TimeoutMs }
    'map'     { Invoke-Script 'ai-map.ps1' }
    'javadoc' { Invoke-Script 'ai-javadoc.ps1' }
    'env'     { Invoke-Script 'env-check.ps1' }
    'audit'   { Invoke-Script 'deobf-audit.ps1' }
    'comments' { Invoke-Script 'comment-audit.ps1' }

    'probe' {
        $cp = "$classes;" + (Get-DsfClasspath)
        & $java -cp $cp com.desertstormfront.mod.ModGameplayProbeTest
    }

    'verify-events' {
        $cp = "$classes;" + (Get-DsfClasspath)
        & $java -cp $cp com.desertstormfront.mod.ModEventFlowTest
        if ($LASTEXITCODE -ne 0) { Write-Host "VERIFY_EVENTS_FAILED code=$LASTEXITCODE" } else { Write-Host 'VERIFY_EVENTS_OK' }
    }

    'clean' {
        foreach ($d in @((Join-Path $root 'appbuild'), (Join-Path $root 'dist'),
                         (Join-Path $srcRoot 'build'), (Join-Path $srcRoot 'dist'))) {
            if (Test-Path $d) { Remove-Item $d -Recurse -Force; Write-Host "removed: $d" }
        }
        Get-ChildItem -Path $runDir -Filter '*.log' -ErrorAction SilentlyContinue |
            ForEach-Object { Remove-Item $_.FullName -Force }
        Write-Host 'CLEAN_DONE'
    }

    'run' {
        if (-not (Test-Path $classes)) { throw "missing build output: $classes (run 'dev.ps1 build' first)" }
        $cp = "$classes;" + (Join-Path $root 'lib\*')
        # -mNobleMaster, not -mReview: Review selects ExpiringLicense(BuildInfo date,
        # 90 days) which is always expired -> stuck on the black BootScreen.
        Write-Host "starting DesktopLauncher (-mNobleMaster -debug) ..."
        # resources (data/...) are resolved against the working directory -> run from project root
        Push-Location $root
        try {
            & $java -cp $cp com.desertstormfront.desktop.DesktopLauncher -mNobleMaster -debug
        } finally {
            Pop-Location
        }
    }

    default {
        Write-Host "unknown task: $Task"
        Write-Host "available: build run smoke package exe clean map javadoc env"
        exit 1
    }
}
