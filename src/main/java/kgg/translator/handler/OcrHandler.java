package kgg.translator.handler;

import kgg.translator.TranslateService;
import kgg.translator.ocrtrans.ResRegion;
import kgg.translator.screen.OcrScreen;
import kgg.translator.util.TranslateExceptionUtil;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        // MC 1.21.5: Screenshot.takeScreenshot(RenderTarget, Consumer<NativeImage>)
        Screenshot.takeScreenshot(client.getMainRenderTarget(), nativeImage -> {
            try {
                byte[] bytes = getBytes(nativeImage);
                CompletableFuture.runAsync(() -> {
                    try {
                        ResRegion[] ocrtrans = TranslateService.ocrtrans(bytes);
                        ocrtrans = Arrays.stream(ocrtrans).map(resRegion ->
                                resRegion.scale(1f / client.getWindow().getGuiScale())).toArray(ResRegion[]::new);
                        screen.setResRegions(ocrtrans);
                    } catch (Exception e) {
                        screen.setError(TranslateExceptionUtil.getDisplayMessage(e));
                    }
                });
            } catch (IOException e) {
                LOGGER.error("Failed to process screenshot", e);
            }
        });
    }

    public static byte[] getBytes(NativeImage nativeImage) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        WritableByteChannel writableByteChannel = Channels.newChannel(byteArrayOutputStream);
        if (!nativeImage.writeToChannel(writableByteChannel)) {
            throw new IOException("Could not write image to byte array");
        }
        writableByteChannel.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
