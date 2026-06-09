package kgg.translator.harness;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import kgg.translator.option.OptionStorage;

import java.util.HashMap;
import java.util.Map;

/**
 * 简易选项存储 —— 用于在无 Minecraft 环境下测试核心翻译流程。
 */
public class HarnessOptionStorage implements OptionStorage {
    private final Map<String, Object> options = new HashMap<>();

    @Override
    public void readAll(JsonObject config) {
        config.entrySet().forEach(entry -> {
            JsonElement element = entry.getValue();
            if (element.isJsonPrimitive()) {
                var prim = element.getAsJsonPrimitive();
                if (prim.isBoolean()) {
                    options.put(entry.getKey(), prim.getAsBoolean());
                } else if (prim.isNumber()) {
                    options.put(entry.getKey(), prim.getAsInt());
                } else {
                    options.put(entry.getKey(), prim.getAsString());
                }
            } else {
                options.put(entry.getKey(), element.toString());
            }
        });
    }

    @Override
    public void writeAll(JsonObject config) {
        options.forEach((key, value) -> {
            if (value instanceof Boolean b) {
                config.addProperty(key, b);
            } else if (value instanceof Integer i) {
                config.addProperty(key, i);
            } else if (value instanceof String s) {
                config.addProperty(key, s);
            } else {
                config.addProperty(key, String.valueOf(value));
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) options.get(key);
    }

    public void set(String key, Object value) {
        options.put(key, value);
    }
}
