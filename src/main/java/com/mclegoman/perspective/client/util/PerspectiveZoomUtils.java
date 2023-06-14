/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0

*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.registry.PerspectiveKeybindings;
import net.minecraft.client.MinecraftClient;

public class PerspectiveZoomUtils {
    public static int OVERLAY;
    public static boolean SET_ZOOM;
    public static boolean isZooming() {
        return PerspectiveKeybindings.KEY_HOLD_ZOOM.isPressed() || SET_ZOOM;
    }
    public static void tick(MinecraftClient client) {
        if (PerspectiveKeybindings.KEY_ZOOM_IN.wasPressed()) in();
        if (PerspectiveKeybindings.KEY_ZOOM_OUT.wasPressed()) out();
        if (PerspectiveKeybindings.KEY_SET_ZOOM.wasPressed()) SET_ZOOM = !SET_ZOOM;
        if (OVERLAY > 0) OVERLAY = OVERLAY -1;
    }
    public static void in() {
        OVERLAY = PerspectiveConfig.OVERLAY_DELAY;
        if (!((PerspectiveConfig.ZOOM_LEVEL - 1) < 0)) {
            PerspectiveConfig.ZOOM_LEVEL = PerspectiveConfig.ZOOM_LEVEL - 1;
            PerspectiveConfig.write_to_file();
        }
    }
    public static void out() {
        OVERLAY = PerspectiveConfig.OVERLAY_DELAY;
        if (!((PerspectiveConfig.ZOOM_LEVEL + 1) > 100)) {
            PerspectiveConfig.ZOOM_LEVEL = PerspectiveConfig.ZOOM_LEVEL + 1;
            PerspectiveConfig.write_to_file();
        }
    }
}