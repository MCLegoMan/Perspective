/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.april_fools_prank.PerspectiveAprilFoolsPrank;
import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.hide.PerspectiveHide;
import com.mclegoman.perspective.client.overlays.PerspectiveHUDOverlays;
import com.mclegoman.perspective.client.panorama.PerspectivePanorama;
import com.mclegoman.perspective.client.perspective.PerspectivePerspective;
import com.mclegoman.perspective.client.shaders.PerspectiveShader;
import com.mclegoman.perspective.client.zoom.PerspectiveZoom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@Environment(EnvType.CLIENT)
public class PerspectiveTick {
    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.isFinishedLoading()) {
                PerspectiveConfigHelper.tick(client);
                PerspectiveAprilFoolsPrank.tick(client);
                PerspectiveKeybindings.tick(client);
                PerspectivePerspective.tick(client);
                PerspectiveZoom.tick(client);
                PerspectiveShader.tick(client);
                PerspectivePanorama.tick(client);
                PerspectiveHUDOverlays.tick(client);
                PerspectiveHide.tick(client);
                PerspectiveUpdateChecker.tick(client);
            }
        });
    }
}