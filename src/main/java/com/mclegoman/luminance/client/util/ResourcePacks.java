/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.util;

import com.mclegoman.luminance.client.translation.Translation;
import com.mclegoman.luminance.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ResourcePacks {
	/**
	 * To add a resource pack to this project, please follow these guidelines:
	 * 1. When registering your resource pack, ensure you include the resource pack's name, and the contributor(s) in the following format:
	 * - Resource Pack Name
	 * - Contributor(s): _________
	 * - Licence: _________
	 * You only need to include the licence in your comment if it is not GNU LGPLv3.
	 */
	public static void init() {
		/*
            Super Secret Settings
            Contributor(s): Mojang Studios, Microsoft Corporation
            Licence: Minecraft EULA
        */
		register(new Identifier("super_secret_settings"), Data.getModContainer("perspective"), Translation.getTranslation(Data.version.getID(), "resource_pack.super_secret_settings"), ResourcePackActivationType.DEFAULT_ENABLED);
	}
	public static void register(Identifier id, ModContainer container, Text text, ResourcePackActivationType activationType) {
		try {
			ResourceManagerHelper.registerBuiltinResourcePack(id, container, text, activationType);
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to register resource pack: {}", error));
		}
	}
}