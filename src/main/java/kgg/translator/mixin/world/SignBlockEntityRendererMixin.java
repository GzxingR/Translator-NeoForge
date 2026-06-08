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
    @Inject(method = "renderText", at = @At("HEAD"))
    public void renderText(BlockPos pos, SignText signText, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int lineHeight, int lineWidth, boolean front, CallbackInfo ci) {
        SignHelper.lineWidth = lineWidth;
        SignHelper.translate = Options.inRange(Vec3.atCenterOf(pos));
    }
}
