package kgg.translator.handler;

import kgg.translator.ChatFormat;
import kgg.translator.TranslateService;
import kgg.translator.event.TranslateChatEvent;
import kgg.translator.option.Options;
import kgg.translator.translator.Source;
import kgg.translator.util.StringUtil;
import kgg.translator.util.TextUtil;
import kgg.translator.util.TranslateExceptionUtil;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import kgg.translator.mixin.hud.ChatComponentAccessor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatHandler {
    private static final List<MutableComponent> translatingTexts = new CopyOnWriteArrayList<>();
    private static final String CLICK_PREFIX = "kgg:";
    // 存储被点击的可翻译文本组件引用
    static final Map<String, ClickData> CLICK_MAP = new ConcurrentHashMap<>();

    public record ClickData(MutableComponent text, boolean clicked) {}

    private static void refresh() {
        Minecraft.getInstance().gui.getChat().refreshTrimmedMessages();
    }

    public static void addTip() {
        if (!Options.chatTip.get()) {
            return;
        }
        for (GuiMessage message : ((ChatComponentAccessor) Minecraft.getInstance().gui.getChat()).getMessages()) {
            MutableComponent text = initText(message.content());
            if (text != null) {
                addTip(text);
            }
        }
        refresh();
    }

    public static void removeTip() {
        for (GuiMessage message : ((ChatComponentAccessor) Minecraft.getInstance().gui.getChat()).getMessages()) {
            MutableComponent text = initText(message.content());
            if (text == null) {
                continue;
            }
            if (hasTranslateClick(text) && !isClicked(text)) {
                int size = text.getSiblings().size();
                if (size >= 2) {
                    text.getSiblings().removeLast();
                    text.getSiblings().removeLast();
                }
            }
        }
        refresh();
    }

    public static Component ensureMutable(Component text) {
        if (!(text instanceof MutableComponent mutable)) {
            return text.copy();
        }
        if (mutable.getSiblings() instanceof ArrayList<?>) {
            return mutable;
        }
        return mutable.copy();
    }

    public static void handleNewMessage(Component text) {
        MutableComponent mutableText = initText(text);
        if (mutableText == null) {
            return;
        }
        if (isModSystemMessage(mutableText)) {
            return;
        }
        if (Options.autoChat.get()) {
            translate(mutableText);
        } else if (Options.chatTip.get() && Minecraft.getInstance().screen instanceof ChatScreen) {
            addTip(mutableText);
        }
    }

    private static final Component TRANSLATING_TIP = Component.literal("[翻译中]")
            .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(2259711)).withClickEvent(
                    new ClickEvent.RunCommand(CLICK_PREFIX + "none")));

    public static void translate(MutableComponent text) {
        if (text == null) {
            return;
        }
        String s = TextUtil.getString(text);
        String s2 = TranslateChatEvent.fire(s);
        String t = ChatFormat.match(s2);
        translatingTexts.add(text);
        CompletableFuture.supplyAsync(() -> {
            try {
                String result = TranslateService.cachedTranslate(t, Source.CHAT);
                return createResultText(result, text);
            } catch (Exception e) {
                return createErrorText(TranslateExceptionUtil.getDisplayMessage(e), text, s);
            }
        }).thenAccept(result -> {
            Minecraft.getInstance().execute(() -> {
                translatingTexts.remove(text);
                String clickId = findClickId(text);
                if (clickId != null) {
                    text.getSiblings().set(text.getSiblings().size() - 1, result);
                } else {
                    text.append(" ").append(result);
                }
                ChatHandler.refresh();
            });
        });
    }

    public static void translateWithTip(MutableComponent text) {
        if (text == null || translatingTexts.contains(text)) {
            return;
        }
        text.getSiblings().removeLast();
        translate(text);
        text.getSiblings().add(TRANSLATING_TIP);
        refresh();
    }

    private static void addTip(MutableComponent text) {
        if (text == null || TextUtil.isSystemText(text) || StringUtil.isBlank(text.getString())) {
            return;
        }
        if (translatingTexts.contains(text)) {
            text.append(" ").append(TRANSLATING_TIP);
        } else if (!hasTranslateClick(text)) {
            String clickId = UUID.randomUUID().toString();
            CLICK_MAP.put(clickId, new ClickData(text, false));
            text.append(" ").append(Component.literal("[翻译]").withStyle(Style.EMPTY
                    .withColor(TextColor.fromRgb(65522))
                    .withHoverEvent(new HoverEvent.ShowText(Component.literal("点击翻译")))
                    .withClickEvent(new ClickEvent.RunCommand(CLICK_PREFIX + clickId))
                    .withInsertion(text.getString())));
        }
    }

    private static boolean isModSystemMessage(Component text) {
        if (text.getContents() instanceof TranslatableContents contents) {
            String key = contents.getKey();
            return key.startsWith("commands.transconfig.") || key.startsWith("translator.");
        }
        return false;
    }

    private static Component createErrorText(String err, MutableComponent originalText, String original) {
        if (originalText == null) {
            return Component.empty();
        }
        String clickId = UUID.randomUUID().toString();
        CLICK_MAP.put(clickId, new ClickData(originalText, true));
        return Component.literal("[" + err + "]").withStyle(Style.EMPTY
                .withColor(TextColor.fromRgb(13378339))
                .withHoverEvent(new HoverEvent.ShowText(Component.literal("点击重新翻译")))
                .withClickEvent(new ClickEvent.RunCommand(CLICK_PREFIX + clickId))
                .withInsertion(original));
    }

    private static Component createResultText(String result, MutableComponent originalText) {
        if (originalText == null) {
            return Component.empty();
        }
        String clickId = UUID.randomUUID().toString();
        CLICK_MAP.put(clickId, new ClickData(originalText, true));
        return Component.literal(result).withStyle(Style.EMPTY
                .withColor(TextColor.fromRgb(3145516))
                .withHoverEvent(new HoverEvent.ShowText(Component.literal("点击重新翻译")))
                .withClickEvent(new ClickEvent.RunCommand(CLICK_PREFIX + clickId))
                .withInsertion(result));
    }

    private static MutableComponent initText(Component text) {
        Component mutable = ensureMutable(text);
        return mutable instanceof MutableComponent mutableText ? mutableText : null;
    }

    @Nullable
    private static String getClickCommand(ClickEvent event) {
        // MC 1.21.5: ClickEvent è¯å°æ¥å£ï¼éè¿æ¨¡å¼å¹éè·åå½ä»¤
        if (event instanceof ClickEvent.RunCommand cmd) {
            return cmd.command();
        }
        return null;
    }

    private static String findClickId(MutableComponent text) {
        if (text == null || text.getSiblings().size() < 2) {
            return null;
        }
        ClickEvent event = text.getSiblings().getLast().getStyle().getClickEvent();
        if (event != null) {
            String cmd = getClickCommand(event);
            if (cmd != null && cmd.startsWith(CLICK_PREFIX) && !cmd.equals(CLICK_PREFIX + "none")) {
                return cmd.substring(CLICK_PREFIX.length());
            }
        }
        return null;
    }

    private static boolean hasTranslateClick(MutableComponent text) {
        return findClickId(text) != null;
    }

    private static boolean isClicked(MutableComponent text) {
        String id = findClickId(text);
        if (id != null) {
            ClickData data = CLICK_MAP.get(id);
            return data != null && data.clicked();
        }
        return false;
    }

    // 由 ScreenMixinForChat 调用
    public static boolean handleClickCommand(String command) {
        if (command != null && command.startsWith(CLICK_PREFIX)) {
            String id = command.substring(CLICK_PREFIX.length());
            ClickData data = CLICK_MAP.get(id);
            if (data != null && data.text() != null) {
                ChatHandler.translateWithTip(data.text());
                return true;
            }
        }
        return false;
    }
}
