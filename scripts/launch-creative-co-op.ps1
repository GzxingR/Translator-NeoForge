$ErrorActionPreference = 'Stop'

$GameDir = 'C:\Users\Administrator\Documents\.minecraft\versions\Creative Co-Operation'
$AssetsDir = 'C:\Users\Administrator\Documents\.minecraft\assets'
$LibraryDir = 'C:\Users\Administrator\Documents\.minecraft\libraries'
$VersionJsonPath = Join-Path $GameDir 'Creative Co-Operation.json'
$NativesDir = Join-Path $GameDir 'Creative Co-Operation-natives'
$Java = 'C:\Program Files\Java\jdk-21.0.11\bin\java.exe'

if (-not (Test-Path $Java)) {
    $Java = (Get-Command java).Source
}

$version = Get-Content $VersionJsonPath -Raw | ConvertFrom-Json
$cp = New-Object System.Collections.Generic.List[string]

foreach ($lib in $version.libraries) {
    if ($lib.rules) {
        $allowed = $false
        foreach ($rule in $lib.rules) {
            if ($rule.action -eq 'allow' -and $rule.os.name -eq 'windows') { $allowed = $true }
        }
        if (-not $allowed) { continue }
    }
    if ($lib.downloads.artifact) {
        $cp.Add((Join-Path $LibraryDir ($lib.downloads.artifact.path -replace '/', '\')))
    }
}

$cp.Add((Join-Path $GameDir 'Creative Co-Operation.jar'))
$classpath = ($cp | Select-Object -Unique | Where-Object { Test-Path $_ }) -join ';'

$modulePath = @(
    (Join-Path $LibraryDir 'cpw\mods\bootstraplauncher\2.0.2\bootstraplauncher-2.0.2.jar'),
    (Join-Path $LibraryDir 'cpw\mods\securejarhandler\3.0.8\securejarhandler-3.0.8.jar'),
    (Join-Path $LibraryDir 'org\ow2\asm\asm-commons\9.8\asm-commons-9.8.jar'),
    (Join-Path $LibraryDir 'org\ow2\asm\asm-util\9.8\asm-util-9.8.jar'),
    (Join-Path $LibraryDir 'org\ow2\asm\asm-analysis\9.8\asm-analysis-9.8.jar'),
    (Join-Path $LibraryDir 'org\ow2\asm\asm-tree\9.8\asm-tree-9.8.jar'),
    (Join-Path $LibraryDir 'org\ow2\asm\asm\9.8\asm-9.8.jar'),
    (Join-Path $LibraryDir 'net\neoforged\JarJarFileSystems\0.4.1\JarJarFileSystems-0.4.1.jar')
) -join ';'

$jvmArgs = @(
    '-Xmx4G',
    "-Djava.library.path=$NativesDir",
    "-Djna.tmpdir=$NativesDir",
    "-Dorg.lwjgl.system.SharedLibraryExtractPath=$NativesDir",
    "-Dio.netty.native.workdir=$NativesDir",
    '-Djava.net.preferIPv6Addresses=system',
    "-DignoreList=client-extra,Creative Co-Operation.jar",
    "-DlibraryDirectory=$LibraryDir",
    '-cp', $classpath,
    '-p', $modulePath,
    '--add-modules', 'ALL-MODULE-PATH',
    '--add-opens', 'java.base/java.util.jar=cpw.mods.securejarhandler',
    '--add-opens', 'java.base/java.lang.invoke=cpw.mods.securejarhandler',
    '--add-exports', 'java.base/sun.security.util=cpw.mods.securejarhandler',
    '--add-exports', 'jdk.naming.dns/com.sun.jndi.dns=java.naming'
)

$gameArgs = @(
    '--username', 'TranslatorTest',
    '--version', 'Creative Co-Operation',
    '--gameDir', $GameDir,
    '--assetsDir', $AssetsDir,
    '--assetIndex', $version.assets,
    '--uuid', '00000000-0000-0000-0000-000000000001',
    '--accessToken', '0',
    '--clientId', '0',
    '--xuid', '0',
    '--userType', 'legacy',
    '--versionType', 'release',
    '--width', '854',
    '--height', '480',
    '--demo',
    '--fml.neoForgeVersion', '21.1.216',
    '--fml.fmlVersion', '4.0.42',
    '--fml.mcVersion', '1.21.1',
    '--fml.neoFormVersion', '20240808.144430',
    '--launchTarget', 'forgeclient'
)

Write-Host "Launching Creative Co-Operation with Translator..."
& $Java @jvmArgs $version.mainClass @gameArgs
