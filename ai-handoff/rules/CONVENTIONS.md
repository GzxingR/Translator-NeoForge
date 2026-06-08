# 编码与协作约定

## 命名

| 类型 | 约定 |
|------|------|
| 包名 | `kgg.translator` |
| 模组 ID | `translator` |
| 翻译器实现 | `XxxTranslator`（common）+ `XxxTranslatorImpl`（NeoForge） |
| Mixin | `*Mixin`，目标类名反映用途 |
| Lang 键 | `translator.<category>.<name>` |

## 分层规则

1. 新增翻译后端：common 抽象类 → NeoForge Impl → 可选 ModMenuImpl → `/transconfig` 子命令。
2. 新增 UI 字符串：同时更新 `zh_cn.json` 与 `en_us.json`。
3. 新增选项：`Options` 常量 + `OptionRegistry` + OptionsScreen 绑定。

## 注释与文档

- 非显而易见逻辑才写注释（业务规则、API 坑、Mixin 原因）。
- 用户面向说明写 `docs/`；AI 上下文写 `ai-handoff/`。

## Gradle

- 版本号：`gradle.properties` → `mod_version`
- 勿硬编码版本到 Java，用 `gradle.properties` / `neoforge.mods.toml` 模板

## 提交信息（建议）

```
<type>: <简短说明>

<可选正文：原因、测试>
```

type: `fix` | `feat` | `docs` | `refactor` | `test` | `chore`

## 与 AI 协作

- 一次对话聚焦一个任务。
- 提供日志、复现步骤、期望行为。
- 大改前先让 AI 读 `ai-handoff/project/MODULES.md` 定位文件。
