本模组是 Minecraft **游戏内翻译工具**，可在**不修改世界数据**的前提下翻译聊天、计分板、Boss 条、标题、Tooltip、告示牌、TextDisplay 悬浮字、书本等多种内容。

**维护者 Gstar**（[GitHub](https://github.com/GzxingR)）· **NeoForge 1.21.1** · **AI 辅助开发**（Cursor）。独立项目，设计参考见 [CREDITS.md](https://github.com/GzxingR/Translator-NeoForge/blob/main/CREDITS.md)。

[English version](https://github.com/GzxingR/Translator-NeoForge/blob/main/modrinth/body_en.md)

---

## 支持平台

| 加载器 | Minecraft | 说明 |
|--------|-----------|------|
| **NeoForge** | **1.21.1** | 需安装 Cloth Config（NeoForge 15.0.127+） |

## 主要功能

- **无侵入翻译** — 纯客户端，不改世界。
- **快捷键**
  - **U** — 打开翻译选项
  - **O** — 屏幕 OCR 翻译（需翻译器支持图片）
- **游戏内命令**
  - `/transconfig` — 配置翻译器、语言、缓存、API 密钥
  - `/translate <文本>` — 翻译文本（结果可复制）
  - `/translate-re <文本>` — 反向翻译
- **自动翻译** — 聊天、Tooltip、标题、计分板、Boss 条、告示牌、实体名 / TextDisplay 等（可在选项中逐项开关）

## 支持的翻译服务

请在游戏内自行配置 API 密钥（Cloth Config 或命令）：

- 百度翻译
- 有道翻译
- **腾讯云机器翻译**
- Bing 翻译
- OpenAI 兼容 LLM 接口

## 快速开始

1. 安装 **NeoForge 21.1.216+**、**Cloth Config API**（NeoForge 15.0.127+）及本模组。
2. 配置腾讯云（示例）：
   ```
   /transconfig tencent <SecretId> <SecretKey>
   ```
   > 请填写控制台中的 **SecretId / SecretKey**，不要填 AppId。
3. 设置语言：
   ```
   /transconfig language "自动" "中文(简体)"
   ```
4. 按 **U** 开启需要的自动翻译选项。

## 安装与指令（中文详版）

完整安装步骤、全部 `/transconfig` 子命令说明：

👉 [docs/INSTALL.md（中文）](https://github.com/GzxingR/Translator-NeoForge/blob/main/docs/INSTALL.md)

## 源码与反馈

- **GitHub：** https://github.com/GzxingR/Translator-NeoForge
- **问题反馈：** https://github.com/GzxingR/Translator-NeoForge/issues
- **中文 README：** https://github.com/GzxingR/Translator-NeoForge/blob/main/README_zh-CN.md

## 许可证

GPL-3.0 · 作者 Gstar
