package kgg.translator.mixin.hud;

import kgg.translator.handler.TranslateHelper;
import kgg.translator.option.Options;
import kgg.translator.translator.Source;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Gui.class)
public abstract class GuiForScoreboardMixin {
    @Redirect(method = "displayScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/scores/Objective;getDisplayName()Lnet/minecraft/network/chat/Component;"))
    private Component translateObjectiveTitle(Objective objective) {
        Component name = objective.getDisplayName();
        if (!Options.autoScoreboard.get()) {
            return name;
        }
        return TranslateHelper.translateNoWait(name, Source.SCOREBOARD);
    }

    @Redirect(method = "displayScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/scores/PlayerTeam;formatNameForTeam(Lnet/minecraft/world/scores/Team;Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;"))
    private Component translateEntryName(Team team, Component playerName) {
        if (!Options.autoScoreboard.get()) {
            return PlayerTeam.formatNameForTeam(team, playerName);
        }
        Component translated = TranslateHelper.translateNoWait(playerName, Source.SCOREBOARD);
        return PlayerTeam.formatNameForTeam(team, translated);
    }
}
