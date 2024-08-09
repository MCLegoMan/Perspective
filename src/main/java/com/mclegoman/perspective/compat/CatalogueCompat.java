/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.compat;

import com.mclegoman.perspective.client.screen.config.ConfigScreen;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public class CatalogueCompat {
	public static Screen createConfigScreen(Screen currentScreen, ModContainer container) {
		return new ConfigScreen(MinecraftClient.getInstance().currentScreen, false, true, 1);
	}
}