/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.hud;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.hide.Hide;
import com.mclegoman.perspective.client.hide.HideHudTypes;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.keybindings.Keybindings;

public class HUDHelper {
	public static void tick() {
		if (Keybindings.cycleDebug != null) {
			if (Keybindings.cycleDebug.wasPressed()) {
				DebugOverlay.shaderColor = Shader.getRandomColor();
				DebugOverlay.debugType = ClientData.minecraft.options.sneakKey.isPressed() ? DebugOverlay.debugType.prev() : DebugOverlay.debugType.next();
			}
		}
		if (Keybindings.toggleVerOverlay.wasPressed()) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "version_overlay", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "version_overlay"));
			ConfigHelper.saveConfig();
		}
		if (Keybindings.togglePosOverlay.wasPressed()) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "position_overlay", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "position_overlay"));
			ConfigHelper.saveConfig();
		}
		if (Keybindings.toggleDayOverlay.wasPressed()) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "day_overlay", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "day_overlay"));
			ConfigHelper.saveConfig();
		}
		if (Keybindings.toggleBiomeOverlay.wasPressed()) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "biome_overlay", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "biome_overlay"));
			ConfigHelper.saveConfig();
		}
	}
	public static boolean shouldHideHUD() {
		return Hide.shouldHideHud(HideHudTypes.zoom) || Hide.shouldHideHud(HideHudTypes.holdPerspectiveBack) || Hide.shouldHideHud(HideHudTypes.holdPerspectiveFront);
	}
	public static int addY(int y) {
		return y + 12;
	}
}