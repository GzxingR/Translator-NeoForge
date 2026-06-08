# Modrinth 页面文案（中文补充）

> **说明：** Modrinth 主页面以 **英文** 为准（`body.md`）。本文件为 **GitHub 上的中文补充**，不会覆盖 Modrinth 英文描述。  
> 英文主文档：[body.md](body.md) · [Modrinth 项目页](https://modrinth.com/mod/translator-neoforge)

本模组是 Minecraft **游戏内翻译工具**，可在**不修改世界数据**的前提下翻译聊天、计分板、Boss 条、标题、Tooltip、告示牌、TextDisplay 悬浮字、书本等。

**维护者 Gstar**（[GitHub](https://github.com/GzxingR)）· **NeoForge 1.21.1**

---

## 支持平台

| 加载器 | Minecraft | 说明 |
|--------|-----------|------|
| **NeoForge** | **1.21.1** | 需 Cloth Config（NeoForge 15.0.127+） |

## 主要功能

- **无侵入翻译** — 纯客户端，不改世界
- **U** — 选项 · **O** — 屏幕 OCR（视翻译器而定）
- `/transconfig`、`/translate`、`/translate-re`
- 自动翻译：聊天、Tooltip、标题、计分板、Boss 条、告示牌、TextDisplay、实体名等

## 支持的翻译服务

百度、有道、**腾讯云**、Bing、OpenAI 兼容 LLM（需自行配置密钥）

## 快速开始

```
/transconfig tencent <SecretId> <SecretKey>
/transconfig language "自动" "中文(简体)"
```

按 **U** 开启需要的自动翻译选项。

## 详细中文文档

👉 [docs/INSTALL.md（安装与全部指令）](https://github.com/GzxingR/Translator-NeoForge/blob/main/docs/INSTALL.md)  
👉 [README_zh-CN.md](https://github.com/GzxingR/Translator-NeoForge/blob/main/README_zh-CN.md)
