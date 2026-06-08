# Installation & Usage Guide (NeoForge 1.21.1)

**English** | [中文 Chinese](INSTALL_zh.md)

This guide covers required dependencies for **Translator** and all in-game commands.

---

## 1. Requirements

| Item | Version |
|------|---------|
| Minecraft | **1.21.1** |
| Mod loader | **NeoForge 21.1.233** or newer |
| Java | **21** (launcher must use Java 21) |

---

## 2. Mods to Install

Place the following in `.minecraft/mods/` (or your pack's `mods` folder):

### Required

| Mod | Recommended | Notes |
|-----|-------------|-------|
| **NeoForge** | 21.1.233+ | Mod loader; install first |
| **Cloth Config API** (NeoForge) | **15.0.127+** | Config UI dependency; **mod will not start without it** |
| **Translator** | This mod JAR | Main mod |

> Search for: `Cloth Config NeoForge 1.21.1`  
> On Modrinth / CurseForge, pick the **NeoForge** build — not Fabric.

### Optional

| Mod | Recommended | Notes |
|-----|-------------|-------|
| **ModMenu (NeoForge Edition)** | 1.0.1+ | Adds a "Configure" button in the mod list |
| **Controlling** or similar | — | Easier keybind management |

> **ModMenu is not required.** Use **U** or `/transconfig config` to open settings.

### Suggested install order

1. Install NeoForge 1.21.1 instance  
2. Add **Cloth Config**  
3. Add **Translator**  
4. (Optional) Add **ModMenu**  
5. Launch and confirm all mods load  

---

## 3. First-time setup (Tencent Cloud example)

1. Get **SecretId** and **SecretKey** from the [Tencent Cloud TMT console](https://console.cloud.tencent.com/tmt)  
2. In-game (replace placeholders with your keys):

```
/transconfig tencent AKIDxxxxxxxxxxxxxxxx SecretKeyxxxxxxxxxxxxxxxx
```

→ **Configure Tencent Cloud API, switch to Tencent translator, and save**

3. Set languages:

```
/transconfig language "Auto" "Chinese (Simplified)"
```

→ **Auto-detect source language; target Simplified Chinese**

4. Press **U** and enable options such as chat, title, and scoreboard translation  

---

## 4. Hotkeys

| Key | Description |
|-----|-------------|
| **U** | Open translation options (auto-translate toggles, distance, etc.) |
| **O** | Screen OCR and translate (requires image/OCR-capable translator) |

---

## 5. Commands

> All commands run on the **client** (single-player or multiplayer local client).  
> Wrap parameters with spaces or non-ASCII text in double quotes `"..."`.  
> Lines ending with **→** explain what the command does.

---

### 5.1 Translation commands

| Command | Description |
|---------|-------------|
| `/translate <text>` | Translate from current source to target language; result is copyable |
| `/translate-re <text>` | Reverse translation (target → source) |

**Examples:**

```
/translate Hello world
```
→ Translate `Hello world` to the configured target language

```
/translate-re 你好世界
```
→ Reverse-translate back to the source language

---

### 5.2 Configuration `/transconfig`

#### Language

| Command | Description |
|---------|-------------|
| `/transconfig language` | Show current source and target languages |
| `/transconfig language "<source>"` | Set source language only |
| `/transconfig language "<source>" "<target>"` | Set both source and target |

**Common language names (match in-game labels):**

| Value | Meaning |
|-------|---------|
| `"Auto"` | Auto-detect |
| `"Chinese (Simplified)"` | Simplified Chinese |
| `"English"` | English |
| `"Japanese"` | Japanese |
| `"Korean"` | Korean |
| `"French"` | French |
| `"German"` | German |
| `"Russian"` | Russian |
| `"Portuguese"` | Portuguese |

**Example:**

```
/transconfig language "Auto" "Chinese (Simplified)"
```
→ Auto-detect source; translate to Simplified Chinese

---

#### Translator selection & API setup

| Command | Description |
|---------|-------------|
| `/transconfig translator` | Show active translator and API configuration status |
| `/transconfig translator "<name>"` | Switch translator (does not change API keys) |

**Built-in translator names:**

| Name | Service |
|------|---------|
| `"Baidu Translate"` | Baidu Translate API |
| `"Youdao Translate"` | Youdao Translate API |
| `"Tencent Translate"` | Tencent Cloud Machine Translation |
| `"Bing Translate"` | Microsoft Bing (no key; rate-limited) |

---

##### Tencent Cloud (shortcut)

| Command | Description |
|---------|-------------|
| `/transconfig tencent <SecretId> <SecretKey>` | Configure keys (default region `ap-guangzhou`, project ID `0`); enable Tencent |
| `/transconfig tencent <SecretId> <SecretKey> <region>` | Same with custom region (e.g. `ap-shanghai`) |
| `/transconfig tencent <SecretId> <SecretKey> <region> <projectId>` | Same with Tencent project ID |

**Examples:**

```
/transconfig tencent AKIDxxxxxxx yourSecretKey
```
→ Guangzhou region; enable Tencent translator

```
/transconfig tencent AKIDxxxxxxx yourSecretKey ap-shanghai 0
```
→ Shanghai region; enable Tencent translator

---

##### Via translator node (equivalent)

| Command | Description |
|---------|-------------|
| `/transconfig translator "Tencent Translate" <SecretId> <SecretKey>` | Configure Tencent keys and enable |
| `/transconfig translator "Tencent Translate" <SecretId> <SecretKey> <region>` | Keys + region |
| `/transconfig translator "Tencent Translate" <SecretId> <SecretKey> <region> <projectId>` | Keys + region + project ID |
| `/transconfig translator "Baidu Translate" <QPS> <AppId> <AppKey>` | Baidu (QPS = max requests/sec, e.g. `10`) |
| `/transconfig translator "Youdao Translate" <AppId> <AppKey>` | Youdao and enable |

**Examples:**

```
/transconfig translator "Tencent Translate" AKIDxxxxxxx yourSecretKey
```
→ Configure and enable Tencent

```
/transconfig translator "Baidu Translate" 10 2024xxxxxx yourAppKey
```
→ Baidu with QPS 10

```
/transconfig translator "Youdao Translate" yourAppId yourAppKey
```
→ Configure and enable Youdao

---

#### Cache & reload

| Command | Description |
|---------|-------------|
| `/transconfig clearcache` | Clear translation cache |
| `/transconfig reload` | Reload config from disk |

---

#### Config UI

| Command | Description |
|---------|-------------|
| `/transconfig config` | Open Cloth Config GUI |
| `/transconfig config json` | Open JSON text editor |

---

#### Chat format

Extract translatable text from chat (e.g. strip `<player>` prefix).

| Command | Description |
|---------|-------------|
| `/transconfig chat-format` | Show current chat format |
| `/transconfig chat-format <name>` | Switch chat format |

**Available formats:**

| Name | Description |
|------|-------------|
| `none` | No pattern; translate entire message |
| `normal` | `<player> message` |
| `hypixel` | Hypixel-style `prefix: message` |

**Example:**

```
/transconfig chat-format hypixel
```
→ Use Hypixel-style extraction

---

### 5.3 LLM / OpenAI-compatible commands

Also available as `/transconfig llm ...` (same effect).

| Command | Description |
|---------|-------------|
| `/llm list` | List configured LLM models |
| `/llm add <name> <URL> <model> <APIKey>` | Add OpenAI-compatible LLM translator |
| `/llm remove "<name>"` | Remove LLM model |
| `/llm use "<name>"` | Switch active translator to LLM |
| `/llm builtin` | Show built-in LLM templates |

**Examples:**

```
/llm add mygpt https://api.openai.com/v1/chat/completions gpt-4o-mini sk-xxxxxxxx
```
→ Add model `mygpt`

```
/llm use "mygpt"
```
→ Switch to `mygpt`

---

## 6. In-game options (U key)

Press **U** to toggle (no commands needed):

| Option | Description |
|--------|-------------|
| Chat translate hint | Show `[Translate]` button on chat messages |
| Auto translate chat | Auto-translate new chat |
| Auto translate tooltip | Translate item tooltip after ~0.4s hover |
| Auto translate title | Auto-translate title / subtitle (action bar) |
| Auto translate scoreboard | Auto-translate sidebar scoreboard |
| Auto translate boss bar | Auto-translate boss bar text |
| Auto translate entity name | Entity display names (incl. TextDisplay) |
| Auto translate player name | Player names |
| Auto translate sign | Sign text |
| Sign combine translate | Merge multi-line signs (more accurate, may truncate) |
| Translate distance | Skip entities/signs beyond range (100 = unlimited) |

**Books:** When reading/editing a book, a **Translate** button appears; click to async-translate the current page.

---

## 7. Config file location

```
config/translator/
```

| File | Purpose |
|------|---------|
| `config.json` | Active translator, languages, API keys |
| `prompt.txt` | Custom LLM prompt (advanced) |

After editing, run `/transconfig reload` in-game.

---

## 8. FAQ

**Q: Game crashes — missing Cloth Config?**  
A: Install **Cloth Config NeoForge 1.21.1** (15.0.127+); not the Fabric build.

**Q: API configured but still "not configured"?**  
A: Check SecretId/SecretKey for extra spaces; run `/transconfig translator`; try `/transconfig reload`.

**Q: Is ModMenu required?**  
A: No. Use **U** or `/transconfig config`.

**Q: Which Tencent region?**  
A: Common values: `ap-guangzhou`, `ap-shanghai`, `ap-beijing` — match your console region.

---

## 9. Related docs

- [DEV.md](DEV.md) — Development & build
- [TEST.md](TEST.md) — Testing checklist
- [MAINTAIN.md](MAINTAIN.md) — Maintenance & releases
