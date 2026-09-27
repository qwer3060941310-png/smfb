# Regression gate (ASCII only).
#
# Chains the checks that must hold after every source change:
#   1. build      - javac exit code 0 and class count does not drop
#   2. events     - ModEventFlowTest (hook forwarding + bus delivery)
#   3. smoke      - game boots, banner present, no exception in stderr
#   4. mod-noop   - game still boots with mod/ disabled (safe fallback)
#
# Exits non-zero when any check fails, so it can be used as a CI gate.
#
# Usage: powershell -File run\test.ps1 [-SkipBuild]
param([switch]$SkipBuild, [switch]$SkipScreen)

# Portable: derive the deploy root from this script's location (run\ -> <src>\run; its parent holds lib/, jdk/, data/).
# Override with the DSF_ROOT environment variable when the layout differs.
. (Join-Path $PSScriptRoot '_env.ps1')
$srcRoot  = Get-DsfSrcRoot
$runDir   = $PSScriptRoot
# The game resolves data/ and mod/ relative to its working directory, so that must be the
# deploy root that actually owns data/ - not merely the parent of the source tree.
$root     = Get-DsfDataRoot
$classes  = Get-DsfClassesDir
$java     = Get-DsfTool 'java'
$disable  = Join-Path $root 'mod\DISABLED'
if (-not $java) { throw 'java not found (set DSF_JAVA_HOME or JAVA_HOME)' }

$script:failures = 0
$script:passed   = 0

# Fatal only. Networking errors (match.operationstormfront.com unreachable) are
# expected offline and are NOT treated as failures.
$fatalPattern = 'ExceptionInInitializerError|NoClassDefFoundError|ClassNotFoundException|' +
                'Couldn''t load file|UnsatisfiedLinkError|GL_INVALID|StackOverflowError|GLException'

function Assert($name, $ok, $detail) {
    if ($ok) {
        $script:passed++
        Write-Host "PASS $name $detail"
    } else {
        $script:failures++
        Write-Host "FAIL $name $detail"
    }
}

function Invoke-Game([int]$TimeoutMs, [string]$OutName, [string]$ErrName, [string]$Market = 'NobleMaster') {
    $outLog = Join-Path $runDir $OutName
    $errLog = Join-Path $runDir $ErrName
    if (Test-Path $outLog) { Remove-Item $outLog -Force }
    if (Test-Path $errLog) { Remove-Item $errLog -Force }
    $cp  = "$classes;" + (Get-DsfClasspath)
    # -mNobleMaster: a licensed market. -mReview uses the always-expired trial license; the
    # BootScreen fail-safe (T05) now reports that readably and exits instead of black-screening
    # (see 24_black screen root cause.md).
    $arg = "-cp `"$cp`" com.desertstormfront.desktop.DesktopLauncher -m$Market -debug"
    $proc = Start-Process -FilePath $java -ArgumentList $arg -WorkingDirectory $root `
        -RedirectStandardOutput $outLog -RedirectStandardError $errLog -NoNewWindow -PassThru
    $null = $proc.WaitForExit($TimeoutMs)
    if (-not $proc.HasExited) { $proc.Kill() }
    return @{ Out = $outLog; Err = $errLog }
}

# --- 1. build ---------------------------------------------------------------
if (-not $SkipBuild) {
    & (Join-Path $runDir 'build.ps1') *> $null
    # Assert the compiler status itself. Without this a failed compile went unnoticed, because
    # appbuild\classes still holds the classes of the previous successful build.
    Assert 'build.exitCode' ($LASTEXITCODE -eq 0) "javac exit=$LASTEXITCODE (must be 0)"
}
$cls = (Get-ChildItem -Path $classes -Recurse -Filter '*.class' -ErrorAction SilentlyContinue).Count
Assert 'build.classes' ($cls -ge 380) "classes=$cls (expected >= 380)"

# --- 2. events --------------------------------------------------------------
$cpEv = "$classes;" + (Get-DsfClasspath)
$evOut = & $java -cp $cpEv com.desertstormfront.mod.ModEventFlowTest 2>&1
$evOk = ($LASTEXITCODE -eq 0) -and ($evOut -match 'EVENT_FLOW PASSED')
Assert 'mod.events' $evOk ("exit=$LASTEXITCODE " + (($evOut | Select-Object -Last 1) -join ''))

