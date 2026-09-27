# 冒烟测试：以 Review 市场启动桌面端，超时自动终止
# 用法: powershell -File run\smoke.ps1 [-TimeoutMs 20000]
# 说明: 运行 classpath 全部使用 ASCII 路径，避免 ANSI/GBK 解码乱码导致 ClassNotFound。
param(
    [int]$TimeoutMs = 20000,
    # Review uses ExpiringLicense(BuildInfo date, 90 days). That build date is from
    # 2013, so the trial is always expired and the game stays on the black
    # BootScreen forever. Any non-Review market (or -lite) uses AcceptedLicense.
    [string]$Market = 'NobleMaster'
)

# Portable: derive the deploy root from this script's location (run\ -> <src>\run; its parent holds lib/, jdk/, data/).
# Override with the DSF_ROOT environment variable when the layout differs.
. (Join-Path $PSScriptRoot '_env.ps1')
$srcRoot = Get-DsfSrcRoot
$root    = if ($env:DSF_ROOT) { $env:DSF_ROOT } else { (Get-DsfRoots)[0] }
$classes = Get-DsfClassesDir                    # ASCII 构建产物
$runDir  = $PSScriptRoot
$java    = Get-DsfTool 'java'
if (-not $java) { throw 'java not found (set DSF_JAVA_HOME or JAVA_HOME)' }

if (-not (Test-Path $classes)) { throw "missing build output: $classes (run build.ps1 first)" }

$cp  = "$classes;" + (Get-DsfClasspath)
$arg = "-cp `"$cp`" com.desertstormfront.desktop.DesktopLauncher -m$Market -debug"

$outLog = Join-Path $runDir 'smoke-out.log'
$errLog = Join-Path $runDir 'smoke-err.log'

# DesktopLauncher 必须指定市场，否则抛 RuntimeException: no market defined
$p = Start-Process -FilePath $java -ArgumentList $arg `
    -WorkingDirectory $root `
    -RedirectStandardOutput $outLog -RedirectStandardError $errLog `
    -NoNewWindow -PassThru

if ($p.WaitForExit($TimeoutMs)) {
    Write-Host "SMOKE_EXIT code=$($p.ExitCode)"
} else {
    $p.Kill()
    Write-Host "SMOKE_TIMEOUT (GUI 已启动或阻塞，已终止)"
}

Write-Host '--- stdout ---'
if (Test-Path $outLog) { Get-Content $outLog -Tail 25 }
Write-Host '--- stderr ---'
if (Test-Path $errLog) { Get-Content $errLog -Tail 25 }
