package kgg.translator.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConfigUtil {
    public static JsonObject load(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] contentBytes = fis.readAllBytes();
            String content = new String(contentBytes, StandardCharsets.UTF_8);
            return (JsonObject) JsonParser.parseString(content);
        }
    }

    public static void save(File file, JsonObject object) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(object.toString().getBytes(StandardCharsets.UTF_8));
        }
    }
}
