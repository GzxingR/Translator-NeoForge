# 开发约束（AI 与人类均须遵守）

## 安全 — 绝对禁止提交

- 腾讯云 / 百度 / 有道 / LLM 的 **SecretId、SecretKey、AppKey、API Key**
- **Modrinth Token**（`mrp_...`）
- **GitHub PAT**（`ghp_...`、`github_pat_...`）
- 用户本地 **`config/translator.json`** 及任何 `config/` 内容
- `.env` 文件

Token 仅通过 **环境变量** 或 CI **Secrets** 使用，见 `tools/env.example`。

## 范围控制

- 只改与任务直接相关的文件；禁止顺手格式化全库。
- 禁止删除或弱化 `.gitignore` 中的密钥/配置排除规则。
- 禁止将 `build/`、`.gradle/` 加入版本控制。

## 架构

- 平台无关逻辑 → `src/main/common/`
- NeoForge / Mixin / Screen → `src/main/java/`
- 不要在 common 层引用 Minecraft/NeoForge 类。

## Minecraft / NeoForge

- 目标版本 **1.21.1+**（兼容 1.21.x 全系列），勿按 1.21.3+ Fabric 代码直接复制 Mixin 签名；Mixin 需在目标 MC 版本上验证。
- 客户端模组：不改服务端世界数据。
- 修改 Component 时注意不可变性问题（参考 ChatHandler）。

## 网络 API

- 腾讯云：使用 `TencentCloudSignUtil`；**不要** 对 Java HttpClient 设置 `Host` 头。
- 语言码：经 `Language.resolveApiLang` 归一化。

## 测试

- 提交前 `./gradlew test build` 应通过。
- 集成测试依赖环境变量时，须在无密钥时 **skip** 而非 fail（CI 为 Linux）。

## Git / 发布

- **未经用户明确要求不要 commit / push**。
- 发布前更新 `CHANGELOG.md` 与 `mod_version`。
- Modrinth 项目 slug：`translator-neoforge`（非旧 `translator`）。

## 许可证

- 本项目 GPL-3.0；保留 CREDITS 与 LICENSE，不删除版权声明。
