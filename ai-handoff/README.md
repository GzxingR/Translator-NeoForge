# AI 开发工具包 / AI Handoff Kit

本文件夹专为 **后续 AI 辅助开发** 准备（Cursor、DeepSeek V4 Pro、ChatGPT、Claude 等均可使用）。  
与游戏源码、Gradle 构建、用户文档分离，避免与 `src/`、`docs/` 混淆。

## 快速开始

1. 将整个工程仓库克隆到本地（见 [project/LINKS.md](project/LINKS.md)）。
2. 把 [prompts/SYSTEM.md](prompts/SYSTEM.md) 作为 **系统提示词 / System Prompt** 粘贴给 AI。
3. 使用 DeepSeek 时，优先阅读 [prompts/deepseek-v4.md](prompts/deepseek-v4.md)。
4. 开发前阅读 [project/OVERVIEW.md](project/OVERVIEW.md) 与 [rules/CONSTRAINTS.md](rules/CONSTRAINTS.md)。
5. 改代码后运行 [tools/test.ps1](tools/test.ps1) 或 [tools/build.ps1](tools/build.ps1)。

## 目录结构

```
ai-handoff/
├── README.md                 ← 你在这里
├── prompts/                  ← 给 AI 的提示词（核心）
│   ├── SYSTEM.md             ← 通用系统提示词
│   ├── deepseek-v4.md        ← DeepSeek V4 Pro 专用
│   ├── cursor.md             ← Cursor Agent 专用
│   └── task-templates.md     ← 常见任务可复制模板
├── project/                  ← 工程项目说明
│   ├── OVERVIEW.md           ← 项目总览、技术栈、版本
│   ├── ARCHITECTURE.md       ← 架构与数据流
│   ├── MODULES.md            ← 模块与关键类索引
│   ├── TREE.txt              ← 目录树快照
│   └── LINKS.md              ← GitHub / Modrinth / 文档链接
├── rules/                    ← 开发约束
│   ├── CONSTRAINTS.md        ← 禁止事项、安全、范围
│   └── CONVENTIONS.md        ← 命名、分层、提交习惯
└── tools/                    ← 一键脚本（相对仓库根目录）
    ├── README.md
    ├── build.ps1
    ├── test.ps1
    ├── run-client.ps1
    ├── publish-modrinth.ps1
    ├── push-github.ps1
    ├── check-secrets.ps1
    └── env.example
```

## 维护者

- **Gstar** — https://github.com/GzxingR
- 本项目为独立 NeoForge 1.21.1 工程，AI 辅助开发

## 更新本工具包

当架构、版本号、发布流程变更时，请同步更新 `ai-handoff/project/` 与 `prompts/SYSTEM.md`，保持 AI 上下文准确。
