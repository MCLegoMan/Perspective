/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.ConfigScreen;
import com.mclegoman.perspective.client.shaders.ShaderDataLoader;
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;

public class ConfigHelper {
    protected static boolean SAVE_VIA_TICK = false;
    protected static int SAVE_VIA_TICK_TICKS = 0;
    protected static final int SAVE_VIA_TICK_SAVE_TICK = 20;
    protected static final int DEFAULT_CONFIG_VERSION = 10;
    private static boolean SEEN_DEVELOPMENT_WARNING = false;
    private static boolean SHOW_DOWNGRADE_WARNING = false;
    private static boolean SEEN_DOWNGRADE_WARNING = false;
    private static boolean SHOW_LICENSE_UPDATE_NOTICE = false;
    private static boolean SEEN_LICENSE_UPDATE_NOTICE = false;
    public static boolean EXPERIMENTS_AVAILABLE = false;
    public static void init() {
        try {
            ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ConfigDataLoader());
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize config: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    protected static void loadConfig() {
        try {
            Config.init();
            ExperimentalConfig.init();
            TutorialsConfig.init();
            WarningsConfig.init();
            ConfigHelper.updateConfig();
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to load configs: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    public static void tick(MinecraftClient client) {
        try {
            if (Keybindings.OPEN_CONFIG.wasPressed()) client.setScreen(new ConfigScreen(client.currentScreen, false));
            if (SAVE_VIA_TICK_TICKS < SAVE_VIA_TICK_SAVE_TICK) SAVE_VIA_TICK_TICKS += 1;
            else {
                if (SAVE_VIA_TICK) {
                    saveConfig(false);
                    SAVE_VIA_TICK = false;
                }
                SAVE_VIA_TICK_TICKS = 0;
            }
            showToasts(client);
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to tick config: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    private static void showToasts(MinecraftClient client) {
        if (Data.PERSPECTIVE_VERSION.isDevelopmentBuild() && !SEEN_DEVELOPMENT_WARNING) {
            Data.PERSPECTIVE_VERSION.getLogger().info("{} Development Build. Please help us improve by submitting bug reports if you encounter any issues.", Data.PERSPECTIVE_VERSION.getName());
            client.getToastManager().add(new Toast(Translation.getTranslation("toasts.title", new Object[]{Translation.getTranslation("name"), Translation.getTranslation("toasts.development_warning.title")}), Translation.getTranslation("toasts.development_warning.description"), 320, Toast.Type.WARNING));
            SEEN_DEVELOPMENT_WARNING = true;
        }
        if (SHOW_DOWNGRADE_WARNING && !SEEN_DOWNGRADE_WARNING) {
            Data.PERSPECTIVE_VERSION.getLogger().info("{} Downgrading is not supported. You may experience configuration related issues.", Data.PERSPECTIVE_VERSION.getName());
            client.getToastManager().add(new Toast(Translation.getTranslation("toasts.title", new Object[]{Translation.getTranslation("name"), Translation.getTranslation("toasts.downgrade_warning.title")}), Translation.getTranslation("toasts.downgrade_warning.description"), 320, Toast.Type.WARNING));
            SEEN_DOWNGRADE_WARNING = true;
        }
        if (SHOW_LICENSE_UPDATE_NOTICE && !SEEN_LICENSE_UPDATE_NOTICE) {
            Data.PERSPECTIVE_VERSION.getLogger().info("{} License Update. Perspective is now licensed under LGPL-3.0-or-later.", Data.PERSPECTIVE_VERSION.getName());
            client.getToastManager().add(new Toast(Translation.getTranslation("toasts.title", new Object[]{Translation.getTranslation("name"), Translation.getTranslation("toasts.license_update.title")}), Translation.getTranslation("toasts.license_update.description"), 320, Toast.Type.INFO));
            SEEN_LICENSE_UPDATE_NOTICE = true;
        }
    }
    protected static void updateConfig() {
        try {
            if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) != DEFAULT_CONFIG_VERSION) {
                if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < DEFAULT_CONFIG_VERSION) {
                    Data.PERSPECTIVE_VERSION.getLogger().info("{} Attempting to update config to the latest version.", Data.PERSPECTIVE_VERSION.getLoggerPrefix());
                    if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 3) {
                        setConfig("zoom_level", 100 - (int)getConfig("zoom_level"));
                    }
                    if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 7) {
                        String SUPER_SECRET_SETTINGS_MODE = Config.CONFIG.getOrDefault("super_secret_settings_mode", ConfigDataLoader.SUPER_SECRET_SETTINGS_MODE);
                        if (SUPER_SECRET_SETTINGS_MODE.equals("false")) setConfig("super_secret_settings_mode", "game");
                        else if (SUPER_SECRET_SETTINGS_MODE.equals("true")) setConfig("super_secret_settings_mode", "screen");
                        else setConfig("super_secret_settings_mode", "game");
                        Boolean HIDE_HUD = Config.CONFIG.getOrDefault("hide_hud", true);
                        setConfig("zoom_hide_hud", HIDE_HUD);
                        setConfig("hold_perspective_hide_hud", HIDE_HUD);
                    }
                    if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 8) {
                        SHOW_LICENSE_UPDATE_NOTICE = true;
                    }
                    setConfig("config_version", DEFAULT_CONFIG_VERSION);
                    Data.PERSPECTIVE_VERSION.getLogger().info("{} Successfully updated config to the latest version.", Data.PERSPECTIVE_VERSION.getLoggerPrefix());
                } else if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) > DEFAULT_CONFIG_VERSION) {
                    if (!SEEN_DOWNGRADE_WARNING) {
                        Data.PERSPECTIVE_VERSION.getLogger().warn("{} Downgrading is not supported. You may experience configuration related issues.", Data.PERSPECTIVE_VERSION.getLoggerPrefix());
                        SHOW_DOWNGRADE_WARNING = true;
                    }
                }
            }
            saveConfig(false);
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to update config: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    public static void saveConfig(boolean onTick) {
        try {
            if (onTick) {
                SAVE_VIA_TICK = true;
            } else {
                Config.save();
                ExperimentalConfig.save();
                TutorialsConfig.save();
                WarningsConfig.save();
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to save config: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    public static void resetConfig() {
        try {
            // Main Config
            setConfig("zoom_level", Math.min(Math.max(ConfigDataLoader.ZOOM_LEVEL, -50), 100));
            setConfig("zoom_increment_size", Math.max(Math.min(ConfigDataLoader.ZOOM_INCREMENT_SIZE, 10), 1));
            setConfig("zoom_mode", ConfigDataLoader.ZOOM_MODE);
            setConfig("zoom_hide_hud", ConfigDataLoader.ZOOM_HIDE_HUD);
            setConfig("zoom_overlay_message", ConfigDataLoader.ZOOM_OVERLAY_MESSAGE);
            setConfig("hold_perspective_hide_hud", ConfigDataLoader.HOLD_PERSPECTIVE_HIDE_HUD);
            setConfig("super_secret_settings", Math.max(Math.min(ConfigDataLoader.SUPER_SECRET_SETTINGS, ShaderDataLoader.getShaderAmount()), 0));
            setConfig("super_secret_settings_mode", ConfigDataLoader.SUPER_SECRET_SETTINGS_MODE);
            setConfig("super_secret_settings_enabled", ConfigDataLoader.SUPER_SECRET_SETTINGS_ENABLED);
            setConfig("super_secret_settings_sound", ConfigDataLoader.SUPER_SECRET_SETTINGS_SOUND);
            setConfig("super_secret_settings_options_screen", ConfigDataLoader.SUPER_SECRET_SETTINGS_OPTIONS_SCREEN);
            setConfig("super_secret_settings_overlay_message", ConfigDataLoader.SUPER_SECRET_SETTINGS_OVERLAY_MESSAGE);
            setConfig("named_textured_entity", ConfigDataLoader.NAMED_TEXTURED_ENTITY);
            setConfig("random_textured_entity", ConfigDataLoader.RANDOM_TEXTURED_ENTITY);
            setConfig("allow_april_fools", ConfigDataLoader.ALLOW_APRIL_FOOLS);
            setConfig("force_april_fools", ConfigDataLoader.FORCE_APRIL_FOOLS);
            setConfig("force_pride", ConfigDataLoader.FORCE_PRIDE);
            setConfig("force_pride_type", ConfigDataLoader.FORCE_PRIDE_TYPE);
            setConfig("force_pride_type_index", Math.max(Math.min(ConfigDataLoader.FORCE_PRIDE_TYPE_INDEX, 0), ClientData.PRIDE_LOGOS.length - 1));
            setConfig("version_overlay", ConfigDataLoader.VERSION_OVERLAY);
            setConfig("hide_armor", ConfigDataLoader.HIDE_ARMOR);
            setConfig("hide_nametags", ConfigDataLoader.HIDE_NAMETAGS);
            setConfig("detect_update_channel", ConfigDataLoader.DETECT_UPDATE_CHANNEL);
            setConfig("tutorials", ConfigDataLoader.TUTORIALS);
            setConfig("hide_block_outline", ConfigDataLoader.HIDE_BLOCK_OUTLINE);
            setConfig("hide_crosshair", ConfigDataLoader.HIDE_CROSSHAIR);
            setConfig("show_death_coordinates", ConfigDataLoader.SHOW_DEATH_COORDINATES);
            // Experimental Config
            if (EXPERIMENTS_AVAILABLE) {
                // There is currently no experiments available.
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to reset main and experimental config values: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    public static void setConfig(String ID, Object VALUE) {
        try {
            switch (ID) {
                case "zoom_level" -> Config.ZOOM_LEVEL = Math.min(Math.max((int)VALUE, -50), 100);
                case "zoom_increment_size" -> Config.ZOOM_INCREMENT_SIZE = Math.max(Math.min((int)VALUE, 10), 1);
                case "zoom_mode" -> Config.ZOOM_MODE = (String)VALUE;
                case "zoom_hide_hud" -> Config.ZOOM_HIDE_HUD = (boolean)VALUE;
                case "zoom_overlay_message" -> Config.ZOOM_OVERLAY_MESSAGE = (boolean)VALUE;
                case "hold_perspective_hide_hud" -> Config.HOLD_PERSPECTIVE_HIDE_HUD = (boolean)VALUE;
                case "super_secret_settings" -> Config.SUPER_SECRET_SETTINGS = Math.max(Math.min((int)VALUE, ShaderDataLoader.getShaderAmount()), 0);
                case "super_secret_settings_mode" -> Config.SUPER_SECRET_SETTINGS_MODE = (String)VALUE;
                case "super_secret_settings_enabled" -> Config.SUPER_SECRET_SETTINGS_ENABLED = (boolean)VALUE;
                case "super_secret_settings_sound" -> Config.SUPER_SECRET_SETTINGS_SOUND = (boolean)VALUE;
                case "super_secret_settings_options_screen" -> Config.SUPER_SECRET_SETTINGS_OPTIONS_SCREEN = (boolean)VALUE;
                case "super_secret_settings_overlay_message" -> Config.SUPER_SECRET_SETTINGS_OVERLAY_MESSAGE = (boolean)VALUE;
                case "named_textured_entity" -> Config.NAMED_TEXTURED_ENTITY = (boolean)VALUE;
                case "random_textured_entity" -> Config.RANDOM_TEXTURED_ENTITY = (boolean)VALUE;
                case "allow_april_fools" -> Config.ALLOW_APRIL_FOOLS = (boolean)VALUE;
                case "force_april_fools" -> Config.FORCE_APRIL_FOOLS = (boolean)VALUE;
                case "force_pride" -> Config.FORCE_PRIDE = (boolean)VALUE;
                case "force_pride_type" -> Config.FORCE_PRIDE_TYPE = (boolean)VALUE;
                case "force_pride_type_index" -> Config.FORCE_PRIDE_TYPE_INDEX = Math.max(Math.min((int)VALUE, 0), ClientData.PRIDE_LOGOS.length - 1);
                case "version_overlay" -> Config.VERSION_OVERLAY = (boolean)VALUE;
                case "hide_armor" -> Config.HIDE_ARMOR = (boolean)VALUE;
                case "hide_nametags" -> Config.HIDE_NAMETAGS = (boolean)VALUE;
                case "detect_update_channel" -> Config.DETECT_UPDATE_CHANNEL = (String)VALUE;
                case "tutorials" -> Config.TUTORIALS = (boolean) VALUE;
                case "hide_block_outline" -> Config.HIDE_BLOCK_OUTLINE = (boolean) VALUE;
                case "hide_crosshair" -> Config.HIDE_CROSSHAIR = (boolean) VALUE;
                case "show_death_coordinates" -> Config.SHOW_DEATH_COORDINATES = (boolean) VALUE;
                case "config_version" -> Config.CONFIG_VERSION = (int)VALUE;
                default -> Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to set {} config value: Invalid Key", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), ID);
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to set {} config value: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), ID, error);
        }
    }
    public static void setExperimentalConfig(String ID, Object VALUE) {
        try {
            // There is currently no experiments available.
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to set {} experimental config value: Invalid Key", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), ID);
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to set {} experimental config value: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), ID, error);
        }
    }
    public static void setTutorialConfig(String ID, Object VALUE) {
        try {
            if (ID.equals("super_secret_settings")) {
                TutorialsConfig.SUPER_SECRET_SETTINGS = (Boolean) VALUE;
            } else {
                Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to set {} tutorial config value: Invalid Key", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), ID);
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to set {} tutorial config value: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), ID, error);
        }
    }
    public static void setWarningConfig(String ID, Object VALUE) {
        try {
            switch (ID) {
                case "photosensitivity" -> WarningsConfig.PHOTOSENSITIVITY = (boolean) VALUE;
                case "prank" -> WarningsConfig.PRANK = (boolean) VALUE;
                default -> Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to set {} warning config value: Invalid Key", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), ID);
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to set {} warning config value: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), ID, error);
        }
    }
    public static Object getConfig(String ID) {
        switch (ID) {
            case "zoom_level" -> {return Math.min(Math.max(Config.ZOOM_LEVEL, -50), 100);}
            case "zoom_increment_size" -> {return Math.max(Math.min(Config.ZOOM_INCREMENT_SIZE, 10), 1);}
            case "zoom_mode" -> {return Config.ZOOM_MODE;}
            case "zoom_hide_hud" -> {return Config.ZOOM_HIDE_HUD;}
            case "zoom_overlay_message" -> {return Config.ZOOM_OVERLAY_MESSAGE;}
            case "hold_perspective_hide_hud" -> {return Config.HOLD_PERSPECTIVE_HIDE_HUD;}
            case "super_secret_settings" -> {return Math.max(Math.min(Config.SUPER_SECRET_SETTINGS, ShaderDataLoader.getShaderAmount()), 0);}
            case "super_secret_settings_mode" -> {return Config.SUPER_SECRET_SETTINGS_MODE;}
            case "super_secret_settings_enabled" -> {return Config.SUPER_SECRET_SETTINGS_ENABLED;}
            case "super_secret_settings_sound" -> {return Config.SUPER_SECRET_SETTINGS_SOUND;}
            case "super_secret_settings_options_screen" -> {return Config.SUPER_SECRET_SETTINGS_OPTIONS_SCREEN;}
            case "super_secret_settings_overlay_message" -> {return Config.SUPER_SECRET_SETTINGS_OVERLAY_MESSAGE;}
            case "named_textured_entity" -> {return Config.NAMED_TEXTURED_ENTITY;}
            case "random_textured_entity" -> {return Config.RANDOM_TEXTURED_ENTITY;}
            case "allow_april_fools" -> {return Config.ALLOW_APRIL_FOOLS;}
            case "force_april_fools" -> {return Config.FORCE_APRIL_FOOLS;}
            case "force_pride" -> {return Config.FORCE_PRIDE;}
            case "force_pride_type" -> {return Config.FORCE_PRIDE_TYPE;}
            case "force_pride_type_index" -> {return Math.max(Math.min(Config.FORCE_PRIDE_TYPE_INDEX, 0), ClientData.PRIDE_LOGOS.length - 1);}
            case "version_overlay" -> {return Config.VERSION_OVERLAY;}
            case "hide_armor" -> {return Config.HIDE_ARMOR;}
            case "hide_nametags" -> {return Config.HIDE_NAMETAGS;}
            case "detect_update_channel" -> {return Config.DETECT_UPDATE_CHANNEL;}
            case "tutorials" -> {return Config.TUTORIALS;}
            case "hide_block_outline" -> {return Config.HIDE_BLOCK_OUTLINE;}
            case "hide_crosshair" -> {return Config.HIDE_CROSSHAIR;}
            case "show_death_coordinates" -> {return Config.SHOW_DEATH_COORDINATES;}
            case "config_version" -> {return Config.CONFIG_VERSION;}
            default -> {
                Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to get {} config value: Invalid Key", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), ID);
                return new Object();
            }
        }
    }
    public static Object getExperimentalConfig(String ID) {
        // There is currently no experiments available.
        Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to get {} experimental config value: Invalid Key", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), ID);
        return new Object();
    }
    public static Object getTutorialConfig(String ID) {
        if (ID.equals("super_secret_settings")) {
            return TutorialsConfig.SUPER_SECRET_SETTINGS;
        } else {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to get {} tutorial config value: Invalid Key", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), ID);
        }
        return new Object();
    }
    public static Object getWarningConfig(String ID) {
        switch (ID) {
            case "photosensitivity" -> {return WarningsConfig.PHOTOSENSITIVITY;}
            case "prank" -> {return WarningsConfig.PRANK;}
            default -> {
                Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to get {} warning config value: Invalid Key", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), ID);
                return new Object();
            }
        }
    }
}