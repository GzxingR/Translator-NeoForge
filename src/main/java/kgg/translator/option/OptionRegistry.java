package kgg.translator.option;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

public class OptionRegistry implements OptionStorage {
    public static final OptionRegistry INSTANCE = new OptionRegistry();
    private static final Gson gson = new Gson();
    public static final Map<String, OptionInstance<?>> options = new HashMap<>();

    private OptionRegistry() {}

    @SuppressWarnings("unchecked")
    public static <T> void readJsonElement(OptionInstance<T> option, JsonElement element) {
        // Gson 2.11+ 禁止 TypeToken<T> 中使⽤类型变量，改为运⾏时判断类型
        T current = option.get();
        Object value;
        if (element.getAsJsonPrimitive().isBoolean()) {
            value = element.getAsBoolean();
        } else if (element.getAsJsonPrimitive().isNumber()) {
            if (current instanceof Integer) {
                value = element.getAsInt();
            } else if (current instanceof Double) {
                value = element.getAsDouble();
            } else {
                value = element.getAsNumber();
            }
        } else {
            value = element.getAsString();
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
