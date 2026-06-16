# Commit and push from repo root. Safe for AI agents — no token in command line.
param(
    [Parameter(Mandatory = $true)]
    [string]$Message,
    [string[]]$Paths = @('.'),
    [string]$Branch = '',
    [switch]$CreateBranch
)

$ErrorActionPreference = 'Stop'
. (Join-Path $PSScriptRoot '_git-auth.ps1')

$root = Split-Path $PSScriptRoot -Parent
Push-Location $root
try {
    Ensure-TranslatorGitAuth

    if ($CreateBranch -and $Branch) {
        git checkout -b $Branch
    } elseif ($Branch) {
        git checkout $Branch
    }

    $current = (git rev-parse --abbrev-ref HEAD).Trim()

    foreach ($p in $Paths) {
        git add -- $p
    }

    $staged = git diff --cached --name-only
    if (-not $staged) {
        Write-Host 'Nothing to commit.'
        exit 0
    }

    & (Join-Path $PSScriptRoot '..\ai-handoff\tools\check-secrets.ps1')

    git commit -m $Message
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

    git push -u origin $current
    exit $LASTEXITCODE
} finally {
    Pop-Location
}
