/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.common.data.PerspectiveData;
import com.mclegoman.simplefabriclibs.simple_config.SimpleConfig;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PerspectiveConfig {
    protected static final String ID = PerspectiveData.ID;
    protected static SimpleConfig CONFIG;
    protected static PerspectiveConfigProvider CONFIG_PROVIDER;
    protected static int ZOOM_LEVEL;
    protected static boolean HIDE_HUD;
    protected static int SUPER_SECRET_SETTINGS;
    protected static boolean SUPER_SECRET_SETTINGS_MODE;
    protected static boolean SUPER_SECRET_SETTINGS_ENABLED;
    protected static boolean NAMED_TEXTURED_ENTITY;
    protected static boolean RANDOM_TEXTURED_ENTITY;
    protected static boolean ALLOW_APRIL_FOOLS;
    protected static boolean FORCE_APRIL_FOOLS;
    protected static boolean FORCE_PRIDE;
    protected static boolean VERSION_OVERLAY;
    protected static boolean SHOW_DEVELOPMENT_WARNING;
    protected static boolean BYPASS_LIMITS;
    protected static int CONFIG_VERSION;
    protected static void init() {
        try {
            CONFIG_PROVIDER = new PerspectiveConfigProvider();
            create();
            CONFIG = SimpleConfig.of(ID).provider(CONFIG_PROVIDER).request();
            assign();
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to initialize " + ID + " config: {}", (Object)error);
        }
    }
    protected static void create() {
        CONFIG_PROVIDER.add(new Pair<>("zoom_level", PerspectiveConfigDataLoader.ZOOM_LEVEL));
        CONFIG_PROVIDER.add(new Pair<>("hide_hud", PerspectiveConfigDataLoader.HIDE_HUD));
        CONFIG_PROVIDER.add(new Pair<>("super_secret_settings", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS));
        CONFIG_PROVIDER.add(new Pair<>("super_secret_settings_mode", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_MODE));
        CONFIG_PROVIDER.add(new Pair<>("super_secret_settings_enabled", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_ENABLED));
        CONFIG_PROVIDER.add(new Pair<>("textured_named_entity", PerspectiveConfigDataLoader.NAMED_TEXTURED_ENTITY));
        CONFIG_PROVIDER.add(new Pair<>("textured_random_entity", PerspectiveConfigDataLoader.RANDOM_TEXTURED_ENTITY));
        CONFIG_PROVIDER.add(new Pair<>("allow_april_fools", PerspectiveConfigDataLoader.ALLOW_APRIL_FOOLS));
        CONFIG_PROVIDER.add(new Pair<>("force_april_fools", PerspectiveConfigDataLoader.FORCE_APRIL_FOOLS));
        CONFIG_PROVIDER.add(new Pair<>("force_pride", PerspectiveConfigDataLoader.FORCE_PRIDE));
        CONFIG_PROVIDER.add(new Pair<>("version_overlay", PerspectiveConfigDataLoader.VERSION_OVERLAY));
        CONFIG_PROVIDER.add(new Pair<>("show_development_warning", PerspectiveConfigDataLoader.SHOW_DEVELOPMENT_WARNING));
        CONFIG_PROVIDER.add(new Pair<>("bypass_limits", PerspectiveConfigDataLoader.BYPASS_LIMITS));
        CONFIG_PROVIDER.add(new Pair<>("config_version", PerspectiveConfigHelper.DEFAULT_CONFIG_VERSION));
    }
    protected static void assign() {
        ZOOM_LEVEL = CONFIG.getOrDefault("zoom_level", PerspectiveConfigDataLoader.ZOOM_LEVEL);
        HIDE_HUD = CONFIG.getOrDefault("hide_hud", PerspectiveConfigDataLoader.HIDE_HUD);
        SUPER_SECRET_SETTINGS = CONFIG.getOrDefault("super_secret_settings", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS);
        SUPER_SECRET_SETTINGS_MODE = CONFIG.getOrDefault("super_secret_settings_mode", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_MODE);
        SUPER_SECRET_SETTINGS_ENABLED = CONFIG.getOrDefault("super_secret_settings_enabled", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_ENABLED);
        NAMED_TEXTURED_ENTITY = CONFIG.getOrDefault("textured_named_entity", PerspectiveConfigDataLoader.NAMED_TEXTURED_ENTITY);
        RANDOM_TEXTURED_ENTITY = CONFIG.getOrDefault("textured_random_entity", PerspectiveConfigDataLoader.RANDOM_TEXTURED_ENTITY);
        ALLOW_APRIL_FOOLS = CONFIG.getOrDefault("allow_april_fools", PerspectiveConfigDataLoader.ALLOW_APRIL_FOOLS);
        FORCE_APRIL_FOOLS = CONFIG.getOrDefault("force_april_fools", PerspectiveConfigDataLoader.FORCE_APRIL_FOOLS);
        FORCE_PRIDE = CONFIG.getOrDefault("force_pride", PerspectiveConfigDataLoader.FORCE_PRIDE);
        VERSION_OVERLAY = CONFIG.getOrDefault("version_overlay", PerspectiveConfigDataLoader.VERSION_OVERLAY);
        SHOW_DEVELOPMENT_WARNING = CONFIG.getOrDefault("show_development_warning", PerspectiveConfigDataLoader.SHOW_DEVELOPMENT_WARNING);
        BYPASS_LIMITS = CONFIG.getOrDefault("bypass_limits", PerspectiveConfigDataLoader.BYPASS_LIMITS);
        CONFIG_VERSION = CONFIG.getOrDefault("config_version", PerspectiveConfigHelper.DEFAULT_CONFIG_VERSION);
    }
    protected static void save() {
        CONFIG_PROVIDER.setConfig("zoom_level", ZOOM_LEVEL);
        CONFIG_PROVIDER.setConfig("hide_hud", HIDE_HUD);
        CONFIG_PROVIDER.setConfig("super_secret_settings", SUPER_SECRET_SETTINGS);
        CONFIG_PROVIDER.setConfig("super_secret_settings_mode", SUPER_SECRET_SETTINGS_MODE);
        CONFIG_PROVIDER.setConfig("super_secret_settings_enabled", SUPER_SECRET_SETTINGS_ENABLED);
        CONFIG_PROVIDER.setConfig("named_textured_entity", NAMED_TEXTURED_ENTITY);
        CONFIG_PROVIDER.setConfig("random_textured_entity", RANDOM_TEXTURED_ENTITY);
        CONFIG_PROVIDER.setConfig("allow_april_fools", ALLOW_APRIL_FOOLS);
        CONFIG_PROVIDER.setConfig("force_april_fools", FORCE_APRIL_FOOLS);
        CONFIG_PROVIDER.setConfig("force_pride", FORCE_PRIDE);
        CONFIG_PROVIDER.setConfig("version_overlay", VERSION_OVERLAY);
        CONFIG_PROVIDER.setConfig("show_development_warning", SHOW_DEVELOPMENT_WARNING);
        CONFIG_PROVIDER.setConfig("bypass_limits", BYPASS_LIMITS);
        CONFIG_PROVIDER.setConfig("config_version", PerspectiveConfigHelper.DEFAULT_CONFIG_VERSION);
    }
}