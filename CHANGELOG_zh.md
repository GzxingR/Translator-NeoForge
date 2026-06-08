# 更新日志

[English](CHANGELOG.md) | **中文 Chinese**

本文档记录 **NeoForge 1.21.1** 移植版的所有重要变更。

格式参考 [Keep a Changelog](https://keepachangelog.com/)。

## [0.0.1] - 2026-06-08 (NeoForge 1.21.1)

Modrinth 版本号：`1.21.1-0.0.1`

NeoForge 1.21.1 移植由 **Gstar** 在 AI 辅助开发（Cursor）下完成，参考 [Cfghtiu/Translator3](https://github.com/Cfghtiu/Translator3) 与 Fabric 1.21.3 源码。

### 新增

- NeoForge 1.21.1 移植（NeoForge 21.1.216+）
- 腾讯云机器翻译 API（`/transconfig tencent`）
- 保存 API 密钥后的连接测试与友好错误提示
- TextDisplay 悬浮文字翻译（`cacheDisplay` 钩子）
- 模组 UI 与错误信息的中/英文语言文件

### 修复

- Java `HttpClient` 在腾讯云 API 签名时拒绝 `Host` 头
- 语言代码 `zh-cn` 映射为腾讯云 API 的 `zh`
- `SignTextMixin` 适配 1.21.1（`getRenderMessages`）
- 聊天处理器不再因富文本 `tellraw` / 系统聊天包崩溃
- `translator.json` 中文翻译器名称 UTF-8 保存

### 依赖

- **必需：** Cloth Config API（NeoForge 15.0.127+）
- **可选：** ModMenu（NeoForge Edition）
