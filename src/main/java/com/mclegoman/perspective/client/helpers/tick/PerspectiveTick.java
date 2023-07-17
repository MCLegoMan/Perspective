/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.helpers.tick;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.util.PerspectivePanoramaUtils;
import com.mclegoman.perspective.client.util.PerspectivePerspectiveUtils;
import com.mclegoman.perspective.client.util.PerspectiveSuperSecretSettingsUtil;
import com.mclegoman.perspective.client.util.PerspectiveZoomUtils;
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