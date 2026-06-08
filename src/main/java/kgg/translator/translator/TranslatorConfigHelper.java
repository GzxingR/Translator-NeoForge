package kgg.translator.translator;

import kgg.translator.TranslateService;
import kgg.translator.TranslatorConfig;
import kgg.translator.TranslatorManager;
import kgg.translator.exception.TranslateException;
import kgg.translator.translator.Source;
import kgg.translator.util.TranslateExceptionUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;

public final class TranslatorConfigHelper {
    private TranslatorConfigHelper() {}

    public static void saveAndEnable(Translator translator, CommandSourceStack source) {
        TranslatorManager.setTranslator(translator);
        TranslatorConfig.writeFile();
        source.sendSystemMessage(Component.translatable("commands.transconfig.querytranslator.configed", translator.getName()));
        testConnection(translator, source);
    }

    private static void testConnection(Translator translator, CommandSourceStack source) {
        CompletableFuture.runAsync(() -> {
            Component message = buildTestResultMessage(translator);
            Minecraft.getInstance().execute(() -> source.sendSystemMessage(message));
        });
    }

    private static Component buildTestResultMessage(Translator translator) {
        try {
            TranslateService.translate("test", translator, TranslatorManager.getFrom(), TranslatorManager.getTo(), Source.CHAT);
            return Component.translatable("commands.transconfig.test.success", translator.getName())
                    .withStyle(ChatFormatting.GREEN);
        } catch (TranslateException e) {
            return Component.translatable(
                    "commands.transconfig.test.failed",
                    translator.getName(),
                    TranslateExceptionUtil.getDisplayMessage(e)
            ).withStyle(ChatFormatting.RED);
        }
    }
}
