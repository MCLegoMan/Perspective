package com.mclegoman.perspective.util;

import com.mclegoman.perspective.dataloader.PerspectiveSSSDataLoader;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.resource.ResourceType;
import org.lwjgl.glfw.GLFW;

public class PerspectiveUtils {
    public static KeyBinding KEY_ZOOM;
    public static KeyBinding KEY_HOLD_TPF;
    public static boolean KEY_HOLD_TPF_LOCK;
    public static KeyBinding KEY_HOLD_TPB;
    public static boolean KEY_HOLD_TPB_LOCK;
    public static KeyBinding KEY_SET_FP;
    public static KeyBinding KEY_SET_TPF;
    public static KeyBinding KEY_SET_TPB;
    public static KeyBinding KEY_CYCLE_SUPER_SECRET_SETTINGS;
    public static KeyBinding KEY_TOGGLE_SUPER_SECRET_SETTINGS;
    public static int getZoomLevel(int fov, int zoom_level) {
        int PERCENT = (100-((zoom_level*fov)/100));
        if (PERCENT <= 0) return 1;
        else if (PERCENT >= 100) return 99;
        else return PERCENT;
    }
    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveSSSDataLoader());
        KeyBindingHelper.registerKeyBinding(PerspectiveUtils.KEY_ZOOM);
        KeyBindingHelper.registerKeyBinding(PerspectiveUtils.KEY_HOLD_TPF);
        KeyBindingHelper.registerKeyBinding(PerspectiveUtils.KEY_HOLD_TPB);
        KeyBindingHelper.registerKeyBinding(PerspectiveUtils.KEY_SET_FP);
        KeyBindingHelper.registerKeyBinding(PerspectiveUtils.KEY_SET_TPF);
        KeyBindingHelper.registerKeyBinding(PerspectiveUtils.KEY_SET_TPB);
        KeyBindingHelper.registerKeyBinding(PerspectiveUtils.KEY_CYCLE_SUPER_SECRET_SETTINGS);
        KeyBindingHelper.registerKeyBinding(PerspectiveUtils.KEY_TOGGLE_SUPER_SECRET_SETTINGS);
    }
    public static String booleanToString(boolean BOOLEAN) {
        if (BOOLEAN) return "ENABLED!";
        else return "DISABLED!";
    }
    static {
        KEY_ZOOM = new KeyBinding("key.perspective.zoom.hold", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, "category.perspective.zoom");
        KEY_HOLD_TPF = new KeyBinding("key.perspective.tpf.hold", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, "category.perspective.zoom");
        KEY_HOLD_TPB = new KeyBinding("key.perspective.tpb.hold", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "category.perspective.zoom");
        KEY_SET_FP = new KeyBinding("key.perspective.fp.set", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.zoom");
        KEY_SET_TPF = new KeyBinding("key.perspective.tpf.set", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.zoom");
        KEY_SET_TPB = new KeyBinding("key.perspective.tpb.set", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.perspective.zoom");
        KEY_CYCLE_SUPER_SECRET_SETTINGS = new KeyBinding("key.perspective.sss.cycle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F7, "category.perspective.sss");
        KEY_TOGGLE_SUPER_SECRET_SETTINGS = new KeyBinding("key.perspective.sss.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F8, "category.perspective.sss");
    }
}