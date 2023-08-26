/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class PerspectiveKeybindings {
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
    public static final KeyBinding TOGGLE_NAMETAGS;
    public static final KeyBinding TOGGLE_SHADERS;
    public static final KeyBinding TOGGLE_ZOOM;
    public static void init() {
        PerspectiveData.LOGGER.info("Initializing keybindings");
    }
    private static KeyBinding getKeybinding(String category, String key, int keyCode) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(PerspectiveTranslation.getKeybindingTranslation(key), InputUtil.Type.KEYSYM, keyCode, PerspectiveTranslation.getKeybindingTranslation(category, true)));
    }
    static {
        CYCLE_SHADERS = getKeybinding(PerspectiveData.ID, "cycle_shaders", GLFW.GLFW_KEY_F7);
        HOLD_PERSPECTIVE_THIRD_PERSON_BACK = getKeybinding(PerspectiveData.ID, "hold_perspective_third_person_back", GLFW.GLFW_KEY_Z);
        HOLD_PERSPECTIVE_THIRD_PERSON_FRONT = getKeybinding(PerspectiveData.ID, "hold_perspective_third_person_front", GLFW.GLFW_KEY_X);
        HOLD_ZOOM = getKeybinding(PerspectiveData.ID, "hold_zoom", GLFW.GLFW_KEY_C);
        OPEN_CONFIG = getKeybinding(PerspectiveData.ID, "open_config", GLFW.GLFW_KEY_END);
        RANDOM_SHADER = getKeybinding(PerspectiveData.ID, "random_shader", GLFW.GLFW_KEY_UNKNOWN);
        SET_PERSPECTIVE_FIRST_PERSON = getKeybinding(PerspectiveData.ID, "set_perspective_first_person", GLFW.GLFW_KEY_KP_1);
        SET_PERSPECTIVE_THIRD_PERSON_BACK = getKeybinding(PerspectiveData.ID, "set_perspective_third_person_back", GLFW.GLFW_KEY_KP_2);
        SET_PERSPECTIVE_THIRD_PERSON_FRONT = getKeybinding(PerspectiveData.ID, "set_perspective_third_person_front", GLFW.GLFW_KEY_KP_3);
        TAKE_PANORAMA_SCREENSHOT = getKeybinding(PerspectiveData.ID, "take_panorama_screenshot", GLFW.GLFW_KEY_UNKNOWN);
        TOGGLE_ZOOM = getKeybinding(PerspectiveData.ID, "toggle_zoom", GLFW.GLFW_KEY_B);
        TOGGLE_SHADERS = getKeybinding(PerspectiveData.ID, "toggle_shaders", GLFW.GLFW_KEY_F8);
        TOGGLE_NAMETAGS = getKeybinding(PerspectiveData.ID + "_experimental", "toggle_nametags", GLFW.GLFW_KEY_F10);
        TOGGLE_ARMOR = getKeybinding(PerspectiveData.ID + "_experimental", "toggle_armor", GLFW.GLFW_KEY_F9);
    }
}