package kgg.translator.util;

import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.locale.Language;

public class TextUtil {
    public static boolean isSystemText(Component text) {
        if (text.getContents() == Component.empty().getContents()) {
            for (Component sibling : text.getSiblings()) {
                if (sibling.getContents() instanceof TranslatableContents content) {
                    if (!Language.getInstance().has(content.getKey())) {
                        return false;
                    }
                } else if (sibling.getContents() instanceof PlainTextContents content) {
                    if (!StringUtil.isBlank(content.text())) {
                        return false;
                    }
                }
            }
            return true;
        } else if (text.getContents() instanceof TranslatableContents content) {
            if (Language.getInstance().has(content.getKey())) {
                for (Object arg : content.getArgs()) {
                    if (arg instanceof Component t) {
                        if (!isSystemText(t)) return false;
                    } else {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static Component toText(String text, Component origin) {
        return Component.literal(text).withStyle(origin.getStyle());
    }

    public static String getString(Component text) {
        return StringUtil.strip(text.getString());
    }

    public static String getString(FormattedCharSequence orderedText) {
        StringBuilder sb = new StringBuilder();
        orderedText.accept((index, style, codePoint) -> {
            sb.appendCodePoint(codePoint);
            return true;
        });
        return sb.toString();
    }
}
