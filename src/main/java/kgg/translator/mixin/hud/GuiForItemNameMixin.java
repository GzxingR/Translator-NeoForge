package kgg.translator.mixin.hud;

import kgg.translator.handler.TranslateHelper;
import kgg.translator.option.Options;
import kgg.translator.translator.Source;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Gui.class)
public abstract class GuiForItemNameMixin {

    // 1.21.5: renderSelectedItemName(GuiGraphics, int) — 包含实际的 drawStringWithBackdrop 调用
    @ModifyArg(
        method = "renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;I)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawStringWithBackdrop(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIII)I"),
        index = 1,
        require = 0
    )
    private Component translator$translateSelectedItemNameWithShift(Component text) {
        if (!Options.autoItemName.get()) {
            return text;
        }
        return TranslateHelper.translateNoWait(text, Source.ITEM_NAME);
    }

    // 1.21.1: renderSelectedItemName(GuiGraphics) — 可能直接包含 drawStringWithBackdrop
    @ModifyArg(
        method = "renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawStringWithBackdrop(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIII)I"),
        index = 1,
        require = 0
    )
    private Component translator$translateSelectedItemNameNoShift(Component text) {
        if (!Options.autoItemName.get()) {
            return text;
        }
        return TranslateHelper.translateNoWait(text, Source.ITEM_NAME);
    }
}
