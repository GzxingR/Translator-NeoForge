package kgg.translator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LanguageTest {
    private static final String LANGUAGE_JSON = "{ \"default\": {\"auto\": \"auto\", \"zh_cn\": \"zh\", \"en_us\": \"en\"}, \"regex\": {\"zh_cn\": \"[\\\\u4e00-\\\\u9fa5]\"}, \"translator\": {\"test\": {\"zh_cn\": \"zh-CHS\"}} }";

    @BeforeEach
    void setUp() {
        Language.clear();
        Language.load(LANGUAGE_JSON);
    }

    @Test
    void mapsRightToLeftLanguage() {
        assertEquals("zh_cn", Language.getLeftLang("test", "zh-CHS"));
    }

    @Test
    void mapsLeftToRightLanguage() {
        assertEquals("zh-CHS", Language.getRightLang("test", "zh_cn"));
    }

    @Test
    void resolvesMinecraftLocaleAliases() {
        assertEquals("zh", Language.resolveApiLang("腾讯翻译", "zh-cn"));
        assertEquals("zh", Language.resolveApiLang("腾讯翻译", "zh_cn"));
        assertEquals("en", Language.resolveApiLang("腾讯翻译", "en-us"));
        assertEquals("zh", Language.resolveApiLang(null, "zh-cn"));
    }

    @Test
    void detectsChineseWithPredicate() {
        assertTrue(Language.getPredicate("zh_cn").test("\u4f60\u597d"));
        assertFalse(Language.getPredicate("zh_cn").test("hello"));
    }
}