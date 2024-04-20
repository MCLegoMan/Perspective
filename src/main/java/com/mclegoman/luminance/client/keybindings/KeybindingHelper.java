/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.keybindings;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeybindingHelper {
	public static boolean hasKeybindingConflicts(KeyBinding... keybindings) {
		for (KeyBinding currentKey1 : keybindings) {
			for (KeyBinding currentKey2 : ClientData.minecraft.options.allKeys) {
				if (!currentKey1.isUnbound() && !currentKey2.isUnbound()) {
					if (currentKey1 != currentKey2) {
						if (KeyBindingHelper.getBoundKeyOf(currentKey1) == KeyBindingHelper.getBoundKeyOf(currentKey2))
							return true;
					}
				}
			}
		}
		return false;
	}
	public static KeyBinding getKeybinding(String namespace, String category, String key, int keyCode) {
		return KeyBindingHelper.registerKeyBinding(new KeyBinding(Translation.getKeybindingTranslation(namespace, key), InputUtil.Type.KEYSYM, keyCode, Translation.getKeybindingTranslation(namespace, category, true)));
	}
}