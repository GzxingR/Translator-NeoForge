# Maintenance Guide

**English** | [中文 Chinese](MAINTAIN_zh.md)

## Syncing common layer with Fabric

Fabric reference repo: `Translator-1.21.3` (sibling directory).

Suggested workflow:

1. After changing `src/main/common/kgg/translator/` in Fabric, diff against NeoForge:
   ```bash
   diff -ru ../Translator-1.21.3/src/main/common ../Translator-1.21.1-neoforge/src/main/common
   ```
2. Merge only platform-agnostic changes; keep `OptionStorage`, `PlatformHooks`, and plain Java events.
3. Platform-specific code stays in each project's `src/main/java/`.

## Dependency upgrade checklist

| Component | File | Notes |
|-----------|------|-------|
| NeoForge | `gradle.properties` → `neo_version` | [NeoForged versions](https://projects.neoforged.net/neoforged/neoforge) |
| Parchment | `parchment_mappings_version` | Match MC version |
| Cloth Config | `cloth_config_version` | CurseForge/Modrinth NeoForge build |
| ModMenu NeoForge | Runtime mod | Not a compile dependency |

## Mixins & version differences

1.21.1 vs 1.21.3 method signatures may differ. When upgrading MC:

1. Verify `@Inject` targets against Parchment sources
2. Update `translator.mixins.json`
3. Check symbol names in `accesstransformer.cfg`

Mixins not yet ported (pending alignment): scoreboard, title/subtitle, book UI, chat input, TextDisplay entity.

## Release workflow

```bash
git tag -a vX.Y.Z -m "Release X.Y.Z"
git push origin vX.Y.Z
```

Pushing the tag triggers `release.yml` to build the JAR and create a GitHub Release.

Local build with explicit version:

```bash
./gradlew clean build -Pmod_version=X.Y.Z
```

Output: `build/libs/Translator-1.21.1-X.Y.Z.jar`

## Related docs

- [PUBLISH.md](../modrinth/PUBLISH.md) — Modrinth publishing
- [DEV.md](DEV.md) — Development guide
