package kgg.translator.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class TranslateChatEvent {
    private static final List<Listener> LISTENERS = new CopyOnWriteArrayList<>();

    private TranslateChatEvent() {}

    public interface Listener {
        String chat(String Component);
    }

    public static void register(Listener listener) {
        LISTENERS.add(listener);
    }

    public static String fire(String Component) {
        for (Listener listener : LISTENERS) {
            String result = listener.chat(Component);
            if (result != null) {
                return result;
            }
        }
        return Component;
    }
}
