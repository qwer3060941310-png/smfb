# Insert class-level JavaDoc into AI sources from a UTF-8 TSV description map.
# Descriptions live in a DATA file (read as UTF-8) so the script itself stays ASCII
# (PowerShell -File decodes scripts as ANSI/GBK).
# Usage: powershell -File run\ai-javadoc.ps1
param()

# Portable: derive the deploy root from this script's location (run\ -> <src>\run; its parent holds lib/, jdk/, data/).
# Override with the DSF_ROOT environment variable when the layout differs.
. (Join-Path $PSScriptRoot '_env.ps1')
$srcRoot = Get-DsfSrcRoot
$aiSrc = Join-Path $srcRoot 'src\com\desertstormfront\ai'
$tsv   = Join-Path $srcRoot 'run\ai-javadoc.tsv'

$enc = New-Object System.Text.UTF8Encoding($false)
$inserted = 0
$missing = 0

foreach ($line in (Get-Content -Path $tsv -Encoding UTF8)) {
    if ([string]::IsNullOrWhiteSpace($line)) { continue }
    $parts = $line -split "`t"
    if ($parts.Count -lt 2) { continue }
    $rel  = $parts[0].Trim()
    $desc = $parts[1].Trim()
    $file = Join-Path $aiSrc $rel
    if (-not (Test-Path $file)) { $missing++; continue }

    $simple = [System.IO.Path]::GetFileNameWithoutExtension($file)
    $text = [System.IO.File]::ReadAllText($file, $enc)

    # avoid duplicate insertion
    if ($text -match [regex]::Escape($desc)) { continue }

    # locate the class declaration line (modifiers vary: public/strictfp/final/abstract...)
    $lines = $text -split "`r?`n"
    $idx = -1
    for ($i = 0; $i -lt $lines.Count; $i++) {
        if ($lines[$i] -match "^\s*(?:\w+\s+)*\b(class|interface|enum)\s+$([regex]::Escape($simple))\b") {
            $idx = $i; break
        }
    }
    if ($idx -lt 0) { $missing++; continue }

    $doc = @("/**", " * $desc", " */")
    $newLines = New-Object System.Collections.Generic.List[string]
    for ($i = 0; $i -lt $lines.Count; $i++) {
        if ($i -eq $idx) { foreach ($d in $doc) { $newLines.Add($d) } }
        $newLines.Add($lines[$i])
    }
    [System.IO.File]::WriteAllText($file, ($newLines -join "`r`n"), $enc)
    $inserted++
}

Write-Host "AI_JAVADOC_DONE inserted=$inserted missing=$missing"
