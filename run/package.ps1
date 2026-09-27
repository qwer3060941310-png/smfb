# 打包：ASCII 构建产物 -> dist\DesertStormfront.jar（含运行依赖与 data 资源）
# 用法: powershell -File run\package.ps1
# 注意: 源目录按内容探测，避免中文路径字面量在脚本中被 GBK 误解码。
# Portable: derive the deploy root from this script's location (run\ -> <src>\run; its parent holds lib/, jdk/, data/).
# Override with the DSF_ROOT environment variable when the layout differs.
. (Join-Path $PSScriptRoot '_env.ps1')
$srcRoot = Get-DsfSrcRoot
$root    = Get-DsfDataRoot

$classes = Get-DsfClassesDir
$dist    = Join-Path $root 'dist'          # ASCII 主产物
$mirror  = Join-Path $srcRoot 'dist'       # 源码根下的交付副本
$res     = Join-Path $root 'data'

if (-not (Test-Path $classes)) { throw "missing build output: $classes (run build.ps1 first)" }

New-Item -ItemType Directory -Force -Path $dist   | Out-Null
New-Item -ItemType Directory -Force -Path $mirror | Out-Null

$jar = Join-Path $dist 'DesertStormfront.jar'
if (Test-Path $jar) { Remove-Item $jar -Force }
$jarTool = Get-DsfTool 'jar'
if (-not $jarTool) { throw 'jar not found (set DSF_JAVA_HOME or JAVA_HOME)' }
& $jarTool cfm $jar (Join-Path $PSScriptRoot 'manifest.txt') -C $classes .

# 运行期依赖与资源
Copy-Item -Path (Join-Path $root 'lib\*.jar') -Destination $dist -Force

# 自定义开发：随包分发 mod 目录（数据驱动覆盖，免编译改数值）
$modSrc = Join-Path $srcRoot 'mod'
if (Test-Path $modSrc) {
    $dstMod = Join-Path $dist 'mod'
    if (Test-Path $dstMod) { Remove-Item $dstMod -Recurse -Force }
    Copy-Item -Path $modSrc -Destination $dstMod -Recurse -Force
}

if (Test-Path $res) {
    $dstData = Join-Path $dist 'data'
    if (Test-Path $dstData) { Remove-Item $dstData -Recurse -Force }
    Copy-Item -Path $res -Destination $dstData -Recurse -Force
}

# 回拷交付副本到源码根
Copy-Item -Path (Join-Path $dist '*') -Destination $mirror -Recurse -Force

Write-Host "PACKAGE_DONE jar=$jar"
Get-ChildItem -Path $dist | Select-Object Name, Length | Format-Table -AutoSize