# --- 2b. semantic mod keys --------------------------------------------------
# Locks the ModLoader.ALIASES contract: a mod file written with "health"/"speed"/... must build
# the same unit as the legacy single-letter keys. Guards the data format during deobfuscation.
$aliasOut = & $java -cp $cpEv com.desertstormfront.mod.ModFieldAliasTest 2>&1
$aliasOk = ($LASTEXITCODE -eq 0) -and ($aliasOut -match 'FIELD_ALIAS PASSED')
Assert 'mod.fieldAliases' $aliasOk ("exit=$LASTEXITCODE " + (($aliasOut | Select-Object -Last 1) -join ''))

# --- 2b. ordered properties unicode decode (blank-UI / black-screen root cause) ---
# Pins the OrderedProperties \uXXXX decoder fix: lowercase and decimal-digit hex
# digits must decode. The broken CFR version dropped them, collapsing every
# translated value to a control char that String.trim() stripped, emptying the UI.
$opOut = & $java -cp $cpEv com.desertstormfront.mod.ModOrderedPropertiesTest 2>&1
$opOk = ($LASTEXITCODE -eq 0) -and ($opOut -match 'ORDERED_PROPS PASSED')
Assert 'mod.orderedProps' $opOk ("exit=$LASTEXITCODE " + (($opOut | Select-Object -Last 1) -join ''))

# --- 2d. gameplay events in a real (headless) match ---------------------------
# ModEventFlowTest only proves the plumbing; this drives an actual World for 5000 steps and
# asserts that the events really fire, in order (attack before the kill it causes).
$probeOut = & $java -cp $cpEv com.desertstormfront.mod.ModGameplayProbeTest 2>&1
$probeOk = ($LASTEXITCODE -eq 0) -and ($probeOut -match 'GAMEPLAY_PROBE PASSED')
Assert 'mod.gameplayEvents' $probeOk ("exit=$LASTEXITCODE " + (($probeOut | Select-Object -Last 1) -join ''))

# --- 2e. core-logic unit tests (dependency-free, head-less) ------------------
# Pure logic that the whole simulation rests on. Each test is a standalone main that
# prints a "<MARKER> PASSED" line and exits non-zero on failure (see src\com\desertstormfront\test).
# No test-framework jar is used: the build stays offline/portable (see Assert.java).
$mhOut = & $java -cp $cpEv com.desertstormfront.test.MathHelperTest 2>&1
Assert 'unit.mathhelper' (($LASTEXITCODE -eq 0) -and ($mhOut -match 'MATHHELPER PASSED')) ("exit=$LASTEXITCODE " + (($mhOut | Select-Object -Last 1) -join ''))

$v2Out = & $java -cp $cpEv com.desertstormfront.test.Vec2Test 2>&1
Assert 'unit.vec2' (($LASTEXITCODE -eq 0) -and ($v2Out -match 'VEC2 PASSED')) ("exit=$LASTEXITCODE " + (($v2Out | Select-Object -Last 1) -join ''))

$v3Out = & $java -cp $cpEv com.desertstormfront.test.Vec3Test 2>&1
Assert 'unit.vec3' (($LASTEXITCODE -eq 0) -and ($v3Out -match 'VEC3 PASSED')) ("exit=$LASTEXITCODE " + (($v3Out | Select-Object -Last 1) -join ''))

# MatchCodec robustness: round-trip + malformed/unsupported-version input (T04).
$mcOut = & $java -cp $cpEv com.desertstormfront.test.MatchCodecTest 2>&1
Assert 'unit.matchcodec' (($LASTEXITCODE -eq 0) -and ($mcOut -match 'MATCHCODEC PASSED')) ("exit=$LASTEXITCODE " + (($mcOut | Select-Object -Last 1) -join ''))

# WorldSerializer round-trip + truncated save (T03/T04).
$wsOut = & $java -cp $cpEv com.desertstormfront.test.WorldSerializerRoundTripTest 2>&1
Assert 'unit.worldserializer' (($LASTEXITCODE -eq 0) -and ($wsOut -match 'WORLDSERIALIZER PASSED')) ("exit=$LASTEXITCODE " + (($wsOut | Select-Object -Last 1) -join ''))

# --- 2c. comment coverage ----------------------------------------------------
# P3 ratchet: the floor is raised as documentation grows, so coverage can never silently
# regress below what a batch already achieved (see 26, P3). Raised 4.0 -> 4.8 (T03) -> 4.9 (T04) -> 5.0 (T12) -> 5.1 (T07).
$caOut = & (Join-Path $runDir 'comment-audit.ps1') -Floor 5.1 | Out-String
$caPct = ''
if ($caOut -match 'pct=([0-9]+(?:\.[0-9]+)?)') { $caPct = $Matches[1] }
Assert 'docs.comments' ($LASTEXITCODE -eq 0) "comment coverage=$caPct% (floor 5.1%)"

