# Changelog

**English** | [中文 Chinese](CHANGELOG_zh.md)

All notable changes to the **NeoForge 1.21.1** port are documented here.

Format based on [Keep a Changelog](https://keepachangelog.com/).

## [0.0.1] - 2026-06-08 (NeoForge 1.21.1)

Modrinth version number: `1.21.1-0.0.1`

NeoForge 1.21.1 port by **Gstar** with AI-assisted development (Cursor), based on [Cfghtiu/Translator3](https://github.com/Cfghtiu/Translator3) and Fabric 1.21.3 reference sources.

### Added

- NeoForge 1.21.1 port (NeoForge 21.1.216+)
- Tencent Cloud Machine Translation API (`/transconfig tencent`)
- Connection test and friendly error messages after saving API keys
- TextDisplay floating text translation (`cacheDisplay` hook)
- Chinese / English language files for mod UI and errors

### Fixed

- Java `HttpClient` rejected `Host` header during Tencent API signing
- Language code `zh-cn` mapped to Tencent API `zh`
- `SignTextMixin` updated for 1.21.1 (`getRenderMessages`)
- Chat handler no longer crashes on rich `tellraw` / system chat packets
- UTF-8 config save for Chinese translator names in `translator.json`

### Dependencies

- **Required:** Cloth Config API (NeoForge 15.0.127+)
- **Optional:** ModMenu (NeoForge Edition)
