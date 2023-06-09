/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.dataloader.PerspectiveDefaultConfigDataLoader;
import com.mclegoman.perspective.client.registry.PerspectiveKeybindings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class PerspectiveZoomUtils {
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
        OVERLAY = PerspectiveConfig.OVERLAY_DELAY;
        if (in) {
            if (PerspectiveConfig.ZOOM_LEVEL <= 0) PerspectiveConfig.ZOOM_LEVEL = 0;
            else PerspectiveConfig.ZOOM_LEVEL -= 1;
        }
        else {
            if (PerspectiveConfig.ZOOM_LEVEL >= 100) PerspectiveConfig.ZOOM_LEVEL = 100;
            else PerspectiveConfig.ZOOM_LEVEL += 1;
        }
        PerspectiveConfig.TICK_SAVE = true;
    }
    public static void reset() {
        OVERLAY = PerspectiveConfig.OVERLAY_DELAY;
        PerspectiveConfig.ZOOM_LEVEL = PerspectiveDefaultConfigDataLoader.ZOOM_LEVEL;
        PerspectiveConfig.TICK_SAVE = true;
    }
}