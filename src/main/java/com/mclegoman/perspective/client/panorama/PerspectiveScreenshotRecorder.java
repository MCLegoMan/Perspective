/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.panorama;

import com.mclegoman.perspective.common.data.PerspectiveData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.util.Util;

import java.io.File;

public class PerspectiveScreenshotRecorder {
    public static void saveScreenshot(File gameDirectory, String fileName, Framebuffer framebuffer) {
        if (!RenderSystem.isOnRenderThread()) RenderSystem.recordRenderCall(() -> saveScreenshotInner(gameDirectory, fileName, framebuffer));
        else saveScreenshotInner(gameDirectory, fileName, framebuffer);
    }
    private static void saveScreenshotInner(File screenshotDir, String fileName, Framebuffer framebuffer) {
        NativeImage nativeImage = ScreenshotRecorder.takeScreenshot(framebuffer);
        File file2 = new File(screenshotDir, fileName);
        Util.getIoWorkerExecutor().execute(() -> {
            try {
                nativeImage.writeTo(file2);
            } catch (Exception error) {
                PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to save screenshot: ", error);
            }
            nativeImage.close();
        });
    }
}