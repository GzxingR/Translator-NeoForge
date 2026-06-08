package kgg.translator.translator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kgg.translator.Language;
import kgg.translator.exception.ErrorCodeException;
import kgg.translator.exception.TranslateException;
import kgg.translator.util.RequestUtil;
import kgg.translator.util.TencentCloudSignUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class TencentTranslator extends Translator {
    public static final String HOST = "tmt.tencentcloudapi.com";
    public static final String SERVICE = "tmt";
    public static final String ACTION = "TextTranslate";
    public static final String VERSION = "2018-03-21";

    protected String secretId = "";
    protected String secretKey = "";
    protected String region = "ap-guangzhou";
    protected int projectId = 0;
    protected int delayTime = 200;

    @Override
    protected synchronized String translate(String text, String from, String to) throws IOException {
        return delay(delayTime, () -> {
            String source = resolveSource(Language.resolveApiLang(getName(), from), text);
            JsonObject body = new JsonObject();
            body.addProperty("SourceText", text);
            body.addProperty("Source", source);
            body.addProperty("Target", Language.resolveApiLang(getName(), to));
            body.addProperty("ProjectId", projectId);
            String payload = body.toString();

            HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create("https://" + HOST))
                    .POST(HttpRequest.BodyPublishers.ofString(payload));
            try {
                TencentCloudSignUtil.applyRequestHeaders(builder, TencentCloudSignUtil.buildAuthHeaders(
                        secretId, secretKey, SERVICE, HOST, region, ACTION, VERSION, payload));
            } catch (Exception e) {
                throw new TranslateException("translator.error.tencent.SignFailed", e);
            }

            String result = RequestUtil.send(builder.build(), HttpResponse.BodyHandlers.ofString()).body();
            JsonObject root = JsonParser.parseString(result).getAsJsonObject();
            JsonObject response = root.getAsJsonObject("Response");
            if (response.has("Error")) {
                JsonObject error = response.getAsJsonObject("Error");
                throw new ErrorCodeException("tencent", error.get("Code").getAsString());
            }
            return response.get("TargetText").getAsString();
        });
    }

    private String resolveSource(String from, String text) {
        if (!"auto".equals(from)) {
            return from;
        }
        if (Language.getPredicate("zh_cn").test(text)) {
            return "zh";
        }
        if (Language.getPredicate("en_us").test(text)) {
            return "en";
        }
        return "auto";
    }

    public void setConfig(String secretId, String secretKey, String region, int projectId) {
        this.secretId = secretId;
        this.secretKey = secretKey;
        if (region != null && !region.isBlank()) {
            this.region = region;
        }
        this.projectId = projectId;
        setConfigured();
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    @Override
    public String getName() {
        return "腾讯翻译";
    }

    @Override
    public void read(JsonObject object) {
        setConfig(
                object.get("secretId").getAsString(),
                object.get("secretKey").getAsString(),
                object.has("region") ? object.get("region").getAsString() : region,
                object.has("projectId") ? object.get("projectId").getAsInt() : 0
        );
        if (object.has("delayTime")) {
            setDelayTime(object.get("delayTime").getAsInt());
        }
    }

    @Override
    public void write(JsonObject object) {
        object.addProperty("secretId", secretId);
        object.addProperty("secretKey", secretKey);
        object.addProperty("region", region);
        object.addProperty("projectId", projectId);
        object.addProperty("delayTime", delayTime);
    }
}