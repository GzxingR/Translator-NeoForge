# 模块与关键类索引

## Common — `kgg.translator`

| 类 | 文件 | 说明 |
|----|------|------|
| `TranslateService` | `TranslateService.java` | 翻译入口、缓存、批量 |
| `TranslatorManager` | `TranslatorManager.java` | 注册/获取翻译器 |
| `TranslatorConfig` | `TranslatorConfig.java` | 全局配置读写 |
| `Language` | `Language.java` | 语言检测与 API 码映射 |
| `ChatFormat` | `ChatFormat.java` | 聊天格式规则 |
| `LLMManager` | `LLMManager.java` | LLM 模型列表 |
| `OptionStorage` | `option/OptionStorage.java` | 选项存储接口 |
| `PlatformHooks` | `platform/PlatformHooks.java` | 平台钩子 |
| `ConfigUtil` | `util/ConfigUtil.java` | JSON 文件 UTF-8 读写 |
| `TencentCloudSignUtil` | `util/TencentCloudSignUtil.java` | TC3 签名 |
| `RequestUtil` | `util/RequestUtil.java` | HTTP 请求封装 |
| `StringUtil` | `util/StringUtil.java` | 文本工具 |

### 翻译器实现（common）

| 类 | 后端 |
|----|------|
| `BaiduTranslator` | 百度 |
| `YouDaoTranslator` | 有道 |
| `TencentTranslator` | 腾讯云 |
| `BingTranslator` | Bing |
| `LLMTranslator` | OpenAI 兼容 |

## NeoForge — `kgg.translator`

| 类 | 说明 |
|----|------|
| `TranslatorMod` | `@Mod` 入口、初始化 |
| `ChatHandler` | 聊天消息翻译与点击复制 |
| `TranslateHelper` | 世界文本、TextDisplay 等 |
| `TipHandler` | Tooltip 翻译 |
| `OcrHandler` / `OcrScreen` | 屏幕 OCR |
| `KeyBindingHandler` | U/O 快捷键 |
| `TranslateConfigCommand` | `/transconfig` |
| `TranslateCommand` | `/translate` |
| `OptionsScreen` | 主选项界面 |
| `OptionRegistry` | 选项注册表实现 |
| `TranslateExceptionUtil` | 用户友好错误 |
| `*TranslatorImpl` | 各后端 NeoForge 绑定 |
| `*ModMenuImpl` | Cloth Config 字段 |

## 资源

| 文件 | 说明 |
|------|------|
| `translator.mixins.json` | Mixin 列表 |
| `META-INF/accesstransformer.cfg` | AT |
| `assets/translator/lang/*.json` | 本地化 |
| `language.json` | 语言定义 |
| `chat-format.json` | 聊天格式 |
| `prompt.txt` | LLM 默认 prompt |

## Gradle / 元数据

| 文件 | 说明 |
|------|------|
| `gradle.properties` | 版本、NeoForge、Modrinth ID |
| `build.gradle` | 依赖、Minotaur、测试 |
| `neoforge.mods.toml` | 模组元数据模板 |
