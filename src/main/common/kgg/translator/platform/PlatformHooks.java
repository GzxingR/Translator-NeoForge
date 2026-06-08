package kgg.translator.platform;

import java.nio.file.Path;
import java.util.function.Supplier;

public final class PlatformHooks {
    private static Supplier<String> gameVersion = () -> "1.21.1";
    private static Supplier<Path> configDir = () -> Path.of("config");

    private PlatformHooks() {}

    public static void init(Supplier<String> gameVersionSupplier, Supplier<Path> configDirSupplier) {
        gameVersion = gameVersionSupplier;
        configDir = configDirSupplier;
    }

    public static String getGameVersion() {
        return gameVersion.get();
    }

    public static Path getConfigDir() {
        return configDir.get();
    }

    public static Path getTranslatorConfigDir() {
        return getConfigDir().resolve("translator");
    }
}
