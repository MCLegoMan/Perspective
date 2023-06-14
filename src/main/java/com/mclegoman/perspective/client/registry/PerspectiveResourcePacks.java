/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.registry;

import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PerspectiveResourcePacks {
    /**
        When including resource packs with Perspective, register it here along with a comment with the following details:
        Resource Pack: ___________________
        Author: _________
        Github: https://github.com/_________
        Licence: _________
     **/
    public static void init() {
        /*
            Resource Pack: Perspective Default
            Author: MCLegoMan
            Github: https://github.com/MCLegoMan
            License: CC-BY 4.0
        */
        ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("perspective"), PerspectiveData.MOD_CONTAINER, Text.translatable("resourcepack.perspective.default.name"), ResourcePackActivationType.DEFAULT_ENABLED);
    }
}