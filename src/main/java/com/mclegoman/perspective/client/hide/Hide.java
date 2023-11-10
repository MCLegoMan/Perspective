/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.hide;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.util.Keybindings;
import net.minecraft.client.MinecraftClient;

public class Hide {
    public static void tick(MinecraftClient client) {
        if (Keybindings.TOGGLE_ARMOR.wasPressed()) ConfigHelper.setConfig("hide_armor", !(boolean) ConfigHelper.getConfig("hide_armor"));
        if (Keybindings.TOGGLE_BLOCK_OUTLINE.wasPressed()) ConfigHelper.setConfig("hide_block_outline", !(boolean) ConfigHelper.getConfig("hide_block_outline"));
        if (Keybindings.TOGGLE_CROSSHAIR.wasPressed()) ConfigHelper.setConfig("hide_crosshair", !(boolean) ConfigHelper.getConfig("hide_crosshair"));
        if (Keybindings.TOGGLE_NAMETAGS.wasPressed()) ConfigHelper.setConfig("hide_nametags", !(boolean) ConfigHelper.getConfig("hide_nametags"));
    }
}
