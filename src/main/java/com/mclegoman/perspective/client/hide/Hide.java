/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.hide;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.util.Keybindings;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;

public class Hide {
    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new HideArmorDataLoader());
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new HideNameTagsDataLoader());
    }
    public static void tick(MinecraftClient client) {
        if (Keybindings.TOGGLE_ARMOR.wasPressed()) ConfigHelper.setConfig("hide_armor", !(boolean) ConfigHelper.getConfig("hide_armor"));
        if (Keybindings.TOGGLE_BLOCK_OUTLINE.wasPressed()) ConfigHelper.setConfig("hide_block_outline", !(boolean) ConfigHelper.getConfig("hide_block_outline"));
        if (Keybindings.TOGGLE_CROSSHAIR.wasPressed()) ConfigHelper.setConfig("hide_crosshair", !(boolean) ConfigHelper.getConfig("hide_crosshair"));
        if (Keybindings.TOGGLE_NAMETAGS.wasPressed()) ConfigHelper.setConfig("hide_nametags", !(boolean) ConfigHelper.getConfig("hide_nametags"));
    }
}
