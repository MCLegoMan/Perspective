/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.panorama;

import com.mclegoman.perspective.common.data.Data;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Util;

import java.io.File;

public class ScreenshotRecorder {
    public static void saveScreenshot(File gameDirectory, String fileName, Framebuffer framebuffer) {
        if (!RenderSystem.isOnRenderThread()) RenderSystem.recordRenderCall(() -> saveScreenshotInner(gameDirectory, fileName, framebuffer));
        else saveScreenshotInner(gameDirectory, fileName, framebuffer);
    }
    private static void saveScreenshotInner(File screenshotDir, String fileName, Framebuffer framebuffer) {
        NativeImage nativeImage = net.minecraft.client.util.ScreenshotRecorder.takeScreenshot(framebuffer);
        File file2 = new File(screenshotDir, fileName);
        Util.getIoWorkerExecutor().execute(() -> {
            try {
                nativeImage.writeTo(file2);
            } catch (Exception error) {
                Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to save screenshot: ", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
            }
            nativeImage.close();
        });
    }
}