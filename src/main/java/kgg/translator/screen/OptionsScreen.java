package kgg.translator.screen;

import kgg.translator.TranslatorConfig;
import kgg.translator.event.OptionsScreenAddBodyEvent;
import kgg.translator.option.Options;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

public class OptionsScreen extends OptionsSubScreen {
    public OptionsScreen() {
        super(null, Minecraft.getInstance().options, Component.translatable("translator.optionscreen.title"));
    }

    @Override
    protected void addOptions() {
        this.list.addBig(Options.autoChat);
        this.list.addBig(Options.chatTip);
        this.list.addBig(Options.autoTooltip);
        this.list.addBig(Options.autoScoreboard);
        this.list.addBig(Options.autoBossBar);
        this.list.addBig(Options.autoTitle);
        this.list.addBig(Options.autoSign);
        this.list.addBig(Options.signCombine);
        this.list.addBig(Options.autoEntityName);
        this.list.addBig(Options.distance);
        this.list.addBig(Options.autoPlayerName);
        OptionsScreenAddBodyEvent.fire(this.list);
    }

    @Override
    public void onClose() {
        TranslatorConfig.writeFile();
        super.onClose();
    }
}
