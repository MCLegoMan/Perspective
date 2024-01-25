/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Keybindings {
	public static final KeyBinding CYCLE_CROSSHAIR;
	public static final KeyBinding CYCLE_DEBUG;
	public static final KeyBinding CYCLE_SHADERS;
	public static final KeyBinding HOLD_PERSPECTIVE_THIRD_PERSON_BACK;
	public static final KeyBinding HOLD_PERSPECTIVE_THIRD_PERSON_FRONT;
	public static final KeyBinding HOLD_ZOOM;
	public static final KeyBinding OPEN_CONFIG;
	public static final KeyBinding RANDOM_SHADER;
	public static final KeyBinding SET_PERSPECTIVE_FIRST_PERSON;
	public static final KeyBinding SET_PERSPECTIVE_THIRD_PERSON_BACK;
	public static final KeyBinding SET_PERSPECTIVE_THIRD_PERSON_FRONT;
	public static final KeyBinding TAKE_PANORAMA_SCREENSHOT;
	public static final KeyBinding TOGGLE_ARMOR;
	public static final KeyBinding TOGGLE_BLOCK_OUTLINE;
	public static final KeyBinding TOGGLE_NAMETAGS;
	public static final KeyBinding TOGGLE_PLAYERS;
	public static final KeyBinding TOGGLE_POSITION_OVERLAY;
	public static final KeyBinding TOGGLE_SHADERS;
	public static final KeyBinding TOGGLE_ZOOM;
	public static final KeyBinding[] ALL_KEYBINDINGS;
	public static boolean SEEN_CONFLICTING_KEYBINDING_TOASTS;

	static {
		ALL_KEYBINDINGS = new KeyBinding[]{
				CYCLE_CROSSHAIR = getKeybinding(Data.VERSION.getID(), "cycle_crosshair", GLFW.GLFW_KEY_UNKNOWN),
				CYCLE_DEBUG = getKeybinding(Data.VERSION.getID(), "debug", GLFW.GLFW_KEY_UNKNOWN),
				CYCLE_SHADERS = getKeybinding(Data.VERSION.getID(), "cycle_shaders", GLFW.GLFW_KEY_F7),
				HOLD_PERSPECTIVE_THIRD_PERSON_BACK = getKeybinding(Data.VERSION.getID(), "hold_perspective_third_person_back", GLFW.GLFW_KEY_Z),
				HOLD_PERSPECTIVE_THIRD_PERSON_FRONT = getKeybinding(Data.VERSION.getID(), "hold_perspective_third_person_front", GLFW.GLFW_KEY_X),
				HOLD_ZOOM = getKeybinding(Data.VERSION.getID(), "hold_zoom", GLFW.GLFW_KEY_C),
				OPEN_CONFIG = getKeybinding(Data.VERSION.getID(), "open_config", GLFW.GLFW_KEY_END),
				RANDOM_SHADER = getKeybinding(Data.VERSION.getID(), "random_shader", GLFW.GLFW_KEY_UNKNOWN),
				SET_PERSPECTIVE_FIRST_PERSON = getKeybinding(Data.VERSION.getID(), "set_perspective_first_person", GLFW.GLFW_KEY_UNKNOWN),
				SET_PERSPECTIVE_THIRD_PERSON_BACK = getKeybinding(Data.VERSION.getID(), "set_perspective_third_person_back", GLFW.GLFW_KEY_UNKNOWN),
				SET_PERSPECTIVE_THIRD_PERSON_FRONT = getKeybinding(Data.VERSION.getID(), "set_perspective_third_person_front", GLFW.GLFW_KEY_UNKNOWN),
				TAKE_PANORAMA_SCREENSHOT = getKeybinding(Data.VERSION.getID(), "take_panorama_screenshot", GLFW.GLFW_KEY_UNKNOWN),
				TOGGLE_ARMOR = getKeybinding(Data.VERSION.getID(), "toggle_armor", GLFW.GLFW_KEY_UNKNOWN),
				TOGGLE_BLOCK_OUTLINE = getKeybinding(Data.VERSION.getID(), "toggle_block_outline", GLFW.GLFW_KEY_UNKNOWN),
				TOGGLE_NAMETAGS = getKeybinding(Data.VERSION.getID(), "toggle_nametags", GLFW.GLFW_KEY_UNKNOWN),
				TOGGLE_PLAYERS = getKeybinding(Data.VERSION.getID(), "toggle_players", GLFW.GLFW_KEY_UNKNOWN),
				TOGGLE_POSITION_OVERLAY = getKeybinding(Data.VERSION.getID(), "toggle_position_overlay", GLFW.GLFW_KEY_UNKNOWN),
				TOGGLE_SHADERS = getKeybinding(Data.VERSION.getID(), "toggle_shaders", GLFW.GLFW_KEY_F8),
				TOGGLE_ZOOM = getKeybinding(Data.VERSION.getID(), "toggle_zoom", GLFW.GLFW_KEY_UNKNOWN)
		};
	}

	public static void init() {
		Data.VERSION.getLogger().info("{} Initializing keybindings", Data.VERSION.getLoggerPrefix());
	}

	public static void tick() {
		if (!SEEN_CONFLICTING_KEYBINDING_TOASTS) {
			if (hasKeybindingConflicts()) {
				Data.VERSION.getLogger().info("{} Conflicting Keybinding. Keybinding conflicts have been detected that could affect Perspective. Please take a moment to review and adjust your keybindings as needed.", Data.VERSION.getName());
				ClientData.CLIENT.getToastManager().add(new Toast(Translation.getTranslation(Data.VERSION.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.VERSION.getID(), "name"), Translation.getTranslation(Data.VERSION.getID(), "toasts.keybinding_conflicts.title")}), Translation.getTranslation(Data.VERSION.getID(), "toasts.keybinding_conflicts.description"), 320, Toast.Type.WARNING));
			}
			SEEN_CONFLICTING_KEYBINDING_TOASTS = true;
		}
	}

	public static boolean hasKeybindingConflicts() {
		for (KeyBinding currentKey1 : ALL_KEYBINDINGS) {
			for (KeyBinding currentKey2 : ClientData.CLIENT.options.allKeys) {
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

	private static KeyBinding getKeybinding(String category, String key, int keyCode) {
		return KeyBindingHelper.registerKeyBinding(new KeyBinding(Translation.getKeybindingTranslation(key), InputUtil.Type.KEYSYM, keyCode, Translation.getKeybindingTranslation(category, true)));
	}
}