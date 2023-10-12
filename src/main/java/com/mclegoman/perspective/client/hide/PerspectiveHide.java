/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.hide;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import net.minecraft.client.MinecraftClient;

public class PerspectiveHide {
    public static void tick(MinecraftClient client) {
        if (PerspectiveKeybindings.TOGGLE_ARMOR.wasPressed()) PerspectiveConfigHelper.setConfig("hide_armor", !(boolean)PerspectiveConfigHelper.getConfig("hide_armor"));
        if (PerspectiveKeybindings.TOGGLE_BLOCK_OUTLINE.wasPressed()) PerspectiveConfigHelper.setConfig("hide_block_outline", !(boolean)PerspectiveConfigHelper.getConfig("hide_block_outline"));
        if (PerspectiveKeybindings.TOGGLE_CROSSHAIR.wasPressed()) PerspectiveConfigHelper.setConfig("hide_crosshair", !(boolean)PerspectiveConfigHelper.getConfig("hide_crosshair"));
        if (PerspectiveKeybindings.TOGGLE_NAMETAGS.wasPressed()) PerspectiveConfigHelper.setConfig("hide_nametags", !(boolean)PerspectiveConfigHelper.getConfig("hide_nametags"));
    }
}
