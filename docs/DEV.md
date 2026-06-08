# Development Guide (NeoForge 1.21.1)

**English** | [中文 Chinese](DEV_zh.md)

## Requirements

- JDK 21
- Git
- Optional: IDE (IntelliJ IDEA / VS Code + Java extensions)

## Clone & build

```bash
git clone <repository-url>
cd Translator-1.21.1-neoforge
./gradlew build
```

On Windows use `gradlew.bat`.

## Run client locally

```bash
./gradlew runClient
```

First run downloads Minecraft and NeoForge dependencies (may take a while).

## Runtime mods

Place mods in `run/mods/` (Cloth Config is pulled via Gradle):

| Mod | Version | Notes |
|-----|---------|-------|
| Cloth Config | 15.0.127 (NeoForge) | Config UI; declared in Gradle |
| ModMenu (NeoForge Edition) | 1.0.1+ | Optional mod-list config entry |

Without ModMenu, use **U** or `/transconfig config` to open settings.

## Common Gradle tasks

| Task | Description |
|------|-------------|
| `./gradlew runClient` | Launch dev client |
| `./gradlew test` | Run unit tests |
| `./gradlew build` | Compile and package JAR |
| `./gradlew clean --refresh-dependencies` | Clean and refresh dependencies |

## Project layout

```
src/main/common/   # Platform-agnostic logic
src/main/java/     # NeoForge layer (mixins, commands, screens)
src/main/resources/# Resources, mixins, Access Transformer
src/test/java/     # JUnit tests
```

## Version properties

See `gradle.properties`: `minecraft_version=1.21.1`, `neo_version=21.1.233`.

## Related docs

- [INSTALL.md](INSTALL.md) — Installation & commands
- [TEST.md](TEST.md) — Testing guide
- [MAINTAIN.md](MAINTAIN.md) — Maintenance guide
