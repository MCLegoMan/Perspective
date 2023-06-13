package com.mclegoman.perspective.client.helpers;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.util.PerspectivePerspectiveUtils;
import com.mclegoman.perspective.client.util.PerspectiveSuperSecretSettingsUtil;
import com.mclegoman.perspective.client.util.PerspectiveZoomUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class PerspectiveTick {
    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PerspectivePerspectiveUtils.tick(client);
            PerspectiveSuperSecretSettingsUtil.tick(client);
            PerspectiveZoomUtils.tick(client);
            PerspectiveConfig.tick(client);
        });
    }
}