package kgg.translator.mixin.world;

import kgg.translator.handler.TranslateHelper;
import kgg.translator.option.Options;
import kgg.translator.translator.Source;
import net.minecraft.world.entity.Display;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Display.TextDisplay.class)
public abstract class TextDisplayEntityMixin {
    @Shadow @Nullable private Display.TextDisplay.CachedInfo clientDisplayCache;

    @Unique
    private boolean translated = false;
    @Unique
    private boolean updated = false;

    @ModifyVariable(method = "cacheDisplay", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Display.TextDisplay.LineSplitter translator$wrapLineSplitter(Display.TextDisplay.LineSplitter splitter) {
        if (translated != Options.autoEntityName.get()) {
            translated = Options.autoEntityName.get();
            updated = true;
        }
        if (updated) {
            updated = false;
            clientDisplayCache = null;
        }
        return (text, width) -> {
            if (translated) {
                text = TranslateHelper.translateNoWait(text, t -> updated = true, Source.ENTITY_NAME);
            }
            return splitter.split(text, width);
        };
    }
}
