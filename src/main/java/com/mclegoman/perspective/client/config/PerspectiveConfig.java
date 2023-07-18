/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.common.data.PerspectiveData;
import com.mclegoman.simplefabric.fabric_simplelibs.simple_config.SimpleConfig;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PerspectiveConfig {
    protected static final String ID = PerspectiveData.ID;
    protected static SimpleConfig CONFIG;
    protected static PerspectiveConfigProvider CONFIG_PROVIDER;
    protected static int ZOOM_LEVEL;
    protected static int OVERLAY_DELAY;
    protected static int SUPER_SECRET_SETTINGS;
    protected static boolean SUPER_SECRET_SETTINGS_MODE;
    protected static boolean SUPER_SECRET_SETTINGS_ENABLED;
    protected static boolean HIDE_HUD;
    protected static boolean TEXTURED_NAMED_ENTITY;
    protected static boolean TEXTURED_RANDOM_ENTITY;
    protected static boolean ALLOW_APRIL_FOOLS;
    protected static boolean FORCE_APRIL_FOOLS;
    protected static boolean SHOW_DEVELOPMENT_WARNING;
    protected static int CONFIG_VERSION;
    protected static void init() {
        CONFIG_PROVIDER = new PerspectiveConfigProvider();
        create();
        CONFIG = SimpleConfig.of(ID).provider(CONFIG_PROVIDER).request();
        assign();
    }
    protected static void create() {
        CONFIG_PROVIDER.add(ID, new Pair<>("zoom_level", PerspectiveDefaultConfigDataLoader.ZOOM_LEVEL));
        CONFIG_PROVIDER.add(ID, new Pair<>("overlay_delay", PerspectiveDefaultConfigDataLoader.OVERLAY_DELAY));
        CONFIG_PROVIDER.add(ID, new Pair<>("super_secret_settings", PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS));
        CONFIG_PROVIDER.add(ID, new Pair<>("super_secret_settings_mode", PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS_MODE));
        CONFIG_PROVIDER.add(ID, new Pair<>("super_secret_settings_enabled", PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS_ENABLED));
        CONFIG_PROVIDER.add(ID, new Pair<>("hide_hud", PerspectiveDefaultConfigDataLoader.HIDE_HUD));
        CONFIG_PROVIDER.add(ID, new Pair<>("textured_named_entity", PerspectiveDefaultConfigDataLoader.TEXTURED_NAMED_ENTITY));
        CONFIG_PROVIDER.add(ID, new Pair<>("textured_random_entity", PerspectiveDefaultConfigDataLoader.TEXTURED_RANDOM_ENTITY));
        CONFIG_PROVIDER.add(ID, new Pair<>("allow_april_fools", PerspectiveDefaultConfigDataLoader.ALLOW_APRIL_FOOLS));
        CONFIG_PROVIDER.add(ID, new Pair<>("force_april_fools", PerspectiveDefaultConfigDataLoader.FORCE_APRIL_FOOLS));
        CONFIG_PROVIDER.add(ID, new Pair<>("show_development_warning", PerspectiveDefaultConfigDataLoader.SHOW_DEVELOPMENT_WARNING));
        CONFIG_PROVIDER.add(ID, new Pair<>("config_version", PerspectiveConfigHelper.DEFAULT_CONFIG_VERSION));
    }
    protected static void assign() {
        ZOOM_LEVEL = CONFIG.getOrDefault("zoom_level", PerspectiveDefaultConfigDataLoader.ZOOM_LEVEL);
        OVERLAY_DELAY = CONFIG.getOrDefault("overlay_delay", PerspectiveDefaultConfigDataLoader.OVERLAY_DELAY);
        SUPER_SECRET_SETTINGS = CONFIG.getOrDefault("super_secret_settings", PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS);
        SUPER_SECRET_SETTINGS_MODE = CONFIG.getOrDefault("super_secret_settings_mode", PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS_MODE);
        SUPER_SECRET_SETTINGS_ENABLED = CONFIG.getOrDefault("super_secret_settings_enabled", PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS_ENABLED);
        HIDE_HUD = CONFIG.getOrDefault("hide_hud", PerspectiveDefaultConfigDataLoader.HIDE_HUD);
        TEXTURED_NAMED_ENTITY = CONFIG.getOrDefault("textured_named_entity", PerspectiveDefaultConfigDataLoader.TEXTURED_NAMED_ENTITY);
        TEXTURED_RANDOM_ENTITY = CONFIG.getOrDefault("textured_random_entity", PerspectiveDefaultConfigDataLoader.TEXTURED_NAMED_ENTITY);
        ALLOW_APRIL_FOOLS = CONFIG.getOrDefault("allow_april_fools", PerspectiveDefaultConfigDataLoader.ALLOW_APRIL_FOOLS);
        FORCE_APRIL_FOOLS = CONFIG.getOrDefault("force_april_fools", PerspectiveDefaultConfigDataLoader.FORCE_APRIL_FOOLS);
        SHOW_DEVELOPMENT_WARNING = CONFIG.getOrDefault("show_development_warning", PerspectiveDefaultConfigDataLoader.SHOW_DEVELOPMENT_WARNING);
        CONFIG_VERSION = CONFIG.getOrDefault("config_version", PerspectiveConfigHelper.DEFAULT_CONFIG_VERSION);
    }
    protected static void save() {
        CONFIG_PROVIDER.setConfig("zoom_level", ZOOM_LEVEL);
        CONFIG_PROVIDER.setConfig("overlay_delay", OVERLAY_DELAY);
        CONFIG_PROVIDER.setConfig("super_secret_settings", SUPER_SECRET_SETTINGS);
        CONFIG_PROVIDER.setConfig("super_secret_settings_mode", SUPER_SECRET_SETTINGS_MODE);
        CONFIG_PROVIDER.setConfig("super_secret_settings_enabled", SUPER_SECRET_SETTINGS_ENABLED);
        CONFIG_PROVIDER.setConfig("hide_hud", HIDE_HUD);
        CONFIG_PROVIDER.setConfig("textured_named_entity", TEXTURED_NAMED_ENTITY);
        CONFIG_PROVIDER.setConfig("textured_random_entity", TEXTURED_NAMED_ENTITY);
        CONFIG_PROVIDER.setConfig("allow_april_fools", ALLOW_APRIL_FOOLS);
        CONFIG_PROVIDER.setConfig("force_april_fools", FORCE_APRIL_FOOLS);
        CONFIG_PROVIDER.setConfig("show_development_warning", SHOW_DEVELOPMENT_WARNING);
        CONFIG_PROVIDER.setConfig("config_version", PerspectiveConfigHelper.DEFAULT_CONFIG_VERSION);
        CONFIG_PROVIDER.saveConfig(ID);
    }
}