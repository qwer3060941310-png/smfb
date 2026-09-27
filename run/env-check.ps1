# Environment check: report available build tooling (ASCII only).
param()

. (Join-Path $PSScriptRoot '_env.ps1')
$srcRoot = Get-DsfSrcRoot
$root    = Get-DsfDataRoot

function Show($name) {
    $cmd = Get-Command $name -ErrorAction SilentlyContinue
    if ($cmd) { Write-Host ("{0} => {1}" -f $name, $cmd.Source) }
    else { Write-Host ("{0} => NOT FOUND" -f $name) }
}

Write-Host '=== build tools ==='
Show 'gradle'
Show 'mvn'
Show 'ant'
Show 'git'

Write-Host '=== roots ==='
Write-Host ("srcRoot => {0}" -f $srcRoot)
Write-Host ("deploy  => {0}" -f $root)
Write-Host ("classes => {0}" -f (Get-DsfClassesDir))

Write-Host '=== jdk ==='
# Resolve through Get-DsfTool so a JAVA_HOME install counts too. The old code only looked at
# <deploy>\jdk and therefore reported NOT FOUND even with a usable JDK on the machine.
$java = Get-DsfTool 'java'
$javac = Get-DsfTool 'javac'
# cmd /c keeps java's own stderr out of PowerShell's error stream (java -version prints there,
# which PowerShell would otherwise turn into noisy ErrorRecords).
if ($java) { Write-Host ("java  => {0}" -f $java); cmd /c "`"$java`" -version 2>&1" | Select-Object -First 3 }
else { Write-Host 'java.exe NOT FOUND (set DSF_JAVA_HOME or JAVA_HOME)' }
if ($javac) { Write-Host ("javac => {0}" -f $javac); cmd /c "`"$javac`" -version 2>&1" | Select-Object -First 1 }
else { Write-Host 'javac.exe NOT FOUND (set DSF_JAVA_HOME or JAVA_HOME)' }

Write-Host '=== dependency jars ==='
Write-Host ("classpath => {0}" -f (Get-DsfClasspath))
foreach ($r in (Get-DsfRoots)) {
    $lib = Join-Path $r 'lib'
    if (Test-Path $lib) {
        $jars = Get-ChildItem -Path $lib -Filter '*.jar' -ErrorAction SilentlyContinue
        if ($jars) {
            Write-Host ("lib {0}" -f $lib)
            $jars | ForEach-Object { Write-Host ("  {0} ({1} bytes)" -f $_.Name, $_.Length) }
            break
        }
    }
}
