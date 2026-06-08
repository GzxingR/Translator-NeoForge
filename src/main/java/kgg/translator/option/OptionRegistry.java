package kgg.translator.option;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class OptionRegistry implements OptionStorage {
    public static final OptionRegistry INSTANCE = new OptionRegistry();
    private static final Gson gson = new Gson();
    public static final Map<String, OptionInstance<?>> options = new HashMap<>();

    private OptionRegistry() {}

    public static <T> void readJsonElement(OptionInstance<T> option, JsonElement element) {
        Type type = new TypeToken<T>(){}.getType();
        Object value = gson.fromJson(element, type);
        if (option.get() instanceof Integer && value instanceof Double) {
            value = ((Double) value).intValue();
        }
        option.set((T) value);
    }

    public static JsonElement createJsonElement(OptionInstance<?> option) {
        return gson.toJsonTree(option.get());
    }

    public static <T> OptionInstance<T> register(String name, OptionInstance<T> option) {
        options.put(name, option);
        return option;
    }

    public static OptionInstance<Boolean> registerBool(String name, boolean defaultValue) {
        return register(name, OptionInstance.createBoolean(
                "translator.option." + name,
                OptionInstance.noTooltip(),
                defaultValue,
                v -> {}
        ));
    }

    public static OptionInstance<Boolean> registerBoolWithTooltip(String name, boolean defaultValue) {
        return register(name, OptionInstance.createBoolean(
                "translator.option." + name,
                OptionInstance.cachedConstantTooltip(Component.translatable("translator.option." + name + ".desc")),
                defaultValue,
                v -> {}
        ));
    }

    @Override
    public void readAll(JsonObject config) {
        options.forEach((key, value) -> {
            if (config.has(key)) {
                readJsonElement(value, config.get(key));
            }
        });
    }

    @Override
    public void writeAll(JsonObject config) {
        options.forEach((key, value) -> config.add(key, createJsonElement(value)));
    }
}
