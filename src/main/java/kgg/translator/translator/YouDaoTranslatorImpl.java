package kgg.translator.translator;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kgg.translator.command.CommandConfigurable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class YouDaoTranslatorImpl extends YouDaoTranslator implements CommandConfigurable {
    @Override
    public void register(LiteralArgumentBuilder<CommandSourceStack> node) {
        node.then(Commands.argument("appId", StringArgumentType.string())
                .then(Commands.argument("appKey", StringArgumentType.string())
                        .executes(context -> {
                            setConfig(
                                    StringArgumentType.getString(context, "appId"),
                                    StringArgumentType.getString(context, "appKey")
                            );
                            TranslatorConfigHelper.saveAndEnable(this, context.getSource());
                            return 0;
                        })));
    }
}
