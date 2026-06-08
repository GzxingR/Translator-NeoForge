# Modrinth 发布指南（NeoForge 1.21.1）

本项目使用 [Minotaur](https://github.com/modrinth/minotaur)，发布到 **[Translator (NeoForge)](https://modrinth.com/mod/translator-neoforge)**（slug: `translator-neoforge`）。

## 页面语言

| 文件 | 语言 | 用途 |
|------|------|------|
| `modrinth/body.md` | **English（主）** | 同步到 Modrinth（`modrinthSyncBody`） |
| `modrinth/body_zh-CN.md` | 简体中文（辅） | 仅 GitHub，链自英文页底部 |
| `README_zh-CN.md` | 简体中文（辅） | GitHub 中文简介 |

## 一、发布前检查

- [ ] `./gradlew test build` 通过
- [ ] `gradle.properties` 中 `mod_version` 已更新
- [ ] `CHANGELOG.md` 已填写
- [ ] 已配置 Modrinth API Token

## 二、命令行发布

```powershell
$env:MODRINTH_TOKEN = "你的Token"
./scripts/publish-modrinth.ps1
```

同步 Modrinth **英文** 长描述：

```powershell
./gradlew modrinthSyncBody --no-configuration-cache
```

## 三、GitHub Release

```bash
git tag -a v1.21.1-0.0.1 -m "Release"
git push origin v1.21.1-0.0.1
```

## 四、依赖 Modrinth ID

| Mod | Project ID |
|-----|------------|
| Cloth Config API | `9s6osm5g` |
