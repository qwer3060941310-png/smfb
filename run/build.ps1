# 一键编译：src -> ASCII 构建目录，并回拷一份到源码根的 build/
# 用法: powershell -File run\build.ps1
# 注意: 脚本内不得出现中文路径字面量（PowerShell -File 按 ANSI/GBK 解码会乱码），
#       源目录改为按内容探测（含 src\com\desertstormfront 的目录）。
# Portable: derive the deploy root from this script's location (run\ -> <src>\run; its parent holds lib/, jdk/, data/).
# Override with the DSF_ROOT environment variable when the layout differs.
. (Join-Path $PSScriptRoot '_env.ps1')
$srcRoot = Get-DsfSrcRoot
$src    = Join-Path $srcRoot 'src'
$out    = Get-DsfClassesDir                    # ASCII，供运行/打包使用
$mirror = Join-Path $srcRoot 'build'           # 源码根下的构建产物副本

New-Item -ItemType Directory -Force -Path $out    | Out-Null
New-Item -ItemType Directory -Force -Path $mirror | Out-Null

# 依赖：libGDX + LWJGL 后端 + 原生库 + 辅助库（按候选部署根聚合，不再依赖单一 $root）
$cp = (@((Get-DsfClasspath)) + @(Get-DsfGetdownJars)) -join ';'

# JDK：部署根自带 jdk 优先，其次 DSF_JAVA_HOME / JAVA_HOME / PATH
$javac = Get-DsfTool 'javac'
if (-not $javac) { throw 'javac not found (set DSF_JAVA_HOME or JAVA_HOME)' }

$files = Get-ChildItem -Path $src -Recurse -Filter '*.java' | ForEach-Object { $_.FullName }
$log   = Join-Path $out 'compile.log'

# Force English diagnostics: javac messages are otherwise localized (e.g. Chinese "错误:"),
# which makes the 'error:' grep below silently miss real compile failures.
# Capture every stream plus the real exit code. Diagnostics are localized (Chinese),
# so the exit code is the reliable signal; 'error:' is only a secondary hint.
# '-J-Duser.language=en' makes javac emit English diagnostics. It must be quoted: PowerShell
# splits an unquoted -Duser.language=en at the dot and javac then rejects the argument.
& $javac '-J-Duser.language=en' '-J-Duser.country=US' -encoding UTF-8 -d $out -cp $cp @files *> $log
$exit = $LASTEXITCODE

# NOTE: keep this script ASCII-only; non-ASCII literals are decoded as GBK and corrupted.
$err = (Get-Content $log | Where-Object { $_ -match 'error:' }).Count
$cls = (Get-ChildItem -Path $out -Recurse -Filter '*.class').Count
Write-Host "BUILD_DONE exit=$exit errors=$err classes=$cls"
if ($exit -ne 0) {
    Write-Host 'BUILD FAILED - first diagnostics:'
    Get-Content $log | Select-Object -First 12
}

# 回拷到源码根（交付完整性）
Copy-Item -Path (Join-Path $out '*') -Destination $mirror -Recurse -Force
Copy-Item -Path $log -Destination (Join-Path $mirror 'compile.log') -Force

if ($err -gt 0) { Get-Content $log | Where-Object { $_ -match 'error:' } | Select-Object -First 20 }

# Propagate the compiler status: run\test.ps1 asserts on it, and a red build must never be
# masked by the class-count check (stale classes from a previous build would satisfy it).
exit $exit
