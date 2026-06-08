package kgg.translator.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class TranslateEvent {
    private static final List<Begin> BEGIN_LISTENERS = new CopyOnWriteArrayList<>();
    private static final List<After> AFTER_LISTENERS = new CopyOnWriteArrayList<>();

    private TranslateEvent() {}

    public interface Begin {
        boolean begin(String Component, String from, String to, String source);
    }

    public interface After {
        String after(String Component, String result, String from, String to, String source);
    }

    public static void registerBegin(Begin listener) {
        BEGIN_LISTENERS.add(listener);
    }

    public static void registerAfter(After listener) {
        AFTER_LISTENERS.add(listener);
    }

    public static boolean fireBegin(String Component, String from, String to, String source) {
        for (Begin listener : BEGIN_LISTENERS) {
            if (!listener.begin(Component, from, to, source)) {
                return false;
            }
        }
        return true;
    }

    public static String fireAfter(String Component, String result, String from, String to, String source) {
        for (After listener : AFTER_LISTENERS) {
            String updated = listener.after(Component, result, from, to, source);
            if (updated != null) {
                result = updated;
            }
        }
        return result;
    }
}
