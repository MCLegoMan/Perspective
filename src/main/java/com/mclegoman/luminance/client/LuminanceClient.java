/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client;

import com.mclegoman.luminance.client.data.ClientData;
import com.mclegoman.luminance.client.util.Tick;
import com.mclegoman.luminance.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.fabricmc.api.ClientModInitializer;

public class LuminanceClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		try {
			Data.VERSION.sendToLog(Helper.LogType.INFO, "Initializing " + Data.VERSION.getName());
			Tick.init();
			ClientData.finishedInitializing();
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.INFO, "Failed to initialize " + Data.VERSION.getName() + ": " + error.getLocalizedMessage());
		}
	}
}