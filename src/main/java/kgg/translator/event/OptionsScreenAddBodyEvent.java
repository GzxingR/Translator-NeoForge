package kgg.translator.event;

import net.minecraft.client.gui.components.OptionsList;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class OptionsScreenAddBodyEvent {
    private static final List<Listener> LISTENERS = new CopyOnWriteArrayList<>();

    private OptionsScreenAddBodyEvent() {}

    public interface Listener {
        void add(OptionsList body);
    }

    public static void register(Listener listener) {
        LISTENERS.add(listener);
    }

    public static void fire(OptionsList body) {
        for (Listener listener : LISTENERS) {
            listener.add(body);
        }
    }
}
