# 安装与使用指南（NeoForge 1.21.1）

本文说明 **Translator / 翻译器** 模组需要安装哪些前置，以及游戏内全部指令的中文含义与用法。

---

## 一、环境要求

| 项目 | 版本 |
|------|------|
| Minecraft | **1.21.1** |
| 模组加载器 | **NeoForge 21.1.233** 或更高 |
| Java | **21**（启动器需使用 Java 21） |

---

## 二、需要安装的 Mod

将以下文件放入 `.minecraft/mods/`（或整合包的 `mods` 文件夹）：

### 必需

| Mod | 推荐版本 | 说明 |
|-----|----------|------|
| **NeoForge** | 21.1.233+ | 模组加载器，必须先装 |
| **Cloth Config API**（NeoForge 版） | **15.0.127+** | 配置界面依赖；**不装会导致模组无法启动** |
| **Translator / 翻译器** | 本模组 JAR | 主模组 |

> Cloth Config 下载关键词：`Cloth Config NeoForge 1.21.1`  
> Modrinth / CurseForge 上选择 **NeoForge** 版本，不要下载 Fabric 版。

### 可选

| Mod | 推荐版本 | 说明 |
|-----|----------|------|
| **ModMenu（NeoForge Edition）** | 1.0.1+ | 在 Mod 列表里增加「配置」按钮，更方便打开设置 |
| **Controlling** 等按键管理 Mod | — | 方便查看/修改快捷键 |

> **不装 ModMenu 也能正常使用。** 可通过 **U 键** 或 `/transconfig config` 打开配置界面。

### 安装顺序建议

1. 安装 NeoForge 1.21.1 启动器/实例  
2. 放入 **Cloth Config**  
3. 放入 **Translator**  
4. （可选）放入 **ModMenu**  
5. 启动游戏，确认 Mod 列表中三个模组均显示为已加载  

---

## 三、首次使用（以腾讯云为例）

