# Bug 报告

> 最后更新: 2026-06-09
> 版本: 0.0.1 (NeoForge 1.21.1)

## 严重 Bug

### B1. `BingTranslator.checkSuccess()` — 错误检测逻辑取反 + JsonNull NPE

- **文件**: `src/main/common/kgg/translator/translator/BingTranslator.java:85`
- **严重程度**: ⚠️ 高 — 可能导致翻译失败时静默吞掉错误，或 JsonNull 上调用 `getAsString()` 抛异常

**问题描述**:

```java
if (result.isJsonObject() && (code=result.getAsJsonObject().get("statusCode")).isJsonNull()){
    throw new ErrorCodeException("bing", code.getAsString());
}
```

1. 当结果不是 JsonObject（即 JsonArray，成功情况）→ 条件 false，不抛异常 ✅
2. 当结果是 JsonObject 且 statusCode=401（错误情况）→ `code.isJsonNull()` 为 false → 条件 false，**不抛异常** ❌（应该抛）
3. 当结果是 JsonObject 但没有 statusCode 字段 → `get()` 返回 `JsonNull.INSTANCE`，`isJsonNull()` 为 true → 抛 ErrorCodeException，但 `code.getAsString()` 在 `JsonNull` 上调用抛 `IllegalStateException` ❌

**修复**: 见 Fix 章节。

---

### B2. `TipHandler.handle()` — 分行数大于原文本行数时 IndexOutOfBoundsException

- **文件**: `src/main/java/kgg/translator/handler/TipHandler.java:89`
- **严重程度**: ⚠️ 高 — 当翻译结果行数多于原始 Tooltip 行数时直接崩溃

**问题描述**:

```java
translatedOrderedText = new FormattedCharSequence[translatedLines.length];
for (int i = 0; i < translatedLines.length; i++) {
    translatedOrderedText[i] = TextUtil.toText(translatedLines[i], texts.get(i)).getVisualOrderText();
}
```

如果 `translatedLines.length > texts.size()`，循环中 `texts.get(i)` 抛出 `IndexOutOfBoundsException`。

**修复**: 取两者较小值作为循环边界。

---

### B3. `ChatHandler.removeTip()` — `removeLast` 无边界保护

- **文件**: `src/main/java/kgg/translator/handler/ChatHandler.java:67`
- **严重程度**: ⚠️ 高 — 如果消息没有翻译提示符，连续两次 `removeLast()` 抛出 `NoSuchElementException`

**问题描述**:

```java
text.getSiblings().removeLast();
text.getSiblings().removeLast();
```

代码假设末尾一定有」+ 翻译提示符两个 sibling，但 `addTip` 可能没被调用过。

**修复**: 检查 siblings 数量是否 >= 2，或只在 `getTranslateClickEvent` 返回非空时移除。

---

### B4. `OcrScreen.render()` — 在 `init()` 前渲染时 `font` 为 null

- **文件**: `src/main/java/kgg/translator/screen/OcrScreen.java:51-76`
- **严重程度**: ⚠️ 高 — OCR 结果返回后渲染但 font 未初始化时 NPE

**问题描述**:

```java
context.drawString(font, esc, width - font.width(esc), ...)
```

`Screen.font` 在 `init()` 中赋值。如果 `render()` 在 `init()` 之前被调用（如屏幕切换动画期间），`font` 为 null → NPE。

**修复**: 在每次渲染前检查 `font != null`。

---

### B5. `TranslateHelper` — `LinkedHashMap` 非线程安全

- **文件**: `src/main/java/kgg/translator/handler/TranslateHelper.java:19`
- **严重程度**: ⚠️ 高 — 多线程同时 `computeIfAbsent` 可能产生 `ConcurrentModificationException`

**问题描述**:

```java
private static final LinkedHashMap<String, TranslationStatus> stateMap = new LinkedHashMap<>(16, 0.75f, true) { ... };
```

`computeIfAbsent` 在非 `ConcurrentHashMap` 上不保证原子性。多个线程同时调用时可能抛出 `ConcurrentModificationException`。

**修复**: 使用 `ConcurrentHashMap` + 显式 `synchronized` 块包装 LRU 行为，或用 `Collections.synchronizedMap`。

---

## 中等 Bug

### B6. `ChatHandler.translateWithTip()` — 异步修改 Component 的线程安全问题

- **文件**: `src/main/java/kgg/translator/handler/ChatHandler.java:108-112`
- **严重程度**: ⚡ 中 — 多线程修改 Component siblings 可能导致渲染不一致

**问题描述**:

