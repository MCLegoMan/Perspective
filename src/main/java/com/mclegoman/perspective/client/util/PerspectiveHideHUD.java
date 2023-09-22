package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.perspective.PerspectivePerspective;
import com.mclegoman.perspective.client.zoom.PerspectiveZoom;

public class PerspectiveHideHUD {
    public static boolean shouldHideHUD() {
        return (PerspectiveZoom.isZooming() || PerspectivePerspective.isHoldingPerspective()) && (Boolean)PerspectiveConfigHelper.getConfig("hide_hud");
    }
}
