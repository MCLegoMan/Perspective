/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client;

import com.mclegoman.perspective.client.april_fools.PerspectiveAprilFoolsUtils;
import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.perspective.PerspectivePerspectiveUtils;
import com.mclegoman.perspective.client.super_secret_settings.PerspectiveSuperSecretSettingsUtil;
import com.mclegoman.perspective.client.textured_entity.PerspectiveTexturedEntityUtils;
import com.mclegoman.perspective.client.tick.PerspectiveTick;
import com.mclegoman.perspective.client.registry.PerspectiveKeybindings;
import com.mclegoman.perspective.client.registry.PerspectiveResourcePacks;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PerspectiveClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PerspectiveData.LOGGER.info(PerspectiveData.PREFIX + "Initializing Perspective");
        PerspectiveConfigHelper.init();
        PerspectiveResourcePacks.init();
        PerspectiveKeybindings.init();
        PerspectiveSuperSecretSettingsUtil.init();
        PerspectiveTexturedEntityUtils.init();
        PerspectiveAprilFoolsUtils.init();
        PerspectivePerspectiveUtils.init();
        PerspectiveTick.init();
    }
}