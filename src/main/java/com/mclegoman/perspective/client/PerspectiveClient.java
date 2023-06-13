package com.mclegoman.perspective.client;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.helpers.PerspectiveTick;
import com.mclegoman.perspective.client.registry.PerspectiveKeybindings;
import com.mclegoman.perspective.client.util.PerspectivePerspectiveUtils;
import com.mclegoman.perspective.client.util.PerspectiveSuperSecretSettingsUtil;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.ClientModInitializer;

public class PerspectiveClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PerspectiveData.LOGGER.info(PerspectiveData.PREFIX + "Initializing Perspective");
        PerspectiveConfig.init();
        PerspectiveKeybindings.init();
        PerspectivePerspectiveUtils.init();
        PerspectiveSuperSecretSettingsUtil.init();
        PerspectiveTick.init();
    }
}