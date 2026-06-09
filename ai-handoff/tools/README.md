# AI 工具脚本

所有脚本从 **仓库根目录** 执行（脚本会自动 `cd` 到根目录）。

| 脚本 | 说明 |
|------|------|
| `build.ps1` | `./gradlew test build` |
| `test.ps1` | `./gradlew test` |
| `run-client.ps1` | `./gradlew runClient` |
| `publish-modrinth.ps1` | 转发到 `scripts/publish-modrinth.ps1` |
| `push-github.ps1` | 转发到 `scripts/push-github.ps1`（首次建仓库 / 推 main） |
| `push-branch.ps1` | 推送**当前分支**（AI 自动化首选） |
| `git-sync.ps1` | **add + commit + push** 一条龙（AI 自动化首选） |
| `check-secrets.ps1` | 扫描即将提交的文件是否含密钥模式 |

## 环境变量

GitHub 凭证推荐放在 **本机**（不要提交、不要贴进聊天）：

1. 复制 `%USERPROFILE%\.hanako\secrets\github.token.example` → `github.token`
2. 写入一行 GitHub PAT（`repo` 权限）
3. 运行一次：`powershell -ExecutionPolicy Bypass -File "$env:USERPROFILE\.hanako\setup-git-auth.ps1"`
4. **重启 HanaAgent**

之后 AI 只需调用 `git-sync.ps1` / `push-branch.ps1`，无需你手动 git。

Modrinth 等其它密钥仍用环境变量（见 `env.example`）。

## DeepSeek / 其他 AI API

本仓库 **不包含** DeepSeek SDK；若用 API 自动化，在项目外单独建脚本，密钥放环境变量，不要放进 `ai-handoff/`。
