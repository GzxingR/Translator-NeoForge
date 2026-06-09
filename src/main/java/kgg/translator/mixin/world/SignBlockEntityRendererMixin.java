package kgg.translator.mixin.world;

import kgg.translator.handler.SignHelper;
import kgg.translator.option.Options;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MC 1.21.5: renderText 被移到了 AbstractSignRenderer.renderSignText (private 方法)
 */
@Mixin(AbstractSignRenderer.class)
public class SignBlockEntityRendererMixin {
    @Inject(method = "renderSignText", at = @At("HEAD"))
    public void renderSignText(BlockPos pos, SignText signText, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int lineHeight, int lineWidth, boolean front, CallbackInfo ci) {
        SignHelper.lineWidth = lineWidth;
        SignHelper.translate = Options.inRange(Vec3.atCenterOf(pos));
    }
}
