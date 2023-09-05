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
    public static double smoothZoom = PerspectiveClientData.CLIENT.options.getFov().getValue();
    public static double zoomFOV(MinecraftClient client) {
        return Math.max(Math.max(1, (100 - (int)PerspectiveConfigHelper.getConfig("zoom_level")) * client.options.getFov().getValue() / 100), Math.min(client.options.getFov().getValue(), (100 - (int)PerspectiveConfigHelper.getConfig("zoom_level")) * client.options.getFov().getValue() / 100));
    }
    public static double getZoomFOV(MinecraftClient client, boolean direction, boolean smooth_zoom) {
        if (smooth_zoom) {
            if (direction) {
                return Math.max(smoothZoom, zoomFOV(client));
            }
            else {
                return Math.min(smoothZoom, PerspectiveClientData.CLIENT.options.getFov().getValue());
            }
        } else return zoomFOV(client);
    }
    public static void setSmoothZoom(MinecraftClient client, int amount, boolean direction) {
        for (int i = 0; i < amount; i++) {
            if (direction) smoothZoom = Math.max(smoothZoom - 1, zoomFOV(client));
            else smoothZoom = Math.min(smoothZoom + 1, PerspectiveClientData.CLIENT.options.getFov().getValue());
        }
    }
    public static boolean SET_ZOOM;
    public static boolean isZooming() {
        try {
            if (PerspectiveClientData.CLIENT.player != null) return (PerspectiveKeybindings.HOLD_ZOOM.isPressed() || SET_ZOOM) && !PerspectiveClientData.CLIENT.player.isUsingSpyglass();
        } catch (Exception ignored) {
        }
        return false;
    }
    public static void tick(MinecraftClient client) {
        try {
            if (PerspectiveKeybindings.TOGGLE_ZOOM.wasPressed()) SET_ZOOM = !SET_ZOOM;
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to tick zoom: {}", (Object)error);
        }
    }
    public static void zoom(boolean in, int amount) {
        try {
            for (int i = 0; i < amount; i++){
                if (in) {
                    if ((int)PerspectiveConfigHelper.getConfig("zoom_level") >= 100) PerspectiveConfigHelper.setConfig("zoom_level", 100);
                    else PerspectiveConfigHelper.setConfig("zoom_level", (int)PerspectiveConfigHelper.getConfig("zoom_level") + 1);
                }
                else {
                    if ((int)PerspectiveConfigHelper.getConfig("zoom_level") <= 0) PerspectiveConfigHelper.setConfig("zoom_level", 0);
                    else PerspectiveConfigHelper.setConfig("zoom_level", (int)PerspectiveConfigHelper.getConfig("zoom_level") - 1);
                }
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
        PerspectiveHUDOverlays.setOverlay(Text.translatable("gui.perspective.message.zoom_level", Text.literal((int)PerspectiveConfigHelper.getConfig("zoom_level") + "%")).formatted(Formatting.GOLD));
    }
}