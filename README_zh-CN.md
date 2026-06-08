# 翻译器 / Translator

[English](README.md) | **简体中文**

[![Modrinth](https://img.shields.io/modrinth/dt/translator-neoforge?logo=modrinth&label=Modrinth&style=flat-square&color=00af5c)](https://modrinth.com/mod/translator-neoforge)
[![Game versions](https://img.shields.io/modrinth/game-versions/translator-neoforge?logo=modrinth&style=flat-square)](https://modrinth.com/mod/translator-neoforge/versions)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg?style=flat-square)](LICENSE)

**NeoForge 1.21.1** 游戏内翻译模组 — 无侵入式翻译聊天、HUD、世界文字、Tooltip、书本等内容。

**作者与维护者：** [Gstar](https://github.com/GzxingR) · **AI 辅助开发**（Cursor）· 独立 NeoForge 项目，设计参考见 [CREDITS.md](CREDITS.md)。

> 本仓库为 **NeoForge 1.21.1** 独立工程，Modrinth 发布页：[translator-neoforge](https://modrinth.com/mod/translator-neoforge)

## 功能特性

- **纯客户端** — 不修改世界存档数据
- **自动翻译** — 聊天、Tooltip、标题、计分板、Boss 条、告示牌、**TextDisplay** 悬浮字、实体名称等
- **手动命令** — `/translate`、`/translate-re`
- **屏幕 OCR** — 按 **O** 键（需当前翻译器支持图像）
- **多后端** — 百度、有道、腾讯云、Bing、OpenAI 兼容 LLM

## 环境要求

| 组件 | 版本 |
|------|------|
| Minecraft | **1.21.1** |
| NeoForge | **21.1.216+**（推荐 21.1.233+） |
| Java | **21** |
| **Cloth Config API**（NeoForge 版） | **15.0.127+**（必需） |
| ModMenu（NeoForge Edition） | 可选 |

## 安装

1. 安装 NeoForge 1.21.1
2. 安装 [Cloth Config API](https://modrinth.com/mod/cloth-config)（**NeoForge** 构建，勿下 Fabric 版）
3. 将 `Translator-1.21.1-<版本>.jar` 放入 `mods/`
4. 进游戏：按 **U** 打开选项，或 `/transconfig config` 配置 API 密钥

完整指令说明：[docs/INSTALL.md](docs/INSTALL.md)（中文）

## 快速开始（腾讯云）

```
/transconfig tencent <SecretId> <SecretKey>
/transconfig language "自动" "中文(简体)"
```

按 **U** 开启 **自动翻译聊天**、**自动翻译实体名称** 等选项。

## 开发

```bash
./gradlew runClient
./gradlew test build
```

详见 [docs/DEV.md](docs/DEV.md)

## 发布到 Modrinth

```powershell
$env:MODRINTH_TOKEN = "你的Token"
./scripts/publish-modrinth.ps1
```

说明：[modrinth/PUBLISH.md](modrinth/PUBLISH.md)

## 相关链接

- **Modrinth：** https://modrinth.com/mod/translator-neoforge
- **源码：** https://github.com/GzxingR/Translator-NeoForge
- **问题反馈：** https://github.com/GzxingR/Translator-NeoForge/issues
- **更新日志：** [CHANGELOG.md](CHANGELOG.md)
- **致谢：** [CREDITS.md](CREDITS.md)

## 许可证

[GPL-3.0](LICENSE) — 署名见 [CREDITS.md](CREDITS.md)。
