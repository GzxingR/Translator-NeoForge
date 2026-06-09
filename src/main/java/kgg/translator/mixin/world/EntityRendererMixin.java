package kgg.translator.mixin.world;

import kgg.translator.handler.TranslateHelper;
import kgg.translator.option.Options;
import kgg.translator.translator.Source;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * MC 1.21.5: renderNameTag 签名变为 (EntityRenderState, Component, PoseStack, MultiBufferSource, int)
 * EntityRenderState 包含坐标信息，通过其 x/y/z 字段获取实体位置
 */
@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @ModifyVariable(method = "renderNameTag", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Component translator$modifyNameTag(Component text, EntityRenderState state) {
        if (!Options.autoEntityName.get() || state == null) {
            return text;
        }
        double x = state.x;
        double y = state.y;
        double z = state.z;
        if (!Options.inRange(new Vec3(x, y, z))) {
            return text;
        }
        // MC 1.21.5 EntityRenderState 不包含实体类型信息，统一按实体名处理
        return TranslateHelper.translateNoWait(text, Source.ENTITY_NAME);
    }
}
