package kgg.translator.translator;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kgg.translator.command.CommandConfigurable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class BaiduTranslatorImpl extends BaiduTranslator implements CommandConfigurable {
    @Override
    public void register(LiteralArgumentBuilder<CommandSourceStack> node) {
        node.then(Commands.argument("qps", IntegerArgumentType.integer(1))
                .then(Commands.argument("appId", StringArgumentType.string())
                        .then(Commands.argument("appKey", StringArgumentType.string())
                                .executes(context -> {
                                    setDelayTime(1000 / IntegerArgumentType.getInteger(context, "qps"));
                                    setConfig(
                                            StringArgumentType.getString(context, "appId"),
                                            StringArgumentType.getString(context, "appKey")
                                    );
                                    TranslatorConfigHelper.saveAndEnable(this, context.getSource());
                                    return 0;
                                }))));
    }
}
