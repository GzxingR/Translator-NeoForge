$ErrorActionPreference = 'Stop'
& (Join-Path $PSScriptRoot '..\..\scripts\push-branch.ps1') @args
