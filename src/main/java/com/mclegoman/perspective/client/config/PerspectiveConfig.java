/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.client.dataloader.PerspectiveDefaultConfigDataLoader;
import com.mclegoman.perspective.client.registry.PerspectiveKeybindings;
import com.mclegoman.perspective.client.screen.PerspectiveConfigScreen;
import com.mclegoman.perspective.common.data.PerspectiveData;
import com.mclegoman.simplefabric.fabric_simplelibs.simple_config.SimpleConfig;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;

public class PerspectiveConfig {
    public static SimpleConfig CONFIG;
    public static PerspectiveConfigProvider getConfigProvider() {return CONFIG_PROVIDER;}
    private static PerspectiveConfigProvider CONFIG_PROVIDER;
    public static int ZOOM_LEVEL;
    public static int OVERLAY_DELAY;
    public static int SUPER_SECRET_SETTINGS;
    public static int PERSPECTIVE;
    public static boolean SHOW_DEVELOPMENT_WARNING;
    public static int CONFIG_VERSION;
    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveDefaultConfigDataLoader());
        CONFIG_PROVIDER = new PerspectiveConfigProvider();
        create();
        CONFIG = SimpleConfig.of(PerspectiveData.ID).provider(CONFIG_PROVIDER).request();
        assign();
        if (CONFIG_VERSION != PerspectiveData.CONFIG_VERSION) reset();
    }
    private static void create() {
        CONFIG_PROVIDER.add(new Pair<>("zoom_level", PerspectiveDefaultConfigDataLoader.ZOOM_LEVEL));
        CONFIG_PROVIDER.add(new Pair<>("overlay_delay", PerspectiveDefaultConfigDataLoader.OVERLAY_DELAY));
        CONFIG_PROVIDER.add(new Pair<>("super_secret_settings", PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS));
        CONFIG_PROVIDER.add(new Pair<>("perspective", PerspectiveDefaultConfigDataLoader.PERSPECTIVE));
        CONFIG_PROVIDER.add(new Pair<>("show_development_warning", PerspectiveDefaultConfigDataLoader.SHOW_DEVELOPMENT_WARNING));
        CONFIG_PROVIDER.add(new Pair<>("config_version", PerspectiveDefaultConfigDataLoader.CONFIG_VERSION));
    }
    private static void assign() {
        ZOOM_LEVEL = CONFIG.getOrDefault("zoom_level", PerspectiveDefaultConfigDataLoader.ZOOM_LEVEL);
        OVERLAY_DELAY = CONFIG.getOrDefault("overlay_delay", PerspectiveDefaultConfigDataLoader.OVERLAY_DELAY);
        SUPER_SECRET_SETTINGS = CONFIG.getOrDefault("super_secret_settings", PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS);
        PERSPECTIVE = CONFIG.getOrDefault("perspective", PerspectiveDefaultConfigDataLoader.PERSPECTIVE);
        SHOW_DEVELOPMENT_WARNING = CONFIG.getOrDefault("show_development_warning", PerspectiveDefaultConfigDataLoader.SHOW_DEVELOPMENT_WARNING);
        CONFIG_VERSION = CONFIG.getOrDefault("config_version", PerspectiveDefaultConfigDataLoader.CONFIG_VERSION);
    }
    public static void reset() {
        ZOOM_LEVEL = PerspectiveDefaultConfigDataLoader.ZOOM_LEVEL;
        OVERLAY_DELAY = PerspectiveDefaultConfigDataLoader.OVERLAY_DELAY;
        SUPER_SECRET_SETTINGS = PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS;
        PERSPECTIVE = PerspectiveDefaultConfigDataLoader.PERSPECTIVE;
        SHOW_DEVELOPMENT_WARNING = PerspectiveDefaultConfigDataLoader.SHOW_DEVELOPMENT_WARNING;
        CONFIG_VERSION = PerspectiveDefaultConfigDataLoader.CONFIG_VERSION;
        write_to_file();
    }
    public static void write_to_file() {
        PerspectiveConfig.getConfigProvider().write_to_file("zoom_level", PerspectiveConfig.ZOOM_LEVEL);
        PerspectiveConfig.getConfigProvider().write_to_file("overlay_delay", PerspectiveConfig.OVERLAY_DELAY);
        PerspectiveConfig.getConfigProvider().write_to_file("super_secret_settings", PerspectiveConfig.SUPER_SECRET_SETTINGS);
        PerspectiveConfig.getConfigProvider().write_to_file("perspective", PerspectiveConfig.PERSPECTIVE);
        PerspectiveConfig.getConfigProvider().write_to_file("show_development_warning", PerspectiveConfig.SHOW_DEVELOPMENT_WARNING);
        PerspectiveConfig.getConfigProvider().write_to_file("config_version", PerspectiveConfig.CONFIG_VERSION);
    }
    public static void tick(MinecraftClient client) {
        if (PerspectiveKeybindings.KEY_CONFIG.wasPressed()) client.setScreen(new PerspectiveConfigScreen(client.currentScreen));
    }
}