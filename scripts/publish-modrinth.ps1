$ErrorActionPreference = 'Stop'
$root = Split-Path $PSScriptRoot -Parent

if (-not $env:MODRINTH_TOKEN) {
    Write-Error 'Set MODRINTH_TOKEN first. See modrinth/PUBLISH.md'
}

Push-Location $root
try {
    ./gradlew test build modrinth
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
    Write-Host 'Published to Modrinth project: translator-neoforge'
} finally {
    Pop-Location
}
