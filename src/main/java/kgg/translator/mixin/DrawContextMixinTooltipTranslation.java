package kgg.translator.mixin;

import kgg.translator.handler.TranslateHelper;
import kgg.translator.option.Options;
import kgg.translator.translator.Source;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(GuiGraphics.class)
public abstract class DrawContextMixinTooltipTranslation {

    // 1.21.1: renderTooltip(Font, List<Component>, Optional<TooltipComponent>, int, int)
    @ModifyVariable(
        method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V",
        at = @At("HEAD"),
        ordinal = 0,
        require = 0
    )
    private List<Component> translator$translateTooltipItemName_v1(List<Component> text, Font font, List<Component> list, Optional<TooltipComponent> data, int x, int y) {
        return translateFirstLine(text);
    }

    // 1.21.5: renderTooltip(Font, List<Component>, int, int)
    @ModifyVariable(
        method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V",
        at = @At("HEAD"),
        ordinal = 0,
        require = 0
    )
    private List<Component> translator$translateTooltipItemName_v2(List<Component> text, Font font, List<Component> list, int x, int y) {
        return translateFirstLine(text);
    }

    @SuppressWarnings("unchecked")
    private List<Component> translateFirstLine(List<?> text) {
        if (!Options.autoTooltip.get() || text == null || text.isEmpty()) {
            return (List<Component>) text;
        }
        // 类型检查：确保第一个元素是 Component（排除 FormattedCharSequence 等）
        Object first = text.get(0);
        if (!(first instanceof Component firstComponent)) {
            return (List<Component>) text;
        }
        ArrayList<Component> newList = new ArrayList<>(text.size());
        for (Object obj : text) {
            newList.add((Component) obj);
        }
        newList.set(0, TranslateHelper.translateNoWait(firstComponent, Source.ITEM_NAME));
        return newList;
    }
}
