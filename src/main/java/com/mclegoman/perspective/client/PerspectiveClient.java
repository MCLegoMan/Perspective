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
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.ui.UIBackground;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.client.util.ResourcePacks;
import com.mclegoman.perspective.client.util.Tick;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.fabricmc.api.ClientModInitializer;

public class PerspectiveClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		try {
			Data.VERSION.sendToLog(Helper.LogType.INFO, Translation.getString("{} Initializing {}", Data.VERSION.getLoggerPrefix(), Data.VERSION.getID()));
			AprilFoolsPrank.init();
			UIBackground.init();
			Zoom.init();
			ConfigHelper.init();
			Contributor.init();
			Hide.init();
			Keybindings.init();
			Panorama.init();
			ResourcePacks.init();
			Shader.init();
			TexturedEntity.init();
			Tick.init();
			ClientData.finishedInitializing();
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.INFO, Translation.getString("{} Failed to run onInitializeClient: {}", Data.VERSION.getLoggerPrefix(), error));
		}
	}
	public static void onInitializeClientAfterConfig() {
		try {
			ResourcePacks.initAfterConfig();
			ClientData.finishedInitializingAfterConfig();
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.INFO, Translation.getString("{} Failed to run onInitializeClientAfterConfig: {}", Data.VERSION.getLoggerPrefix(), error));
		}
	}
}