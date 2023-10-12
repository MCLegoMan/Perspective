/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.toasts.PerspectiveToast;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
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
    public static final KeyBinding TOGGLE_BLOCK_OUTLINE;
    public static final KeyBinding TOGGLE_CROSSHAIR;
    public static final KeyBinding TOGGLE_NAMETAGS;
    public static final KeyBinding TOGGLE_SHADERS;
    public static final KeyBinding TOGGLE_ZOOM;
    private static final KeyBinding[] ALL_KEYBINDINGS;
    public static boolean SEEN_CONFLICTING_KEYBINDING_TOASTS;
    public static void init() {
        PerspectiveData.PERSPECTIVE_VERSION.getLogger().info("{} Initializing keybindings", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix());
    }
    public static void tick(MinecraftClient client) {
        if (!SEEN_CONFLICTING_KEYBINDING_TOASTS) {
            if (hasKeybindingConflicts(client)) {
                PerspectiveData.PERSPECTIVE_VERSION.getLogger().info("{} Conflicting Keybinding. Keybinding conflicts have been detected that could affect Perspective. Please take a moment to review and adjust your keybindings as needed.", PerspectiveData.PERSPECTIVE_VERSION.getName());
                client.getToastManager().add(new PerspectiveToast(PerspectiveTranslation.getTranslation("toasts.title", new Object[]{PerspectiveTranslation.getTranslation("name"), PerspectiveTranslation.getTranslation("toasts.keybinding_conflicts.title")}), PerspectiveTranslation.getTranslation("toasts.keybinding_conflicts.description"), 320, PerspectiveToast.Type.WARNING));
            }
            SEEN_CONFLICTING_KEYBINDING_TOASTS = true;
        }
    }
    public static boolean hasKeybindingConflicts(MinecraftClient client) {
        for (KeyBinding currentKey1 : ALL_KEYBINDINGS) {
            for (KeyBinding currentKey2: client.options.allKeys) {
                if (!currentKey1.isUnbound() && !currentKey2.isUnbound()) {
                    if (currentKey1 != currentKey2) {
                        if (KeyBindingHelper.getBoundKeyOf(currentKey1) == KeyBindingHelper.getBoundKeyOf(currentKey2)) return true;
                    }
                }
            }
        }
        return false;
    }
    private static KeyBinding getKeybinding(String category, String key, int keyCode) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(PerspectiveTranslation.getKeybindingTranslation(key), InputUtil.Type.KEYSYM, keyCode, PerspectiveTranslation.getKeybindingTranslation(category, true)));
    }
    static {
        ALL_KEYBINDINGS = new KeyBinding[]{
                CYCLE_SHADERS = getKeybinding(PerspectiveData.PERSPECTIVE_VERSION.getID(), "cycle_shaders", GLFW.GLFW_KEY_F7),
                HOLD_PERSPECTIVE_THIRD_PERSON_BACK = getKeybinding(PerspectiveData.PERSPECTIVE_VERSION.getID(), "hold_perspective_third_person_back", GLFW.GLFW_KEY_Z),
                HOLD_PERSPECTIVE_THIRD_PERSON_FRONT = getKeybinding(PerspectiveData.PERSPECTIVE_VERSION.getID(), "hold_perspective_third_person_front", GLFW.GLFW_KEY_X),
                HOLD_ZOOM = getKeybinding(PerspectiveData.PERSPECTIVE_VERSION.getID(), "hold_zoom", GLFW.GLFW_KEY_C),
                OPEN_CONFIG = getKeybinding(PerspectiveData.PERSPECTIVE_VERSION.getID(), "open_config", GLFW.GLFW_KEY_END),
                RANDOM_SHADER = getKeybinding(PerspectiveData.PERSPECTIVE_VERSION.getID(), "random_shader", GLFW.GLFW_KEY_UNKNOWN),
                SET_PERSPECTIVE_FIRST_PERSON = getKeybinding(PerspectiveData.PERSPECTIVE_VERSION.getID(), "set_perspective_first_person", GLFW.GLFW_KEY_KP_1),
                SET_PERSPECTIVE_THIRD_PERSON_BACK = getKeybinding(PerspectiveData.PERSPECTIVE_VERSION.getID(), "set_perspective_third_person_back", GLFW.GLFW_KEY_KP_2),
                SET_PERSPECTIVE_THIRD_PERSON_FRONT = getKeybinding(PerspectiveData.PERSPECTIVE_VERSION.getID(), "set_perspective_third_person_front", GLFW.GLFW_KEY_KP_3),
                TAKE_PANORAMA_SCREENSHOT = getKeybinding(PerspectiveData.PERSPECTIVE_VERSION.getID(), "take_panorama_screenshot", GLFW.GLFW_KEY_UNKNOWN),
                TOGGLE_ARMOR = getKeybinding(PerspectiveData.PERSPECTIVE_VERSION.getID(), "toggle_armor", GLFW.GLFW_KEY_F9),
                TOGGLE_BLOCK_OUTLINE = getKeybinding(PerspectiveData.PERSPECTIVE_VERSION.getID(), "toggle_block_outline", GLFW.GLFW_KEY_UNKNOWN),
                TOGGLE_CROSSHAIR = getKeybinding(PerspectiveData.PERSPECTIVE_VERSION.getID(), "toggle_crosshair", GLFW.GLFW_KEY_UNKNOWN),
                TOGGLE_NAMETAGS = getKeybinding(PerspectiveData.PERSPECTIVE_VERSION.getID(), "toggle_nametags", GLFW.GLFW_KEY_F10),
                TOGGLE_SHADERS = getKeybinding(PerspectiveData.PERSPECTIVE_VERSION.getID(), "toggle_shaders", GLFW.GLFW_KEY_F8),
                TOGGLE_ZOOM = getKeybinding(PerspectiveData.PERSPECTIVE_VERSION.getID(), "toggle_zoom", GLFW.GLFW_KEY_UNKNOWN)
        };
    }
}