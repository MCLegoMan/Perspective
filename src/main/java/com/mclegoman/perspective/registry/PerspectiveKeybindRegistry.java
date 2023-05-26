package com.mclegoman.perspective.registry;

import com.mclegoman.perspective.util.PerspectiveUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class PerspectiveKeybindRegistry {
    public static KeyBinding KEY_ZOOM;
    public static KeyBinding KEY_ZOOM_IN;
    public static KeyBinding KEY_ZOOM_OUT;
    public static KeyBinding KEY_HOLD_PERSPECTIVE_TPF;
    public static KeyBinding KEY_HOLD_PERSPECTIVE_TPB;
    public static KeyBinding KEY_CYCLE_PERSPECTIVE;
    public static KeyBinding KEY_SET_PERSPECTIVE_FP;
    public static KeyBinding KEY_SET_PERSPECTIVE_TPF;
    public static KeyBinding KEY_SET_PERSPECTIVE_TPB;
    public static KeyBinding KEY_CYCLE_SUPER_SECRET_SETTINGS;
    public static KeyBinding KEY_TOGGLE_SUPER_SECRET_SETTINGS;

    public static boolean IS_ZOOMING;
    public static boolean IS_ZOOMING_IN;
    public static boolean IS_ZOOMING_OUT;
    public static boolean HOLD_PERSPECTIVE_TPF_LOCK;
    public static boolean HOLD_PERSPECTIVE_TPB_LOCK;

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (KEY_ZOOM.isPressed()) {
                IS_ZOOMING = true;
            }
            if (!KEY_ZOOM.isPressed()) {
                IS_ZOOMING = false;
            }
            if (KEY_ZOOM_IN.isPressed()) {
                IS_ZOOMING_IN = true;
                PerspectiveUtils.SHOW_ZOOM_LEVEL = 180;
            }
            if (!KEY_ZOOM_IN.isPressed()) {
                IS_ZOOMING_IN = false;
            }
            if (KEY_ZOOM_OUT.isPressed()) {
                IS_ZOOMING_OUT = true;
                PerspectiveUtils.SHOW_ZOOM_LEVEL = 180;
            }
            if (!KEY_ZOOM_OUT.isPressed()) {
                IS_ZOOMING_OUT = false;
            }
        });
    }
    static {
        KEY_ZOOM = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.zoom.hold", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, "category.perspective.zoom"));
        KEY_ZOOM_IN = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.zoom.in", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_PAGE_UP, "category.perspective.zoom"));
        KEY_ZOOM_OUT = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.zoom.out", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_PAGE_DOWN, "category.perspective.zoom"));
        KEY_HOLD_PERSPECTIVE_TPF = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.tpf.hold", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, "category.perspective.zoom"));
        KEY_HOLD_PERSPECTIVE_TPB = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.tpb.hold", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "category.perspective.zoom"));
        KEY_CYCLE_PERSPECTIVE = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.cycle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.zoom"));
        KEY_SET_PERSPECTIVE_FP = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.fp.set", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.zoom"));
        KEY_SET_PERSPECTIVE_TPF = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.tpf.set", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.zoom"));
        KEY_SET_PERSPECTIVE_TPB = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.tpb.set", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.zoom"));
        KEY_CYCLE_SUPER_SECRET_SETTINGS = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.sss.cycle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F7, "category.perspective.sss"));
        KEY_TOGGLE_SUPER_SECRET_SETTINGS = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.sss.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F8, "category.perspective.sss"));
    }
}
