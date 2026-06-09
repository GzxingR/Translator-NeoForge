package kgg.translator.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import kgg.translator.TranslateService;
import kgg.translator.TranslatorManager;
import kgg.translator.exception.TranslateException;
import kgg.translator.translator.Source;
import kgg.translator.util.TranslateExceptionUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

import java.util.concurrent.CompletableFuture;

public class TranslateCommand {
    public static void register(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("translate")
                .then(Commands.argument("text", StringArgumentType.greedyString())
                        .executes(context -> {
                            translate(context, StringArgumentType.getString(context, "text"), TranslatorManager.getFrom(), TranslatorManager.getTo());
                            return 0;
                        })));
        dispatcher.register(Commands.literal("translate-re")
                .then(Commands.argument("text", StringArgumentType.greedyString())
                        .executes(context -> {
                            translate(context, StringArgumentType.getString(context, "text"), TranslatorManager.getTo(), TranslatorManager.getFrom());
                            return 0;
                        })));
    }

    private static void translate(CommandContext<CommandSourceStack> context, String text, String from, String to) {
        CompletableFuture.runAsync(() -> {
            Component message;
            try {
                String result = TranslateService.translate(text, TranslatorManager.getCurrent(), from, to, Source.CHAT);
                message = Component.literal("[结果] " + result).withStyle(Style.EMPTY
                        .withClickEvent(new ClickEvent.CopyToClipboard(result))
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("点击复制"))));
            } catch (TranslateException e) {
                message = Component.translatable("commands.translate.error", TranslateExceptionUtil.getDisplayMessage(e))
                        .withStyle(ChatFormatting.RED);
            }
            Component finalMessage = message;
            Minecraft.getInstance().execute(() -> context.getSource().sendSystemMessage(finalMessage));
        });
    }
}
