package kgg.translator.mixin;

import kgg.translator.handler.TipHandler;
import kgg.translator.option.Options;
import kgg.translator.util.TextUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiGraphics.class)
public abstract class DrawContextMixinTooltip {
    // 1.21.2+: renderTooltip(Font, List<Component>, int, int) — ResourceLocation parameter removed
    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V", at = @At("HEAD"), require = 0)
    public void onRenderTooltip(Font font, List<Component> text, int x, int y, CallbackInfo ci) {
        if (Options.autoTooltip.get()) {
            TipHandler.handle((GuiGraphics) (Object) this, text, x, y, 0.4f);
        }
    }
}
