/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client;

import com.mclegoman.luminance.client.keybindings.Keybindings;
import com.mclegoman.luminance.client.shaders.Uniforms;
import com.mclegoman.luminance.client.translation.Translation;
import com.mclegoman.luminance.client.util.Tick;
import com.mclegoman.luminance.common.data.Data;
import com.mclegoman.luminance.config.ConfigHelper;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.fabricmc.api.ClientModInitializer;

public class LuminanceClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		try {
			Data.version.sendToLog(Helper.LogType.INFO, Translation.getString("Initializing {}", Data.version.getName()));
			ConfigHelper.init();
			Keybindings.init();
			Tick.init();
			Uniforms.init();
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to run onInitializeClient: {}", error));
		}
	}
}