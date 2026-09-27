# Screen capture diagnosis (ASCII only).
#
# Starts the game, waits, captures the primary screen and reports how much of it
# is actually painted. This answers objectively whether the game window shows
# content or is black, without relying on a human looking at it.
#
# Privacy: no image is kept - the bitmap is written to TEMP, measured, deleted.
#
# Usage: powershell -File run\screen-check.ps1 [-WaitMs 9000]
param(
    [int]$WaitMs = 9000,
    [string]$MainClass = 'com.desertstormfront.desktop.DesktopLauncher',
    [string]$ExtraCp = '',
    [string]$AppArgs = '-mNobleMaster -debug'
)

# Portable: derive the deploy root from this script's location (run\ -> <src>\run; its parent holds lib/, jdk/, data/).
# Override with the DSF_ROOT environment variable when the layout differs.
. (Join-Path $PSScriptRoot '_env.ps1')
$srcRoot = Get-DsfSrcRoot
$root    = if ($env:DSF_ROOT) { $env:DSF_ROOT } else { (Get-DsfRoots)[0] }
$classes = Get-DsfClassesDir
$java    = Get-DsfTool 'java'
if (-not $java) { throw 'java not found (set DSF_JAVA_HOME or JAVA_HOME)' }

if (-not (Test-Path $classes)) { throw "missing build output: $classes (run build.ps1 first)" }

Add-Type -AssemblyName System.Windows.Forms
Add-Type -AssemblyName System.Drawing

$cp = "$classes;" + (Get-DsfClasspath)
if ($ExtraCp -ne '') { $cp = "$cp;$ExtraCp" }
$arg = "-cp `"$cp`" $MainClass $AppArgs"
$outLog = Join-Path $PSScriptRoot 'diag-out.log'
$errLog = Join-Path $PSScriptRoot 'diag-err.log'

$p = Start-Process -FilePath $java -ArgumentList $arg -WorkingDirectory $root `
    -RedirectStandardOutput $outLog -RedirectStandardError $errLog -NoNewWindow -PassThru
Write-Host "started pid=$($p.Id), waiting $WaitMs ms ..."
Start-Sleep -Milliseconds $WaitMs

$bounds = [System.Windows.Forms.Screen]::PrimaryScreen.Bounds
$shot = New-Object System.Drawing.Bitmap($bounds.Width, $bounds.Height)
$g = [System.Drawing.Graphics]::FromImage($shot)
$g.CopyFromScreen($bounds.Location, [System.Drawing.Point]::Empty, $bounds.Size)
$g.Dispose()

$tmp = Join-Path $env:TEMP 'dsf-screen-check.png'
$shot.Save($tmp, [System.Drawing.Imaging.ImageFormat]::Png)

# Downscale so the pixel scan is fast.
$small = New-Object System.Drawing.Bitmap(240, 135)
$sg = [System.Drawing.Graphics]::FromImage($small)
$sg.DrawImage($shot, 0, 0, 240, 135)
$sg.Dispose()
$shot.Dispose()

$total = 0
$painted = 0
$sum = 0
$grid = New-Object int[] 9
for ($y = 0; $y -lt 135; $y++) {
    for ($x = 0; $x -lt 240; $x++) {
        $px = $small.GetPixel($x, $y)
        $lum = [int](0.299 * $px.R + 0.587 * $px.G + 0.114 * $px.B)
        $total++
        $sum += $lum
        if ($lum -gt 12) { $painted++ }
        $gi = [Math]::Floor($y / 45) * 3 + [Math]::Floor($x / 80)
        $grid[$gi] += $lum
    }
}
$small.Dispose()
Remove-Item $tmp -Force -ErrorAction SilentlyContinue

Write-Host ("screen={0}x{1} paintedRatio={2:N3} avgLuminance={3:N1}" -f $bounds.Width, $bounds.Height, ($painted / $total), ($sum / $total))
# Machine readable: Write-Host goes to the information stream and cannot be
# captured by a caller, so the value test.ps1 asserts on is emitted here.
$center = $grid[4] / (45 * 80)
Write-Output ("RESULT_CENTER=" + $center.ToString('0.0', [System.Globalization.CultureInfo]::InvariantCulture))
Write-Host 'grid average luminance (3x3, left-to-right, top-to-bottom):'
for ($i = 0; $i -lt 9; $i++) {
    Write-Host ("  cell{0}={1:N1}" -f $i, ($grid[$i] / (45 * 80)))
}

if (-not $p.HasExited) { $p.Kill() }
Write-Host 'SCREEN_CHECK_DONE'
