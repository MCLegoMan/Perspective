/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.zoom;

import com.mclegoman.perspective.client.config.PerspectiveConfigDataLoader;
import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.overlays.PerspectiveHUDOverlays;
import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class PerspectiveZoom {
    public static double fov;
    private static boolean zoomUpdated;
    public static double prevZoomMultiplier;
    public static double zoomMultiplier;
    public static void updateZoomMultiplier() {
        float f = getZoomMultiplier();
        prevZoomMultiplier = zoomMultiplier;
        zoomMultiplier += (f - zoomMultiplier) * 0.5F;
    }

    public static float getZoomMultiplier() {
        return isZooming() ? 1 - ((float) getZoomLevel() / 100) : 1;
    }
    public static double limitFov(double fov) {
        return Math.max(Math.max(0.1, fov), Math.min(fov, 110));
    }

    public static boolean SET_ZOOM;
    public static boolean isZooming() {
        try {
            if (PerspectiveClientData.CLIENT.player != null) return PerspectiveKeybindings.HOLD_ZOOM.isPressed() || SET_ZOOM;
        } catch (Exception ignored) {
        }
        return false;
    }
    public static void tick(MinecraftClient client) {
        try {
            updateZoomMultiplier();
            if (PerspectiveKeybindings.TOGGLE_ZOOM.wasPressed()) SET_ZOOM = !SET_ZOOM;
            if (!isZooming() && zoomUpdated) {
                PerspectiveConfigHelper.saveConfig(true);
                zoomUpdated = false;
            }
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to tick zoom: {}", (Object)error);
        }
    }
    public static int getZoomLevel() {
        return (int)PerspectiveConfigHelper.getConfig("zoom_level");
    }
    public static void zoom(boolean in) {
        try {
            boolean updated = false;
                if (in) {
                    if (!(getZoomLevel() >= 100)) {
                        PerspectiveConfigHelper.setConfig("zoom_level", getZoomLevel() + 1);
                        updated = true;
                        zoomUpdated = true;
                    }
                }
                else {
                    if (!(getZoomLevel() <= -50)) {
                        PerspectiveConfigHelper.setConfig("zoom_level", getZoomLevel() - 1);
                        updated = true;
                        zoomUpdated = true;
                    }
                }
            if (updated) {
                setOverlay();
                updated = false;
            }
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
        PerspectiveHUDOverlays.setOverlay(Text.translatable("gui.perspective.message.zoom_level", Text.literal((int)PerspectiveConfigHelper.getConfig("zoom_level") + "%")).formatted(Formatting.GOLD));
    }
}