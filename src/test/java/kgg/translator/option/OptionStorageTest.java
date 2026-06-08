package kgg.translator.option;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OptionStorageTest {
    @Test
    void readAllAndWriteAllRoundTrip() {
        Map<String, Boolean> values = new HashMap<>();
        OptionStorage storage = new OptionStorage() {
            @Override
            public void readAll(JsonObject config) {
                values.clear();
                config.entrySet().forEach(e -> values.put(e.getKey(), e.getValue().getAsBoolean()));
            }

            @Override
            public void writeAll(JsonObject config) {
                values.forEach(config::addProperty);
            }
        };

        JsonObject in = new JsonObject();
        in.addProperty("auto_chat", true);
        in.addProperty("chat_tip", false);
        storage.readAll(in);
        assertTrue(values.get("auto_chat"));
        assertFalse(values.get("chat_tip"));

        JsonObject out = new JsonObject();
        storage.writeAll(out);
        assertTrue(out.get("auto_chat").getAsBoolean());
        assertFalse(out.get("chat_tip").getAsBoolean());
    }
}