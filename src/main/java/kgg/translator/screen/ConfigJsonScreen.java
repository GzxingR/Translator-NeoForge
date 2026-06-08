package kgg.translator.screen;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import kgg.translator.TranslatorConfig;
import kgg.translator.modmenu.ModMenuApiImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModList;

public class ConfigJsonScreen extends Screen {
    private EditBox configFieldWidget;
    private boolean needLoad = false;

    public ConfigJsonScreen() {
        super(Component.translatable("translator.configscreen.title"));
    }

    private void load() {
        JsonObject object = new JsonObject();
        TranslatorConfig.writeConfig(object);
        configFieldWidget.setValue(object.toString());
    }

    @Override
    protected void init() {
        configFieldWidget = new EditBox(font, width / 2 - 100, height / 4 + 30, 200, 20, Component.translatable("translator.configscreen.edit"));
        configFieldWidget.setMaxLength(99999);
        if (needLoad) {
            load();
            needLoad = false;
        } else {
            load();
        }
        addRenderableWidget(configFieldWidget);
        addRenderableWidget(Button.builder(Component.translatable("translator.configscreen.save"), e -> save()).bounds(width / 2 - 100, height / 4 + 96, 200, 20).build());
        Button btn = addRenderableWidget(Button.builder(Component.translatable("translator.configscreen.modmenu"), e -> {
            needLoad = true;
            minecraft.setScreen(ModMenuApiImpl.createScreen(this));
        }).bounds(width / 2 - 100, height / 4 + 118, 200, 20).build());
        btn.active = ModList.get().isLoaded("cloth_config");
    }

    private void save() {
        String str = configFieldWidget.getValue();
        LocalPlayer player = minecraft.player;
        try {
            JsonObject object = JsonParser.parseString(str).getAsJsonObject();
            boolean read = TranslatorConfig.readConfig(object);
            if (player != null) {
                player.displayClientMessage(Component.literal(read ? "OK" : "Failed to load config"), false);
            }
        } catch (JsonSyntaxException e) {
            if (player != null) {
                player.displayClientMessage(Component.literal("Invalid json"), false);
            }
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredString(font, title, width / 2, 15, 0xFFFFFF);
    }
}
