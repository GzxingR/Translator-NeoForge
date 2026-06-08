$ErrorActionPreference = 'Stop'
$root = (Resolve-Path (Join-Path $PSScriptRoot '..\..')).Path
Push-Location $root

$patterns = @(
    'AKID[a-zA-Z0-9]{10,}',
    'github_pat_[a-zA-Z0-9]+',
    'ghp_[a-zA-Z0-9]+',
    'mrp_[a-zA-Z0-9]{20,}',
    'sk-[a-zA-Z0-9]{10,}'
)

$hits = @()
foreach ($p in $patterns) {
    $found = git grep -n -E $p -- ':!ai-handoff/tools/env.example' ':!ai-handoff/tools/check-secrets.ps1' 2>$null
    if ($found) { $hits += $found }
}

if ($hits.Count -gt 0) {
    Write-Error "Possible secrets detected:`n$($hits -join "`n")"
}
Write-Host 'No secret patterns found in tracked grep scope.'
