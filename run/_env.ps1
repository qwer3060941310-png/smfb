# Shared environment bootstrap (ASCII only - PowerShell -File decodes as ANSI).
#
# The project used to live in <deploy>\<source>, where <deploy> also held jdk/, lib/,
# data/ and the getdown jars. That layout is no longer present, so every path is now
# resolved by probing candidate locations instead of assuming a parent directory.
#
# Override anything explicitly:
#   DSF_ROOT      deploy root (holds lib/, data/, jdk/, getdown-*.jar)
#   DSF_JAVA_HOME JDK to compile/run with (defaults to JAVA_HOME, then PATH)
#
# Dot-source from another script:
#   . (Join-Path $PSScriptRoot '_env.ps1')

$script:SrcRoot = $null

# --- source root: the directory that holds run\ and src\com\desertstormfront -------
$candidate = Split-Path $PSScriptRoot -Parent
if (Test-Path (Join-Path $candidate 'src\com\desertstormfront')) {
    $script:SrcRoot = $candidate
}
if (-not $script:SrcRoot -and $env:DSF_ROOT) {
    Get-ChildItem -Path $env:DSF_ROOT -Directory -ErrorAction SilentlyContinue |
        Where-Object { Test-Path (Join-Path $_.FullName 'src\com\desertstormfront') } |
        ForEach-Object { if (-not $script:SrcRoot) { $script:SrcRoot = $_.FullName } }
}
if (-not $script:SrcRoot) { throw 'cannot locate source root' }

