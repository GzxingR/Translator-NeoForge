# 工程项目总览

## 基本信息

| 项 | 值 |
|----|-----|
| 项目名 | Translator (NeoForge) |
| 模组 ID | `translator` |
| 当前版本 | `0.0.1`（Modrinth: `1.21.1-0.0.1`） |
| Minecraft | 1.21.1+（兼容 1.21.x） |
| NeoForge | 21.1.233（最低 21.1.216） |
| Java | 21 |
| 构建 | Gradle + NeoForged ModDevGradle |
| 作者 | Gstar |

## 产物

| 输出 | 路径 |
|------|------|
| 开发 JAR | `build/libs/Translator-<mc_version>-<version>.jar` |
| 运行时配置 | `config/translator/`（用户本地，**不进 Git**） |

## 依赖

| Mod | 必需 | Modrinth slug |
|-----|------|---------------|
| Cloth Config API (NeoForge) | 是 | `cloth-config` |
| ModMenu NeoForge Edition | 否 | — |

## 源码结构

```
Translator-NeoForge/
├── src/main/common/kgg/translator/     # 核心业务（翻译器、配置、工具）
├── src/main/java/kgg/translator/        # NeoForge 入口、Mixin、GUI、命令
├── src/main/resources/                  # 资源与 mixin 配置
├── src/test/java/                       # 单元测试
├── docs/                                # 用户/开发者文档
├── modrinth/                            # Modrinth 页面 body、发布说明
├── scripts/                             # 仓库级脚本（发布、推送）
├── ai-handoff/                          # 本 AI 工具包（你正在读）
├── build.gradle
└── gradle.properties
```

## 已实现功能（0.0.1）

- 多后端翻译（百度、有道、腾讯、Bing、LLM）
- 自动翻译：聊天、Tooltip、标题、计分板、Boss 条、告示牌、TextDisplay、实体名等
- `/translate`、`/translate-re`、`/transconfig`
- 腾讯云签名、语言码归一化、UTF-8 配置保存
- 中英文模组 UI

## 已知注意点

| 主题 | 说明 |
|------|------|
| 腾讯云 | 勿填 AppId；语言用 `zh` 非 `zh-cn` |
| HttpClient | 签名时勿设受限头 `Host` |
| 聊天 | `ClientboundSystemChatPacket` 需 mutable Component |
| 告示牌 1.21.1 | `getRenderMessages` / 字段 `renderMessages` |
| CI | 集成测试在无 `APPDATA` 时跳过本地配置路径 |

## 版本与发布

1. 改 `gradle.properties` → `mod_version`
2. 写 `CHANGELOG.md`
3. `./gradlew test build`
4. `git tag -a v<minecraft_version>-<mod_version> -m "..."`
5. `git push origin main --tags`

> 版本范围已放宽至 `[1.21.1,1.22)`，可兼容 1.21.x 全系列。

GitHub Actions：`ci.yml`（PR/push 测试）、`release.yml`（tag 发 Release + 可选 Modrinth）。

## 延伸阅读

- [ARCHITECTURE.md](ARCHITECTURE.md)
- [MODULES.md](MODULES.md)
- [LINKS.md](LINKS.md)
- 仓库 `docs/DEV.md`、`docs/INSTALL.md`
