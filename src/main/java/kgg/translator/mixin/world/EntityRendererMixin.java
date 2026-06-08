package kgg.translator.mixin.world;

import kgg.translator.handler.TranslateHelper;
import kgg.translator.option.Options;
import kgg.translator.translator.Source;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @ModifyVariable(method = "renderNameTag", at = @At("HEAD"), ordinal = 0)
    private Component translator$modifyNameTag(Component text, Entity entity) {
        if (!Options.autoEntityName.get() || entity == null) {
            return text;
        }
        if (!Options.inRange(new Vec3(entity.getX(), entity.getY(), entity.getZ()))) {
            return text;
        }
        if (entity instanceof net.minecraft.world.entity.player.Player) {
            if (Options.autoPlayerName.get()) {
                return TranslateHelper.translateNoWait(text, Source.PLAYER_NAME);
            }
            return text;
        }
        return TranslateHelper.translateNoWait(text, Source.ENTITY_NAME);
    }
}
