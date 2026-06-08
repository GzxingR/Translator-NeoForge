$ErrorActionPreference = 'Stop'
$project = Split-Path (Split-Path $PSScriptRoot -Parent) -Parent
if (-not (Test-Path "$project\build.gradle")) {
    $project = Split-Path $PSScriptRoot -Parent
}

$id = $env:TENCENT_SECRET_ID
$key = $env:TENCENT_SECRET_KEY
if (-not $id -or -not $key) {
    $configPath = Join-Path $env:APPDATA '.minecraft\versions\Creative Co-Operation\config\translator.json'
    if (Test-Path $configPath) {
        $config = Get-Content $configPath -Raw -Encoding UTF8 | ConvertFrom-Json
        $entry = $config.'腾讯翻译'
        if ($entry) {
            $id = $entry.secretId
            $key = $entry.secretKey
        }
    }
}

if (-not $id -or -not $key) {
    Write-Error 'Missing Tencent credentials. Set TENCENT_SECRET_ID/TENCENT_SECRET_KEY or configure translator.json.'
}

Push-Location $project
try {
    $env:TENCENT_SECRET_ID = $id
    $env:TENCENT_SECRET_KEY = $key
    ./gradlew test --tests kgg.translator.translator.TencentTranslatorIntegrationTest.liveTranslateEnglishToChinese 2>&1
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
    Write-Host 'Tencent API verification passed.'
} finally {
    Pop-Location
}
