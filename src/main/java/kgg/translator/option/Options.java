package kgg.translator.option;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

import static kgg.translator.option.OptionRegistry.*;

public class Options {
    public static void init() {}

    public static final OptionInstance<Boolean> chatTip = registerBool("chat_tip", false);
    public static final OptionInstance<Boolean> autoChat = registerBool("auto_chat", false);
    public static final OptionInstance<Boolean> autoTooltip = registerBoolWithTooltip("auto_tooltip", false);
    public static final OptionInstance<Boolean> autoTitle = registerBool("auto_title", false);
    public static final OptionInstance<Boolean> autoScoreboard = registerBool("auto_scoreboard", false);
    public static final OptionInstance<Boolean> autoBossBar = registerBool("auto_boss_bar", false);
    public static final OptionInstance<Integer> distance = register("distance", new OptionInstance<>(
            "translator.option.distance",
            OptionInstance.cachedConstantTooltip(Component.translatable("translator.option.distance.desc")),
            (p, v) -> v == 100
                    ? Component.translatable("translator.option.distance.unlimited", p)
                    : Component.translatable("translator.option.distance.value", p, v),
            new OptionInstance.IntRange(0, 100),
            30,
            v -> {}
    ));

    public static boolean inRange(Vec3 pos) {
        if (distance.get() == 100) {
            return true;
        }
        return Minecraft.getInstance().cameraEntity.position().distanceTo(pos) <= distance.get();
    }

    public static final OptionInstance<Boolean> autoEntityName = registerBool("auto_entity_name", false);
    public static final OptionInstance<Boolean> autoPlayerName = registerBool("auto_player_name", false);
    public static final OptionInstance<Boolean> autoSign = registerBool("auto_sign", false);
    public static final OptionInstance<Boolean> signCombine = registerBoolWithTooltip("sign_combine", true);
    public static OptionInstance<Boolean> multiTranslation = registerBoolWithTooltip("multi-translation", false);
    public static final OptionInstance<Boolean> autoContainer = registerBoolWithTooltip("auto_container", false);
}
