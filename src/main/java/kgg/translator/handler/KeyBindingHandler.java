package kgg.translator.handler;

import kgg.translator.screen.OptionsScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class KeyBindingHandler {
    public static final KeyMapping TRANSLATE_SCREEN_KEY = new KeyMapping("key.translator.options", GLFW.GLFW_KEY_U, "key.categories.translator");
    public static final KeyMapping OCR_KEY = new KeyMapping("key.translator.ocr", GLFW.GLFW_KEY_O, "key.categories.translator");

    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(TRANSLATE_SCREEN_KEY);
        event.register(OCR_KEY);
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) {
            return;
        }
        while (TRANSLATE_SCREEN_KEY.consumeClick()) {
            client.setScreen(new OptionsScreen());
        }
        while (OCR_KEY.consumeClick()) {
            OcrHandler.start();
        }
    }
}
