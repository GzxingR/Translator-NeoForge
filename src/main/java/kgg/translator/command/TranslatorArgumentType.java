package kgg.translator.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import kgg.translator.TranslatorManager;
import kgg.translator.translator.Translator;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TranslatorArgumentType implements ArgumentType<String> {
    public static TranslatorArgumentType translator() {
        return new TranslatorArgumentType();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readQuotedString();
    }

    public static Translator getTranslator(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        String string = context.getArgument(name, String.class);
        for (Translator translator : TranslatorManager.getTranslators()) {
            if (translator.getName().equals(string)) {
                return translator;
            }
        }
        throw BlockPosArgument.ERROR_OUT_OF_WORLD.create();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof SharedSuggestionProvider) {
            return SharedSuggestionProvider.suggest(TranslatorManager.getTranslators().stream().map(t -> "\"" + t.getName() + "\""), builder);
        }
        return Suggestions.empty();
    }
}
