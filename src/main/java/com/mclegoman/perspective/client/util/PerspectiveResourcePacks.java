/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PerspectiveResourcePacks {
    /**
     To add a resource pack to this project, please follow these guidelines:
     1. When registering your resource pack, ensure you include the resource pack's name, and the contributor(s) in the following format:
     - Resource Pack Name
     - Contributor(s): _________
     2. Your resource pack must use the GNU LGPLv3 license.
    **/
    public static void init() {
        /*
            Perspective: Default
            Contributor(s): MCLegoMan
        */
        ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("perspective"), PerspectiveData.PERSPECTIVE_VERSION.getModContainer(), PerspectiveTranslation.getTranslation("resource_pack.perspective_default"), ResourcePackActivationType.DEFAULT_ENABLED);
    }
}