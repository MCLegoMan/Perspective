/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.displaynames;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.common.util.Couple;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;

import java.util.UUID;

public class DisplayNames {
	public static void init() {
		try {
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new DisplayNamesDataLoader());
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, "Failed to initialize DisplayNames!");
		}
	}
	public static Text getDisplayName(UUID uuid) {
		if (!DisplayNamesDataLoader.REGISTRY.isEmpty()) {
			for (Couple<UUID, Text> player : DisplayNamesDataLoader.REGISTRY) {
				if (player.getFirst().equals(uuid)) {
					return player.getSecond();
				}
			}
		}
		if (ClientData.CLIENT.world != null && ClientData.CLIENT.world.getPlayerByUuid(uuid) != null) {
			return ClientData.CLIENT.world.getPlayerByUuid(uuid).getDisplayName();
		}
		return Text.of("");
	}
}
