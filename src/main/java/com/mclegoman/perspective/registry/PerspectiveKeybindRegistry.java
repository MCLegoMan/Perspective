package com.mclegoman.perspective.registry;

import com.mclegoman.perspective.config.PerspectiveConfig;
import com.mclegoman.perspective.dataloader.PerspectiveSSSDataLoader;
import com.mclegoman.perspective.util.PerspectiveUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

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
    public static boolean IS_ZOOMING_HOLD;
    public static boolean IS_ZOOMING_SET;
    public static boolean IS_ZOOMING_IN;
    public static boolean IS_ZOOMING_OUT;
    public static boolean HOLD_PERSPECTIVE_TPF_LOCK;
    public static boolean HOLD_PERSPECTIVE_TPB_LOCK;

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (KEY_HOLD_ZOOM.isPressed()) {
                IS_ZOOMING_HOLD = true;
            }
            if (!KEY_HOLD_ZOOM.isPressed()) {
                IS_ZOOMING_HOLD = false;
            }
            if (KEY_SET_ZOOM.wasPressed()) {
                IS_ZOOMING_SET = !IS_ZOOMING_SET;
            }
            if (KEY_ZOOM_IN.isPressed()) {
                IS_ZOOMING_IN = true;
                PerspectiveUtils.SHOW_OVERLAY = PerspectiveConfig.OVERLAY_DELAY;
            }
            if (!KEY_ZOOM_IN.isPressed()) {
                IS_ZOOMING_IN = false;
            }
            if (KEY_ZOOM_OUT.isPressed()) {
                IS_ZOOMING_OUT = true;
                PerspectiveUtils.SHOW_OVERLAY = PerspectiveConfig.OVERLAY_DELAY;
            }
            if (!KEY_ZOOM_OUT.isPressed()) {
                IS_ZOOMING_OUT = false;
            }
            if (KEY_CYCLE_PERSPECTIVE.wasPressed()) {
                if (PerspectiveUtils.PERSPECTIVE_COUNT < (PerspectiveUtils.PERSPECTIVES.length - 1)) PerspectiveUtils.PERSPECTIVE_COUNT = PerspectiveUtils.PERSPECTIVE_COUNT + 1;
                else PerspectiveUtils.PERSPECTIVE_COUNT = 0;
                client.options.setPerspective(PerspectiveUtils.PERSPECTIVES[PerspectiveUtils.PERSPECTIVE_COUNT]);
            }
            if (KEY_HOLD_PERSPECTIVE_TPB.isPressed()) {
                PerspectiveKeybindRegistry.HOLD_PERSPECTIVE_TPB_LOCK = true;
                client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            }
            if (!PerspectiveKeybindRegistry.KEY_HOLD_PERSPECTIVE_TPB.isPressed() && PerspectiveKeybindRegistry.HOLD_PERSPECTIVE_TPB_LOCK) {
                PerspectiveKeybindRegistry.HOLD_PERSPECTIVE_TPB_LOCK = false;
                client.options.setPerspective(Perspective.FIRST_PERSON);
            }
            if (KEY_HOLD_PERSPECTIVE_TPF.isPressed()) {
                PerspectiveKeybindRegistry.HOLD_PERSPECTIVE_TPF_LOCK = true;
                client.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
            }
            if (!PerspectiveKeybindRegistry.KEY_HOLD_PERSPECTIVE_TPF.isPressed() && PerspectiveKeybindRegistry.HOLD_PERSPECTIVE_TPF_LOCK) {
                PerspectiveKeybindRegistry.HOLD_PERSPECTIVE_TPF_LOCK = false;
                client.options.setPerspective(Perspective.FIRST_PERSON);
            }
            if (KEY_SET_PERSPECTIVE_FP.wasPressed()) {
                client.options.setPerspective(Perspective.FIRST_PERSON);
            }
            if (KEY_SET_PERSPECTIVE_TPB.wasPressed()) {
                client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            }
            if (KEY_SET_PERSPECTIVE_TPF.wasPressed()) {
                client.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
            }
            if (KEY_TOGGLE_SUPER_SECRET_SETTINGS.wasPressed()) {
                client.gameRenderer.togglePostProcessorEnabled();
                PerspectiveUtils.sendSSSMessage(PerspectiveUtils.booleanToString(client.gameRenderer.postProcessorEnabled));
            }
            if (KEY_CYCLE_SUPER_SECRET_SETTINGS.wasPressed()) {
                if (client.getCameraEntity() instanceof PlayerEntity) {
                    if (PerspectiveUtils.SSS_COUNT < (PerspectiveSSSDataLoader.SHADERS.size() - 1)) PerspectiveUtils.SSS_COUNT = PerspectiveUtils.SSS_COUNT + 1;
                    else PerspectiveUtils.SSS_COUNT = 0;
                    if (!client.gameRenderer.postProcessorEnabled) {
                        client.gameRenderer.togglePostProcessorEnabled();
                        PerspectiveUtils.sendSSSMessage(PerspectiveUtils.booleanToString(client.gameRenderer.postProcessorEnabled));
                    }
                    client.gameRenderer.loadPostProcessor(PerspectiveSSSDataLoader.SHADERS.get(PerspectiveUtils.SSS_COUNT));
                    PerspectiveUtils.sendSSSMessage(Text.literal(PerspectiveSSSDataLoader.SHADERS_NAME.get(PerspectiveUtils.SSS_COUNT)));
                }
            }
        });
    }
    static {
        KEY_HOLD_ZOOM = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.zoom.hold", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, "category.perspective.zoom"));
        KEY_SET_ZOOM = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.zoom.set", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.zoom"));
        KEY_ZOOM_IN = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.zoom.in", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_PAGE_UP, "category.perspective.zoom"));
        KEY_ZOOM_OUT = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.zoom.out", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_PAGE_DOWN, "category.perspective.zoom"));
        KEY_HOLD_PERSPECTIVE_TPF = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.tpf.hold", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, "category.perspective.zoom"));
        KEY_HOLD_PERSPECTIVE_TPB = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.tpb.hold", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "category.perspective.zoom"));
        KEY_CYCLE_PERSPECTIVE = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.cycle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.zoom"));
        KEY_SET_PERSPECTIVE_FP = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.fp.set", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.zoom"));
        KEY_SET_PERSPECTIVE_TPF = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.tpf.set", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.zoom"));
        KEY_SET_PERSPECTIVE_TPB = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.tpb.set", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.zoom"));
        KEY_TOGGLE_SUPER_SECRET_SETTINGS = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.sss.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F8, "category.perspective.sss"));
        KEY_CYCLE_SUPER_SECRET_SETTINGS = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.perspective.sss.cycle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F7, "category.perspective.sss"));
    }
}
