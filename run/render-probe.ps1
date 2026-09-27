# OpenGL render probe (ASCII only: PowerShell -File decodes scripts as ANSI/GBK).
#
# Renders a red clear colour and reads the pixel back, in two modes:
#   plain = standalone LWJGL Display
#   awt   = libGDX style embedding (Display.setParent on a java.awt.Canvas)
#
# Interpretation:
#   plain RED_OK + awt NOT_RED/ERROR -> AWT embedding is broken on this JVM
#   (LWJGL 2.9.2 + JDK 9+), which is what libGDX LwjglApplication uses.
#
# Usage: powershell -File run\render-probe.ps1 [-Mode plain|awt|both]
param([string]$Mode = 'both')

# Portable bootstrap (see _env.ps1): the old "parent of the source root" rule resolved to D:\
# under the junction layout, so javac was looked for at D:\jdk\bin\javac.exe. The dependency jars
# now come from the shared classpath helper instead of being enumerated by hand.
. (Join-Path $PSScriptRoot '_env.ps1')
$jdkBin  = Split-Path (Get-DsfTool 'javac')
$out     = Join-Path (Split-Path (Get-DsfClassesDir) -Parent) 'probe'
$srcFile = Join-Path $PSScriptRoot 'probe\RenderProbe.java'
$cp      = Get-DsfClasspath

New-Item -ItemType Directory -Force -Path $out | Out-Null
& (Join-Path $jdkBin 'javac.exe') -encoding UTF-8 -d $out -cp $cp $srcFile
if ($LASTEXITCODE -ne 0) { Write-Host "PROBE_COMPILE_FAILED exit=$LASTEXITCODE"; exit 1 }

$runCp = "$out;$cp"
$modes = @()
if ($Mode -eq 'both') { $modes = @('plain', 'awt') } else { $modes = @($Mode) }

foreach ($m in $modes) {
    Write-Host "--- mode=$m ---"
    & (Join-Path $jdkBin 'java.exe') -cp $runCp RenderProbe $m
    Write-Host "exit=$LASTEXITCODE"
}
