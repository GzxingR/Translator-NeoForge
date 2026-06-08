# AI 工具脚本

所有脚本从 **仓库根目录** 执行（脚本会自动 `cd` 到根目录）。

| 脚本 | 说明 |
|------|------|
| `build.ps1` | `./gradlew test build` |
| `test.ps1` | `./gradlew test` |
| `run-client.ps1` | `./gradlew runClient` |
| `publish-modrinth.ps1` | 转发到 `scripts/publish-modrinth.ps1` |
| `push-github.ps1` | 转发到 `scripts/push-github.ps1` |
| `check-secrets.ps1` | 扫描即将提交的文件是否含密钥模式 |

## 环境变量

复制 `env.example` 为本地环境变量（**不要** 提交 `.env`）：

```powershell
$env:MODRINTH_TOKEN = "..."
$env:GH_TOKEN = "..."
$env:TENCENT_SECRET_ID = "..."   # 仅本地联调测试
$env:TENCENT_SECRET_KEY = "..."
```

## DeepSeek / 其他 AI API

本仓库 **不包含** DeepSeek SDK；若用 API 自动化，在项目外单独建脚本，密钥放环境变量，不要放进 `ai-handoff/`。
