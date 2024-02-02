/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.hud;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.hide.Hide;
import com.mclegoman.perspective.client.hide.HideHudTypes;
import com.mclegoman.perspective.client.perspective.Perspective;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.client.zoom.Zoom;

public class HUDHelper {
	public static void tick() {
		if (Keybindings.CYCLE_DEBUG.wasPressed()) {
			DebugOverlay.shaderColor = Shader.getRandomColor();
			DebugOverlay.debugType = DebugOverlay.debugType.next();
		}
		if (Keybindings.TOGGLE_POSITION_OVERLAY.wasPressed()) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "position_overlay", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "position_overlay"));
			ConfigHelper.saveConfig(true);
		}
	}
	public static boolean shouldHideHUD() {
		return Hide.shouldHideHud(HideHudTypes.ZOOM) || Hide.shouldHideHud(HideHudTypes.HOLD_PERSPECTIVE);
	}
	public static int addY(int y) {
		return y + 1 + 9;
	}
}