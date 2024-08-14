/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.keybindings;

import net.minecraft.client.option.KeyBinding;

public class KeybindingHelper extends com.mclegoman.luminance.client.keybindings.KeybindingHelper {
	public static boolean seenConflictingKeybindingToasts;
	public static KeyBinding getKeybinding(String namespace, String category, String key, int keyCode, boolean shouldRegister) {
		return shouldRegister ? getKeybinding(namespace, category, key, keyCode) : null;
	}
}
