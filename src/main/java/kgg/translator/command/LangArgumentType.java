package kgg.translator.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import kgg.translator.Language;
import kgg.translator.TranslatorManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class LangArgumentType implements ArgumentType<String> {
    public static LangArgumentType lang() {
        return new LangArgumentType();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readQuotedString();
    }

    public static String getLanguage(CommandContext<CommandSourceStack> context, String name) {
        String string = context.getArgument(name, String.class);
        Map<String, String> map = new HashMap<>(Language.defaultMap);
        Map<String, String> translatorMap = Language.translatorMap.get(TranslatorManager.getCurrent().getLanguageType());
        if (translatorMap != null) {
            map.putAll(translatorMap);
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (Component.translatable("language." + entry.getKey()).getString().equals(string)) {
                return entry.getValue();
            }
        }
        return string;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof SharedSuggestionProvider) {
            Set<String> support = Language.getTranslatorSupport(TranslatorManager.getCurrent().getLanguageType());
            return SharedSuggestionProvider.suggest(support.stream().map(c -> '"' + Component.translatable("language." + c).getString() + '"').toList(), builder);
        }
        return Suggestions.empty();
    }
}
