package kgg.translator.mixin;

import kgg.translator.handler.ChatHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Screen.class)
public class ScreenMixinForChat {
    @Redirect(method = "handleComponentClicked", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V", ordinal = 2, remap = false))
    public void translator$handleClick(org.slf4j.Logger instance, String s, Object o) {
        if (o instanceof ChatHandler.TranslateClickEvent event) {
            if (event.text != null) {
                ChatHandler.translateWithTip(event.text);
            }
        } else {
            instance.error(s, o);
        }
    }
}
