package com.mclegoman.perspective;

import com.mclegoman.perspective.config.PerspectiveConfig;
import com.mclegoman.perspective.util.PerspectiveUtils;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerspectiveMain implements ClientModInitializer {
    public static Logger LOGGER = LoggerFactory.getLogger("perspective");
    @Override
    public void onInitializeClient() {
        PerspectiveConfig.init();
        PerspectiveUtils.init();
    }
}