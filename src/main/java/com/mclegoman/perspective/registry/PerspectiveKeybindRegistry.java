package com.mclegoman.perspective.registry;

import com.mclegoman.perspective.config.PerspectiveConfig;
import com.mclegoman.perspective.screen.PerspectiveConfigScreen;
import com.mclegoman.perspective.util.PerspectiveUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class PerspectiveKeybindRegistry {
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
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (KEY_CONFIG.wasPressed()) {
                client.setScreen(new PerspectiveConfigScreen(client.currentScreen));
            }
            if (KEY_HOLD_ZOOM.isPressed()) {
                PerspectiveUtils.IS_ZOOMING_HOLD = true;
            }
            if (!KEY_HOLD_ZOOM.isPressed()) {
                PerspectiveUtils.IS_ZOOMING_HOLD = false;
            }
            if (KEY_SET_ZOOM.wasPressed()) {
                PerspectiveUtils.IS_ZOOMING_SET = !PerspectiveUtils.IS_ZOOMING_SET;
            }
            if (KEY_ZOOM_IN.isPressed()) {
                PerspectiveUtils.IS_ZOOMING_IN = true;
                PerspectiveUtils.SHOW_OVERLAY = PerspectiveConfig.OVERLAY_DELAY;
            }
            if (!KEY_ZOOM_IN.isPressed()) {
                PerspectiveUtils.IS_ZOOMING_IN = false;
            }
            if (KEY_ZOOM_OUT.isPressed()) {
                PerspectiveUtils.IS_ZOOMING_OUT = true;
                PerspectiveUtils.SHOW_OVERLAY = PerspectiveConfig.OVERLAY_DELAY;
            }
            if (!KEY_ZOOM_OUT.isPressed()) {
                PerspectiveUtils.IS_ZOOMING_OUT = false;
            }
            if (KEY_CYCLE_PERSPECTIVE.wasPressed()) {
                if (PerspectiveUtils.PERSPECTIVE_COUNT < (PerspectiveUtils.PERSPECTIVES.length - 1)) PerspectiveUtils.PERSPECTIVE_COUNT = PerspectiveUtils.PERSPECTIVE_COUNT + 1;
                else PerspectiveUtils.PERSPECTIVE_COUNT = 0;
                client.options.setPerspective(PerspectiveUtils.PERSPECTIVES[PerspectiveUtils.PERSPECTIVE_COUNT]);
            }
            if (KEY_HOLD_PERSPECTIVE_TPB.isPressed()) {
                PerspectiveUtils.HOLD_PERSPECTIVE_TPB_LOCK = true;
                client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            }
            if (!PerspectiveKeybindRegistry.KEY_HOLD_PERSPECTIVE_TPB.isPressed() && PerspectiveUtils.HOLD_PERSPECTIVE_TPB_LOCK) {
                PerspectiveUtils.HOLD_PERSPECTIVE_TPB_LOCK = false;
                client.options.setPerspective(Perspective.FIRST_PERSON);
            }
            if (KEY_HOLD_PERSPECTIVE_TPF.isPressed()) {
                PerspectiveUtils.HOLD_PERSPECTIVE_TPF_LOCK = true;
                client.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
            }
            if (!PerspectiveKeybindRegistry.KEY_HOLD_PERSPECTIVE_TPF.isPressed() && PerspectiveUtils.HOLD_PERSPECTIVE_TPF_LOCK) {
                PerspectiveUtils.HOLD_PERSPECTIVE_TPF_LOCK = false;
                client.options.setPerspective(Perspective.FIRST_PERSON);
            }
            if (KEY_SET_PERSPECTIVE_FP.wasPressed()) {
                client.options.setPerspective(Perspective.FIRST_PERSON);
                PerspectiveUtils.PERSPECTIVE_COUNT = 0;
            }
            if (KEY_SET_PERSPECTIVE_TPB.wasPressed()) {
                client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
                PerspectiveUtils.PERSPECTIVE_COUNT = 1;
            }
            if (KEY_SET_PERSPECTIVE_TPF.wasPressed()) {
                client.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
                PerspectiveUtils.PERSPECTIVE_COUNT = 2;
            }
            if (KEY_TOGGLE_SUPER_SECRET_SETTINGS.wasPressed()) {
                PerspectiveUtils.toggleSSS(client);
            }
            if (KEY_CYCLE_SUPER_SECRET_SETTINGS.wasPressed()) {
                PerspectiveUtils.cycleSSS(client);
            }
        });
    }
    static {
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