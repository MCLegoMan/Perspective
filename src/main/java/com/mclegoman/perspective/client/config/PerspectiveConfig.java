/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.client.dataloader.PerspectiveDefaultConfigDataLoader;
import com.mclegoman.perspective.client.registry.PerspectiveKeybindings;
import com.mclegoman.perspective.client.screen.config.PerspectiveConfigScreen;
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
    public static boolean ZOOM_SHOW_HUD;
    public static int OVERLAY_DELAY;
    public static boolean PERSPECTIVE_HOLD;
    public static boolean PERSPECTIVE_HOLD_SHOW_HUD;
    public static int SUPER_SECRET_SETTINGS;
    public static String DEFAULT_SUPER_SECRET_SETTINGS_SOUND;
    public static boolean TEXTURED_NAMED_ENTITY;
    public static boolean TEXTURED_RANDOM_ENTITY;
    public static boolean ALLOW_APRIL_FOOLS;
    public static boolean FORCE_APRIL_FOOLS;
    public static boolean SHOW_DEVELOPMENT_WARNING;
    public static int CONFIG_VERSION;
    public static boolean TICK_SAVE;
    private static int SAVE_TICKS;
    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveDefaultConfigDataLoader());
        CONFIG_PROVIDER = new PerspectiveConfigProvider();
        create();
        CONFIG = SimpleConfig.of(PerspectiveData.ID).provider(CONFIG_PROVIDER).request();
        assign();
    }
    private static void create() {
        CONFIG_PROVIDER.add(new Pair<>("zoom_level", PerspectiveDefaultConfigDataLoader.ZOOM_LEVEL));
        CONFIG_PROVIDER.add(new Pair<>("zoom_show_hud", PerspectiveDefaultConfigDataLoader.ZOOM_SHOW_HUD));
        CONFIG_PROVIDER.add(new Pair<>("overlay_delay", PerspectiveDefaultConfigDataLoader.OVERLAY_DELAY));
        CONFIG_PROVIDER.add(new Pair<>("perspective_hold", PerspectiveDefaultConfigDataLoader.PERSPECTIVE_HOLD));
        CONFIG_PROVIDER.add(new Pair<>("perspective_hold_show_hud", PerspectiveDefaultConfigDataLoader.PERSPECTIVE_HOLD_SHOW_HUD));
        CONFIG_PROVIDER.add(new Pair<>("super_secret_settings", PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS));
        CONFIG_PROVIDER.add(new Pair<>("default_super_secret_settings_sound", PerspectiveDefaultConfigDataLoader.DEFAULT_SUPER_SECRET_SETTINGS_SOUND));
        CONFIG_PROVIDER.add(new Pair<>("textured_named_entity", PerspectiveDefaultConfigDataLoader.TEXTURED_NAMED_ENTITY));
        CONFIG_PROVIDER.add(new Pair<>("textured_random_entity", PerspectiveDefaultConfigDataLoader.TEXTURED_RANDOM_ENTITY));
        CONFIG_PROVIDER.add(new Pair<>("allow_april_fools", PerspectiveDefaultConfigDataLoader.ALLOW_APRIL_FOOLS));
        CONFIG_PROVIDER.add(new Pair<>("force_april_fools", PerspectiveDefaultConfigDataLoader.FORCE_APRIL_FOOLS));
        CONFIG_PROVIDER.add(new Pair<>("show_development_warning", PerspectiveDefaultConfigDataLoader.SHOW_DEVELOPMENT_WARNING));
        CONFIG_PROVIDER.add(new Pair<>("config_version", PerspectiveDefaultConfigDataLoader.CONFIG_VERSION));
    }
    private static void assign() {
        ZOOM_LEVEL = CONFIG.getOrDefault("zoom_level", PerspectiveDefaultConfigDataLoader.ZOOM_LEVEL);
        ZOOM_SHOW_HUD = CONFIG.getOrDefault("zoom_show_hud", PerspectiveDefaultConfigDataLoader.ZOOM_SHOW_HUD);
        OVERLAY_DELAY = CONFIG.getOrDefault("overlay_delay", PerspectiveDefaultConfigDataLoader.OVERLAY_DELAY);
        PERSPECTIVE_HOLD = CONFIG.getOrDefault("perspective_hold", PerspectiveDefaultConfigDataLoader.PERSPECTIVE_HOLD);
        PERSPECTIVE_HOLD_SHOW_HUD = CONFIG.getOrDefault("perspective_hold_show_hud", PerspectiveDefaultConfigDataLoader.PERSPECTIVE_HOLD_SHOW_HUD);
        SUPER_SECRET_SETTINGS = CONFIG.getOrDefault("super_secret_settings", PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS);
        DEFAULT_SUPER_SECRET_SETTINGS_SOUND = CONFIG.getOrDefault("default_super_secret_settings_sound", PerspectiveDefaultConfigDataLoader.DEFAULT_SUPER_SECRET_SETTINGS_SOUND);
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
        ZOOM_SHOW_HUD = PerspectiveDefaultConfigDataLoader.ZOOM_SHOW_HUD;
        OVERLAY_DELAY = PerspectiveDefaultConfigDataLoader.OVERLAY_DELAY;
        PERSPECTIVE_HOLD = PerspectiveDefaultConfigDataLoader.PERSPECTIVE_HOLD;
        PERSPECTIVE_HOLD_SHOW_HUD = PerspectiveDefaultConfigDataLoader.PERSPECTIVE_HOLD_SHOW_HUD;
        SUPER_SECRET_SETTINGS = PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS;
        DEFAULT_SUPER_SECRET_SETTINGS_SOUND = PerspectiveDefaultConfigDataLoader.DEFAULT_SUPER_SECRET_SETTINGS_SOUND;
        SHOW_DEVELOPMENT_WARNING = PerspectiveDefaultConfigDataLoader.SHOW_DEVELOPMENT_WARNING;
        TEXTURED_NAMED_ENTITY = PerspectiveDefaultConfigDataLoader.TEXTURED_NAMED_ENTITY;
        TEXTURED_RANDOM_ENTITY = PerspectiveDefaultConfigDataLoader.TEXTURED_RANDOM_ENTITY;
        ALLOW_APRIL_FOOLS = PerspectiveDefaultConfigDataLoader.ALLOW_APRIL_FOOLS;
        FORCE_APRIL_FOOLS = PerspectiveDefaultConfigDataLoader.FORCE_APRIL_FOOLS;
        CONFIG_VERSION = PerspectiveDefaultConfigDataLoader.CONFIG_VERSION;
        write_to_file();
    }
    public static void write_to_file() {
        PerspectiveData.LOGGER.info(PerspectiveData.PREFIX + "Writing config to file.");
        getConfigProvider().write_to_file("zoom_level", ZOOM_LEVEL);
        getConfigProvider().write_to_file("zoom_show_hud", ZOOM_SHOW_HUD);
        getConfigProvider().write_to_file("overlay_delay", OVERLAY_DELAY);
        getConfigProvider().write_to_file("perspective_hold", PERSPECTIVE_HOLD);
        getConfigProvider().write_to_file("perspective_hold_show_hud", PERSPECTIVE_HOLD_SHOW_HUD);
        getConfigProvider().write_to_file("super_secret_settings", SUPER_SECRET_SETTINGS);
        getConfigProvider().write_to_file("default_super_secret_settings_sound", DEFAULT_SUPER_SECRET_SETTINGS_SOUND);
        getConfigProvider().write_to_file("show_development_warning", SHOW_DEVELOPMENT_WARNING);
        getConfigProvider().write_to_file("textured_named_entity", TEXTURED_NAMED_ENTITY);
        getConfigProvider().write_to_file("textured_random_entity", TEXTURED_RANDOM_ENTITY);
        getConfigProvider().write_to_file("allow_april_fools", ALLOW_APRIL_FOOLS);
        getConfigProvider().write_to_file("force_april_fools", FORCE_APRIL_FOOLS);
        getConfigProvider().write_to_file("config_version", PerspectiveDefaultConfigDataLoader.CONFIG_VERSION);
    }
    public static void tick(MinecraftClient client) {
        if (PerspectiveKeybindings.KEY_CONFIG.wasPressed()) client.setScreen(new PerspectiveConfigScreen(client.currentScreen));
        if (SAVE_TICKS < 20) SAVE_TICKS += 1;
        else {
            if (TICK_SAVE) {
                write_to_file();
                TICK_SAVE = false;
            }
            SAVE_TICKS = 0;
        }
    }
}