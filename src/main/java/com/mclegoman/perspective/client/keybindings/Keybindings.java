/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.keybindings;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.option.KeyBinding;
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
	public static final KeyBinding toggleVerOverlay;
	public static final KeyBinding togglePosOverlay;
	public static final KeyBinding toggleDayOverlay;
	public static final KeyBinding toggleShaders;
	public static final KeyBinding toggleZoom;
	public static final KeyBinding toggleZoomCinematic;
	public static final KeyBinding[] allKeybindings;

	static {
		allKeybindings = new KeyBinding[]{
				cycleCrosshair = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "cycle_crosshair", GLFW.GLFW_KEY_UNKNOWN),
				cycleDebug = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "debug", GLFW.GLFW_KEY_UNKNOWN),
				cycleShaders = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "cycle_shaders", GLFW.GLFW_KEY_F7),
				holdPerspectiveThirdPersonBack = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "hold_perspective_third_person_back", GLFW.GLFW_KEY_Z),
				holdPerspectiveThirdPersonFront = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "hold_perspective_third_person_front", GLFW.GLFW_KEY_V),
				holdZoom = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "hold_zoom", GLFW.GLFW_KEY_R),
				openConfig = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "open_config", GLFW.GLFW_KEY_END),
				randomizeShader = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "random_shader", GLFW.GLFW_KEY_UNKNOWN),
				setPerspectiveFirstPerson = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "set_perspective_first_person", GLFW.GLFW_KEY_UNKNOWN),
				setPerspectiveThirdPersonBack = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "set_perspective_third_person_back", GLFW.GLFW_KEY_UNKNOWN),
				setPerspectiveThirdPersonFront = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "set_perspective_third_person_front", GLFW.GLFW_KEY_UNKNOWN),
				takePanoScreenshot = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "take_panorama_screenshot", GLFW.GLFW_KEY_UNKNOWN),
				toggleArmour = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "toggle_armor", GLFW.GLFW_KEY_UNKNOWN),
				toggleBlockOutline = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "toggle_block_outline", GLFW.GLFW_KEY_UNKNOWN),
				toggleNametags = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "toggle_nametags", GLFW.GLFW_KEY_UNKNOWN),
				togglePlayers = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "toggle_players", GLFW.GLFW_KEY_UNKNOWN),
				toggleVerOverlay = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "toggle_version_overlay", GLFW.GLFW_KEY_UNKNOWN),
				togglePosOverlay = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "toggle_position_overlay", GLFW.GLFW_KEY_UNKNOWN),
				toggleDayOverlay = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "toggle_day_overlay", GLFW.GLFW_KEY_UNKNOWN),
				toggleShaders = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "toggle_shaders", GLFW.GLFW_KEY_F8),
				toggleZoom = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "toggle_zoom", GLFW.GLFW_KEY_UNKNOWN),
				toggleZoomCinematic = KeybindingHelper.getKeybinding(Data.version.getID(), Data.version.getID(), "toggle_zoom_cinematic", GLFW.GLFW_KEY_UNKNOWN)
		};
	}
	public static void init() {
		Data.version.sendToLog(LogType.INFO, Translation.getString("Initializing keybindings!"));
	}
	public static void tick() {
		if (!KeybindingHelper.seenConflictingKeybindingToasts) {
			if (KeybindingHelper.hasKeybindingConflicts()) {
				Data.version.sendToLog(LogType.INFO, Translation.getString("Conflicting Keybinding. Keybinding conflicts have been detected that could affect Perspective. Please take a moment to review and adjust your keybindings as needed."));
				ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.keybinding_conflicts.title")}), Translation.getTranslation(Data.version.getID(), "toasts.keybinding_conflicts.description"), 320, Toast.Type.WARNING));
			}
			KeybindingHelper.seenConflictingKeybindingToasts = true;
		}
	}
}