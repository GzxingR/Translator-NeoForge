# Shared GitHub auth helpers for Translator automation scripts.
# Token is read from GH_TOKEN or ~/.hanako/secrets/github.token — never from chat.

function Get-TranslatorGhToken {
    if ($env:GH_TOKEN) {
        return $env:GH_TOKEN.Trim()
    }
    $tokenFile = Join-Path $env:USERPROFILE '.hanako\secrets\github.token'
    if (Test-Path $tokenFile) {
        return (Get-Content $tokenFile -Raw).Trim()
    }
    throw 'GitHub token missing. Create %USERPROFILE%\.hanako\secrets\github.token (one line PAT) and run %USERPROFILE%\.hanako\setup-git-auth.ps1 once.'
}

function Ensure-TranslatorGitAuth {
    param(
        [string]$RemoteUrl = 'https://github.com/GzxingR/Translator-NeoForge.git'
    )

    $token = Get-TranslatorGhToken
    $env:GH_TOKEN = $token

    git remote set-url origin $RemoteUrl

    $gh = 'C:\Program Files\GitHub CLI\gh.exe'
    if (-not (Test-Path $gh)) { $gh = 'gh' }

    $status = & $gh auth status 2>&1
    if ($LASTEXITCODE -ne 0) {
        $token | & $gh auth login --with-token
        if ($LASTEXITCODE -ne 0) { throw 'gh auth login failed.' }
    }

    & $gh auth setup-git | Out-Null
}
