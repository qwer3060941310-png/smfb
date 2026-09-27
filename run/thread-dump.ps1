# Zero-intrusive render diagnosis (ASCII only).
#
# Starts the game in the background, waits, then takes a thread dump of the live
# process. Reading where the libGDX render thread sits tells us whether it is
# rendering, blocked on a resource, or stuck during create().
#
# Usage: powershell -File run\thread-dump.ps1 [-WaitMs 8000]
param([int]$WaitMs = 8000)

# Portable bootstrap (see _env.ps1). The old "parent of the source root" rule resolved to D:\
# under the junction layout, so the JDK and the build output were never found.
. (Join-Path $PSScriptRoot '_env.ps1')
$root    = Get-DsfDataRoot
$classes = Get-DsfClassesDir
$java    = Get-DsfTool 'java'
$jcmd    = Get-DsfTool 'jcmd'
$jstack  = Get-DsfTool 'jstack'

if (-not (Test-Path $classes)) { throw "missing build output: $classes (run build.ps1 first)" }

$cp = "$classes;" + (Get-DsfClasspath)
# -mNobleMaster, not -mReview: since T05 the expired review license is reported and the process
# exits before create() runs, so a dump taken from that run would never show the render stack.
$arg = "-cp `"$cp`" com.desertstormfront.desktop.DesktopLauncher -mNobleMaster -debug"
$outLog = Join-Path $PSScriptRoot 'diag-out.log'
$errLog = Join-Path $PSScriptRoot 'diag-err.log'
$dump   = Join-Path $PSScriptRoot 'thread-dump.txt'

$p = Start-Process -FilePath $java -ArgumentList $arg -WorkingDirectory $root `
    -RedirectStandardOutput $outLog -RedirectStandardError $errLog -NoNewWindow -PassThru

Write-Host "started pid=$($p.Id), waiting $WaitMs ms ..."
Start-Sleep -Milliseconds $WaitMs

if (Test-Path $jcmd) {
    & $jcmd $p.Id Thread.print > $dump 2>&1
} elseif (Test-Path $jstack) {
    & $jstack $p.Id > $dump 2>&1
} else {
    Write-Host 'no jcmd/jstack available'
}

if (-not $p.HasExited) { $p.Kill() }
Write-Host "DUMP_WRITTEN $dump"

Write-Host '--- render thread frames ---'
if (Test-Path $dump) {
    Get-Content $dump | Select-String -Pattern '"LWJGL Application"|"AWT-EventQueue|"main"' -Context 0,18
}
