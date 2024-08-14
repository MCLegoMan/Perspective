/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.util.Identifier;

public class ResourcePacks extends com.mclegoman.luminance.client.util.ResourcePacks {
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
            Perspective: Default
            Contributor(s): MCLegoMan
        */
		register(Identifier.of("perspective_default"), Data.version.getModContainer(), Translation.getTranslation(Data.version.getID(), "resource_pack.perspective_default"), ResourcePackActivationType.DEFAULT_ENABLED);
		/*
            Perspective: Developer Config
            Contributor(s): MCLegoMan
        */
		register(Identifier.of("dev_config"), Data.version.getModContainer(), Translation.getTranslation(Data.version.getID(), "resource_pack.dev_config"), ResourcePackActivationType.NORMAL);
	}
}