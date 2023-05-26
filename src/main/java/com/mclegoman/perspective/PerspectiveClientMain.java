package com.mclegoman.perspective;

import com.mclegoman.perspective.config.PerspectiveConfig;
import com.mclegoman.perspective.data.PerspectiveData;
import com.mclegoman.perspective.registry.PerspectiveKeybindRegistry;
import com.mclegoman.perspective.util.PerspectiveUtils;
import net.fabricmc.api.ClientModInitializer;

public class PerspectiveClientMain implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PerspectiveData.LOGGER.info(PerspectiveData.PREFIX + "Initializing Perspective.");
        PerspectiveConfig.init();
        PerspectiveKeybindRegistry.init();
        PerspectiveUtils.init();
    }
}