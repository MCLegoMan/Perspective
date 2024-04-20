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
		if (Keybindings.cycleDebug.wasPressed()) {
			DebugOverlay.shaderColor = Shader.getRandomColor();
			DebugOverlay.debugType = ClientData.minecraft.options.sneakKey.isPressed() ? DebugOverlay.debugType.prev() : DebugOverlay.debugType.next();
		}
		if (Keybindings.togglePosOverlay.wasPressed()) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "position_overlay", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "position_overlay"));
			ConfigHelper.saveConfig();
		}
	}
	public static boolean shouldHideHUD() {
		return Hide.shouldHideHud(HideHudTypes.zoom) || Hide.shouldHideHud(HideHudTypes.holdPerspective);
	}
	public static int addY(int y) {
		return y + 1 + 9;
	}
}