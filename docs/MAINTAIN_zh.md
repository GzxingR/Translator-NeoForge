# 维护指南

[English](MAINTAIN.md) | **中文 Chinese**

## 与 Fabric 版同步 common 层

Fabric 参照仓库：`Translator-1.21.3`（同级目录）。

建议流程：

1. 在 Fabric 仓库修改 `src/main/common/kgg/translator/` 后，对 NeoForge 项目执行 diff：
   ```bash
   diff -ru ../Translator-1.21.3/src/main/common ../Translator-1.21.1-neoforge/src/main/common
   ```
2. 仅合并平台无关改动；事件/配置抽象保持使用 `OptionStorage`、`PlatformHooks`、纯 Java 事件。
3. 平台相关代码留在各自 `src/main/java/`。

## 依赖升级检查点

| 组件 | 文件 | 说明 |
|------|------|------|
| NeoForge | `gradle.properties` → `neo_version` | [NeoForged 版本页](https://projects.neoforged.net/neoforged/neoforge) |
| Parchment | `parchment_mappings_version` | 与 MC 版本匹配 |
| Cloth Config | `cloth_config_version` | CurseForge/Modrinth NeoForge 构建 |
| ModMenu NeoForge | 运行时 mod | 非编译依赖 |

## Mixin 与版本差异

1.21.1 与 1.21.3 方法签名可能不同。升级 MC 时：

1. 对照 Parchment 源核对 `@Inject` 目标
2. 更新 `translator.mixins.json`
3. 验证 `accesstransformer.cfg` 中符号名

当前暂未移植（待后续版本对齐）的 mixin：计分板、标题/副标题、书本界面、聊天输入框、TextDisplay 实体。

## 发布流程

```bash
git tag -a vX.Y.Z -m "Release X.Y.Z"
git push origin vX.Y.Z
```

tag 推送后 `release.yml` 自动构建 JAR 并创建 GitHub Release。

本地指定版本号构建：

```bash
./gradlew clean build -Pmod_version=X.Y.Z
```

输出：`build/libs/Translator-1.21.1-X.Y.Z.jar`

## 相关文档

- [PUBLISH_zh.md](../modrinth/PUBLISH_zh.md) — Modrinth 发布
- [DEV_zh.md](DEV_zh.md) — 开发指南
