# Translator / 翻译器

[English](README.md) | [简体中文](README_zh-CN.md)

[![Modrinth](https://img.shields.io/modrinth/dt/translator-neoforge?logo=modrinth&label=Modrinth&style=flat-square&color=00af5c)](https://modrinth.com/mod/translator-neoforge)
[![Game versions](https://img.shields.io/modrinth/game-versions/translator-neoforge?logo=modrinth&style=flat-square)](https://modrinth.com/mod/translator-neoforge/versions)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg?style=flat-square)](LICENSE)

**NeoForge 1.21.1** in-game translator — non-intrusive translation for chat, HUD, world text, tooltips, books, and more.

**Author & maintainer:** [Gstar](https://github.com/GzxingR) · **AI-assisted development** (Cursor). Independent NeoForge rebuild; design reference noted in [CREDITS.md](CREDITS.md).

<!-- modrinth-exclude-start -->
> This repository is a **standalone NeoForge 1.21.1** project, published on Modrinth as [translator-neoforge](https://modrinth.com/mod/translator-neoforge).
<!-- modrinth-exclude-end -->

## Features

- Client-side only — does not modify world data
- Auto-translate: chat, tooltips, titles, scoreboard, boss bar, signs, **TextDisplay** floating text, entity names
- Manual `/translate` and `/translate-re` commands
- OCR screen translation (**O** key) when the active service supports images
- Multiple backends: Baidu, Youdao, Tencent Cloud, Bing, OpenAI-compatible LLM

## Requirements

| Component | Version |
|-----------|---------|
| Minecraft | **1.21.1** |
| NeoForge | **21.1.216+** (recommended 21.1.233+) |
| Java | **21** |
| **Cloth Config API** (NeoForge) | **15.0.127+** (required) |
| ModMenu (NeoForge Edition) | optional |

## Installation

1. Install NeoForge 1.21.1
2. Add [Cloth Config API](https://modrinth.com/mod/cloth-config) (NeoForge build)
3. Add `Translator-1.21.1-<version>.jar` to `mods/`
4. In-game: **U** → options, or `/transconfig config` to set API keys

Full command reference: [docs/INSTALL.md](docs/INSTALL.md)

## Quick start (Tencent Cloud)

```
/transconfig tencent <SecretId> <SecretKey>
/transconfig language "Auto" "Chinese (Simplified)"
```

Press **U** and enable **Auto translate chat** / **Auto translate entity names** as needed.

## Development

```bash
./gradlew runClient
./gradlew test build
```

See [docs/DEV.md](docs/DEV.md).

## Publish to Modrinth

```powershell
$env:MODRINTH_TOKEN = "mrp_..."
./scripts/publish-modrinth.ps1
```

Details: [modrinth/PUBLISH.md](modrinth/PUBLISH.md)

## Links

- **Modrinth:** https://modrinth.com/mod/translator-neoforge
- **Source:** https://github.com/GzxingR/Translator-NeoForge
- **Issues:** https://github.com/GzxingR/Translator-NeoForge/issues
- **Changelog:** [CHANGELOG.md](CHANGELOG.md)
- **Credits:** [CREDITS.md](CREDITS.md)

## License

[GPL-3.0](LICENSE) — See [CREDITS.md](CREDITS.md) for attribution.
