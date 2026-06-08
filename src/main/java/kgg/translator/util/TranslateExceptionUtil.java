package kgg.translator.util;

import kgg.translator.exception.ErrorCodeException;
import kgg.translator.exception.TranslateException;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

public final class TranslateExceptionUtil {
    private static final String TENCENT_SIGN_FAILED = "translator.error.tencent.SignFailed";

    private TranslateExceptionUtil() {}

    public static String getDisplayMessage(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof ErrorCodeException errorCodeException) {
                return errorCodeException.getMessage();
            }
            String message = current.getMessage();
            if (message != null && !message.isBlank()) {
                if (message.startsWith("translator.error.")) {
                    return translateKey(message);
                }
                if (message.contains("Failed to sign Tencent Cloud request")) {
                    return translateKey(TENCENT_SIGN_FAILED);
                }
            }
            current = current.getCause();
        }
        return translateKey("translator.error.unknown");
    }

    public static Component getDisplayComponent(Throwable throwable) {
        return Component.literal(getDisplayMessage(throwable));
    }

    private static String translateKey(String key) {
        return Language.getInstance().getOrDefault(key);
    }
}
