package kgg.translator.mixin.world;

import kgg.translator.handler.SignHelper;
import kgg.translator.handler.TranslateHelper;
import kgg.translator.option.Options;
import kgg.translator.translator.Source;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.function.Function;

@Mixin(SignText.class)
public abstract class SignTextMixin {
    @Shadow @Nullable private FormattedCharSequence[] renderMessages;
    @Shadow private boolean renderMessagedFiltered;
    @Shadow public abstract Component getMessage(int line, boolean filtered);

    @Unique
    private boolean autoSign = false;
    @Unique
    private boolean signCombine = false;
    @Unique
    private boolean updated = false;
    @Unique
    private boolean translate;

    @Overwrite
    public FormattedCharSequence[] getRenderMessages(boolean filtered, Function<Component, FormattedCharSequence> messageOrderer) {
        updateFlags();

        if (renderMessages == null || filtered != this.renderMessagedFiltered) {
            this.renderMessagedFiltered = filtered;
            renderMessages = new FormattedCharSequence[4];
            if (autoSign && !(Minecraft.getInstance().screen instanceof AbstractSignEditScreen) && translate) {
                if (signCombine) {
                    handleCombinedTranslation();
                } else {
                    handleLineByLineTranslation();
                }
            } else {
                for (int i = 0; i < 4; ++i) {
                    Component message = getMessage(i, filtered);
                    renderMessages[i] = messageOrderer.apply(message);
                }
            }
        }
        return renderMessages;
    }

    @Unique
    private void updateFlags() {
        if (autoSign != Options.autoSign.get() || signCombine != Options.signCombine.get() || translate != SignHelper.translate) {
            autoSign = Options.autoSign.get();
            signCombine = Options.signCombine.get();
            translate = SignHelper.translate;
            updated = true;
        }
        if (updated) {
            updated = false;
            renderMessages = null;
        }
    }

    @Unique
    private void handleCombinedTranslation() {
        MutableComponent combined = getMessage(0, renderMessagedFiltered).copy();
        for (int i = 1; i < 4; ++i) {
            combined.append(getMessage(i, renderMessagedFiltered));
        }
        Component combinedMessage = TranslateHelper.translateNoWait(combined, t -> updated = true, Source.SIGN);

        List<FormattedCharSequence> list = Minecraft.getInstance().font.split(combinedMessage, SignHelper.lineWidth);
        for (int i = 0; i < 4; ++i) {
            if (i < list.size()) {
                renderMessages[i] = list.get(i);
            } else {
                renderMessages[i] = Component.empty().getVisualOrderText();
            }
        }
    }

    @Unique
    private void handleLineByLineTranslation() {
        for (int i = 0; i < 4; ++i) {
            Component message = getMessage(i, renderMessagedFiltered);
            message = TranslateHelper.translateNoWait(message, t -> updated = true, Source.SIGN);
            renderMessages[i] = message.getVisualOrderText();
        }
    }
}
