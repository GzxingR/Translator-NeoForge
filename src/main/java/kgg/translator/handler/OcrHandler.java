package kgg.translator.handler;

import kgg.translator.TranslatorManager;
import kgg.translator.TranslateService;
import kgg.translator.ocrtrans.ResRegion;
import kgg.translator.screen.OcrScreen;
import kgg.translator.util.TranslateExceptionUtil;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.stb.STBImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class OcrHandler {
    private static final Logger LOGGER = LogManager.getLogger(OcrHandler.class);

    public static void start() {
        Minecraft client = Minecraft.getInstance();
        OcrScreen screen = new OcrScreen(client.screen);
        client.setScreen(screen);

        LOGGER.info("OCR started, current translator: {} (configured: {})",
            TranslatorManager.getCurrent() != null ? TranslatorManager.getCurrent().getName() : "none",
            TranslatorManager.getCurrent() != null ? TranslatorManager.getCurrent().isConfigured() : false);

        try (NativeImage nativeImage = Screenshot.takeScreenshot(client.getMainRenderTarget())) {
            LOGGER.info("Screenshot taken: {}x{}", nativeImage.getWidth(), nativeImage.getHeight());
            byte[] bytes = getBytes(nativeImage);
            LOGGER.info("Screenshot bytes: {}", bytes.length);
            CompletableFuture.runAsync(() -> {
                try {
                    ResRegion[] ocrtrans = TranslateService.ocrtrans(bytes);
                    LOGGER.info("OCR translation completed, regions: {}", ocrtrans.length);
                    ocrtrans = Arrays.stream(ocrtrans).map(resRegion ->
                            resRegion.scale(1f / client.getWindow().getGuiScale())).toArray(ResRegion[]::new);
                    screen.setResRegions(ocrtrans);
                } catch (Exception e) {
                    LOGGER.error("OCR translation failed", e);
                    String msg = TranslateExceptionUtil.getDisplayMessage(e);
                    if (msg.contains("不支持图片翻译")) {
                        String current = TranslatorManager.getCurrent() != null ? TranslatorManager.getCurrent().getName() : "unknown";
                        msg = current + " 不支持图片翻译。\n请使用百度翻译（支持OCR图片翻译）并配置正确的API密钥。";
                    }
                    screen.setError(msg);
                }
            });
        } catch (IOException e) {
            LOGGER.error("OCR screenshot failed", e);
            throw new RuntimeException(e);
        }
    }

    public static byte[] getBytes(NativeImage nativeImage) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        WritableByteChannel writableByteChannel = Channels.newChannel(byteArrayOutputStream);
        if (!nativeImage.writeToChannel(writableByteChannel)) {
            throw new IOException("Could not write image to byte array: " + STBImage.stbi_failure_reason());
        }
        writableByteChannel.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
