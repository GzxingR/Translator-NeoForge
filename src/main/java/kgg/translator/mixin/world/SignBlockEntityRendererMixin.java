package kgg.translator.mixin.world;

import kgg.translator.handler.SignHelper;
import kgg.translator.option.Options;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignRenderer.class)
public class SignBlockEntityRendererMixin {
    // 1.21.1: renderSignText
    @Inject(method = "renderSignText", at = @At("HEAD"), require = 0)
    public void onRenderSignText(BlockPos pos, SignText signText, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int lineHeight, int lineWidth, boolean front, CallbackInfo ci) {
        SignHelper.lineWidth = lineWidth;
        SignHelper.translate = Options.inRange(Vec3.atCenterOf(pos));
    }

    // 1.21.5: renderText
    @Inject(method = "renderText", at = @At("HEAD"), require = 0)
    public void onRenderText(BlockPos pos, SignText signText, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int lineHeight, int lineWidth, boolean front, CallbackInfo ci) {
        SignHelper.lineWidth = lineWidth;
        SignHelper.translate = Options.inRange(Vec3.atCenterOf(pos));
    }
}
