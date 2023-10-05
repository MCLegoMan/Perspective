/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client;

import com.mclegoman.perspective.client.april_fools_prank.PerspectiveAprilFoolsPrank;
import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.overlays.PerspectiveHUDOverlays;
import com.mclegoman.perspective.client.panorama.PerspectivePanorama;
import com.mclegoman.perspective.client.shaders.PerspectiveShader;
import com.mclegoman.perspective.client.textured_entity.PerspectiveTexturedEntity;
import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import com.mclegoman.perspective.client.util.PerspectiveResourcePacks;
import com.mclegoman.perspective.client.util.PerspectiveTick;
import com.mclegoman.perspective.common.data.PerspectiveData;
import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PerspectiveClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		try {
			PerspectiveData.PERSPECTIVE_VERSION.getLogger().info("{} Initializing {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), PerspectiveData.PERSPECTIVE_VERSION.getID());
			CrowdinTranslate.downloadTranslations(PerspectiveData.PERSPECTIVE_VERSION.getID());
			PerspectiveConfigHelper.init();
			PerspectiveResourcePacks.init();
			PerspectiveKeybindings.init();
			PerspectiveShader.init();
			PerspectivePanorama.init();
			PerspectiveTexturedEntity.init();
			PerspectiveAprilFoolsPrank.init();
			PerspectiveHUDOverlays.init();
			PerspectiveTick.init();
		} catch (Exception error) {
			PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to run onInitializeClient: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
		}
	}
}