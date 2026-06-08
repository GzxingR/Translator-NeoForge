# 任务模板 — 复制给 AI 的用户消息

---

## 模板 A：修复 Bug

```
【任务】修复 Bug

现象：
<描述游戏内或日志中的现象>

相关日志（如有）：
```
<粘贴 latest.log 片段>
```

怀疑文件：
- <路径>

要求：
1. 最小改动修复根因
2. ./gradlew test build 通过
3. 说明原因与测试方式
```

---

## 模板 B：新增自动翻译目标

```
【任务】为 <目标，如某 HUD/实体> 增加自动翻译

行为：
- 在 Options 中增加开关（如需要）
- 使用 TranslateService / TranslateHelper 现有流程
- 客户端 Mixin 注入，不修改服务端世界数据

验收：
- 开关关闭时不翻译
- 开关开启时正确翻译
- 中英文 lang 文件已更新
```

---

## 模板 C：对接新翻译 API

```
【任务】添加翻译后端 <名称>

参考现有实现：
- BaiduTranslator / TencentTranslator（common 层）
- *Impl.java + *ModMenuImpl.java（NeoForge 层）

要求：
- 配置写入 config/translator/（运行时，不进 Git）
- /transconfig 子命令或 Cloth Config 入口
- 错误信息走 TranslateExceptionUtil + lang 键
- 单元测试至少覆盖签名/参数归一化（如适用）
```

---

## 模板 D：版本发布

```
【任务】发布版本 <mod_version>

步骤：
1. 更新 gradle.properties mod_version
2. 更新 CHANGELOG.md
3. ./gradlew test build
4. git tag v<minecraft_version>-<mod_version>
5. push main 与 tag（触发 GitHub Release）

Modrinth slug: translator-neoforge
不要提交任何 Token。
```

---

## 模板 E：仅代码审查

```
【任务】审查以下改动，不直接改代码

关注点：
- 是否破坏 common/neoforge 分层
- Mixin 目标是否匹配 1.21.x
- 是否泄露密钥或写入 config/
- 线程/客户端-only 假设是否正确

<粘贴 diff 或文件路径列表>
```

---

## 模板 F：DeepSeek 一次性上下文包

```
以下为本项目 AI 上下文，请确认已读后再执行后续任务。

=== SYSTEM ===
<粘贴 prompts/SYSTEM.md>

=== 架构摘要 ===
<粘贴 project/ARCHITECTURE.md 前半>

=== 本次任务 ===
<你的具体需求>
```