# --- 2f. script bootstrap consistency ---------------------------------------
# Every script that needs the deploy root (JDK, libs, build output, data) must dot-source _env.ps1.
# Hand-rolling "parent of the source root" resolves to D:\ under the junction layout
# (D:\desertstormfront\源代码 -> D:\源代码); four scripts were already broken that way
# (deobf-map-skeleton, show-log, thread-dump, render-probe) and one of them is a gate itself.
# This assertion stops the next one from shipping unnoticed.
$bootScripts = Get-ChildItem -Path $runDir -Filter '*.ps1' | Where-Object { $_.Name -ne '_env.ps1' }
$noBootstrap = $bootScripts | Where-Object { -not (Select-String -Path $_.FullName -Pattern '_env\.ps1' -Quiet) }
$missingNames = ($noBootstrap | ForEach-Object { $_.Name }) -join ','
Assert 'scripts.envBootstrap' ($noBootstrap.Count -eq 0) "bootstrap ok $($bootScripts.Count - $noBootstrap.Count)/$($bootScripts.Count); missing=[$missingNames]"

# --- 3. smoke ---------------------------------------------------------------
$r = Invoke-Game 20000 'test-out.log' 'test-err.log'
# Cast to string: Get-Content -Raw returns $null for an empty file, and
# '$null -notmatch x' evaluates to $null (falsy) instead of $false.
$out = if (Test-Path $r.Out) { [string](Get-Content $r.Out -Raw) } else { '' }
$err = if (Test-Path $r.Err) { [string](Get-Content $r.Err -Raw) } else { '' }
Assert 'smoke.banner' ($out -match 'OSF: Desert Stormfront') 'version banner present'
Assert 'smoke.noFatal' ($err -notmatch $fatalPattern) 'no fatal exception in stderr'
Assert 'smoke.modOverride' ($out -match '\[ModLoader\]|\[GameFile\] mod override') 'mod pipeline active'

# --- 4. mod disabled (no-op safety) ----------------------------------------
New-Item -ItemType File -Force -Path $disable | Out-Null
try {
    $r2 = Invoke-Game 20000 'test-nomod-out.log' 'test-nomod-err.log'
    $out2 = if (Test-Path $r2.Out) { [string](Get-Content $r2.Out -Raw) } else { '' }
    $err2 = if (Test-Path $r2.Err) { [string](Get-Content $r2.Err -Raw) } else { '' }
    Assert 'mod.disabled.boots' ($out2 -match 'OSF: Desert Stormfront') 'boots without mod/'
    Assert 'mod.disabled.noFatal' ($err2 -notmatch $fatalPattern) 'no fatal exception in stderr'
} finally {
    if (Test-Path $disable) { Remove-Item $disable -Force }
}

# --- 4b. invalid license fails safe (readable error, no black-screen hang) ----
# -mReview builds an ExpiringLicense from the 2016 build date with a 90-day window, so it is
# expired: the boot screen must report that readably and exit, instead of returning null and
# hanging on a black frame forever (see 24_black screen root cause.md). T05 fail-safe.
$rev = Invoke-Game 15000 'test-review-out.log' 'test-review-err.log' 'Review'
$revOut = if (Test-Path $rev.Out) { [string](Get-Content $rev.Out -Raw) } else { '' }
Assert 'boot.invalidLicense' ($revOut -match 'License is invalid or expired') 'expired license reported readably (no black-screen hang)'

# --- 5. screen actually painted (guards the black-screen regression) --------
if (-not $SkipScreen) {
    $sc = & (Join-Path $runDir 'screen-check.ps1') -WaitMs 9000 | Out-String
    $lum = -1.0
    if ($sc -match 'RESULT_CENTER=([0-9]+(?:\.[0-9]+)?)') {
        $lum = [double]::Parse($Matches[1], [System.Globalization.CultureInfo]::InvariantCulture)
    }
    Assert 'screen.painted' ($lum -gt 5.0) "center luminance=$lum (black screen if ~0)"
}

# Known non-fatal issue: randomly generated/selected tutorial worlds can contain a
# unit configuration whose host cannot host (MapDefinition.a). The game catches it
# and keeps running, so it is reported as a warning, not a failure.
$worldErrors = ([regex]::Matches($out + $out2, 'Error loading world')).Count
if ($worldErrors -gt 0) {
    Write-Host "WARN world.load.errors=$worldErrors (non-fatal: caught, game continues)"
}

Write-Host "TEST_DONE passed=$($script:passed) failed=$($script:failures)"
if ($script:failures -gt 0) { exit 1 }
