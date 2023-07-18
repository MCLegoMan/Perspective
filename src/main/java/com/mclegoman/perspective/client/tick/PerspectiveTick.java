/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.tick;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.panorama.PerspectivePanoramaUtils;
import com.mclegoman.perspective.client.perspective.PerspectivePerspectiveUtils;
import com.mclegoman.perspective.client.super_secret_settings.PerspectiveSuperSecretSettingsUtil;
import com.mclegoman.perspective.client.zoom.PerspectiveZoomUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@Environment(EnvType.CLIENT)
public class PerspectiveTick {
    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PerspectivePerspectiveUtils.tick(client);
            PerspectiveSuperSecretSettingsUtil.tick(client);
            PerspectiveZoomUtils.tick(client);
            PerspectiveConfigHelper.tick(client);
            PerspectivePanoramaUtils.tick(client);
        });
    }
}