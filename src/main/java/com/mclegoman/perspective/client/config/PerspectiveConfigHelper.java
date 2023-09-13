/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.screen.config.PerspectiveConfigScreen;
import com.mclegoman.perspective.client.screen.developmentwarning.PerspectiveDevelopmentWarningScreen;
import com.mclegoman.perspective.client.screen.downgradewarning.PerspectiveDowngradeWarningScreen;
import com.mclegoman.perspective.client.shaders.PerspectiveShaderDataLoader;
import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.resource.ResourceType;

public class PerspectiveConfigHelper {
    protected static boolean SAVE_VIA_TICK;
    protected static int SAVE_VIA_TICK_TICKS;
    protected static final int SAVE_VIA_TICK_SAVE_TICK = 20;
    protected static final int DEFAULT_CONFIG_VERSION = 7;
    private static boolean DEV_WARN;
    private static boolean DG_WARN;
    public static boolean EXPERIMENTS_AVAILABLE = false;
    public static void init() {
        try {
            ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveConfigDataLoader());
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to initialize config: {}", (Object)error);
        }
    }
    protected static void loadConfig() {
        try {
            PerspectiveConfig.init();
            PerspectiveExperimentalConfig.init();
            PerspectiveConfigHelper.updateConfig();
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to load configs: {}", (Object)error);
        }
    }
    public static void tick(MinecraftClient client) {
        try {
            if (DEV_WARN) {
                if ((boolean)PerspectiveConfigHelper.getConfig("show_development_warning") && PerspectiveClientData.CLIENT.currentScreen instanceof TitleScreen) {
                    PerspectiveClientData.CLIENT.setScreen(new PerspectiveDevelopmentWarningScreen(PerspectiveClientData.CLIENT.currentScreen, 200, true));
                    DEV_WARN = false;
                }
            }
            if (DG_WARN) {
                if (PerspectiveClientData.CLIENT.currentScreen instanceof TitleScreen) {
                    PerspectiveClientData.CLIENT.setScreen(new PerspectiveDowngradeWarningScreen(PerspectiveClientData.CLIENT.currentScreen, 200));
                    DG_WARN = false;
                }
            }
            if (PerspectiveKeybindings.OPEN_CONFIG.wasPressed()) client.setScreen(new PerspectiveConfigScreen(client.currentScreen, false));
            if (SAVE_VIA_TICK_TICKS < SAVE_VIA_TICK_SAVE_TICK) SAVE_VIA_TICK_TICKS += 1;
            else {
                if (SAVE_VIA_TICK) {
                    saveConfig(false);
                    SAVE_VIA_TICK = false;
                }
                SAVE_VIA_TICK_TICKS = 0;
            }
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to tick config: {}", (Object)error);
        }
    }
    protected static void updateConfig() {
        try {
            if (PerspectiveConfig.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) != DEFAULT_CONFIG_VERSION) {
                if (PerspectiveConfig.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < DEFAULT_CONFIG_VERSION) {
                    PerspectiveData.LOGGER.info(PerspectiveData.PREFIX + "Attempting to update config to the latest version.");
                    if (PerspectiveConfig.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 3) {
                        setConfig("zoom_level", 100 - (int)getConfig("zoom_level"));
                    }
                    if (PerspectiveConfig.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 7) {
                        String SUPER_SECRET_SETTINGS_MODE = PerspectiveConfig.CONFIG.getOrDefault("super_secret_settings_mode", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_MODE);
                        if (SUPER_SECRET_SETTINGS_MODE.equals("false")) setConfig("super_secret_settings_mode", "game");
                        else if (SUPER_SECRET_SETTINGS_MODE.equals("true")) setConfig("super_secret_settings_mode", "screen");
                        else setConfig("super_secret_settings_mode", "game");
                        Boolean HIDE_HUD = PerspectiveConfig.CONFIG.getOrDefault("hide_hud", true);
                        setConfig("zoom_hide_hud", HIDE_HUD);
                        setConfig("hold_perspective_hide_hud", HIDE_HUD);
                    }
                    setConfig("config_version", DEFAULT_CONFIG_VERSION);
                    PerspectiveData.LOGGER.info(PerspectiveData.PREFIX + "Successfully updated config to the latest version.");
                } else if (PerspectiveConfig.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) > DEFAULT_CONFIG_VERSION) {
                    PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Downgrading Perspective is not supported. You may experience some unexpected bugs and/or issues.");
                    DG_WARN = true;
                }
            }
            if (PerspectiveData.IS_DEVELOPMENT) {
                PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "You are running a development build of Perspective. You may experience some unexpected bugs and/or issues.");
                DEV_WARN = true;
            }
            saveConfig(false);
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to update config: {}", (Object)error);
        }
    }
    public static void saveConfig(boolean onTick) {
        try {
            if (onTick) {
                SAVE_VIA_TICK = true;
            } else {
                PerspectiveData.LOGGER.info(PerspectiveData.PREFIX + "Writing config to file.");
                PerspectiveConfig.save();
                PerspectiveConfig.CONFIG_PROVIDER.saveConfig(PerspectiveConfig.ID);
                PerspectiveExperimentalConfig.save();
                PerspectiveExperimentalConfig.CONFIG_PROVIDER.saveConfig(PerspectiveExperimentalConfig.ID);
            }
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to save config: {}", (Object)error);
        }
    }
    public static void resetConfig() {
        try {
            setConfig("zoom_level", Math.min(Math.max(PerspectiveConfigDataLoader.ZOOM_LEVEL, -50), 100));
            setConfig("zoom_increment_size", Math.max(Math.min(PerspectiveConfigDataLoader.ZOOM_INCREMENT_SIZE, 10), 1));
            setConfig("zoom_mode", PerspectiveConfigDataLoader.ZOOM_MODE);
            setConfig("zoom_smooth_scale", PerspectiveConfigDataLoader.ZOOM_SMOOTH_SCALE);
            setConfig("zoom_hide_hud", PerspectiveConfigDataLoader.ZOOM_HIDE_HUD);
            setConfig("zoom_overlay_message", PerspectiveConfigDataLoader.ZOOM_OVERLAY_MESSAGE);
            setConfig("hold_perspective_hide_hud", PerspectiveConfigDataLoader.HOLD_PERSPECTIVE_HIDE_HUD);
            setConfig("super_secret_settings", Math.max(Math.min(PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS, PerspectiveShaderDataLoader.getShaderAmount()), 0));
            setConfig("super_secret_settings_mode", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_MODE);
            setConfig("super_secret_settings_enabled", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_ENABLED);
            setConfig("super_secret_settings_sound", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_SOUND);
            setConfig("super_secret_settings_options_screen", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_OPTIONS_SCREEN);
            setConfig("named_textured_entity", PerspectiveConfigDataLoader.NAMED_TEXTURED_ENTITY);
            setConfig("random_textured_entity", PerspectiveConfigDataLoader.RANDOM_TEXTURED_ENTITY);
            setConfig("allow_april_fools", PerspectiveConfigDataLoader.ALLOW_APRIL_FOOLS);
            setConfig("force_april_fools", PerspectiveConfigDataLoader.FORCE_APRIL_FOOLS);
            setConfig("force_pride", PerspectiveConfigDataLoader.FORCE_PRIDE);
            setConfig("force_pride_type", PerspectiveConfigDataLoader.FORCE_PRIDE_TYPE);
            setConfig("force_pride_type_index", Math.max(Math.min(PerspectiveConfigDataLoader.FORCE_PRIDE_TYPE_INDEX, 0), PerspectiveClientData.PRIDE_LOGOS.length - 1));
            setConfig("version_overlay", PerspectiveConfigDataLoader.VERSION_OVERLAY);
            setConfig("hide_armor", PerspectiveConfigDataLoader.HIDE_ARMOR);
            setConfig("hide_nametags", PerspectiveConfigDataLoader.HIDE_NAMETAGS);
            setConfig("show_development_warning", PerspectiveConfigDataLoader.SHOW_DEVELOPMENT_WARNING);
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to reset config: {}", (Object)error);
        }

    }
    public static void setConfig(String ID, Object VALUE) {
        try {
            switch (ID) {
                case "zoom_level" -> PerspectiveConfig.ZOOM_LEVEL = Math.min(Math.max((int)VALUE, -50), 100);
                case "zoom_increment_size" -> PerspectiveConfig.ZOOM_INCREMENT_SIZE = Math.max(Math.min((int)VALUE, 10), 1);
                case "zoom_mode" -> PerspectiveConfig.ZOOM_MODE = (String)VALUE;
                case "zoom_smooth_scale" -> PerspectiveConfig.ZOOM_SMOOTH_SCALE = Math.max((float)VALUE, 0.0F);
                case "zoom_hide_hud" -> PerspectiveConfig.ZOOM_HIDE_HUD = (boolean)VALUE;
                case "zoom_overlay_message" -> PerspectiveConfig.ZOOM_OVERLAY_MESSAGE = (boolean)VALUE;
                case "hold_perspective_hide_hud" -> PerspectiveConfig.HOLD_PERSPECTIVE_HIDE_HUD = (boolean)VALUE;
                case "super_secret_settings" -> PerspectiveConfig.SUPER_SECRET_SETTINGS = Math.max(Math.min((int)VALUE, PerspectiveShaderDataLoader.getShaderAmount()), 0);
                case "super_secret_settings_mode" -> PerspectiveConfig.SUPER_SECRET_SETTINGS_MODE = (String)VALUE;
                case "super_secret_settings_enabled" -> PerspectiveConfig.SUPER_SECRET_SETTINGS_ENABLED = (boolean)VALUE;
                case "super_secret_settings_sound" -> PerspectiveConfig.SUPER_SECRET_SETTINGS_SOUND = (boolean)VALUE;
                case "super_secret_settings_options_screen" -> PerspectiveConfig.SUPER_SECRET_SETTINGS_OPTIONS_SCREEN = (boolean)VALUE;
                case "named_textured_entity" -> PerspectiveConfig.NAMED_TEXTURED_ENTITY = (boolean)VALUE;
                case "random_textured_entity" -> PerspectiveConfig.RANDOM_TEXTURED_ENTITY = (boolean)VALUE;
                case "allow_april_fools" -> PerspectiveConfig.ALLOW_APRIL_FOOLS = (boolean)VALUE;
                case "force_april_fools" -> PerspectiveConfig.FORCE_APRIL_FOOLS = (boolean)VALUE;
                case "force_pride" -> PerspectiveConfig.FORCE_PRIDE = (boolean)VALUE;
                case "force_pride_type" -> PerspectiveConfig.FORCE_PRIDE_TYPE = (boolean)VALUE;
                case "force_pride_type_index" -> PerspectiveConfig.FORCE_PRIDE_TYPE_INDEX = Math.max(Math.min((int)VALUE, 0), PerspectiveClientData.PRIDE_LOGOS.length - 1);
                case "version_overlay" -> PerspectiveConfig.VERSION_OVERLAY = (boolean)VALUE;
                case "hide_armor" -> PerspectiveConfig.HIDE_ARMOR = (boolean)VALUE;
                case "hide_nametags" -> PerspectiveConfig.HIDE_NAMETAGS = (boolean)VALUE;
                case "show_development_warning" -> PerspectiveConfig.SHOW_DEVELOPMENT_WARNING = (boolean)VALUE;
                case "config_version" -> PerspectiveConfig.CONFIG_VERSION = (int)VALUE;
            }
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to set {} config value: {}", ID, error);
        }
    }
    public static Object getConfig(String ID) {
        switch (ID) {
            case "zoom_level" -> {return Math.min(Math.max(PerspectiveConfig.ZOOM_LEVEL, -50), 100);}
            case "zoom_increment_size" -> {return Math.max(Math.min(PerspectiveConfig.ZOOM_INCREMENT_SIZE, 10), 1);}
            case "zoom_mode" -> {return PerspectiveConfig.ZOOM_MODE;}
            case "zoom_smooth_scale" -> {return Math.max(PerspectiveConfig.ZOOM_SMOOTH_SCALE, 0.0F);}
            case "zoom_hide_hud" -> {return PerspectiveConfig.ZOOM_HIDE_HUD;}
            case "zoom_overlay_message" -> {return PerspectiveConfig.ZOOM_OVERLAY_MESSAGE;}
            case "hold_perspective_hide_hud" -> {return PerspectiveConfig.HOLD_PERSPECTIVE_HIDE_HUD;}
            case "super_secret_settings" -> {return Math.max(Math.min(PerspectiveConfig.SUPER_SECRET_SETTINGS, PerspectiveShaderDataLoader.getShaderAmount()), 0);}
            case "super_secret_settings_mode" -> {return PerspectiveConfig.SUPER_SECRET_SETTINGS_MODE;}
            case "super_secret_settings_enabled" -> {return PerspectiveConfig.SUPER_SECRET_SETTINGS_ENABLED;}
            case "super_secret_settings_sound" -> {return PerspectiveConfig.SUPER_SECRET_SETTINGS_SOUND;}
            case "super_secret_settings_options_screen" -> {return PerspectiveConfig.SUPER_SECRET_SETTINGS_OPTIONS_SCREEN;}
            case "named_textured_entity" -> {return PerspectiveConfig.NAMED_TEXTURED_ENTITY;}
            case "random_textured_entity" -> {return PerspectiveConfig.RANDOM_TEXTURED_ENTITY;}
            case "allow_april_fools" -> {return PerspectiveConfig.ALLOW_APRIL_FOOLS;}
            case "force_april_fools" -> {return PerspectiveConfig.FORCE_APRIL_FOOLS;}
            case "force_pride" -> {return PerspectiveConfig.FORCE_PRIDE;}
            case "force_pride_type" -> {return PerspectiveConfig.FORCE_PRIDE_TYPE;}
            case "force_pride_type_index" -> {return Math.max(Math.min(PerspectiveConfig.FORCE_PRIDE_TYPE_INDEX, 0), PerspectiveClientData.PRIDE_LOGOS.length - 1);}
            case "version_overlay" -> {return PerspectiveConfig.VERSION_OVERLAY;}
            case "hide_armor" -> {return PerspectiveConfig.HIDE_ARMOR;}
            case "hide_nametags" -> {return PerspectiveConfig.HIDE_NAMETAGS;}
            case "show_development_warning" -> {return PerspectiveConfig.SHOW_DEVELOPMENT_WARNING;}
            case "config_version" -> {return PerspectiveConfig.CONFIG_VERSION;}
            default -> {
                PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to get {} config value: Invalid Key", ID);
                return new Object();
            }
        }
    }
}