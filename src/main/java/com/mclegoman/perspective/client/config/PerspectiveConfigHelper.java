/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.screen.config.PerspectiveConfigScreen;
import com.mclegoman.perspective.client.shaders.PerspectiveShaderDataLoader;
import com.mclegoman.perspective.client.toasts.PerspectiveToast;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;

public class PerspectiveConfigHelper {
    protected static boolean SAVE_VIA_TICK = false;
    protected static int SAVE_VIA_TICK_TICKS = 0;
    protected static final int SAVE_VIA_TICK_SAVE_TICK = 20;
    protected static final int DEFAULT_CONFIG_VERSION = 9;
    private static boolean SEEN_DEVELOPMENT_WARNING = false;
    private static boolean SHOW_DOWNGRADE_WARNING = false;
    private static boolean SEEN_DOWNGRADE_WARNING = false;
    private static boolean SHOW_LICENSE_UPDATE_NOTICE = false;
    private static boolean SEEN_LICENSE_UPDATE_NOTICE = false;
    public static boolean EXPERIMENTS_AVAILABLE = false;
    public static void init() {
        try {
            ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveConfigDataLoader());
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize config: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    protected static void loadConfig() {
        try {
            PerspectiveConfig.init();
            PerspectiveExperimentalConfig.init();
            PerspectiveTutorialsConfig.init();
            PerspectiveWarningsConfig.init();
            PerspectiveConfigHelper.updateConfig();
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to load configs: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
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
            showToasts(client);
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to tick config: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    private static void showToasts(MinecraftClient client) {
        if (PerspectiveData.PERSPECTIVE_VERSION.isDevelopmentBuild() && !SEEN_DEVELOPMENT_WARNING) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().info("{} Development Build. Please help us improve by submitting bug reports if you encounter any issues.", PerspectiveData.PERSPECTIVE_VERSION.getName());
            client.getToastManager().add(new PerspectiveToast(PerspectiveTranslation.getTranslation("toasts.title", new Object[]{PerspectiveTranslation.getTranslation("name"), PerspectiveTranslation.getTranslation("toasts.development_warning.title")}), PerspectiveTranslation.getTranslation("toasts.development_warning.description"), 320, PerspectiveToast.Type.WARNING));
            SEEN_DEVELOPMENT_WARNING = true;
        }
        if (SHOW_DOWNGRADE_WARNING && !SEEN_DOWNGRADE_WARNING) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().info("{} Downgrading is not supported. You may experience configuration related issues.", PerspectiveData.PERSPECTIVE_VERSION.getName());
            client.getToastManager().add(new PerspectiveToast(PerspectiveTranslation.getTranslation("toasts.title", new Object[]{PerspectiveTranslation.getTranslation("name"), PerspectiveTranslation.getTranslation("toasts.downgrade_warning.title")}), PerspectiveTranslation.getTranslation("toasts.downgrade_warning.description"), 320, PerspectiveToast.Type.WARNING));
            SEEN_DOWNGRADE_WARNING = true;
        }
        if (SHOW_LICENSE_UPDATE_NOTICE && !SEEN_LICENSE_UPDATE_NOTICE) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().info("{} License Update. Perspective is now licensed under LGPL-3.0-or-later.", PerspectiveData.PERSPECTIVE_VERSION.getName());
            client.getToastManager().add(new PerspectiveToast(PerspectiveTranslation.getTranslation("toasts.title", new Object[]{PerspectiveTranslation.getTranslation("name"), PerspectiveTranslation.getTranslation("toasts.license_update.title")}), PerspectiveTranslation.getTranslation("toasts.license_update.description"), 320, PerspectiveToast.Type.INFO));
            SEEN_LICENSE_UPDATE_NOTICE = true;
        }
    }
    protected static void updateConfig() {
        try {
            if (PerspectiveConfig.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) != DEFAULT_CONFIG_VERSION) {
                if (PerspectiveConfig.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < DEFAULT_CONFIG_VERSION) {
                    PerspectiveData.PERSPECTIVE_VERSION.getLogger().info("{} Attempting to update config to the latest version.", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix());
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
                    if (PerspectiveConfig.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 8) {
                        SHOW_LICENSE_UPDATE_NOTICE = true;
                    }
                    setConfig("config_version", DEFAULT_CONFIG_VERSION);
                    PerspectiveData.PERSPECTIVE_VERSION.getLogger().info("{} Successfully updated config to the latest version.", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix());
                } else if (PerspectiveConfig.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) > DEFAULT_CONFIG_VERSION) {
                    if (!SEEN_DOWNGRADE_WARNING) {
                        PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Downgrading is not supported. You may experience configuration related issues.", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix());
                        SHOW_DOWNGRADE_WARNING = true;
                    }
                }
            }
            saveConfig(false);
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to update config: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    public static void saveConfig(boolean onTick) {
        try {
            if (onTick) {
                SAVE_VIA_TICK = true;
            } else {
                PerspectiveConfig.save();
                PerspectiveExperimentalConfig.save();
                PerspectiveTutorialsConfig.save();
                PerspectiveWarningsConfig.save();
            }
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to save config: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    public static void resetConfig() {
        try {
            // Main Config
            setConfig("zoom_level", Math.min(Math.max(PerspectiveConfigDataLoader.ZOOM_LEVEL, -50), 100));
            setConfig("zoom_increment_size", Math.max(Math.min(PerspectiveConfigDataLoader.ZOOM_INCREMENT_SIZE, 10), 1));
            setConfig("zoom_mode", PerspectiveConfigDataLoader.ZOOM_MODE);
            setConfig("zoom_hide_hud", PerspectiveConfigDataLoader.ZOOM_HIDE_HUD);
            setConfig("zoom_overlay_message", PerspectiveConfigDataLoader.ZOOM_OVERLAY_MESSAGE);
            setConfig("hold_perspective_hide_hud", PerspectiveConfigDataLoader.HOLD_PERSPECTIVE_HIDE_HUD);
            setConfig("super_secret_settings", Math.max(Math.min(PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS, PerspectiveShaderDataLoader.getShaderAmount()), 0));
            setConfig("super_secret_settings_mode", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_MODE);
            setConfig("super_secret_settings_enabled", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_ENABLED);
            setConfig("super_secret_settings_sound", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_SOUND);
            setConfig("super_secret_settings_options_screen", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_OPTIONS_SCREEN);
            setConfig("super_secret_settings_overlay_message", PerspectiveConfigDataLoader.SUPER_SECRET_SETTINGS_OVERLAY_MESSAGE);
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
            setConfig("detect_update_channel", PerspectiveConfigDataLoader.DETECT_UPDATE_CHANNEL);
            setConfig("tutorials", PerspectiveConfigDataLoader.TUTORIALS);
            setConfig("hide_block_outline", PerspectiveConfigDataLoader.HIDE_BLOCK_OUTLINE);
            // Experimental Config
            if (EXPERIMENTS_AVAILABLE) {
                // There is currently no experiments available.
            }
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to reset main and experimental config values: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    public static void setConfig(String ID, Object VALUE) {
        try {
            switch (ID) {
                case "zoom_level" -> PerspectiveConfig.ZOOM_LEVEL = Math.min(Math.max((int)VALUE, -50), 100);
                case "zoom_increment_size" -> PerspectiveConfig.ZOOM_INCREMENT_SIZE = Math.max(Math.min((int)VALUE, 10), 1);
                case "zoom_mode" -> PerspectiveConfig.ZOOM_MODE = (String)VALUE;
                case "zoom_hide_hud" -> PerspectiveConfig.ZOOM_HIDE_HUD = (boolean)VALUE;
                case "zoom_overlay_message" -> PerspectiveConfig.ZOOM_OVERLAY_MESSAGE = (boolean)VALUE;
                case "hold_perspective_hide_hud" -> PerspectiveConfig.HOLD_PERSPECTIVE_HIDE_HUD = (boolean)VALUE;
                case "super_secret_settings" -> PerspectiveConfig.SUPER_SECRET_SETTINGS = Math.max(Math.min((int)VALUE, PerspectiveShaderDataLoader.getShaderAmount()), 0);
                case "super_secret_settings_mode" -> PerspectiveConfig.SUPER_SECRET_SETTINGS_MODE = (String)VALUE;
                case "super_secret_settings_enabled" -> PerspectiveConfig.SUPER_SECRET_SETTINGS_ENABLED = (boolean)VALUE;
                case "super_secret_settings_sound" -> PerspectiveConfig.SUPER_SECRET_SETTINGS_SOUND = (boolean)VALUE;
                case "super_secret_settings_options_screen" -> PerspectiveConfig.SUPER_SECRET_SETTINGS_OPTIONS_SCREEN = (boolean)VALUE;
                case "super_secret_settings_overlay_message" -> PerspectiveConfig.SUPER_SECRET_SETTINGS_OVERLAY_MESSAGE = (boolean)VALUE;
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
                case "detect_update_channel" -> PerspectiveConfig.DETECT_UPDATE_CHANNEL = (String)VALUE;
                case "tutorials" -> PerspectiveConfig.TUTORIALS = (boolean) VALUE;
                case "hide_block_outline" -> PerspectiveConfig.HIDE_BLOCK_OUTLINE = (boolean) VALUE;
                case "config_version" -> PerspectiveConfig.CONFIG_VERSION = (int)VALUE;
                default -> PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to set {} config value: Invalid Key", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), ID);
            }
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to set {} config value: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), ID, error);
        }
    }
    public static void setExperimentalConfig(String ID, Object VALUE) {
        try {
            // There is currently no experiments available.
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to set {} experimental config value: Invalid Key", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), ID);
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to set {} experimental config value: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), ID, error);
        }
    }
    public static void setTutorialConfig(String ID, Object VALUE) {
        try {
            if (ID.equals("super_secret_settings")) {
                PerspectiveTutorialsConfig.SUPER_SECRET_SETTINGS = (Boolean) VALUE;
            } else {
                PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to set {} tutorial config value: Invalid Key", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), ID);
            }
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to set {} tutorial config value: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), ID, error);
        }
    }
    public static void setWarningConfig(String ID, Object VALUE) {
        try {
            if (ID.equals("photosensitivity")) {
                PerspectiveWarningsConfig.PHOTOSENSITIVITY = (Boolean) VALUE;
            } else {
                PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to set {} warning config value: Invalid Key", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), ID);
            }
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to set {} warning config value: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), ID, error);
        }
    }
    public static Object getConfig(String ID) {
        switch (ID) {
            case "zoom_level" -> {return Math.min(Math.max(PerspectiveConfig.ZOOM_LEVEL, -50), 100);}
            case "zoom_increment_size" -> {return Math.max(Math.min(PerspectiveConfig.ZOOM_INCREMENT_SIZE, 10), 1);}
            case "zoom_mode" -> {return PerspectiveConfig.ZOOM_MODE;}
            case "zoom_hide_hud" -> {return PerspectiveConfig.ZOOM_HIDE_HUD;}
            case "zoom_overlay_message" -> {return PerspectiveConfig.ZOOM_OVERLAY_MESSAGE;}
            case "hold_perspective_hide_hud" -> {return PerspectiveConfig.HOLD_PERSPECTIVE_HIDE_HUD;}
            case "super_secret_settings" -> {return Math.max(Math.min(PerspectiveConfig.SUPER_SECRET_SETTINGS, PerspectiveShaderDataLoader.getShaderAmount()), 0);}
            case "super_secret_settings_mode" -> {return PerspectiveConfig.SUPER_SECRET_SETTINGS_MODE;}
            case "super_secret_settings_enabled" -> {return PerspectiveConfig.SUPER_SECRET_SETTINGS_ENABLED;}
            case "super_secret_settings_sound" -> {return PerspectiveConfig.SUPER_SECRET_SETTINGS_SOUND;}
            case "super_secret_settings_options_screen" -> {return PerspectiveConfig.SUPER_SECRET_SETTINGS_OPTIONS_SCREEN;}
            case "super_secret_settings_overlay_message" -> {return PerspectiveConfig.SUPER_SECRET_SETTINGS_OVERLAY_MESSAGE;}
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
            case "detect_update_channel" -> {return PerspectiveConfig.DETECT_UPDATE_CHANNEL;}
            case "tutorials" -> {return PerspectiveConfig.TUTORIALS;}
            case "hide_block_outline" -> {return PerspectiveConfig.HIDE_BLOCK_OUTLINE;}
            case "config_version" -> {return PerspectiveConfig.CONFIG_VERSION;}
            default -> {
                PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to get {} config value: Invalid Key", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), ID);
                return new Object();
            }
        }
    }
    public static Object getExperimentalConfig(String ID) {
        // There is currently no experiments available.
        PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to get {} experimental config value: Invalid Key", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), ID);
        return new Object();
    }
    public static Object getTutorialConfig(String ID) {
        if (ID.equals("super_secret_settings")) {
            return PerspectiveTutorialsConfig.SUPER_SECRET_SETTINGS;
        } else {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to get {} tutorial config value: Invalid Key", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), ID);
        }
        return new Object();
    }
    public static Object getWarningConfig(String ID) {
        if (ID.equals("photosensitivity")) {
            return PerspectiveWarningsConfig.PHOTOSENSITIVITY;
        } else {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to get {} warning config value: Invalid Key", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), ID);
        }
        return new Object();
    }
}