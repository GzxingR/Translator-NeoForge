$ErrorActionPreference = 'Stop'
. (Join-Path $PSScriptRoot '_git-auth.ps1')
$root = Split-Path $PSScriptRoot -Parent
$repo = 'GzxingR/Translator-NeoForge'

Ensure-TranslatorGitAuth -RemoteUrl "https://github.com/$repo.git"

$gh = 'C:\Program Files\GitHub CLI\gh.exe'
if (-not (Test-Path $gh)) { $gh = 'gh' }

Push-Location $root
try {
    $exists = $false
    try {
        & $gh api "repos/$repo" --jq .name 2>$null | Out-Null
        if ($LASTEXITCODE -eq 0) { $exists = $true }
    } catch { }

    if (-not $exists) {
        Write-Host "Creating $repo ..."
        & $gh repo create $repo --public `
            --description 'NeoForge 1.21.1 in-game translator — independent project by Gstar (AI-assisted).' `
            --source . --remote origin --push
    } else {
        git remote set-url origin "https://github.com/$repo.git"
        git push -u origin main
        git push origin --tags
    }
    Write-Host "GitHub: https://github.com/$repo"
} finally {
    Pop-Location
}
