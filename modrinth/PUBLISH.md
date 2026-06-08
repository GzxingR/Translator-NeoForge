# Modrinth Publishing Guide (NeoForge 1.21.1)

**English** | [中文 Chinese](PUBLISH_zh.md)

This project uses [Minotaur](https://github.com/modrinth/minotaur) to publish to **[Translator (NeoForge)](https://modrinth.com/mod/translator-neoforge)** (slug: `translator-neoforge`).

## Page languages

| File | Language | Purpose |
|------|----------|---------|
| `modrinth/body.md` | **English (primary)** | Synced to Modrinth (`modrinthSyncBody`) |
| `modrinth/body_zh.md` | Simplified Chinese (supplement) | GitHub only; linked from English page footer |
| `README_zh.md` | Simplified Chinese (supplement) | GitHub Chinese overview |

## Pre-release checklist

- [ ] `./gradlew test build` passes
- [ ] `mod_version` updated in `gradle.properties`
- [ ] `CHANGELOG.md` updated
- [ ] Modrinth API token configured

## Command-line publish

```powershell
$env:MODRINTH_TOKEN = "your-token"
./scripts/publish-modrinth.ps1
```

Sync Modrinth **English** long description:

```powershell
./gradlew modrinthSyncBody --no-configuration-cache
```

## GitHub Release

```bash
git tag -a v1.21.1-0.0.1 -m "Release"
git push origin v1.21.1-0.0.1
```

## Modrinth dependency IDs

| Mod | Project ID |
|-----|------------|
| Cloth Config API | `9s6osm5g` |
