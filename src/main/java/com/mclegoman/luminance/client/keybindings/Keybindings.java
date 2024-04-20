/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.keybindings;

import com.mclegoman.luminance.client.translation.Translation;
import com.mclegoman.luminance.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class Keybindings {
	public static final KeyBinding adjustAlpha;
	public static final KeyBinding[] allKeybindings;
	static {
		allKeybindings = new KeyBinding[]{
				adjustAlpha = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "adjust_alpha", GLFW.GLFW_KEY_UNKNOWN)
		};
	}
	public static void init() {
		Data.version.sendToLog(Helper.LogType.INFO, Translation.getString("Initializing keybindings!"));
	}
}