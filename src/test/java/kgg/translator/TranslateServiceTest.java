package kgg.translator;

import com.google.gson.JsonObject;
import kgg.translator.translator.Translator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TranslateServiceTest {
    private static final String LANGUAGE_JSON = "{ \"default\": {\"auto\": \"auto\", \"zh_cn\": \"zh\", \"en_us\": \"en\"}, \"regex\": {\"zh_cn\": \"[\\\\u4e00-\\\\u9fa5]\", \"en_us\": \"^[a-zA-Z0-9\\\\s\\\\W_]+$\"}, \"translator\": {\"test\": {}} }";

    private final Translator stubTranslator = new Translator() {
        @Override
        public String getName() { return "test"; }
        @Override
        public void read(JsonObject object) {}
        @Override
        public void write(JsonObject object) {}
    };

    @BeforeEach
    void setUp() {
        Language.clear();
        Language.load(LANGUAGE_JSON);
        TranslateService.clearCache();
    }

    @Test
    void skipsBlankText() {
        assertTrue(TranslateService.shouldSkipTranslation(stubTranslator, "   ", "zh"));
    }

    @Test
    void skipsNumericText() {
        assertTrue(TranslateService.shouldSkipTranslation(stubTranslator, "12345", "zh"));
    }

    @Test
    void doesNotSkipEnglishWhenTargetIsChinese() {
        assertFalse(TranslateService.shouldSkipTranslation(stubTranslator, "hello world", "zh"));
    }

    @Test
    void skipsChineseWhenTargetIsChinese() {
        assertTrue(TranslateService.shouldSkipTranslation(stubTranslator, "\u4f60\u597d\u4e16\u754c", "zh"));
    }
}