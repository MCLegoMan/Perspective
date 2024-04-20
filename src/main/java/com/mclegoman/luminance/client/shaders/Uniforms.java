/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.shaders;

import com.mclegoman.luminance.config.ConfigHelper;
import com.mclegoman.luminance.client.keybindings.Keybindings;

public class Uniforms {
	private static int alpha = 100;
	public static void init() {
		alpha = (int)ConfigHelper.getConfig("alpha_level");
		prevAlpha = alpha;
	}
	public static void tick() {
		if (!updatingAlpha() && updatingAlpha) {
			updatingAlpha = false;
			if (alpha != prevAlpha) ConfigHelper.setConfig("alpha_level", alpha);
		}
	}
	public static float getAlpha() {
		return Math.max(0.0F, Math.min((alpha / 100.0F), 1.0F));
	}
	public static void setAlpha(int value) {
		alpha = Math.max(0, Math.min((value), 100));
	}
	public static void resetAlpha() {
		setAlpha(100);
	}
	public static void adjustAlpha(int amount) {
		setAlpha(alpha + amount);
	}
	public static boolean updatingAlpha = false;
	private static int prevAlpha = 100;
	public static boolean updatingAlpha() {
		boolean value = Keybindings.adjustAlpha.isPressed();
		if (value) {
			if (!updatingAlpha) {
				prevAlpha = alpha;
			}
			updatingAlpha = true;
		}
		return value;
	}
}