1. 在 [腾讯云机器翻译控制台](https://console.cloud.tencent.com/tmt) 获取 **SecretId** 与 **SecretKey**  
2. 进入游戏后执行（将示例值替换为你的密钥）：

```
/transconfig tencent AKIDxxxxxxxxxxxxxxxx SecretKeyxxxxxxxxxxxxxxxx
```

→ **配置腾讯云翻译 API，并自动切换为「腾讯翻译」、保存配置**

3. 设置翻译语言：

```
/transconfig language "自动" "中文(简体)"
```

→ **将源语言设为自动检测，目标语言设为简体中文**

4. 按 **U** 打开选项，按需开启「翻译聊天」「翻译标题」「翻译计分板」等开关  

---

## 四、快捷键

| 按键 | 中文说明 |
|------|----------|
| **U** | 打开翻译选项（自动翻译开关、翻译距离等） |
| **O** | 屏幕 OCR 识别并翻译（需当前翻译器支持图片/OCR） |

---

## 五、指令一览

> 所有指令均在**客户端**执行（单人或多人在本地客户端均可）。  
> 带空格或中文的参数请用英文双引号 `"..."` 包裹。  
> 每条指令后 **→** 后为中文说明。

---

### 5.1 翻译指令

| 指令 | 中文说明 |
|------|----------|
| `/translate <文本>` | 将文本从当前「源语言」翻译为「目标语言」，结果可复制 |
| `/translate-re <文本>` | 反向翻译（目标语言 → 源语言） |

**示例：**

```
/translate Hello world
```
→ 把 `Hello world` 翻译成当前设定的目标语言

```
/translate-re 你好世界
```
→ 把 `你好世界` 反向翻译回源语言

---

### 5.2 配置指令 `/transconfig`

#### 语言

| 指令 | 中文说明 |
|------|----------|
| `/transconfig language` | 查看当前源语言与目标语言 |
| `/transconfig language "<源语言>"` | 仅设置源语言 |
| `/transconfig language "<源语言>" "<目标语言>"` | 同时设置源语言与目标语言 |

**常用语言名（与游戏内提示一致）：**

| 指令中填写 | 含义 |
|------------|------|
| `"自动"` | 自动检测 |
| `"中文(简体)"` | 简体中文 |
| `"英语"` | 英语 |
| `"日语"` | 日语 |
| `"韩语"` | 韩语 |
| `"法语"` | 法语 |
| `"德语"` | 德语 |
| `"俄语"` | 俄语 |
| `"葡萄牙语"` | 葡萄牙语 |

**示例：**

```
/transconfig language "自动" "中文(简体)"
```
→ 自动检测原文，翻译为简体中文

---

#### 翻译器切换与 API 配置

| 指令 | 中文说明 |
|------|----------|
| `/transconfig translator` | 查看当前使用的翻译器及是否已配置 API |
| `/transconfig translator "<翻译器名>"` | 切换当前翻译器（不修改 API 密钥） |

**内置翻译器名称：**

| 名称 | 说明 |
|------|------|
| `"百度翻译"` | 百度翻译 API |
| `"有道翻译"` | 有道翻译 API |
| `"腾讯翻译"` | 腾讯云机器翻译 API |
| `"Bing翻译"` | 微软 Bing 翻译（无需密钥，有频率限制） |

---

##### 腾讯云（推荐快捷写法）

| 指令 | 中文说明 |
|------|----------|
| `/transconfig tencent <SecretId> <SecretKey>` | 配置腾讯云密钥（默认区域 `ap-guangzhou`，项目 ID `0`），并启用腾讯翻译 |
| `/transconfig tencent <SecretId> <SecretKey> <区域>` | 同上，并指定区域（如 `ap-shanghai`） |
| `/transconfig tencent <SecretId> <SecretKey> <区域> <项目ID>` | 同上，并指定腾讯云项目 ID |

**示例：**

```
/transconfig tencent AKIDxxxxxxx yourSecretKey
```
→ 使用广州区域配置并启用腾讯翻译

```
/transconfig tencent AKIDxxxxxxx yourSecretKey ap-shanghai 0
```
→ 使用上海区域配置并启用腾讯翻译

---

##### 通过翻译器节点配置（与上面等效）

| 指令 | 中文说明 |
|------|----------|
| `/transconfig translator "腾讯翻译" <SecretId> <SecretKey>` | 配置腾讯云 SecretId / SecretKey 并启用 |
| `/transconfig translator "腾讯翻译" <SecretId> <SecretKey> <区域>` | 配置密钥 + 区域并启用 |
| `/transconfig translator "腾讯翻译" <SecretId> <SecretKey> <区域> <项目ID>` | 配置密钥 + 区域 + 项目 ID 并启用 |
| `/transconfig translator "百度翻译" <QPS> <AppId> <AppKey>` | 配置百度翻译（QPS 为每秒请求上限，如 `10` 表示每秒最多 10 次） |
| `/transconfig translator "有道翻译" <AppId> <AppKey>` | 配置有道翻译并启用 |

**示例：**

```
/transconfig translator "腾讯翻译" AKIDxxxxxxx yourSecretKey
```
→ 配置并启用腾讯翻译

```
/transconfig translator "百度翻译" 10 2024xxxxxx yourAppKey
```
→ 配置百度翻译，QPS 限制为 10，并启用

```
/transconfig translator "有道翻译" yourAppId yourAppKey
```
→ 配置有道翻译并启用

---

#### 缓存与重载

| 指令 | 中文说明 |
|------|----------|
| `/transconfig clearcache` | 清空翻译缓存 |
| `/transconfig reload` | 从磁盘重新读取配置文件 |

---

#### 配置界面

| 指令 | 中文说明 |
|------|----------|
| `/transconfig config` | 打开 Cloth Config 图形配置界面（填写 API、切换翻译器等） |
| `/transconfig config json` | 打开 JSON 文本配置编辑器 |

---

#### 聊天格式

用于从聊天消息中提取需要翻译的正文（例如去掉 `<玩家名>` 前缀）。

| 指令 | 中文说明 |
|------|----------|
| `/transconfig chat-format` | 查看当前聊天匹配格式 |
| `/transconfig chat-format <格式名>` | 切换聊天格式 |

**可用格式名：**

| 格式名 | 中文说明 |
|--------|----------|
| `none` | 不匹配，整句翻译 |
| `normal` | 普通格式：`<玩家名> 消息内容` |
| `hypixel` | Hypixel 风格：`前缀: 消息内容` |

**示例：**

```
/transconfig chat-format hypixel
```
→ 切换为 Hypixel 风格聊天提取规则

---

### 5.3 LLM / OpenAI 兼容接口指令

以下指令也可通过 `/transconfig llm ...` 访问（效果相同）。

| 指令 | 中文说明 |
|------|----------|
| `/llm list` | 列出已配置的 LLM 模型 |
| `/llm add <名称> <URL> <模型名> <APIKey>` | 添加一个 OpenAI 兼容的 LLM 翻译模型 |
| `/llm remove "<名称>"` | 删除指定 LLM 模型 |
| `/llm use "<名称>"` | 切换当前翻译器到该 LLM 模型 |
| `/llm builtin` | 显示内置 LLM 模板列表 |

**示例：**

```
/llm add mygpt https://api.openai.com/v1/chat/completions gpt-4o-mini sk-xxxxxxxx
```
→ 添加名为 `mygpt` 的 OpenAI 兼容模型

```
/llm use "mygpt"
```
→ 切换当前翻译器到 `mygpt`

---

## 六、游戏内选项（U 键）

按 **U** 可开关以下功能（无需记指令）：

| 选项 | 中文说明 |
|------|----------|
| 聊天翻译提示 | 打开聊天栏时，在消息末尾显示 `[翻译]` 按钮 |
| 翻译聊天 | 新聊天消息自动翻译 |
| 翻译工具提示 | 鼠标悬停物品约 0.4 秒后翻译 Tooltip |
| 翻译标题 | 自动翻译屏幕标题 / 副标题（ActionBar） |
| 翻译计分板 | 自动翻译侧边栏计分板 |
| 翻译 boss 栏 | 自动翻译 Boss 血条文字 |
| 翻译实体名 | 自动翻译实体显示名（含 TextDisplay 等） |
| 翻译玩家名 | 自动翻译玩家名称 |
| 翻译告示牌 | 自动翻译告示牌文字 |
| 告示牌结合翻译 | 多行告示牌合并翻译（更准确，可能截断） |
| 翻译距离 | 实体/告示牌超出该格数则不翻译（100 = 无限） |

**书本界面：** 打开书（阅读/编辑）时，界面会出现 **「翻译」** 按钮，点击后异步翻译当前页内容。

---

## 七、配置文件位置

配置保存在：

```
config/translator/
```

主要文件：

| 文件 | 说明 |
|------|------|
| `config.json` | 当前翻译器、语言、各 API 密钥等 |
| `prompt.txt` | LLM 自定义提示词（高级） |

修改后可在游戏内执行 `/transconfig reload` 重新加载。

---

## 八、常见问题

**Q：游戏启动报错，提示缺少 Cloth Config？**  
A：请安装 **Cloth Config NeoForge 1.21.1**（15.0.127+），且不要装 Fabric 版。

**Q：配置了 API 仍提示「未配置」？**  
A：确认指令中的 SecretId / SecretKey 无多余空格；执行 `/transconfig translator` 查看状态；必要时 `/transconfig reload`。

**Q：ModMenu 是必须的吗？**  
A：不是。用 **U 键** 或 `/transconfig config` 即可配置。

**Q：腾讯云区域填什么？**  
A：常用 `ap-guangzhou`（广州）、`ap-shanghai`（上海）、`ap-beijing`（北京），与腾讯云控制台开通的地域一致即可。

---

## 九、相关文档

- [DEV.md](DEV.md) — 开发与构建
- [TEST.md](TEST.md) — 测试与验收清单
- [MAINTAIN.md](MAINTAIN.md) — 版本维护说明
