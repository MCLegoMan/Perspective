/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0

*/

package com.mclegoman.perspective.client.registry;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class PerspectiveKeybindings {
    public static KeyBinding KEY_HOLD_ZOOM;
    public static KeyBinding KEY_SET_ZOOM;
    public static KeyBinding KEY_ZOOM_IN;
    public static KeyBinding KEY_ZOOM_OUT;
    public static KeyBinding KEY_HOLD_PERSPECTIVE_TPF;
    public static KeyBinding KEY_HOLD_PERSPECTIVE_TPB;
    public static KeyBinding KEY_CYCLE_PERSPECTIVE;
    public static KeyBinding KEY_SET_PERSPECTIVE_FP;
    public static KeyBinding KEY_SET_PERSPECTIVE_TPF;
    public static KeyBinding KEY_SET_PERSPECTIVE_TPB;
    public static KeyBinding KEY_TOGGLE_SUPER_SECRET_SETTINGS;
    public static KeyBinding KEY_CYCLE_SUPER_SECRET_SETTINGS;
    public static KeyBinding KEY_CONFIG;
    public static void init() {
        KEY_CONFIG = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.config", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.perspective"));
        KEY_HOLD_ZOOM = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.zoom.hold", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, "category.perspective.perspective"));
        KEY_SET_ZOOM = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.zoom.set", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.perspective"));
        KEY_ZOOM_IN = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.zoom.in", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.perspective"));
        KEY_ZOOM_OUT = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.zoom.out", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.perspective"));
        KEY_HOLD_PERSPECTIVE_TPF = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.tpf.hold", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, "category.perspective.perspective"));
        KEY_HOLD_PERSPECTIVE_TPB = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.tpb.hold", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "category.perspective.perspective"));
        KEY_CYCLE_PERSPECTIVE = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.cycle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.perspective"));
        KEY_SET_PERSPECTIVE_FP = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.fp.set", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.perspective"));
        KEY_SET_PERSPECTIVE_TPF = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.tpf.set", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.perspective"));
        KEY_SET_PERSPECTIVE_TPB = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.tpb.set", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.perspective"));
        KEY_TOGGLE_SUPER_SECRET_SETTINGS = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.sss.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F8, "category.perspective.sss"));
        KEY_CYCLE_SUPER_SECRET_SETTINGS = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.sss.cycle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F7, "category.perspective.sss"));
    }
}