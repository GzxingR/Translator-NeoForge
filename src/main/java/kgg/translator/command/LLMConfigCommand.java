package kgg.translator.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import kgg.translator.LLMManager;
import kgg.translator.TranslatorConfig;
import kgg.translator.TranslatorManager;
import kgg.translator.translator.Translator;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LLMConfigCommand {
    public static void register(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("llm")
            .then(Commands.literal("list").executes(LLMConfigCommand::listModels))
            .then(Commands.literal("add")
                .then(Commands.argument("name", StringArgumentType.word())
                    .then(Commands.argument("url", StringArgumentType.string())
                        .then(Commands.argument("model", StringArgumentType.string())
                            .then(Commands.argument("apikey", StringArgumentType.string())
                                .executes(LLMConfigCommand::addModel))))))
            .then(Commands.literal("remove")
                .then(Commands.argument("name", LLMModelArgumentType.llmModel())
                    .executes(LLMConfigCommand::removeModel)))
            .then(Commands.literal("use")
                .then(Commands.argument("name", LLMModelArgumentType.llmModel())
                    .executes(LLMConfigCommand::useModel)))
            .then(Commands.literal("builtin")
                .executes(LLMConfigCommand::showBuiltinModels));

        dispatcher.register(command);
        dispatcher.register(Commands.literal("transconfig").then(Commands.literal("llm").redirect(command.build())));
    }

    private static int listModels(CommandContext<CommandSourceStack> context) {
        Map<String, LLMManager.Model> models = LLMManager.getModels();
        if (models.isEmpty()) {
            context.getSource().sendSystemMessage(Component.literal("没有配置任何LLM模型").withStyle(ChatFormatting.YELLOW));
            return 0;
        }
        context.getSource().sendSystemMessage(Component.literal("已配置的LLM模型:").withStyle(ChatFormatting.GREEN));
        models.keySet().forEach(name -> context.getSource().sendSystemMessage(Component.literal("- " + name)));
        return models.size();
    }

    private static int addModel(CommandContext<CommandSourceStack> context) {
        LLMManager.addModel(new LLMManager.Model(
            StringArgumentType.getString(context, "name"),
            StringArgumentType.getString(context, "url"),
            StringArgumentType.getString(context, "model"),
            StringArgumentType.getString(context, "apikey")));
        TranslatorConfig.writeFile();
        context.getSource().sendSystemMessage(Component.literal("成功添加LLM模型").withStyle(ChatFormatting.GREEN));
        return 1;
    }

    private static int removeModel(CommandContext<CommandSourceStack> context) {
        String name = LLMModelArgumentType.getLLMModel(context, "name");
        if (LLMManager.removeModel(name)) {
            TranslatorConfig.writeFile();
            context.getSource().sendSystemMessage(Component.literal("已删除: " + name).withStyle(ChatFormatting.GREEN));
            return 1;
        }
        context.getSource().sendFailure(Component.literal("未找到模型: " + name));
        return 0;
    }

    private static int useModel(CommandContext<CommandSourceStack> context) {
        String name = LLMModelArgumentType.getLLMModel(context, "name");
        for (Translator translator : TranslatorManager.getTranslators()) {
            if (translator.getName().equals(name)) {
                TranslatorManager.setTranslator(translator);
                TranslatorConfig.writeFile();
                context.getSource().sendSystemMessage(Component.literal("已切换到: " + name).withStyle(ChatFormatting.GREEN));
                return 1;
            }
        }
        context.getSource().sendFailure(Component.literal("未找到: " + name));
        return 0;
    }

    private static int showBuiltinModels(CommandContext<CommandSourceStack> context) {
        for (LLMManager.Model model : LLMManager.geBuiltInModels()) {
            context.getSource().sendSystemMessage(Component.literal("- " + model.name + ": " + model.url));
        }
        return 1;
    }

    public static class LLMModelArgumentType implements com.mojang.brigadier.arguments.ArgumentType<String> {
        public static LLMModelArgumentType llmModel() { return new LLMModelArgumentType(); }
        @Override
        public String parse(com.mojang.brigadier.StringReader reader) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
            return reader.readQuotedString();
        }
        public static String getLLMModel(CommandContext<CommandSourceStack> context, String name) {
            return context.getArgument(name, String.class);
        }
        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            if (context.getSource() instanceof SharedSuggestionProvider) {
                return SharedSuggestionProvider.suggest(LLMManager.getModels().keySet().stream().map(n -> "\"" + n + "\"").toList(), builder);
            }
            return Suggestions.empty();
        }
    }
}
