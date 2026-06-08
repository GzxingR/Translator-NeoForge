package kgg.translator.mixin.hud;

import kgg.translator.handler.TranslateHelper;
import kgg.translator.option.Options;
import kgg.translator.translator.Source;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Gui.class)
public abstract class GuiForTitleMixin {
    @ModifyArg(method = "renderTitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;width(Lnet/minecraft/network/chat/FormattedText;)I"), index = 0)
    public FormattedText translateTitleWidth(FormattedText text) {
        if (!Options.autoTitle.get() || !(text instanceof Component component)) {
            return text;
        }
        return TranslateHelper.translateNoWait(component, Source.TITLE);
    }

    @ModifyArg(method = "renderTitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawStringWithBackdrop(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIII)I"), index = 1)
    public Component translateTitleDraw(Component text) {
        if (!Options.autoTitle.get()) {
            return text;
        }
        return TranslateHelper.translateNoWait(text, Source.TITLE);
    }

    @ModifyArg(method = "renderOverlayMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;width(Lnet/minecraft/network/chat/FormattedText;)I"), index = 0)
    public FormattedText translateOverlayWidth(FormattedText text) {
        if (!Options.autoTitle.get() || !(text instanceof Component component)) {
            return text;
        }
        return TranslateHelper.translateNoWait(component, Source.TITLE);
    }

    @ModifyArg(method = "renderOverlayMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawStringWithBackdrop(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIII)I"), index = 1)
    public Component translateOverlayDraw(Component text) {
        if (!Options.autoTitle.get()) {
            return text;
        }
        return TranslateHelper.translateNoWait(text, Source.TITLE);
    }
}
