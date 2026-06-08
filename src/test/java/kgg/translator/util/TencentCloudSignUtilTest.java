package kgg.translator.util;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TencentCloudSignUtilTest {
    @Test
    void buildsRequiredHeaders() throws Exception {
        String payload = "{\"SourceText\":\"hello\",\"Source\":\"en\",\"Target\":\"zh\",\"ProjectId\":0}";
        Map<String, String> headers = TencentCloudSignUtil.buildAuthHeaders(
                "AKIDEXAMPLE",
                "SecretKeyExample",
                "tmt",
                "tmt.tencentcloudapi.com",
                "ap-guangzhou",
                "TextTranslate",
                "2018-03-21",
                payload
        );

        assertTrue(headers.get("Authorization").startsWith("TC3-HMAC-SHA256 Credential=AKIDEXAMPLE/"));
        assertEquals("TextTranslate", headers.get("X-TC-Action"));
        assertEquals("2018-03-21", headers.get("X-TC-Version"));
        assertEquals("ap-guangzhou", headers.get("X-TC-Region"));
        assertNotNull(headers.get("X-TC-Timestamp"));
    }

    @Test
    void requestHeadersCanBeAppliedToHttpClient() throws Exception {
        String payload = "{\"SourceText\":\"hello\",\"Source\":\"en\",\"Target\":\"zh\",\"ProjectId\":0}";
        Map<String, String> headers = TencentCloudSignUtil.buildAuthHeaders(
                "AKIDEXAMPLE",
                "SecretKeyExample",
                "tmt",
                "tmt.tencentcloudapi.com",
                "ap-guangzhou",
                "TextTranslate",
                "2018-03-21",
                payload
        );
        var builder = java.net.http.HttpRequest.newBuilder(java.net.URI.create("https://tmt.tencentcloudapi.com"))
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(payload));
        assertDoesNotThrow(() -> TencentCloudSignUtil.applyRequestHeaders(builder, headers));
    }
}