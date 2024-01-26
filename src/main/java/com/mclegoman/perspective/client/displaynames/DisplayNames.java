/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.displaynames;

import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class DisplayNames {
	public static void init() {
		try {
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new DisplayNamesDataLoader());
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, "Failed to initialize DisplayNames!");
		}
	}
}