`translate()` 先调用 `CompletableFuture.supplyAsync` 异步翻译，然后在 `thenAccept` 回调中直接调用 `text.getSiblings().set(...)` / `text.getSiblings().add(...)` 修改 Component。这些修改应在主线程进行。

**修复**: 将对 Component 的修改包装在 `Minecraft.getInstance().execute()` 中。

---

### B7. `TipHandler` — `translatedOrderedText` 和 `drawTranslateText` 缺少 volatile

- **文件**: `src/main/java/kgg/translator/handler/TipHandler.java:26-27`
- **严重程度**: ⚡ 中 — 异步线程写入、主线程读取，无内存屏障可能导致读取到过时值

**修复**: 给共享可写字段加上 `volatile` 关键字。

---

### B8. `SignTextMixin` — 使用 `@Overwrite` 破坏跨版本兼容

- **文件**: `src/main/java/kgg/translator/mixin/world/SignTextMixin.java`
- **严重程度**: ⚡ 中 — 如果 MC 更新后 `getRenderMessages` 签名改变，`@Overwrite` 静默失败

**问题描述**: `@Overwrite` 直接覆盖整个方法体。当 MC 1.21.2+ 修改此方法时，Mixin 静默失败而不是像 inject 那样优雅降级。

**修复**: 改用 `@Inject` + 调用原始方法（需要考虑具体重构方式，当前为 Overwrite 由于重写逻辑较复杂，暂不修改，标注为已知问题）。

---

### B9. `Translator` — 使用了已废弃的 `NotImplementedException`

- **文件**: `src/main/common/kgg/translator/translator/Translator.java:22`
- **严重程度**: 🟢 低 — 编译警告，不影响运行

```java
throw new NotImplementedException();
```

`org.apache.commons.lang3.NotImplementedException` 已废弃。

**修复**: 改用 `UnsupportedOperationException` 或自定义异常。

---

### B10. `BingTranslator` — 缺少频率限制

- **文件**: `src/main/common/kgg/translator/translator/BingTranslator.java`
- **严重程度**: 🟢 低 — 高频调用可能被 Bing 暂时封禁

百度/有道/腾讯都有 `delayTime` / QPS 控制，Bing 没有。

**修复**: 添加简单的限速机制（父类 `Translator` 的 `delay()` 方法可用于此）。

---

### B11. `ModMenuApiImpl` — 反射访问私有字段 `LLMManager.prompt`

- **文件**: `src/main/java/kgg/translator/modmenu/ModMenuApiImpl.java:212-214`
- **严重程度**: ⚡ 中 — 反射修改私有静态字段脆弱，未来代码重构时可能静默失败

```java
Field promptField = LLMManager.class.getDeclaredField("prompt");
promptField.setAccessible(true);
promptField.set(null, prompt);
```

**修复**: 在 `LLMManager` 中添加公开的 `setPrompt(String)` 方法。

---

## 结构优化建议

### S1. `LLMManager.addModel()` 当 model.name 为空时行为未定义

如果 `Model.name` 为 `""`，后续查找和删除该模型会产生歧义。

### S2. `TranslatorConfig.readFile()` 在配置文件不存在时不优雅

首次运行时 `ConfigUtil.load(file)` 尝试解析不存在的文件会抛异常，被 catch 后打印 `Failed to read config file`。虽然不算 bug，但对用户体验不友好。

### S3. `BingTranslator` 在 `TranslatorMod` 中未被注册

`BingTranslator` 类存在于 `common` 包中，但 `TranslatorMod` 的构造函数中没有注册它。用户无法在游戏中选择 Bing 翻译。

### S4. `harness/` 模块未被纳入测试流程

之前创建的 harness 模块没有集成到 CI 测试或开发者流程中。

---

## 测试清单

| ID | 状态 | 说明 |
|----|------|------|
| B1 | ✅ 已修复 | Bing.checkSuccess — 逻辑取反 + NPE |
| B2 | ✅ 已修复 | TipHandler 数组越界 |
| B3 | ✅ 已修复 | ChatHandler.removeTip 无保护 removeLast |
| B4 | ✅ 已修复 | OcrScreen font null 检查 |
| B5 | ✅ 已修复 | TranslateHelper ConcurrentHashMap |
| B6 | ✅ 已修复 | ChatHandler Component 修改移入主线程 |
| B7 | ✅ 已修复 | TipHandler volatile 关键字 |
| B8 | ⚪ 观察 | @Overwrite 兼容性（需 MC 跨版本时调整） |
| B9 | ✅ 已修复 | NotImplementedException → UnsupportedOperationException |
| B10 | ✅ 已修复 | BingTranslator 增加 minDelayMs 限速 |
| B11 | ✅ 已修复 | LLMManager.setPrompt() 公开方法替代反射 |
