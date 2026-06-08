This mod serves as a translation tool within the game, designed to translate text found in various in-game elements **without altering world data**. It supports chat, scoreboards, boss bars, titles, tooltips, signs, TextDisplay floating text, books, and more.

**Maintained by Gstar** ([GitHub](https://github.com/GzxingR)) — **NeoForge 1.21.1**, developed with **AI-assisted tooling** (Cursor). Independent project; see [CREDITS.md](https://github.com/GzxingR/Translator-NeoForge/blob/main/CREDITS.md) for design references.

## Platform

| Loader | Minecraft | Notes |
|--------|-----------|--------|
| **NeoForge** | **1.21.1** | Requires Cloth Config (NeoForge 15.0.127+) |

## Key features

- **Non-intrusive translation** — client-side only; does not modify the world.
- **Hotkeys**
  - **U** — open translation options
  - **O** — screen OCR (when the active translator supports images)
- **In-game commands**
  - `/transconfig` — configure translators, languages, cache, and API keys
  - `/translate <text>` — translate text (result is copyable)
  - `/translate-re <text>` — reverse translation
- **Auto-translate** — chat, tooltips, titles, scoreboard, boss bar, signs, entity names / TextDisplay, and more (toggle per feature in options)

## Supported translation services

Configure your own API keys in-game (Cloth Config or commands):

- Baidu Translate
- Youdao Translate
- **Tencent Cloud Machine Translation**
- Bing Translate
- OpenAI-compatible LLM endpoints

## Quick start

1. Install **NeoForge 21.1.216+**, **Cloth Config API** (NeoForge 15.0.127+), and this mod.
2. Configure Tencent Cloud (example):
   ```
   /transconfig tencent <SecretId> <SecretKey>
   ```
3. Set languages:
   ```
   /transconfig language "Auto" "Chinese (Simplified)"
   ```
4. Press **U** and enable the auto-translate options you need.

Full command reference (English): [docs/INSTALL.md](https://github.com/GzxingR/Translator-NeoForge/blob/main/docs/INSTALL.md)

---

## 中文文档 / Chinese documentation

Modrinth 页面以英文为主。完整中文说明请见 GitHub：

- [README_zh-CN.md（中文简介）](https://github.com/GzxingR/Translator-NeoForge/blob/main/README_zh-CN.md)
- [modrinth/body_zh-CN.md（中文 Modrinth 补充说明）](https://github.com/GzxingR/Translator-NeoForge/blob/main/modrinth/body_zh-CN.md)
- [docs/INSTALL.md（安装与全部指令，中文）](https://github.com/GzxingR/Translator-NeoForge/blob/main/docs/INSTALL.md)
