# Testing Guide

**English** | [中文 Chinese](TEST_zh.md)

## Unit tests

```bash
./gradlew test
```

Coverage:

| Test class | Focus |
|------------|-------|
| `StringUtilTest` | Formatting code stripping, blank checks |
| `LanguageTest` | Language mapping, regex predicates |
| `TranslateServiceTest` | Skip rules (blank/numeric/already target language) |
| `OptionStorageTest` | Config JSON read/write |

## Manual acceptance checklist

### Startup

- [ ] `./gradlew runClient` loads without mixin/AT crash
- [ ] Log shows `Config read successfully`

### Commands

- [ ] `/translate <text>` returns translation
- [ ] `/translate-re <text>` reverse translation
- [ ] `/transconfig translator <name>` switches translator
- [ ] `/transconfig language <source> <target>` sets languages
- [ ] `/transconfig clearcache` clears cache
- [ ] `/transconfig config` opens Cloth Config

### Hotkeys

- [ ] **U** opens translation options
- [ ] **O** starts screen OCR

### Auto-translate

- [ ] Chat (with auto_chat enabled)
- [ ] Tooltips (auto_tooltip)
- [ ] Boss bar (auto_boss_bar)
- [ ] Entity names (auto_entity_name)
- [ ] Player names (auto_player_name)
- [ ] Signs (auto_sign, including sign_combine)

### Configuration

- [ ] Switch translator and API keys in ModMenu / Cloth Config
- [ ] Add/remove LLM models; save prompt
- [ ] Config under `config/translator/` persists after restart

### Edge cases

- [ ] Distance option limits sign/entity translation range
- [ ] Blank/numeric-only text does not trigger translation

## Related docs

- [INSTALL.md](INSTALL.md) — Installation & commands
- [DEV.md](DEV.md) — Development guide
