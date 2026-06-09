package kgg.translator.mixin;

import kgg.translator.handler.ChatHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class ScreenMixinForChat {
    // MC 1.21.5: 拦截 Screen.handleComponentClicked，直接处理 kgg: 前缀的点击事件
    @Inject(method = "handleComponentClicked", at = @At("HEAD"), cancellable = true)
    private void translator$handleClick(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style != null) {
            ClickEvent clickEvent = style.getClickEvent();
            if (clickEvent instanceof ClickEvent.RunCommand cmd
                    && cmd.command() != null
                    && ChatHandler.handleClickCommand(cmd.command())) {
                cir.setReturnValue(true);
            }
        }
    }
}
