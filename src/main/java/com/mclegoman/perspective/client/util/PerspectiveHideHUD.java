/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.perspective.PerspectivePerspective;
import com.mclegoman.perspective.client.zoom.PerspectiveZoom;

public class PerspectiveHideHUD {
    public static boolean shouldHideHUD() {
        return (PerspectiveZoom.isZooming() && (Boolean)PerspectiveConfigHelper.getConfig("zoom_hide_hud")) || (PerspectivePerspective.isHoldingPerspective() && (Boolean)PerspectiveConfigHelper.getConfig("hold_perspective_hide_hud")) || PerspectiveClientData.CLIENT.gameRenderer.isRenderingPanorama();
    }
}
