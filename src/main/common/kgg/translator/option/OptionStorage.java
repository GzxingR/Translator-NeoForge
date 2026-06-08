package kgg.translator.option;

import com.google.gson.JsonObject;

public interface OptionStorage {
    void readAll(JsonObject config);

    void writeAll(JsonObject config);
}
