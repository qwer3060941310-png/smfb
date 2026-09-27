# Package native Windows app image (DesertStormfront.exe) with jpackage (ASCII only).
# Usage: powershell -ExecutionPolicy Bypass -File run\package-exe.ps1 [-Console]
# Self-healing: rebuilds classes (build.ps1) and the jar (package.ps1) when missing.
param(
    [switch]$Console
)

# Portable: derive the deploy root from this script's location (run\ -> <src>\run; its parent holds lib/, jdk/, data/).
# Override with the DSF_ROOT environment variable when the layout differs.
. (Join-Path $PSScriptRoot '_env.ps1')
$srcRoot = Get-DsfSrcRoot
$root    = Get-DsfDataRoot

$runDir = Join-Path $srcRoot 'run'
$javac  = Get-DsfTool 'javac'
$jarExe = Get-DsfTool 'jar'
$jpkg   = Get-DsfTool 'jpackage'
foreach ($t in @($javac, $jarExe, $jpkg)) {
    if (-not $t) { throw "missing jdk tool (set DSF_JAVA_HOME or JAVA_HOME)" }
}

$classes = Get-DsfClassesDir
$dist    = Join-Path $root 'dist'
$input   = Join-Path $root 'appbuild\jpkg'   # jars only
$dest    = Join-Path $root 'dist\win'
$name    = 'DesertStormfront'

# 0) self-healing prerequisites
if (-not (Test-Path (Join-Path $classes 'com\desertstormfront\desktop\DesktopLauncher.class'))) {
    Write-Host 'classes missing -> run build.ps1'
    & (Join-Path $runDir 'build.ps1')
}
$mainJar = Join-Path $dist 'DesertStormfront.jar'
if (-not (Test-Path $mainJar)) {
    Write-Host 'jar missing -> run package.ps1'
    & (Join-Path $runDir 'package.ps1')
}
if (-not (Test-Path $mainJar)) { throw "still missing jar: $mainJar" }

# 1) input directory: main jar + runtime dependencies
#    a running instance locks app\*.jar and the exe, so stop it first
$running = Get-Process -Name $name -ErrorAction SilentlyContinue
if ($running) {
    Write-Host ("stopping {0} running instance(s) of {1}" -f $running.Count, $name)
    $running | Stop-Process -Force -ErrorAction SilentlyContinue
    Start-Sleep -Seconds 2
}
if (Test-Path $input) { Remove-Item $input -Recurse -Force -ErrorAction SilentlyContinue }
if (Test-Path $input) { throw "cannot remove $input - close the game and retry" }
New-Item -ItemType Directory -Force -Path $input | Out-Null
Copy-Item -Path (Join-Path $dist '*.jar') -Destination $input -Force
$mainJarSize = (Get-Item (Join-Path $input 'DesertStormfront.jar')).Length
Write-Host ("main jar MB = {0}" -f [math]::Round($mainJarSize / 1MB, 1))

# 2) resource jar: libGDX 'data/...' lookups fall back to the classpath when the
#    working-directory copy is missing, so the game never dies with a black screen.
$res = Join-Path $root 'data'
if (Test-Path $res) {
    $resJar = Join-Path $input 'dsf-resources.jar'
    if (Test-Path $resJar) { Remove-Item $resJar -Force }
    Push-Location $root
    try {
        & $jarExe cf $resJar 'data'   # stores entries as data/config_dsf/... etc.
        if ($LASTEXITCODE -ne 0) { throw "jar failed for resources (exit $LASTEXITCODE)" }
    } finally {
        Pop-Location
    }
    Write-Host ("resources jar MB = {0}" -f [math]::Round((Get-Item $resJar).Length / 1MB, 1))
} else {
    Write-Host 'WARNING: no data/ directory, the game will show a black screen'
}

# 3) clean previous image
if (Test-Path $dest) { Remove-Item $dest -Recurse -Force -ErrorAction SilentlyContinue }
if (Test-Path $dest) { throw "cannot remove $dest - close the running game (DesertStormfront.exe) and retry" }
New-Item -ItemType Directory -Force -Path $dest | Out-Null

$modules = 'java.base,java.desktop,java.logging,java.management,java.naming,' +
           'java.scripting,java.sql,java.xml,jdk.unsupported,jdk.crypto.ec,jdk.crypto.cryptoki'

$args = @(
    '--type', 'app-image',
    '--name', $name,
    '--app-version', '1.0.0',
    '--vendor', 'NobleMaster',
    '--description', 'Desert Stormfront',
    '--input', $input,
    '--main-jar', 'DesertStormfront.jar',
    '--main-class', 'com.desertstormfront.desktop.DesktopLauncher',
    '--dest', $dest,
    '--add-modules', $modules,
    '--java-options', '-Xms512M -Xmx1024M -Dsun.java2d.noddraw=true',
    # not -mReview: that selects ExpiringLicense(BuildInfo build date 2016-11-23 + 90 days),
    # long expired. Since T05 the BootScreen fail-safe reports it readably and exits, but the
    # shipped exe must still start normally, so use a licensed market key.
    '--arguments', '-mNobleMaster'
)
if ($Console) { $args += '--win-console' }

Write-Host 'running jpackage (jlink runtime, may take 1-3 min) ...'
& $jpkg @args
if ($LASTEXITCODE -ne 0) { throw "jpackage failed with exit code $LASTEXITCODE" }

$appDir = Join-Path $dest $name

# 4) external copies next to the exe (overridable / mod friendly; found before the jar)
if (Test-Path $res) {
    $dstData = Join-Path $appDir 'data'
    if (Test-Path $dstData) { Remove-Item $dstData -Recurse -Force }
    Copy-Item -Path $res -Destination $dstData -Recurse -Force
}

$modSrc = Join-Path $srcRoot 'mod'
if (Test-Path $modSrc) {
    $dstMod = Join-Path $appDir 'mod'
    if (Test-Path $dstMod) { Remove-Item $dstMod -Recurse -Force }
    Copy-Item -Path $modSrc -Destination $dstMod -Recurse -Force
}

foreach ($dll in @('lwjgl.dll', 'OpenAL32.dll')) {
    $p = Join-Path $root $dll
    if (Test-Path $p) { Copy-Item -Path $p -Destination $appDir -Force }
}

# 5) console launcher for troubleshooting
#    NOTE: the bundled runtime has no java.exe (jpackage ships its own launcher),
#    so fall back to the project JDK; if absent, start the exe with -debug.
$bat = Join-Path $appDir 'DesertStormfront-debug.bat'
@(
    '@echo off',
    'cd /d "%~dp0"',
    'set "JDKJAVA=%~dp0..\..\..\jdk\bin\java.exe"',
    'if exist "%JDKJAVA%" (',
    '  "%JDKJAVA%" -cp "app\*" com.desertstormfront.desktop.DesktopLauncher -mNobleMaster -debug',
    ') else (',
    '  start "" "%~dp0DesertStormfront.exe" -mNobleMaster -debug',
    ')',
    'pause'
) | Set-Content -Path $bat -Encoding ASCII

$exe = Join-Path $appDir "$name.exe"
if (-not (Test-Path $exe)) { throw "exe not generated: $exe" }
Write-Host "EXE_DONE $exe"

# NOTE: keep <srcRoot>\dist\win deleted - a second (stale) app image there is launched
# by mistake and fails with 'Error opening ...\app\DesertStormfront.cfg'.
Get-ChildItem -Path $appDir | Select-Object Name, Length | Format-Table -AutoSize
