/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.zoom;

import com.mclegoman.perspective.client.config.PerspectiveConfigDataLoader;
import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.overlay.PerspectiveOverlay;
import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class PerspectiveZoom {
    public static Text OVERLAY_MESSAGE;
    public static int OVERLAY_REMAINING;
    public static double getZoomFOV(MinecraftClient client) {
        return Math.max(Math.max(1, (100 - (int)PerspectiveConfigHelper.getConfig("zoom_level")) * client.options.getFov().getValue() / 100), Math.min(client.options.getFov().getValue(), (100 - (int)PerspectiveConfigHelper.getConfig("zoom_level")) * client.options.getFov().getValue() / 100));
    }
    public static boolean SET_ZOOM;
    public static boolean isZooming() {
        try {
            return PerspectiveKeybindings.HOLD_ZOOM.isPressed() || SET_ZOOM;
        } catch (Exception ignored) {
        }
        return false;
    }
    public static void tick(MinecraftClient client) {
        try {
            if (PerspectiveKeybindings.TOGGLE_ZOOM.wasPressed()) SET_ZOOM = !SET_ZOOM;
            if (OVERLAY_REMAINING > 0) OVERLAY_REMAINING -= 1;
            else OVERLAY_MESSAGE = null;
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to tick zoom: {}", (Object)error);
        }
    }
    public static void zoom(boolean in, MinecraftClient client) {
        try {
            if (in) {
                if ((int)PerspectiveConfigHelper.getConfig("zoom_level") >= 100) PerspectiveConfigHelper.setConfig("zoom_level", 100);
                else PerspectiveConfigHelper.setConfig("zoom_level", (int)PerspectiveConfigHelper.getConfig("zoom_level") + 1);
            }
            else {
                if ((int)PerspectiveConfigHelper.getConfig("zoom_level") <= 0) PerspectiveConfigHelper.setConfig("zoom_level", 0);
                else PerspectiveConfigHelper.setConfig("zoom_level", (int)PerspectiveConfigHelper.getConfig("zoom_level") - 1);
            }
            setOverlay();
            PerspectiveConfigHelper.saveConfig(true);
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to set zoom level: {}", (Object)error);
        }
    }
    public static void reset(MinecraftClient client) {
        try {
            if ((int)PerspectiveConfigHelper.getConfig("zoom_level") != PerspectiveConfigDataLoader.ZOOM_LEVEL) {
                PerspectiveConfigHelper.setConfig("zoom_level", PerspectiveConfigDataLoader.ZOOM_LEVEL);
                setOverlay();
                PerspectiveConfigHelper.saveConfig(true);
            }
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to reset zoom level: {}", (Object)error);
        }
    }
    private static void setOverlay(){
        PerspectiveOverlay.setOverlay(Text.translatable("gui.perspective.message.zoom_level", Text.literal((int)PerspectiveConfigHelper.getConfig("zoom_level") + "%")).formatted(Formatting.GOLD));
    }
}