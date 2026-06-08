package kgg.translator.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import kgg.translator.*;
import kgg.translator.command.CommandConfigurable;
import kgg.translator.handler.TranslateHelper;
import kgg.translator.modmenu.ModMenuApiImpl;
import kgg.translator.screen.ConfigJsonScreen;
import kgg.translator.translator.TencentTranslator;
import kgg.translator.translator.Translator;
import kgg.translator.translator.TranslatorConfigHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

public class TranslateConfigCommand {
    public static void register(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("transconfig");

        root.then(Commands.literal("language")
                .executes(TranslateConfigCommand::queryLanguage)
                .then(Commands.argument("from", LangArgumentType.lang())
                        .executes(context -> {
                            TranslatorManager.setFrom(LangArgumentType.getLanguage(context, "from"));
                            TranslatorConfig.writeFile();
                            return queryLanguage(context);
                        })
                        .then(Commands.argument("to", LangArgumentType.lang())
                                .executes(context -> {
                                    TranslatorManager.setFrom(LangArgumentType.getLanguage(context, "from"));
                                    TranslatorManager.setTo(LangArgumentType.getLanguage(context, "to"));
                                    TranslatorConfig.writeFile();
                                    return queryLanguage(context);
                                }))));

        LiteralArgumentBuilder<CommandSourceStack> selectNode = Commands.literal("translator")
            .executes(TranslateConfigCommand::queryTranslator);
        for (Translator translator : TranslatorManager.getTranslators()) {
            LiteralArgumentBuilder<CommandSourceStack> translatorNode = Commands.literal("\"" + translator.getName() + "\"")
                .executes(context -> selectTranslator(context, translator));
            if (translator instanceof CommandConfigurable configurable) {
                configurable.register(translatorNode);
            }
            selectNode.then(translatorNode);
        }
        selectNode.then(Commands.argument("translator", TranslatorArgumentType.translator())
            .executes(context -> selectTranslator(context, TranslatorArgumentType.getTranslator(context, "translator"))));
        root.then(selectNode);

        root.then(Commands.literal("tencent")
                .then(Commands.argument("secretId", StringArgumentType.string())
                        .then(Commands.argument("secretKey", StringArgumentType.string())
                                .executes(context -> configureTencent(context, "ap-guangzhou", 0))
                                .then(Commands.argument("region", StringArgumentType.string())
                                        .executes(context -> configureTencent(
                                                context,
                                                StringArgumentType.getString(context, "region"),
                                                0
                                        ))
                                        .then(Commands.argument("projectId", IntegerArgumentType.integer(0))
                                                .executes(context -> configureTencent(
                                                        context,
                                                        StringArgumentType.getString(context, "region"),
                                                        IntegerArgumentType.getInteger(context, "projectId")
                                                )))))));

        root.then(Commands.literal("clearcache")
                .executes(context -> {
                    TranslateService.clearCache();
                    TranslateHelper.clearCache();
                    context.getSource().sendSystemMessage(Component.literal("OK"));
                    return 0;
                }));

        Minecraft client = Minecraft.getInstance();
        root.then(Commands.literal("config")
            .executes(context -> {
                client.execute(() -> client.setScreen(ModMenuApiImpl.createScreen(null)));
                return 0;
            })
            .then(Commands.literal("json")
                .executes(context -> {
                    client.execute(() -> client.setScreen(new ConfigJsonScreen()));
                    return 0;
                })));

        root.then(Commands.literal("reload")
            .executes(context -> {
                TranslatorConfig.readFile();
                context.getSource().sendSystemMessage(Component.literal("OK"));
                return 0;
            }));

        root.then(Commands.literal("chat-format")
            .then(Commands.argument("format", ChatFormatArgumentType.chatFormat())
                .executes(context -> {
                    ChatFormat.setCurrentFormat(ChatFormatArgumentType.getChatFormat(context, "format"));
                    TranslatorConfig.writeFile();
                    context.getSource().sendSystemMessage(Component.literal("OK"));
                    return 0;
                }))
            .executes(context -> {
                context.getSource().sendSystemMessage(Component.literal("Current format: " + ChatFormat.getCurrentFormat()));
                return 0;
            }));

        dispatcher.register(root);
    }

    private static int selectTranslator(CommandContext<CommandSourceStack> context, Translator translator) {
        boolean b = TranslatorManager.setTranslator(translator);
        TranslatorConfig.writeFile();
        int a = queryTranslator(context);
        if (!b) {
            context.getSource().sendFailure(Component.translatable("commands.transconfig.querytranslator.unsupported", translator.getName()));
        }
        return a;
    }

    private static int queryLanguage(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSystemMessage(Component.translatable("commands.transconfig.querylanguage", TranslatorManager.getFrom(), TranslatorManager.getTo()));
        return 0;
    }

    private static int queryTranslator(CommandContext<CommandSourceStack> context) {
        Translator translator = TranslatorManager.getCurrent();
        context.getSource().sendSystemMessage(Component.translatable("commands.transconfig.querytranslator", translator.getName()));
        Component message = translator.isConfigured()
            ? Component.translatable("commands.transconfig.querytranslator.configed", translator.getName()).withColor(0x00ff00)
            : Component.translatable("commands.transconfig.querytranslator.unconfiged", translator.getName()).withColor(0xff0000);
        context.getSource().sendSystemMessage(message);
        return 0;
    }

    private static int configureTencent(CommandContext<CommandSourceStack> context, String region, int projectId) {
        Translator translator = TranslatorManager.getTranslators().stream()
                .filter(t -> t instanceof TencentTranslator)
                .findFirst()
                .orElse(null);
        if (translator == null) {
            context.getSource().sendFailure(Component.literal("腾讯翻译未加载"));
            return 0;
        }
        ((TencentTranslator) translator).setConfig(
                StringArgumentType.getString(context, "secretId"),
                StringArgumentType.getString(context, "secretKey"),
                region,
                projectId
        );
        TranslatorConfigHelper.saveAndEnable(translator, context.getSource());
        return 0;
    }
}
