/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import com.mclegoman.perspective.client.config.screen.PerspectiveConfigScreen;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;

public class PerspectiveConfigHelper {
    protected static boolean SAVE_VIA_TICK;
    protected static int SAVE_VIA_TICK_TICKS;
    protected static final int SAVE_VIA_TICK_SAVE_TICK = 20;
    protected static final int DEFAULT_CONFIG_VERSION = 3;
    public static void init() {
        try {
            ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveConfigDataLoader());
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to initialize config: {}", (Object)error);
        }
    }
    public static void tick(MinecraftClient client) {
        try {
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
            if ((int)getConfig("config_version") != DEFAULT_CONFIG_VERSION) {
                PerspectiveData.LOGGER.info(PerspectiveData.PREFIX + "Attempting to update config to the latest version.");
                if ((int)getConfig("config_version") < 3) {
                    setConfig("zoom_level", 100 - (int)getConfig("zoom_level"));
                }
                setConfig("config_version", DEFAULT_CONFIG_VERSION);
                PerspectiveData.LOGGER.info(PerspectiveData.PREFIX + "Successfully updated config to the latest version.");
            }
            saveConfig(false);
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to update config: {}", (Object)error);
        }
    }
    public static void saveConfig(boolean onTick) {
        if (onTick) {
            SAVE_VIA_TICK = true;
        } else {
            PerspectiveData.LOGGER.info(PerspectiveData.PREFIX + "Writing config to file.");
            PerspectiveConfig.save();
            PerspectiveConfig.CONFIG_PROVIDER.saveConfig(PerspectiveConfig.ID);
            PerspectiveExperimentalConfig.save();
            PerspectiveExperimentalConfig.CONFIG_PROVIDER.saveConfig(PerspectiveExperimentalConfig.ID);
        }
    }
    public static void resetConfig() {
        try {
            setConfig("zoom_level", PerspectiveConfigDataLoader.ZOOM_LEVEL);
            setConfig("hide_hud", PerspectiveConfigDataLoader.HIDE_HUD);
            setConfig("super_secret_settings", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS);
            setConfig("super_secret_settings_mode", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_MODE);
            setConfig("super_secret_settings_enabled", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_ENABLED);
            setConfig("named_textured_entity", PerspectiveConfigDataLoader.NAMED_TEXTURED_ENTITY);
            setConfig("random_textured_entity", PerspectiveConfigDataLoader.RANDOM_TEXTURED_ENTITY);
            setConfig("allow_april_fools", PerspectiveConfigDataLoader.ALLOW_APRIL_FOOLS);
            setConfig("force_april_fools", PerspectiveConfigDataLoader.FORCE_APRIL_FOOLS);
            setConfig("force_pride", PerspectiveConfigDataLoader.FORCE_PRIDE);
            setConfig("show_development_warning", PerspectiveConfigDataLoader.SHOW_DEVELOPMENT_WARNING);
            setConfig("hide_armor", PerspectiveConfigDataLoader.HIDE_ARMOR);
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to reset config: {}", (Object)error);
        }

    }
    public static void setConfig(String ID, Object VALUE) {
        try {
            switch (ID) {
                case "zoom_level" -> PerspectiveConfig.ZOOM_LEVEL = (int)VALUE;
                case "hide_hud" -> PerspectiveConfig.HIDE_HUD = (boolean)VALUE;
                case "super_secret_settings" -> PerspectiveConfig.SUPER_SECRET_SETTINGS = (int)VALUE;
                case "super_secret_settings_mode" -> PerspectiveConfig.SUPER_SECRET_SETTINGS_MODE = (boolean)VALUE;
                case "super_secret_settings_enabled" -> PerspectiveConfig.SUPER_SECRET_SETTINGS_ENABLED = (boolean)VALUE;
                case "named_textured_entity" -> PerspectiveConfig.NAMED_TEXTURED_ENTITY = (boolean)VALUE;
                case "random_textured_entity" -> PerspectiveConfig.RANDOM_TEXTURED_ENTITY = (boolean)VALUE;
                case "allow_april_fools" -> PerspectiveConfig.ALLOW_APRIL_FOOLS = (boolean)VALUE;
                case "force_april_fools" -> PerspectiveConfig.FORCE_APRIL_FOOLS = (boolean)VALUE;
                case "force_pride" -> PerspectiveConfig.FORCE_PRIDE = (boolean)VALUE;
                case "show_development_warning" -> PerspectiveConfig.SHOW_DEVELOPMENT_WARNING = (boolean)VALUE;
                case "config_version" -> PerspectiveConfig.CONFIG_VERSION = (int)VALUE;
                case "hide_armor" -> PerspectiveExperimentalConfig.HIDE_ARMOR = (boolean)VALUE;
            }
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to set {} config value: {}", ID, error);
        }
    }
    public static Object getConfig(String ID) {
        switch (ID) {
            case "zoom_level" -> {return PerspectiveConfig.ZOOM_LEVEL;}
            case "hide_hud" -> {return PerspectiveConfig.HIDE_HUD;}
            case "super_secret_settings" -> {return PerspectiveConfig.SUPER_SECRET_SETTINGS;}
            case "super_secret_settings_mode" -> {return PerspectiveConfig.SUPER_SECRET_SETTINGS_MODE;}
            case "super_secret_settings_enabled" -> {return PerspectiveConfig.SUPER_SECRET_SETTINGS_ENABLED;}
            case "named_textured_entity" -> {return PerspectiveConfig.NAMED_TEXTURED_ENTITY;}
            case "random_textured_entity" -> {return PerspectiveConfig.RANDOM_TEXTURED_ENTITY;}
            case "allow_april_fools" -> {return PerspectiveConfig.ALLOW_APRIL_FOOLS;}
            case "force_april_fools" -> {return PerspectiveConfig.FORCE_APRIL_FOOLS;}
            case "force_pride" -> {return PerspectiveConfig.FORCE_PRIDE;}
            case "show_development_warning" -> {return PerspectiveConfig.SHOW_DEVELOPMENT_WARNING;}
            case "config_version" -> {return PerspectiveConfig.CONFIG_VERSION;}
            case "hide_armor" -> {return PerspectiveExperimentalConfig.HIDE_ARMOR;}
        }
        return new Object();
    }
}