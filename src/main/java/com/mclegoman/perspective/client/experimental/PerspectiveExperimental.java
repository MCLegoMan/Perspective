/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.experimental;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import net.minecraft.client.MinecraftClient;

public class PerspectiveExperimental {
    public static void tick(MinecraftClient client) {
        if (PerspectiveKeybindings.TOGGLE_ARMOR.wasPressed()) PerspectiveConfigHelper.setConfig("hide_armor", !(boolean)PerspectiveConfigHelper.getConfig("hide_armor"));
        if (PerspectiveKeybindings.TOGGLE_ARMOR.wasPressed()) PerspectiveConfigHelper.setConfig("hide_nametags", !(boolean)PerspectiveConfigHelper.getConfig("hide_nametags"));
    }
}