# --- deploy roots, most specific first --------------------------------------------
function Get-DsfRoots {
    $list = [System.Collections.Generic.List[string]]::new()
    if ($env:DSF_ROOT) { $list.Add($env:DSF_ROOT) }
    # A deploy root that links back to THIS source tree. When the checkout lives outside the
    # deploy root, the deploy root reaches it through a junction/symlink (e.g.
    # <deploy>\<source> -> D:\<checkout>). Following the link back is the only discovery rule
    # that cannot pick an unrelated project that merely happens to have lib/ and data/.
    $parent = Split-Path $script:SrcRoot -Parent
    if ($parent) {
        foreach ($d in (Get-ChildItem -Path $parent -Directory -ErrorAction SilentlyContinue)) {
            if (-not (Get-ChildItem -Path (Join-Path $d.FullName 'lib') -Filter '*.jar' -ErrorAction SilentlyContinue)) { continue }
            foreach ($c in (Get-ChildItem -Path $d.FullName -Directory -ErrorAction SilentlyContinue)) {
                if (-not $c.LinkType) { continue }
                $target = $c.Target
                if (-not $target) { continue }
                try { $resolved = (Resolve-Path -LiteralPath $target -ErrorAction Stop).Path.TrimEnd('\') } catch { continue }
                if ($resolved -eq $script:SrcRoot.TrimEnd('\')) { $list.Add($d.FullName); break }
            }
        }
    }
    $list.Add((Split-Path $script:SrcRoot -Parent))
    $dist = Join-Path $script:SrcRoot 'dist'
    if (Test-Path $dist) {
        $list.Add($dist)
        $client = Join-Path $dist 'client'
        if (Test-Path $client) {
            Get-ChildItem -Path $client -Directory -ErrorAction SilentlyContinue |
                ForEach-Object { $list.Add($_.FullName) }
        }
    }
    return $list
}

# --- source root: the directory that owns THESE scripts ----------------------
# Always prefer the parent of this run\ directory. Probing $root's subdirectories
# (the historical behaviour) can silently select a different copy of the tree - e.g. a
# junction or a stale checkout that also contains src\com\desertstormfront - so the
# code that lives next to this script must win. Probing is only a fallback, for the
# case where run\ was copied somewhere without its source tree.
function Get-DsfSrcRoot {
    $own = Split-Path $PSScriptRoot -Parent
    if (Test-Path (Join-Path $own 'src\com\desertstormfront')) { return $own }
    $found = $null
    foreach ($r in (Get-DsfRoots)) {
        if (Test-Path (Join-Path $r 'src\com\desertstormfront')) { $found = $r; break }
        $sub = Get-ChildItem -Path $r -Directory -ErrorAction SilentlyContinue |
            Where-Object { Test-Path (Join-Path $_.FullName 'src\com\desertstormfront') } |
            Select-Object -First 1
        if ($sub) { $found = $sub.FullName; break }
    }
    if (-not $found) { throw 'cannot locate source root' }
    return $found
}

# --- toolchain: deploy jdk, else JAVA_HOME, else PATH ------------------------------
function Get-DsfTool([string]$tool) {
    $exe = "$tool.exe"
    # Every consumer here needs a real JDK: javac/jar/jpackage obviously, and java too because
    # RenameAst runs as a single-file source program and needs the jdk.compiler module. A
    # jpackage runtime image (dist\client\...\jdk\bin\java.exe) has no javac.exe and would fail
    # with "Module jdk.compiler not found", so it must never win.
    foreach ($r in (Get-DsfRoots)) {
        $p = Join-Path $r "jdk\bin\$exe"
        if ((Test-Path $p) -and (Test-Path (Join-Path $r 'jdk\bin\javac.exe'))) { return $p }
    }
    $homeDir = if ($env:DSF_JAVA_HOME) { $env:DSF_JAVA_HOME } else { $env:JAVA_HOME }
    if ($homeDir) {
        $p = Join-Path $homeDir "bin\$exe"
        if (Test-Path $p) { return $p }
    }
    $cmd = Get-Command $exe -ErrorAction SilentlyContinue
    if ($cmd) { return $cmd.Source }
    return $null
}

# --- dependency jars: any lib\ folder plus jars sitting directly in a root ---------
function Get-DsfClasspath {
    $parts = [System.Collections.Generic.List[string]]::new()
    foreach ($r in (Get-DsfRoots)) {
        # Stop at the first root that actually provides jars: the roots are ordered from most to
        # least specific, and mixing them would put the packaged copy in dist\ (possibly stale)
        # on the classpath next to the live lib\ one.
        $lib = Join-Path $r 'lib'
        if ((Test-Path $lib) -and (Get-ChildItem -Path $lib -Filter '*.jar' -ErrorAction SilentlyContinue)) {
            $parts.Add((Join-Path $lib '*'))
        }
        if (Get-ChildItem -Path $r -Filter '*.jar' -ErrorAction SilentlyContinue) {
            $parts.Add((Join-Path $r '*.jar'))
        }
        if ($parts.Count -gt 0) { break }
    }
    if ($parts.Count -eq 0) { throw 'cannot locate dependency jars (set DSF_ROOT)' }
    return ($parts -join ';')
}

# --- getdown jars: optional, GetdownRunner.java is skipped when they are absent ----
function Get-DsfGetdownJars {
    foreach ($r in (Get-DsfRoots)) {
        $a = Join-Path $r 'getdown-client-1.2.jar'
        $b = Join-Path $r 'getdown-runner.jar'
        if ((Test-Path $a) -and (Test-Path $b)) { return @($a, $b) }
    }
    return @()
}

# --- data directory used as the working directory when running the game ------------
function Get-DsfDataRoot {
    foreach ($r in (Get-DsfRoots)) {
        $d = Join-Path $r 'data'
        if (Test-Path $d) { return $r }
    }
    return (Get-DsfRoots)[0]
}

# --- build output: prefer an ASCII path, fall back to TEMP when the root is not ----
function Get-DsfClassesDir {
    foreach ($r in (Get-DsfRoots)) {
        if ($r -match '^[ -~]+$') { return (Join-Path $r 'appbuild\classes') }
    }
    return (Join-Path $env:TEMP 'dsf-appbuild\classes')
}
