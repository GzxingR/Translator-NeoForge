$ErrorActionPreference = 'Stop'
& (Join-Path $PSScriptRoot '..\..\scripts\git-sync.ps1') @args
