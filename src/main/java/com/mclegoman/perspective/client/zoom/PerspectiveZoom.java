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
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class PerspectiveZoom {
    public static double zoomFOV(double fov) {
        return Math.max(Math.max(1, (100 - (int)PerspectiveConfigHelper.getConfig("zoom_level")) * fov / 100), Math.min(fov, (100 - (int)PerspectiveConfigHelper.getConfig("zoom_level")) * fov / 100));
    }
    public static double CURRENT_FOV = PerspectiveClientData.CLIENT.options.getFov().getValue();
    public static double getZoom(boolean in, boolean smooth, double fov) {
        if (in) {
            if (smooth) {
                CURRENT_FOV = MathHelper.lerp(PerspectiveClientData.CLIENT.getLastFrameDuration() / 2, CURRENT_FOV, PerspectiveZoom.zoomFOV(fov));
            } else {
                CURRENT_FOV = zoomFOV(fov);
            }
        } else {
            if (smooth) {
                CURRENT_FOV = MathHelper.lerp(PerspectiveClientData.CLIENT.getLastFrameDuration() / 2, CURRENT_FOV, fov);
            } else {
                CURRENT_FOV = PerspectiveClientData.CLIENT.options.getFov().getValue();
            }
        }
        return CURRENT_FOV;
    }
    public static void stopZoom(double fov) {
        CURRENT_FOV = fov;
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