package kgg.translator.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public interface CommandConfigurable {
    void register(LiteralArgumentBuilder<net.minecraft.commands.CommandSourceStack> node);
}
