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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;

@Environment(EnvType.CLIENT)
public class PerspectiveConfig {
    public static SimpleConfig CONFIG;
    public static PerspectiveConfigProvider getConfigProvider() {return CONFIG_PROVIDER;}
    private static PerspectiveConfigProvider CONFIG_PROVIDER;
    public static int ZOOM_LEVEL;
    public static int OVERLAY_DELAY;
    public static int SUPER_SECRET_SETTINGS;
    public static String DEFAULT_SUPER_SECRET_SETTINGS_SOUND;
    public static int PERSPECTIVE;
    public static boolean TEXTURED_NAMED_ENTITY;
    public static boolean TEXTURED_RANDOM_ENTITY;
    public static boolean ALLOW_APRIL_FOOLS;
    public static boolean FORCE_APRIL_FOOLS;
    public static boolean SHOW_DEVELOPMENT_WARNING;
    public static int CONFIG_VERSION;
    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveDefaultConfigDataLoader());
        CONFIG_PROVIDER = new PerspectiveConfigProvider();
        create();
        CONFIG = SimpleConfig.of(PerspectiveData.ID).provider(CONFIG_PROVIDER).request();
        assign();
    }
    private static void create() {
        CONFIG_PROVIDER.add(new Pair<>("zoom_level", PerspectiveDefaultConfigDataLoader.ZOOM_LEVEL));
        CONFIG_PROVIDER.add(new Pair<>("overlay_delay", PerspectiveDefaultConfigDataLoader.OVERLAY_DELAY));
        CONFIG_PROVIDER.add(new Pair<>("super_secret_settings", PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS));
        CONFIG_PROVIDER.add(new Pair<>("default_super_secret_settings_sound", PerspectiveDefaultConfigDataLoader.DEFAULT_SUPER_SECRET_SETTINGS_SOUND));
        CONFIG_PROVIDER.add(new Pair<>("perspective", PerspectiveDefaultConfigDataLoader.PERSPECTIVE));
        CONFIG_PROVIDER.add(new Pair<>("textured_named_entity", PerspectiveDefaultConfigDataLoader.TEXTURED_NAMED_ENTITY));
        CONFIG_PROVIDER.add(new Pair<>("textured_random_entity", PerspectiveDefaultConfigDataLoader.TEXTURED_RANDOM_ENTITY));
        CONFIG_PROVIDER.add(new Pair<>("allow_april_fools", PerspectiveDefaultConfigDataLoader.ALLOW_APRIL_FOOLS));
        CONFIG_PROVIDER.add(new Pair<>("force_april_fools", PerspectiveDefaultConfigDataLoader.FORCE_APRIL_FOOLS));
        CONFIG_PROVIDER.add(new Pair<>("show_development_warning", PerspectiveDefaultConfigDataLoader.SHOW_DEVELOPMENT_WARNING));
        CONFIG_PROVIDER.add(new Pair<>("config_version", PerspectiveDefaultConfigDataLoader.CONFIG_VERSION));
    }
    private static void assign() {
        ZOOM_LEVEL = CONFIG.getOrDefault("zoom_level", PerspectiveDefaultConfigDataLoader.ZOOM_LEVEL);
        OVERLAY_DELAY = CONFIG.getOrDefault("overlay_delay", PerspectiveDefaultConfigDataLoader.OVERLAY_DELAY);
        SUPER_SECRET_SETTINGS = CONFIG.getOrDefault("super_secret_settings", PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS);
        DEFAULT_SUPER_SECRET_SETTINGS_SOUND = CONFIG.getOrDefault("default_super_secret_settings_sound", PerspectiveDefaultConfigDataLoader.DEFAULT_SUPER_SECRET_SETTINGS_SOUND);
        PERSPECTIVE = CONFIG.getOrDefault("perspective", PerspectiveDefaultConfigDataLoader.PERSPECTIVE);
        TEXTURED_NAMED_ENTITY = CONFIG.getOrDefault("textured_named_entity", PerspectiveDefaultConfigDataLoader.TEXTURED_NAMED_ENTITY);
        TEXTURED_RANDOM_ENTITY = CONFIG.getOrDefault("textured_random_entity", PerspectiveDefaultConfigDataLoader.TEXTURED_RANDOM_ENTITY);
        ALLOW_APRIL_FOOLS = CONFIG.getOrDefault("allow_april_fools", PerspectiveDefaultConfigDataLoader.ALLOW_APRIL_FOOLS);
        FORCE_APRIL_FOOLS = CONFIG.getOrDefault("force_april_fools", PerspectiveDefaultConfigDataLoader.FORCE_APRIL_FOOLS);
        SHOW_DEVELOPMENT_WARNING = CONFIG.getOrDefault("show_development_warning", PerspectiveDefaultConfigDataLoader.SHOW_DEVELOPMENT_WARNING);
        CONFIG_VERSION = CONFIG.getOrDefault("config_version", PerspectiveDefaultConfigDataLoader.CONFIG_VERSION);
        write_to_file();
    }
    public static void reset() {
        ZOOM_LEVEL = PerspectiveDefaultConfigDataLoader.ZOOM_LEVEL;
        OVERLAY_DELAY = PerspectiveDefaultConfigDataLoader.OVERLAY_DELAY;
        SUPER_SECRET_SETTINGS = PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS;
        DEFAULT_SUPER_SECRET_SETTINGS_SOUND = PerspectiveDefaultConfigDataLoader.DEFAULT_SUPER_SECRET_SETTINGS_SOUND;
        PERSPECTIVE = PerspectiveDefaultConfigDataLoader.PERSPECTIVE;
        SHOW_DEVELOPMENT_WARNING = PerspectiveDefaultConfigDataLoader.SHOW_DEVELOPMENT_WARNING;
        TEXTURED_NAMED_ENTITY = PerspectiveDefaultConfigDataLoader.TEXTURED_NAMED_ENTITY;
        TEXTURED_RANDOM_ENTITY = PerspectiveDefaultConfigDataLoader.TEXTURED_RANDOM_ENTITY;
        ALLOW_APRIL_FOOLS = PerspectiveDefaultConfigDataLoader.ALLOW_APRIL_FOOLS;
        FORCE_APRIL_FOOLS = PerspectiveDefaultConfigDataLoader.FORCE_APRIL_FOOLS;
        CONFIG_VERSION = PerspectiveDefaultConfigDataLoader.CONFIG_VERSION;
        write_to_file();
    }
    public static void write_to_file() {
        PerspectiveConfig.getConfigProvider().write_to_file("zoom_level", PerspectiveConfig.ZOOM_LEVEL);
        PerspectiveConfig.getConfigProvider().write_to_file("overlay_delay", PerspectiveConfig.OVERLAY_DELAY);
        PerspectiveConfig.getConfigProvider().write_to_file("super_secret_settings", PerspectiveConfig.SUPER_SECRET_SETTINGS);
        PerspectiveConfig.getConfigProvider().write_to_file("default_super_secret_settings_sound", PerspectiveConfig.DEFAULT_SUPER_SECRET_SETTINGS_SOUND);
        PerspectiveConfig.getConfigProvider().write_to_file("perspective", PerspectiveConfig.PERSPECTIVE);
        PerspectiveConfig.getConfigProvider().write_to_file("show_development_warning", PerspectiveConfig.SHOW_DEVELOPMENT_WARNING);
        PerspectiveConfig.getConfigProvider().write_to_file("textured_named_entity", PerspectiveConfig.TEXTURED_NAMED_ENTITY);
        PerspectiveConfig.getConfigProvider().write_to_file("textured_random_entity", PerspectiveConfig.TEXTURED_RANDOM_ENTITY);
        PerspectiveConfig.getConfigProvider().write_to_file("allow_april_fools", PerspectiveConfig.ALLOW_APRIL_FOOLS);
        PerspectiveConfig.getConfigProvider().write_to_file("force_april_fools", PerspectiveConfig.FORCE_APRIL_FOOLS);
        PerspectiveConfig.getConfigProvider().write_to_file("config_version", PerspectiveDefaultConfigDataLoader.CONFIG_VERSION);
    }
    public static void tick(MinecraftClient client) {
        if (PerspectiveKeybindings.KEY_CONFIG.wasPressed()) client.setScreen(new PerspectiveConfigScreen(client.currentScreen));
    }
}