package kgg.translator.handler;

import kgg.translator.TranslateService;
import kgg.translator.util.TextUtil;
import net.minecraft.network.chat.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class TranslateHelper {
    private static final LinkedHashMap<String, TranslationStatus> stateMap = new LinkedHashMap<>(16, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, TranslationStatus> eldest) {
            return size() > 1000;
        }
    };
    private static final int MAX_FAILED_TEXT_CACHE_TIME = 1000 * 60 * 2;

    public static Component translateNoWait(Component text, String source) {
        return translateNoWait(text, s -> {}, source);
    }

    public static Component translateNoWait(Component text, Consumer<String> comparable, String source) {
        return TextUtil.toText(translateNoWait(TextUtil.getString(text), source, comparable), text);
    }

    public static String translateNoWait(String text, String source, Consumer<String> comparable) {
        TranslationStatus status = stateMap.computeIfAbsent(text, t -> new TranslationStatus());
        synchronized (status) {
            switch (status.state) {
                case PENDING -> {
                    status.state = State.RUNNING;
                    CompletableFuture.runAsync(() -> {
                        try {
                            TranslateService.cachedTranslate(text, source);
                            comparable.accept(text);
                            synchronized (status) {
                                status.state = State.SUCCESS;
                            }
                        } catch (Exception e) {
                            synchronized (status) {
                                status.state = State.FAILED;
                                status.failedTime = System.currentTimeMillis();
                            }
                        }
                    });
                    return text;
                }
                case RUNNING -> {
                    return text;
                }
                case SUCCESS -> {
                    try {
                        return TranslateService.cachedTranslate(text, source);
                    } catch (Exception e) {
                        return text;
                    }
                }
                case FAILED -> {
                    if (System.currentTimeMillis() - status.failedTime > MAX_FAILED_TEXT_CACHE_TIME) {
                        status.state = State.PENDING;
                    }
                    return text;
                }
            }
        }
        return text;
    }

    public static void clearCache() {
        stateMap.clear();
    }

    private enum State { PENDING, RUNNING, SUCCESS, FAILED }

    private static class TranslationStatus {
        State state = State.PENDING;
        long failedTime;
    }
}
