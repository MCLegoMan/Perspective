/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.experimental.PerspectiveExperimental;
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
            PerspectivePerspective.tick(client);
            PerspectiveShader.tick(client);
            PerspectiveZoom.tick(client);
            PerspectiveConfigHelper.tick(client);
            PerspectivePanorama.tick(client);
            PerspectiveExperimental.tick(client);
        });
    }
}