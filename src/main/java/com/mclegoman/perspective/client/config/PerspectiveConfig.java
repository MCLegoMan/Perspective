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
    protected static int CHANGE_ZOOM_MULTIPLIER;
    protected static boolean SMOOTH_ZOOM;
    protected static boolean HIDE_HUD;
    protected static int SUPER_SECRET_SETTINGS;
    protected static boolean SUPER_SECRET_SETTINGS_MODE;
    protected static boolean SUPER_SECRET_SETTINGS_ENABLED;
    protected static boolean SUPER_SECRET_SETTINGS_SOUND;
    protected static boolean SUPER_SECRET_SETTINGS_OPTIONS_SCREEN;
    protected static boolean NAMED_TEXTURED_ENTITY;
    protected static boolean RANDOM_TEXTURED_ENTITY;
    protected static boolean ALLOW_APRIL_FOOLS;
    protected static boolean FORCE_APRIL_FOOLS;
    protected static boolean FORCE_PRIDE;
    protected static boolean FORCE_PRIDE_TYPE;
    protected static int FORCE_PRIDE_TYPE_INDEX;
    protected static boolean VERSION_OVERLAY;
    protected static boolean HIDE_ARMOR;
    protected static boolean HIDE_NAMETAGS;
    protected static boolean SHOW_DEVELOPMENT_WARNING;
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
        CONFIG_PROVIDER.add(new Pair<>("change_zoom_multiplier", PerspectiveConfigDataLoader.CHANGE_ZOOM_MULTIPLIER));
        CONFIG_PROVIDER.add(new Pair<>("smooth_zoom", PerspectiveConfigDataLoader.SMOOTH_ZOOM));
        CONFIG_PROVIDER.add(new Pair<>("hide_hud", PerspectiveConfigDataLoader.HIDE_HUD));
        CONFIG_PROVIDER.add(new Pair<>("super_secret_settings", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS));
        CONFIG_PROVIDER.add(new Pair<>("super_secret_settings_mode", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_MODE));
        CONFIG_PROVIDER.add(new Pair<>("super_secret_settings_enabled", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_ENABLED));
        CONFIG_PROVIDER.add(new Pair<>("super_secret_settings_sound", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_SOUND));
        CONFIG_PROVIDER.add(new Pair<>("super_secret_settings_options_screen", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_OPTIONS_SCREEN));
        CONFIG_PROVIDER.add(new Pair<>("textured_named_entity", PerspectiveConfigDataLoader.NAMED_TEXTURED_ENTITY));
        CONFIG_PROVIDER.add(new Pair<>("textured_random_entity", PerspectiveConfigDataLoader.RANDOM_TEXTURED_ENTITY));
        CONFIG_PROVIDER.add(new Pair<>("allow_april_fools", PerspectiveConfigDataLoader.ALLOW_APRIL_FOOLS));
        CONFIG_PROVIDER.add(new Pair<>("force_april_fools", PerspectiveConfigDataLoader.FORCE_APRIL_FOOLS));
        CONFIG_PROVIDER.add(new Pair<>("force_pride", PerspectiveConfigDataLoader.FORCE_PRIDE));
        CONFIG_PROVIDER.add(new Pair<>("force_pride_type", PerspectiveConfigDataLoader.FORCE_PRIDE_TYPE));
        CONFIG_PROVIDER.add(new Pair<>("force_pride_type_index", PerspectiveConfigDataLoader.FORCE_PRIDE_TYPE_INDEX));
        CONFIG_PROVIDER.add(new Pair<>("version_overlay", PerspectiveConfigDataLoader.VERSION_OVERLAY));
        CONFIG_PROVIDER.add(new Pair<>("hide_armor", PerspectiveConfigDataLoader.HIDE_ARMOR));
        CONFIG_PROVIDER.add(new Pair<>("hide_nametags", PerspectiveConfigDataLoader.HIDE_NAMETAGS));
        CONFIG_PROVIDER.add(new Pair<>("show_development_warning", PerspectiveConfigDataLoader.SHOW_DEVELOPMENT_WARNING));
        CONFIG_PROVIDER.add(new Pair<>("config_version", PerspectiveConfigHelper.DEFAULT_CONFIG_VERSION));
    }
    protected static void assign() {
        ZOOM_LEVEL = CONFIG.getOrDefault("zoom_level", PerspectiveConfigDataLoader.ZOOM_LEVEL);
        CHANGE_ZOOM_MULTIPLIER = CONFIG.getOrDefault("change_zoom_multiplier", PerspectiveConfigDataLoader.CHANGE_ZOOM_MULTIPLIER);
        HIDE_HUD = CONFIG.getOrDefault("hide_hud", PerspectiveConfigDataLoader.HIDE_HUD);
        SMOOTH_ZOOM = CONFIG.getOrDefault("smooth_zoom", PerspectiveConfigDataLoader.SMOOTH_ZOOM);
        SUPER_SECRET_SETTINGS = CONFIG.getOrDefault("super_secret_settings", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS);
        SUPER_SECRET_SETTINGS_MODE = CONFIG.getOrDefault("super_secret_settings_mode", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_MODE);
        SUPER_SECRET_SETTINGS_ENABLED = CONFIG.getOrDefault("super_secret_settings_enabled", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_ENABLED);
        SUPER_SECRET_SETTINGS_SOUND = CONFIG.getOrDefault("super_secret_settings_sound", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_SOUND);
        SUPER_SECRET_SETTINGS_OPTIONS_SCREEN = CONFIG.getOrDefault("super_secret_settings_options_screen", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_OPTIONS_SCREEN);
        NAMED_TEXTURED_ENTITY = CONFIG.getOrDefault("textured_named_entity", PerspectiveConfigDataLoader.NAMED_TEXTURED_ENTITY);
        RANDOM_TEXTURED_ENTITY = CONFIG.getOrDefault("textured_random_entity", PerspectiveConfigDataLoader.RANDOM_TEXTURED_ENTITY);
        ALLOW_APRIL_FOOLS = CONFIG.getOrDefault("allow_april_fools", PerspectiveConfigDataLoader.ALLOW_APRIL_FOOLS);
        FORCE_APRIL_FOOLS = CONFIG.getOrDefault("force_april_fools", PerspectiveConfigDataLoader.FORCE_APRIL_FOOLS);
        FORCE_PRIDE = CONFIG.getOrDefault("force_pride", PerspectiveConfigDataLoader.FORCE_PRIDE);
        FORCE_PRIDE_TYPE = CONFIG.getOrDefault("force_pride_type", PerspectiveConfigDataLoader.FORCE_PRIDE_TYPE);
        FORCE_PRIDE_TYPE_INDEX = CONFIG.getOrDefault("force_pride_type_index", PerspectiveConfigDataLoader.FORCE_PRIDE_TYPE_INDEX);
        VERSION_OVERLAY = CONFIG.getOrDefault("version_overlay", PerspectiveConfigDataLoader.VERSION_OVERLAY);
        HIDE_ARMOR = CONFIG.getOrDefault("hide_armor", PerspectiveConfigDataLoader.HIDE_ARMOR);
        HIDE_NAMETAGS = CONFIG.getOrDefault("hide_nametags", PerspectiveConfigDataLoader.HIDE_NAMETAGS);
        SHOW_DEVELOPMENT_WARNING = CONFIG.getOrDefault("show_development_warning", PerspectiveConfigDataLoader.SHOW_DEVELOPMENT_WARNING);
        CONFIG_VERSION = CONFIG.getOrDefault("config_version", PerspectiveConfigHelper.DEFAULT_CONFIG_VERSION);
    }
    protected static void save() {
        CONFIG_PROVIDER.setConfig("zoom_level", ZOOM_LEVEL);
        CONFIG_PROVIDER.setConfig("change_zoom_multiplier", CHANGE_ZOOM_MULTIPLIER);
        CONFIG_PROVIDER.setConfig("smooth_zoom", SMOOTH_ZOOM);
        CONFIG_PROVIDER.setConfig("hide_hud", HIDE_HUD);
        CONFIG_PROVIDER.setConfig("super_secret_settings", SUPER_SECRET_SETTINGS);
        CONFIG_PROVIDER.setConfig("super_secret_settings_mode", SUPER_SECRET_SETTINGS_MODE);
        CONFIG_PROVIDER.setConfig("super_secret_settings_enabled", SUPER_SECRET_SETTINGS_ENABLED);
        CONFIG_PROVIDER.setConfig("super_secret_settings_sound", SUPER_SECRET_SETTINGS_SOUND);
        CONFIG_PROVIDER.setConfig("super_secret_settings_options_screen", SUPER_SECRET_SETTINGS_OPTIONS_SCREEN);
        CONFIG_PROVIDER.setConfig("named_textured_entity", NAMED_TEXTURED_ENTITY);
        CONFIG_PROVIDER.setConfig("random_textured_entity", RANDOM_TEXTURED_ENTITY);
        CONFIG_PROVIDER.setConfig("allow_april_fools", ALLOW_APRIL_FOOLS);
        CONFIG_PROVIDER.setConfig("force_april_fools", FORCE_APRIL_FOOLS);
        CONFIG_PROVIDER.setConfig("force_pride", FORCE_PRIDE);
        CONFIG_PROVIDER.setConfig("force_pride_type", FORCE_PRIDE_TYPE);
        CONFIG_PROVIDER.setConfig("force_pride_type_index", FORCE_PRIDE_TYPE_INDEX);
        CONFIG_PROVIDER.setConfig("version_overlay", VERSION_OVERLAY);
        CONFIG_PROVIDER.setConfig("hide_armor", HIDE_ARMOR);
        CONFIG_PROVIDER.setConfig("hide_nametags", HIDE_NAMETAGS);
        CONFIG_PROVIDER.setConfig("show_development_warning", SHOW_DEVELOPMENT_WARNING);
        CONFIG_PROVIDER.setConfig("config_version", PerspectiveConfigHelper.DEFAULT_CONFIG_VERSION);
    }
}