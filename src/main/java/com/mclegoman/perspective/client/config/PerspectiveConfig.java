package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.client.registry.PerspectiveKeybindings;
import com.mclegoman.perspective.client.screen.PerspectiveConfigScreen;
import com.mclegoman.simplefabric.fabric_simplelibs.simple_config.SimpleConfig;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.MinecraftClient;

public class PerspectiveConfig {
    public static SimpleConfig CONFIG;
    public static PerspectiveConfigProvider getConfigProvider() {return CONFIG_PROVIDER;}
    private static PerspectiveConfigProvider CONFIG_PROVIDER;
    public static int ZOOM_LEVEL;
    public static int OVERLAY_DELAY;
    public static int SUPER_SECRET_SETTINGS;
    public static int PERSPECTIVE;
    public static void init() {
        CONFIG_PROVIDER = new PerspectiveConfigProvider();
        create();
        CONFIG = SimpleConfig.of("perspective").provider(CONFIG_PROVIDER).request();
        assign();
    }
    private static void create() {
        CONFIG_PROVIDER.add(new Pair<>("zoom_level", 20));
        CONFIG_PROVIDER.add(new Pair<>("overlay_delay", 200));
        CONFIG_PROVIDER.add(new Pair<>("super_secret_settings", 0));
        CONFIG_PROVIDER.add(new Pair<>("perspective", 0));
    }
    private static void assign() {
        ZOOM_LEVEL = CONFIG.getOrDefault("zoom_level", 20);
        OVERLAY_DELAY = CONFIG.getOrDefault("overlay_delay", 200);
        SUPER_SECRET_SETTINGS = CONFIG.getOrDefault("super_secret_settings", 0);
        SUPER_SECRET_SETTINGS = CONFIG.getOrDefault("perspective", 0);
    }
    public static void write_to_file() {
        PerspectiveConfig.getConfigProvider().write_to_file("zoom_level", PerspectiveConfig.ZOOM_LEVEL);
        PerspectiveConfig.getConfigProvider().write_to_file("overlay_delay", PerspectiveConfig.OVERLAY_DELAY);
        PerspectiveConfig.getConfigProvider().write_to_file("super_secret_settings", PerspectiveConfig.SUPER_SECRET_SETTINGS);
        PerspectiveConfig.getConfigProvider().write_to_file("perspective", PerspectiveConfig.PERSPECTIVE);
    }
    public static void tick(MinecraftClient client) {
        if (PerspectiveKeybindings.KEY_CONFIG.wasPressed()) client.setScreen(new PerspectiveConfigScreen(client.currentScreen));
    }
}