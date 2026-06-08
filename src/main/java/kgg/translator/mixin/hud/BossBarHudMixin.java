package kgg.translator.mixin.hud;

import kgg.translator.handler.TranslateHelper;
import kgg.translator.option.Options;
import kgg.translator.translator.Source;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BossHealthOverlay.class)
public class BossBarHudMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/LerpingBossEvent;getName()Lnet/minecraft/network/chat/Component;"))
    public Component render(LerpingBossEvent instance) {
        if (!Options.autoBossBar.get()) {
            return instance.getName();
        }
        return TranslateHelper.translateNoWait(instance.getName(), Source.BOSS_BAR);
    }
}
