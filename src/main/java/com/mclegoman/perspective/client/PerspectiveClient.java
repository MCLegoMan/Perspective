/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client;

import com.mclegoman.perspective.client.april_fools_prank.AprilFoolsPrank;
import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.contributor.Contributor;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hide.Hide;
import com.mclegoman.perspective.client.panorama.Panorama;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.client.util.ResourcePacks;
import com.mclegoman.perspective.client.util.Tick;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.api.ClientModInitializer;

public class PerspectiveClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		try {
			Data.PERSPECTIVE_VERSION.getLogger().info("{} Initializing {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), Data.PERSPECTIVE_VERSION.getID());
			AprilFoolsPrank.init();
			ConfigHelper.init();
			Contributor.init();
			Hide.init();
			Keybindings.init();
			Shader.init();
			TexturedEntity.init();
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to run onInitializeClient: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
		}
	}
	public static void onInitializeClientAfterConfig() {
		try {
			Panorama.init();
			ResourcePacks.init();
			Tick.init();
			ClientData.finishedInitializing();
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to run onInitializeClientAfterConfig: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
		}
	}
}