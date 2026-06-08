# Modrinth 发布指南（NeoForge 1.21.1）

本项目使用 [Minotaur](https://github.com/modrinth/minotaur)，发布到 **[Translator (NeoForge)](https://modrinth.com/mod/translator-neoforge)**（slug: `translator-neoforge`）。

## 一、发布前检查

- [ ] `./gradlew test build` 通过
- [ ] `gradle.properties` 中 `mod_version` 已更新；Modrinth 版本号为 `1.21.1-<mod_version>`
- [ ] `CHANGELOG.md` 已填写本版本说明
- [ ] 已在 [Modrinth 账户设置](https://modrinth.com/settings/account) 创建 API Token（`CREATE_VERSION`、`PROJECT_WRITE`）

## 二、命令行发布

```powershell
$env:MODRINTH_TOKEN = "你的Token"
./scripts/publish-modrinth.ps1
```

同步 Modrinth 长描述：

```powershell
./gradlew modrinthSyncBody
```

## 三、GitHub Release

```bash
git tag -a v1.21.1-0.0.1 -m "Release"
git push origin v1.21.1-0.0.1
```

仓库 Secrets 配置 `MODRINTH_TOKEN` 后，`release.yml` 会在打 tag 时自动上传 Modrinth。

## 四、依赖 Modrinth ID

| Mod | Slug | Project ID |
|-----|------|------------|
| Cloth Config API | `cloth-config` | `9s6osm5g` |
