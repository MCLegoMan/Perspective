/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.client.dataloader.PerspectiveConfigDefaultsDataLoader;
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
    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveConfigDefaultsDataLoader());
        CONFIG_PROVIDER = new PerspectiveConfigProvider();
        create();
        CONFIG = SimpleConfig.of(PerspectiveData.ID).provider(CONFIG_PROVIDER).request();
        assign();
    }
    private static void create() {
        CONFIG_PROVIDER.add(new Pair<>("zoom_level", PerspectiveConfigDefaultsDataLoader.ZOOM_LEVEL));
        CONFIG_PROVIDER.add(new Pair<>("overlay_delay", PerspectiveConfigDefaultsDataLoader.OVERLAY_DELAY));
        CONFIG_PROVIDER.add(new Pair<>("super_secret_settings", PerspectiveConfigDefaultsDataLoader.SUPER_SECRET_SETTINGS));
        CONFIG_PROVIDER.add(new Pair<>("perspective", PerspectiveConfigDefaultsDataLoader.PERSPECTIVE));
    }
    private static void assign() {
        ZOOM_LEVEL = CONFIG.getOrDefault("zoom_level", PerspectiveConfigDefaultsDataLoader.ZOOM_LEVEL);
        OVERLAY_DELAY = CONFIG.getOrDefault("overlay_delay", PerspectiveConfigDefaultsDataLoader.OVERLAY_DELAY);
        SUPER_SECRET_SETTINGS = CONFIG.getOrDefault("super_secret_settings", PerspectiveConfigDefaultsDataLoader.SUPER_SECRET_SETTINGS);
        PERSPECTIVE = CONFIG.getOrDefault("perspective", PerspectiveConfigDefaultsDataLoader.PERSPECTIVE);
    }
    public static void reset() {
        ZOOM_LEVEL = PerspectiveConfigDefaultsDataLoader.ZOOM_LEVEL;
        OVERLAY_DELAY = PerspectiveConfigDefaultsDataLoader.OVERLAY_DELAY;
        SUPER_SECRET_SETTINGS = PerspectiveConfigDefaultsDataLoader.SUPER_SECRET_SETTINGS;
        PERSPECTIVE = PerspectiveConfigDefaultsDataLoader.PERSPECTIVE;
        write_to_file();
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