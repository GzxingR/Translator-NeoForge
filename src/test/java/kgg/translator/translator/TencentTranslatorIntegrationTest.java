package kgg.translator.translator;

import com.google.gson.JsonObject;
import kgg.translator.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class TencentTranslatorIntegrationTest {
    private static final String LANGUAGE_JSON = """
            {
              "default": {"auto": "auto", "zh_cn": "zh", "en_us": "en"},
              "regex": {"zh_cn": "[\\\\u4e00-\\\\u9fa5]", "en_us": "^[a-zA-Z0-9\\\\s\\\\W_]+$"},
              "translator": {
                "腾讯翻译": {"auto": "auto", "zh_cn": "zh", "en_us": "en"}
              }
            }
            """;

    @BeforeEach
    void setUp() {
        Language.clear();
        Language.load(LANGUAGE_JSON);
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "TENCENT_SECRET_ID", matches = ".+")
    void liveTranslateEnglishToChinese() throws Exception {
        TencentTranslator translator = new TencentTranslator() {
            @Override
            public void read(JsonObject object) {}

            @Override
            public void write(JsonObject object) {}
        };
        translator.setConfig(
                System.getenv("TENCENT_SECRET_ID"),
                System.getenv("TENCENT_SECRET_KEY"),
                "ap-guangzhou",
                0
        );
        translator.setDelayTime(0);

        String result = translator.translate("hello", "en", "zh");
        assertNotNull(result);
        assertFalse(result.isBlank());
        assertNotEquals("hello", result);
    }

    @Test
    void resolvesLegacyConfigLanguageCodes() {
        assertEquals("zh", Language.resolveApiLang("腾讯翻译", "zh-cn"));
    }

    @Test
    void canLoadCreativeCoOperationConfigWhenPresent() throws Exception {
        Path configPath = Path.of(System.getenv("APPDATA"), ".minecraft", "versions", "Creative Co-Operation", "config", "translator.json");
        if (!Files.exists(configPath)) {
            return;
        }
        String json = Files.readString(configPath, StandardCharsets.UTF_8);
        assertTrue(json.contains("secretId") || json.contains("from"));
    }
}
