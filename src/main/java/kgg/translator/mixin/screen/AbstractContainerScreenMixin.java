package kgg.translator.mixin.screen;

import kgg.translator.handler.TranslateHelper;
import kgg.translator.option.Options;
import kgg.translator.translator.Source;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

    @ModifyArg(
        method = "renderLabels",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I"),
        index = 1
    )
    private Component translator$translateContainerLabel(Component text) {
        if (!Options.autoContainer.get()) {
            return text;
        }
        return TranslateHelper.translateNoWait(text, Source.CONTAINER);
    }
}
