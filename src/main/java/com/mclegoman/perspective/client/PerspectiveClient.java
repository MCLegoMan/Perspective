/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.april_fools_prank.AprilFoolsPrank;
import com.mclegoman.perspective.client.contributor.Contributor;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hide.Hide;
import com.mclegoman.perspective.client.panorama.Panorama;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.shaders.Shaders;
import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.ui.UIBackground;
import com.mclegoman.perspective.client.keybindings.Keybindings;
import com.mclegoman.perspective.client.logo.PerspectiveLogo;
import com.mclegoman.perspective.client.util.ResourcePacks;
import com.mclegoman.perspective.client.util.Tick;
import com.mclegoman.perspective.client.util.Update;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigHelper;
import net.fabricmc.api.ClientModInitializer;

public class PerspectiveClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		try {
			Data.version.sendToLog(LogType.INFO, Translation.getString("Initializing {}", Data.version.getName()));
			ConfigHelper.init();
			ResourcePacks.init();
			AprilFoolsPrank.init();
			UIBackground.init();
			Zoom.init();
			Contributor.init();
			Hide.init();
			Keybindings.init();
			Panorama.init();
			Shader.init();
			TexturedEntity.init();
			Tick.init();
			ClientData.setFinishedInitializing(true);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to run onInitializeClient: {}", error));
		}
	}
	public static void afterInitializeConfig() {
		try {
			Data.version.sendToLog(LogType.INFO, Translation.getString("AfterConfiging {}", Data.version.getName()));
			ResourcePacks.initAfterConfig();
			Update.checkForUpdates(Data.version);
			Shaders.init();
			PerspectiveLogo.init();
			ClientData.setFinishedInitializingAfterConfig(true);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to run afterInitializeConfig: {}", error));
		}
	}
}