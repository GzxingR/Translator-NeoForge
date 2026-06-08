package kgg.translator.mixin.hud;

import kgg.translator.handler.ChatHandler;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @Inject(method = "init", at = @At("HEAD"))
    private void translator$init(CallbackInfo ci) {
        ChatHandler.addTip();
    }

    @Inject(method = "removed", at = @At("HEAD"))
    private void translator$removed(CallbackInfo ci) {
        ChatHandler.removeTip();
    }
}
