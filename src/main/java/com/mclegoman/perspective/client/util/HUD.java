/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.perspective.Perspective;
import com.mclegoman.perspective.client.zoom.Zoom;

public class HUD {
    public static boolean DEBUG = false;
    public static void tick() {
        if (Keybindings.DEBUG.wasPressed()) DEBUG = !DEBUG;
    }
    public static boolean shouldHideHUD() {
        return (Zoom.isZooming() && (Boolean) ConfigHelper.getConfig("zoom_hide_hud")) || (Perspective.isHoldingPerspective() && (Boolean) ConfigHelper.getConfig("hold_perspective_hide_hud")) || ClientData.CLIENT.gameRenderer.isRenderingPanorama();
    }
    public static int addY(int y) {
        return y + 2 + 9;
    }
}
