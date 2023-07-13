/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.helpers.PerspectiveTick;
import com.mclegoman.perspective.client.registry.PerspectiveKeybindings;
import com.mclegoman.perspective.client.registry.PerspectiveResourcePacks;
import com.mclegoman.perspective.client.util.*;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PerspectiveClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PerspectiveData.LOGGER.info(PerspectiveData.PREFIX + "Initializing Perspective");
        PerspectiveConfig.init();
        PerspectiveResourcePacks.init();
        PerspectiveKeybindings.init();
        PerspectiveSuperSecretSettingsUtil.init();
        PerspectiveTexturedEntityUtils.init();
        PerspectiveAprilFoolsUtils.init();
        PerspectivePerspectiveUtils.init();
        PerspectiveTick.init();
    }
}