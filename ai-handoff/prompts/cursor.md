# Cursor Agent 专用提示词

## 使用方式

在 Cursor 中打开仓库 `Translator-NeoForge` 根目录，将以下内容加入 **Project Rules** 或对话首条消息。

---

## Cursor 项目规则（精简版）

```
@ai-handoff/prompts/SYSTEM.md
@ai-handoff/project/OVERVIEW.md
@ai-handoff/rules/CONSTRAINTS.md

你是 Translator NeoForge 1.21.1 的维护助手。

规则：
- 工作区根目录即 Gradle 项目根，不是 Fabric 的 Translator-1.21.3。
- 改 common 层时运行 ./gradlew test。
- 不要 commit 除非用户明确要求。
- 密钥只用环境变量，见 ai-handoff/tools/env.example。
- 发布相关见 ai-handoff/tools/publish-modrinth.ps1 与 modrinth/PUBLISH.md。
```

## Cursor @ 引用建议

| 任务 | 建议 @ 文件 |
|------|-------------|
| 新翻译后端 | `common/.../translator/`, `TranslatorManager.java` |
| 聊天崩溃 | `ChatHandler.java`, `ChatHudMixin.java` |
| 腾讯云 | `TencentTranslator.java`, `TencentCloudSignUtil.java` |
| UI/配置 | `OptionsScreen.java`, `OptionRegistry.java`, lang JSON |
| Mixin 失败 | `translator.mixins.json`, 对应 Mixin 类 |

## 终端命令（Cursor 内置终端）

```powershell
cd <仓库根>
./gradlew test build
./ai-handoff/tools/run-client.ps1
```
