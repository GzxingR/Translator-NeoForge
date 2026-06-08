package kgg.translator.handler;

import kgg.translator.TranslateService;
import kgg.translator.exception.TranslateException;
import kgg.translator.translator.Source;
import kgg.translator.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;

public class TipHandler {
    private static final Logger LOGGER = LogManager.getLogger(TipHandler.class);
    private static boolean drawTranslateText = false;
    private static boolean needTranslate = false;
    private static boolean handleAfter = false;
    private static FormattedCharSequence[] translatedOrderedText;
    private static List<Component> lastText;
    private static long time = 0;
    private static boolean isTranslated = false;

    public static boolean isDrawTranslateText() { return drawTranslateText; }
    public static boolean isNeedTranslate() { return needTranslate; }

    public static boolean isHandleAfter() {
        if (handleAfter) {
            handleAfter = false;
            return true;
        }
        return false;
    }

    public static void handle(GuiGraphics drawContext, List<Component> text, int mouseX, int mouseY, float delayTime) {
        handleAfter = true;
        if (!text.equals(lastText)) {
            resetState(text);
            return;
        }
        lastText = text;
        if (System.currentTimeMillis() > time + (int) (delayTime * 1000)) {
            if (!isTranslated) {
                isTranslated = true;
                if (text.stream().filter(t -> TextUtil.isSystemText(t) || TranslateService.shouldSkipTranslation(t.getString())).count() == text.size()) {
                    return;
                }
                needTranslate = true;
                startTranslation(text);
            }
        }
    }

    private static void resetState(List<Component> text) {
        time = System.currentTimeMillis();
        lastText = text;
        isTranslated = false;
        drawTranslateText = false;
        needTranslate = false;
    }

    private static void startTranslation(List<Component> texts) {
        StringJoiner joiner = new StringJoiner("\n");
        texts.forEach(t -> joiner.add(TextUtil.getString(t)));
        String combined = joiner.toString();

        CompletableFuture.supplyAsync(() -> {
            try {
                return TranslateService.cachedTranslate(combined, Source.TOOLTIP);
            } catch (TranslateException e) {
                throw new RuntimeException(e);
            }
        }).exceptionally(throwable -> {
            LOGGER.error("Translation failed", throwable);
            return "翻译失败";
        }).thenAccept(translated -> {
            if (translated.equals(combined)) {
                needTranslate = false;
                return;
            }
            String[] translatedLines = translated.split("\n");
            if (translatedLines.length == 1 && texts.size() != 1) {
                translatedOrderedText = Minecraft.getInstance().font.split(Component.literal(translated), 120).toArray(FormattedCharSequence[]::new);
            } else {
                translatedOrderedText = new FormattedCharSequence[translatedLines.length];
                for (int i = 0; i < translatedLines.length; i++) {
                    translatedOrderedText[i] = TextUtil.toText(translatedLines[i], texts.get(i)).getVisualOrderText();
                }
            }
            drawTranslateText = true;
        });
    }

    public static FormattedCharSequence[] getTranslatedOrderedText() {
        return translatedOrderedText;
    }

    public record SidebarEntry(Component name, Component score, int scoreWidth) {}
}