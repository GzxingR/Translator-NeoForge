# 开发指南 (NeoForge 1.21.1)

## 环境要求

- JDK 21
- Git
- 可选：IDE（IntelliJ IDEA / VS Code + Java 扩展）

## 克隆与构建

```bash
git clone <repository-url>
cd Translator-1.21.1-neoforge
./gradlew build
```

Windows 使用 `gradlew.bat`。

## 本地运行客户端

```bash
./gradlew runClient
```

首次运行会下载 Minecraft 与 NeoForge 依赖，耗时较长。

## 运行时前置 Mod

将以下 mod 放入 `run/mods/`（或通过 Gradle 自动拉取 Cloth Config）：

| Mod | 版本 | 说明 |
|-----|------|------|
| Cloth Config | 15.0.127 (NeoForge) | 配置界面，已在 Gradle 中声明 |
| ModMenu (NeoForge Edition) | 1.0.1+ | 可选，Mod 列表配置入口 |

无 ModMenu 时仍可通过 **U 键** 或 `/translate config` 打开配置。

## 常用 Gradle 任务

| 任务 | 说明 |
|------|------|
| `./gradlew runClient` | 启动开发客户端 |
| `./gradlew test` | 运行单元测试 |
| `./gradlew build` | 编译并打包 JAR |
| `./gradlew clean --refresh-dependencies` | 清理并刷新依赖 |

## 项目结构

```
src/main/common/   # 平台无关业务逻辑
src/main/java/     # NeoForge 平台层（mixin、命令、屏幕）
src/main/resources/# 资源、mixins、Access Transformer
src/test/java/     # JUnit 单元测试
```

## 版本属性

见 `gradle.properties`：`minecraft_version=1.21.1`，`neo_version=21.1.233`。