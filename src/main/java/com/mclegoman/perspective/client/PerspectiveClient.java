/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client;

import com.mclegoman.perspective.client.april_fools_prank.AprilFoolsPrank;
import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.hide.Hide;
import com.mclegoman.perspective.client.panorama.Panorama;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.client.util.ResourcePacks;
import com.mclegoman.perspective.client.util.Tick;
import com.mclegoman.perspective.common.data.Data;
import de.guntram.mcmod.crowdintranslate.CrowdinTranslate;
import net.fabricmc.api.ClientModInitializer;

public class PerspectiveClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		try {
			Data.PERSPECTIVE_VERSION.getLogger().info("{} Initializing {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), Data.PERSPECTIVE_VERSION.getID());
			CrowdinTranslate.downloadTranslations(Data.PERSPECTIVE_VERSION.getID());
			ConfigHelper.init();
			ResourcePacks.init();
			Hide.init();
			Keybindings.init();
			Shader.init();
			Panorama.init();
			TexturedEntity.init();
			AprilFoolsPrank.init();
			Tick.init();
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to run onInitializeClient: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
		}
	}
}