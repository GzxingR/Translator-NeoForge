# Modrinth 页面文案

| 文件 | 语言 | 用途 |
|------|------|------|
| `body_zh-CN.md` | 简体中文 | **默认同步到 Modrinth**（`modrinthSyncBody`） |
| `body_en.md` | English | 英文版备份，链自中文页 |
| `body.md` | — | 已弃用，请改 `body_zh-CN.md` |

同步命令：

```powershell
$env:MODRINTH_TOKEN = "你的Token"
./gradlew modrinthSyncBody --no-configuration-cache
```
