# 测试指南

## 单元测试

```bash
./gradlew test
```

覆盖范围：

| 测试类 | 覆盖点 |
|--------|--------|
| `StringUtilTest` | 格式化码剥离、空白判断 |
| `LanguageTest` | 语言映射、正则 predicate |
| `TranslateServiceTest` | 跳过规则（空白/数字/已为目标语言） |
| `OptionStorageTest` | 配置 JSON 读写接口 |

## 手动验收清单

### 启动

- [ ] `./gradlew runClient` 进入游戏，mod 加载无 mixin/AT 崩溃
- [ ] 日志中出现 `Config read successfully`

### 命令

- [ ] `/translate <文本>` 返回翻译结果
- [ ] `/translate-re <文本>` 反向翻译
- [ ] `/transconfig translator <名称>` 切换翻译器
- [ ] `/transconfig language <源> <目标>` 设置语言
- [ ] `/transconfig clearcache` 清理缓存
- [ ] `/translate config` 打开 Cloth Config

### 快捷键

- [ ] **U** 打开翻译选项（Options 子界面）
- [ ] **O** 启动 OCR 屏幕识别

### 自动翻译

- [ ] 聊天栏（开启 auto_chat 后）
- [ ] 工具提示（auto_tooltip）
- [ ] BOSS 血条（auto_boss_bar）
- [ ] 实体名称（auto_entity_name）
- [ ] 玩家名称（auto_player_name）
- [ ] 告示牌（auto_sign，含 sign_combine）

### 配置

- [ ] ModMenu / Cloth Config 中切换翻译器、填写 API Key
- [ ] LLM 模型增删、prompt 保存
- [ ] 重启后 `config/translator/` 配置保留

### 边界

- [ ] 距离选项（distance）限制告示牌/实体翻译范围
- [ ] 纯数字/空白文本不触发翻译