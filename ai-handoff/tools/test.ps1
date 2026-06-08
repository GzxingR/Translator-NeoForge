$ErrorActionPreference = 'Stop'
$root = (Resolve-Path (Join-Path $PSScriptRoot '..\..')).Path
Push-Location $root
try {
    ./gradlew test
    exit $LASTEXITCODE
} finally {
    Pop-Location
}
