/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client;

import com.mclegoman.perspective.client.aprilfoolsprank.PerspectiveAprilFoolsPrank;
import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.perspective.PerspectivePerspective;
import com.mclegoman.perspective.client.shaders.PerspectiveSuperSecretSettings;
import com.mclegoman.perspective.client.texturedentity.PerspectiveTexturedEntity;
import com.mclegoman.perspective.client.util.PerspectiveTick;
import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import com.mclegoman.perspective.client.util.PerspectiveResourcePacks;
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
        PerspectiveSuperSecretSettings.init();
        PerspectiveTexturedEntity.init();
        PerspectiveAprilFoolsPrank.init();
        PerspectivePerspective.init();
        PerspectiveTick.init();
    }
}