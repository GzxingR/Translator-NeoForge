package kgg.translator.mixin.screen;

import kgg.translator.TranslateService;
import kgg.translator.exception.TranslateException;
import kgg.translator.translator.Source;
import kgg.translator.util.TextUtil;
import kgg.translator.util.TranslateExceptionUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;

@Mixin(BookViewScreen.class)
public abstract class BookViewScreenMixin {
    @Shadow @Final protected static int TEXT_WIDTH;
    @Shadow private List<FormattedCharSequence> cachedPageComponents;

    @Unique
    private String translateText;

    @Inject(method = "init", at = @At("HEAD"))
    private void translator$init(CallbackInfo ci) {
        translateText = null;
        ScreenFieldsAccessor screen = (ScreenFieldsAccessor) this;
        int width = screen.translator$getWidth();
        ((ScreenInvoker) this).invokeAddRenderableWidget(Button.builder(Component.literal("翻译"), button -> CompletableFuture.runAsync(() -> {
            try {
                StringJoiner joiner = new StringJoiner(" ");
                cachedPageComponents.forEach(line -> joiner.add(TextUtil.getString(line)));
                translateText = TranslateService.cachedTranslate(joiner.toString(), Source.BOOK);
            } catch (TranslateException e) {
                translateText = TranslateExceptionUtil.getDisplayMessage(e);
            }
        })).bounds(width / 2 + 2 - 49, 220, 98, 20).build());
    }

    @Inject(method = "setPage", at = @At("HEAD"))
    private void translator$setPage(int page, CallbackInfoReturnable<Boolean> cir) {
        translateText = null;
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void translator$render(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (translateText == null) {
            return;
        }
        ScreenFieldsAccessor screen = (ScreenFieldsAccessor) this;
        var font = screen.translator$getFont();
        int width = screen.translator$getWidth();
        int y = 32;
        for (FormattedCharSequence line : font.split(Component.literal(translateText), TEXT_WIDTH)) {
            context.drawString(font, line, (width + TEXT_WIDTH) / 2 + 20, y, 0xFFFFFF, false);
            y += font.lineHeight;
        }
    }
}
