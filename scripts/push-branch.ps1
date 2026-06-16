# Push current (or named) branch. Safe for AI agents — no token in command line.
param(
    [string]$Branch = ''
)

$ErrorActionPreference = 'Stop'
. (Join-Path $PSScriptRoot '_git-auth.ps1')

$root = Split-Path $PSScriptRoot -Parent
Push-Location $root
try {
    Ensure-TranslatorGitAuth

    if (-not $Branch) {
        $Branch = (git rev-parse --abbrev-ref HEAD).Trim()
    }

    git push -u origin $Branch
    exit $LASTEXITCODE
} finally {
    Pop-Location
}
