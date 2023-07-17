/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.config.PerspectiveDefaultConfigDataLoader;
import com.mclegoman.perspective.client.registry.PerspectiveKeybindings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class PerspectiveZoomUtils {
    public static double getZoomFOV(MinecraftClient client) {
        return Math.max(Math.max(1, (100 - (int)PerspectiveConfigHelper.getConfig("zoom_level")) * client.options.getFov().getValue() / 100), Math.min(client.options.getFov().getValue(), (100 - (int)PerspectiveConfigHelper.getConfig("zoom_level")) * client.options.getFov().getValue() / 100));
    }
    public static int OVERLAY;
    public static boolean SET_ZOOM;
    public static boolean isZooming() {
        try {
            return PerspectiveKeybindings.KEY_HOLD_ZOOM.isPressed() || SET_ZOOM;
        } catch (Exception ignored) {
        }
        return false;
    }
    public static void tick(MinecraftClient client) {
        if (PerspectiveKeybindings.KEY_ZOOM_IN.wasPressed()) zoom(true);
        if (PerspectiveKeybindings.KEY_ZOOM_OUT.wasPressed()) zoom(false);
        if (PerspectiveKeybindings.KEY_ZOOM_RESET.wasPressed()) reset();
        if (PerspectiveKeybindings.KEY_SET_ZOOM.wasPressed()) SET_ZOOM = !SET_ZOOM;
        if (OVERLAY > 0) OVERLAY = OVERLAY - 1;
    }
    public static void zoom(boolean in) {
        OVERLAY = Math.min((int)PerspectiveConfigHelper.getConfig("overlay_delay") * 20, (10) * 20);
        if (in) {
            if ((int)PerspectiveConfigHelper.getConfig("zoom_level") >= 100) PerspectiveConfigHelper.setConfig("zoom_level", 100);
            else PerspectiveConfigHelper.setConfig("zoom_level", (int)PerspectiveConfigHelper.getConfig("zoom_level") + 1);
        }
        else {
            if ((int)PerspectiveConfigHelper.getConfig("zoom_level") <= 0) PerspectiveConfigHelper.setConfig("zoom_level", 0);
            else PerspectiveConfigHelper.setConfig("zoom_level", (int)PerspectiveConfigHelper.getConfig("zoom_level") - 1);
        }
        PerspectiveConfigHelper.saveConfig(true);
    }
    public static void reset() {
        OVERLAY = Math.min((int)PerspectiveConfigHelper.getConfig("overlay_delay") * 20, (10) * 20);
        PerspectiveConfigHelper.setConfig("zoom_level", PerspectiveDefaultConfigDataLoader.ZOOM_LEVEL);
        PerspectiveConfigHelper.saveConfig(true);
    }
}