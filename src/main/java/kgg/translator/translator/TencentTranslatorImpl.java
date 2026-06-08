package kgg.translator.translator;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import kgg.translator.command.CommandConfigurable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class TencentTranslatorImpl extends TencentTranslator implements CommandConfigurable {
    @Override
    public void register(LiteralArgumentBuilder<CommandSourceStack> node) {
        node.then(Commands.argument("secretId", StringArgumentType.string())
                .then(Commands.argument("secretKey", StringArgumentType.string())
                        .executes(context -> {
                            setConfig(
                                    StringArgumentType.getString(context, "secretId"),
                                    StringArgumentType.getString(context, "secretKey"),
                                    region,
                                    projectId
                            );
                            TranslatorConfigHelper.saveAndEnable(this, context.getSource());
                            return 0;
                        })
                        .then(Commands.argument("region", StringArgumentType.string())
                                .executes(context -> {
                                    setConfig(
                                            StringArgumentType.getString(context, "secretId"),
                                            StringArgumentType.getString(context, "secretKey"),
                                            StringArgumentType.getString(context, "region"),
                                            projectId
                                    );
                                    TranslatorConfigHelper.saveAndEnable(this, context.getSource());
                                    return 0;
                                })
                                .then(Commands.argument("projectId", IntegerArgumentType.integer(0))
                                        .executes(context -> {
                                            setConfig(
                                                    StringArgumentType.getString(context, "secretId"),
                                                    StringArgumentType.getString(context, "secretKey"),
                                                    StringArgumentType.getString(context, "region"),
                                                    IntegerArgumentType.getInteger(context, "projectId")
                                            );
                                            TranslatorConfigHelper.saveAndEnable(this, context.getSource());
                                            return 0;
                                        })))));
    }
}
