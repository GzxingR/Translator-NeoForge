package kgg.translator.exception;

import net.minecraft.network.chat.Component;

public class ErrorCodeException extends TranslateException {
    public final String code;
    public final String id;

    public ErrorCodeException(String id, String code) {
        this.id = id;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return Component.translatable("translator.error.%s.%s".formatted(id, code)).getString();
    }
}
