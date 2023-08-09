package com.mclegoman.perspective.client.panorama;

import com.mclegoman.perspective.common.data.PerspectiveData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.function.Consumer;

public class PerspectiveScreenshotRecorder {
    public static void saveScreenshot(File gameDirectory, @Nullable String fileName, Framebuffer framebuffer, Consumer<Text> messageReceiver) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> {
                saveScreenshotInner(gameDirectory, fileName, framebuffer, messageReceiver);
            });
        } else {
            saveScreenshotInner(gameDirectory, fileName, framebuffer, messageReceiver);
        }

    }
    private static void saveScreenshotInner(File screenshotDir, @Nullable String fileName, Framebuffer framebuffer, Consumer<Text> messageReceiver) {
        NativeImage nativeImage = ScreenshotRecorder.takeScreenshot(framebuffer);
        File file2;
        if (fileName == null) {
            file2 = ScreenshotRecorder.getScreenshotFilename(screenshotDir);
        } else {
            file2 = new File(screenshotDir, fileName);
        }
        Util.getIoWorkerExecutor().execute(() -> {
            try {
                nativeImage.writeTo(file2);
                Text text = Text.literal(file2.getName()).formatted(Formatting.UNDERLINE).styled((style) -> {
                    return style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath()));
                });
                messageReceiver.accept(Text.translatable("screenshot.success", text));
            } catch (Exception var7) {
                PerspectiveData.LOGGER.warn("Couldn't save screenshot", var7);
                messageReceiver.accept(Text.translatable("screenshot.failure", var7.getMessage()));
            } finally {
                nativeImage.close();
            }
        });
    }
}
