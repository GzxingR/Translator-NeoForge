package kgg.translator.screen;

import kgg.translator.ocrtrans.ResRegion;
import kgg.translator.util.TextUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.Component;

import java.util.List;

public class OcrScreen extends Screen {
    private final Screen parent;
    private ResRegion[] resRegions;
    private static final MutableComponent translatingText = Component.literal("翻译中");
    private static final MutableComponent esc = Component.literal("按Esc取消");
    private MutableComponent error;

    public OcrScreen(Screen parent) {
        super(Component.literal("Ocr"));
        this.parent = parent;
    }

    public void setResRegions(ResRegion[] resRegions) {
        this.resRegions = resRegions;
    }

    public void setError(String error) {
        this.error = Component.literal(error);
    }

    @Override
    protected void init() {
        if (resRegions != null) {
            this.minecraft.setScreen(this.parent);
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawString(font, esc, width - font.width(esc), height - font.lineHeight, 0xFFFFFF, true);
        if (error != null) {
            context.drawCenteredString(font, error, width / 2, height / 2, 0xFF0000);
        } else if (resRegions == null) {
            context.drawCenteredString(font, translatingText, width / 2, height / 2, 0xFFFFFF);
        } else {
            for (ResRegion resRegion : resRegions) {
                context.fill(resRegion.x(), resRegion.y(), resRegion.x() + resRegion.w(), resRegion.y() + resRegion.h(), 0x96ff7272);

                PoseStack pose = context.pose();
                pose.pushPose();

                String dstText = resRegion.dst();
                float textWidth = font.width(dstText);
                float textHeight = font.lineHeight;

                float scale = Math.min(resRegion.w() / textWidth, resRegion.h() / textHeight);
                scale = Math.max(scale, 1.0f);

                if (resRegion.h() > textHeight * 2) {
                    List<FormattedCharSequence> lines = font.split(Component.literal(dstText), resRegion.w());
                    float totalHeight = lines.size() * textHeight;
                    float lineSpacing = font.lineHeight + 1;
                    scale = Math.min(resRegion.w() / textWidth, resRegion.h() / (totalHeight + (lines.size() - 1) * lineSpacing));
                    scale = Math.max(scale, 1.0f);

                    float lineVerticalRange = (resRegion.h() - (lines.size() * (textHeight + lineSpacing) * scale)) / (lines.size() + 1);
                    float textY = resRegion.y() + lineVerticalRange + (textHeight * scale) / 2;

                    pose.scale(scale, scale, scale);
                    for (FormattedCharSequence line : lines) {
                        float lineWidth = font.width(TextUtil.getString(line));
                        float textX = resRegion.x() + (resRegion.w() - lineWidth * scale) / 2;
                        context.drawString(font, line, (int) (textX / scale), (int) (textY / scale), 0x1bb7ff, true);
                        textY += lineVerticalRange + lineSpacing * scale;
                    }
                } else {
                    float textWidth2 = textWidth * scale;
                    float textHeight2 = textHeight * scale;
                    float textX = resRegion.x() + (resRegion.w() - textWidth2) / 2;
                    float textY = resRegion.y() + (resRegion.h() - textHeight2) / 2;

                    pose.scale(scale, scale, scale);
                    context.drawString(font, dstText, (int) (textX / scale), (int) (textY / scale), 0x1bb7ff, true);
                }

                pose.popPose();
            }
        }
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (resRegions == null || resRegions.length == 0) {
            super.renderBackground(context, mouseX, mouseY, delta);
        }
    }
}
