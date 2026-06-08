package kgg.translator.event;

import kgg.translator.translator.Translator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class SetTranslatorEvent {
    private static final List<Listener> LISTENERS = new CopyOnWriteArrayList<>();

    private SetTranslatorEvent() {}

    public interface Listener {
        void setTranslator(Translator translator);
    }

    public static void register(Listener listener) {
        LISTENERS.add(listener);
    }

    public static void invoke(Translator translator) {
        for (Listener listener : LISTENERS) {
            listener.setTranslator(translator);
        }
    }
}
