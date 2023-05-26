package com.mclegoman.perspective.util;

import com.mclegoman.perspective.config.PerspectiveConfig;
import com.mclegoman.perspective.dataloader.PerspectiveSSSDataLoader;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class PerspectiveUtils {
    public static KeyBinding KEY_ZOOM;
    public static KeyBinding KEY_ZOOM_IN;
    public static KeyBinding KEY_ZOOM_OUT;
    public static KeyBinding KEY_HOLD_PERSPECTIVE_TPF;
    public static boolean KEY_HOLD_PERSPECTIVE_TPF_LOCK;
    public static KeyBinding KEY_HOLD_PERSPECTIVE_TPB;
    public static boolean KEY_HOLD_PERSPECTIVE_TPB_LOCK;
    public static KeyBinding KEY_CYCLE_PERSPECTIVE;
    public static KeyBinding KEY_SET_PERSPECTIVE_FP;
    public static KeyBinding KEY_SET_PERSPECTIVE_TPF;
    public static KeyBinding KEY_SET_PERSPECTIVE_TPB;
    public static KeyBinding KEY_CYCLE_SUPER_SECRET_SETTINGS;
    public static KeyBinding KEY_TOGGLE_SUPER_SECRET_SETTINGS;
    public static Formatting[] COLORS = new Formatting[]{Formatting.DARK_BLUE, Formatting.DARK_GREEN, Formatting.DARK_AQUA, Formatting.DARK_RED, Formatting.DARK_PURPLE, Formatting.GOLD, Formatting.BLUE, Formatting.GREEN, Formatting.AQUA, Formatting.RED, Formatting.LIGHT_PURPLE, Formatting.YELLOW};
    public static Perspective[] PERSPECTIVES = new Perspective[]{Perspective.FIRST_PERSON, Perspective.THIRD_PERSON_BACK, Perspective.THIRD_PERSON_FRONT};
    public static int PERSPECTIVE_COUNT = 0;
    public static int SSS_COUNT = 0;
    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveSSSDataLoader());
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!((PerspectiveConfig.ZOOM_LEVEL + 1) > 100) && KEY_ZOOM_OUT.isPressed()) {
                PerspectiveConfig.ZOOM_LEVEL = PerspectiveConfig.ZOOM_LEVEL + 1;
                PerspectiveConfig.write_to_file();
            }
            if (!((PerspectiveConfig.ZOOM_LEVEL - 1) < 0) && KEY_ZOOM_IN.isPressed()) {
                PerspectiveConfig.ZOOM_LEVEL = PerspectiveConfig.ZOOM_LEVEL - 1;
                PerspectiveConfig.write_to_file();
            }
        });
    }
    public static String booleanToString(boolean BOOLEAN) {
        if (BOOLEAN) return "enabled";
        else return "disabled";
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