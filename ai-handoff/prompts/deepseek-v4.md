# DeepSeek V4 Pro 专用提示词

## 使用方式

1. 在 DeepSeek 对话中选择 **系统提示词** 或首条消息粘贴 [SYSTEM.md](SYSTEM.md) 全文。
2. 再粘贴下方 **DeepSeek 补充指令**。
3. 具体任务从 [task-templates.md](task-templates.md) 复制对应模板。

---

## DeepSeek 补充指令（追加到系统提示词后）

```
【DeepSeek 工作模式】

你正在维护 Minecraft NeoForge 1.21.x 模组仓库 Translator-NeoForge。

工作原则：
1. 每次只解决一个明确问题；输出可运行的最小补丁。
2. 修改 Java 前先说明影响的层（common / neoforge / mixin / test）。
3. 涉及网络 API 时，检查 TencentCloudSignUtil、RequestUtil，不要重复引入 HttpClient 受限头。
4. 涉及聊天/Component 时，检查 ChatHandler.ensureMutable 与 ChatHudMixin。
5. 涉及 TextDisplay 时，看 TextDisplayEntityMixin 与 TranslateHelper。
6. 完成修改后，列出应运行的命令：./gradlew test build

若缺少文件内容，请明确列出需要用户粘贴的路径，不要猜测 Mixin 目标方法名。

仓库根目录相对路径示例：
- src/main/common/kgg/translator/TranslateService.java
- src/main/java/kgg/translator/handler/ChatHandler.java
- src/main/resources/translator.mixins.json
```

---

## API 调用示例（可选，用于外部自动化）

若通过 DeepSeek API 驱动脚本开发，推荐消息结构：

```json
{
  "model": "deepseek-chat",
  "messages": [
    {
      "role": "system",
      "content": "<粘贴 SYSTEM.md 全文 + 上方 DeepSeek 补充指令>"
    },
    {
      "role": "user",
      "content": "<从 task-templates.md 复制的具体任务>"
    }
  ],
  "temperature": 0.2
}
```

**注意**：不要把 `DEEPSEEK_API_KEY`、Modrinth Token、GitHub PAT 写进仓库；使用环境变量。

---

## 推荐 DeepSeek 任务流

| 步骤 | 动作 |
|------|------|
| 1 | 粘贴 SYSTEM + 本文件补充指令 |
| 2 | 粘贴 `project/OVERVIEW.md` 中与任务相关的章节 |
| 3 | 粘贴待修改文件的完整内容或 diff |
| 4 | 使用 task-templates 中的验收标准 |
| 5 | 本地 `ai-handoff/tools/test.ps1` 验证 |
