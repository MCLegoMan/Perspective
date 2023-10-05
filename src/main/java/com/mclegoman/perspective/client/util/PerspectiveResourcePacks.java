/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
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
        When including resource packs with Perspective, register it here along with a comment with the following details:
        Resource Pack: ___________________
        Author: _________
        Github: github.com/_________
        Licence: _________
     **/
    public static void init() {
        /*
            Resource Pack: Perspective Default
            Author: MCLegoMan
            Github: https://github.com/MCLegoMan
            License: CC-BY 4.0
        */
        ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("perspective"), PerspectiveData.PERSPECTIVE_VERSION.getModContainer(), PerspectiveTranslation.getTranslation("resource_pack.perspective_default"), ResourcePackActivationType.DEFAULT_ENABLED);
    }
}