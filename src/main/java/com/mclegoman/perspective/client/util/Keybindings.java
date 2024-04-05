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
	public static final KeyBinding cycleCrosshair;
	public static final KeyBinding cycleDebug;
	public static final KeyBinding cycleShaders;
	public static final KeyBinding holdPerspectiveThirdPersonBack;
	public static final KeyBinding holdPerspectiveThirdPersonFront;
	public static final KeyBinding holdZoom;
	public static final KeyBinding openConfig;
	public static final KeyBinding randomizeShader;
	public static final KeyBinding setPerspectiveFirstPerson;
	public static final KeyBinding setPerspectiveThirdPersonBack;
	public static final KeyBinding setPerspectiveThirdPersonFront;
	public static final KeyBinding takePanoScreenshot;
	public static final KeyBinding toggleArmour;
	public static final KeyBinding toggleBlockOutline;
	public static final KeyBinding toggleNametags;
	public static final KeyBinding togglePlayers;
	public static final KeyBinding togglePosOverlay;
	public static final KeyBinding toggleShaders;
	public static final KeyBinding toggleZoom;
	public static final KeyBinding[] allKeybindings;
	public static boolean seenConflictingKeybindingToasts;

	static {
		allKeybindings = new KeyBinding[]{
				cycleCrosshair = getKeybinding(Data.VERSION.getID(), "cycle_crosshair", GLFW.GLFW_KEY_UNKNOWN),
				cycleDebug = getKeybinding(Data.VERSION.getID(), "debug", GLFW.GLFW_KEY_UNKNOWN),
				cycleShaders = getKeybinding(Data.VERSION.getID(), "cycle_shaders", GLFW.GLFW_KEY_F7),
				holdPerspectiveThirdPersonBack = getKeybinding(Data.VERSION.getID(), "hold_perspective_third_person_back", GLFW.GLFW_KEY_Z),
				holdPerspectiveThirdPersonFront = getKeybinding(Data.VERSION.getID(), "hold_perspective_third_person_front", GLFW.GLFW_KEY_X),
				holdZoom = getKeybinding(Data.VERSION.getID(), "hold_zoom", GLFW.GLFW_KEY_C),
				openConfig = getKeybinding(Data.VERSION.getID(), "open_config", GLFW.GLFW_KEY_END),
				randomizeShader = getKeybinding(Data.VERSION.getID(), "random_shader", GLFW.GLFW_KEY_UNKNOWN),
				setPerspectiveFirstPerson = getKeybinding(Data.VERSION.getID(), "set_perspective_first_person", GLFW.GLFW_KEY_UNKNOWN),
				setPerspectiveThirdPersonBack = getKeybinding(Data.VERSION.getID(), "set_perspective_third_person_back", GLFW.GLFW_KEY_UNKNOWN),
				setPerspectiveThirdPersonFront = getKeybinding(Data.VERSION.getID(), "set_perspective_third_person_front", GLFW.GLFW_KEY_UNKNOWN),
				takePanoScreenshot = getKeybinding(Data.VERSION.getID(), "take_panorama_screenshot", GLFW.GLFW_KEY_UNKNOWN),
				toggleArmour = getKeybinding(Data.VERSION.getID(), "toggle_armor", GLFW.GLFW_KEY_UNKNOWN),
				toggleBlockOutline = getKeybinding(Data.VERSION.getID(), "toggle_block_outline", GLFW.GLFW_KEY_UNKNOWN),
				toggleNametags = getKeybinding(Data.VERSION.getID(), "toggle_nametags", GLFW.GLFW_KEY_UNKNOWN),
				togglePlayers = getKeybinding(Data.VERSION.getID(), "toggle_players", GLFW.GLFW_KEY_UNKNOWN),
				togglePosOverlay = getKeybinding(Data.VERSION.getID(), "toggle_position_overlay", GLFW.GLFW_KEY_UNKNOWN),
				toggleShaders = getKeybinding(Data.VERSION.getID(), "toggle_shaders", GLFW.GLFW_KEY_F8),
				toggleZoom = getKeybinding(Data.VERSION.getID(), "toggle_zoom", GLFW.GLFW_KEY_UNKNOWN)
		};
	}

	public static void init() {
		Data.VERSION.getLogger().info("{} Initializing keybindings", Data.VERSION.getLoggerPrefix());
	}

	public static void tick() {
		if (!seenConflictingKeybindingToasts) {
			if (hasKeybindingConflicts()) {
				Data.VERSION.getLogger().info("{} Conflicting Keybinding. Keybinding conflicts have been detected that could affect Perspective. Please take a moment to review and adjust your keybindings as needed.", Data.VERSION.getName());
				ClientData.CLIENT.getToastManager().add(new Toast(Translation.getTranslation(Data.VERSION.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.VERSION.getID(), "name"), Translation.getTranslation(Data.VERSION.getID(), "toasts.keybinding_conflicts.title")}), Translation.getTranslation(Data.VERSION.getID(), "toasts.keybinding_conflicts.description"), 320, Toast.Type.WARNING));
			}
			seenConflictingKeybindingToasts = true;
		}
	}

	public static boolean hasKeybindingConflicts() {
		for (KeyBinding currentKey1 : allKeybindings) {
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