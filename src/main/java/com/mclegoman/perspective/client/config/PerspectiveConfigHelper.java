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
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveDefaultConfigDataLoader());
    }
    public static void tick(MinecraftClient client) {
        if (PerspectiveKeybindings.KEY_CONFIG.wasPressed()) client.setScreen(new PerspectiveConfigScreen(client.currentScreen));
        if (SAVE_VIA_TICK_TICKS < SAVE_VIA_TICK_SAVE_TICK) SAVE_VIA_TICK_TICKS += 1;
        else {
            if (SAVE_VIA_TICK) {
                saveConfig(false);
                SAVE_VIA_TICK = false;
            }
            SAVE_VIA_TICK_TICKS = 0;
        }
    }
    protected static void updateConfig() {
        if ((int)getConfig("config_version") != DEFAULT_CONFIG_VERSION) {
            PerspectiveData.LOGGER.info(PerspectiveData.PREFIX + "Updating config to the latest version.");
            if ((int)getConfig("config_version") < 3) {
                setConfig("zoom_level", 100 - (int)getConfig("zoom_level"));
                setConfig("overlay_delay", (int)getConfig("overlay_delay") / 20);
            }
            setConfig("config_version", DEFAULT_CONFIG_VERSION);
        }
        saveConfig(false);
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
        setConfig("zoom_level", PerspectiveDefaultConfigDataLoader.ZOOM_LEVEL);
        setConfig("overlay_delay", PerspectiveDefaultConfigDataLoader.OVERLAY_DELAY);
        setConfig("super_secret_settings", PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS);
        setConfig("super_secret_settings_mode", PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS_MODE);
        setConfig("super_secret_settings_enabled", PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS_ENABLED);
        setConfig("hide_hud", PerspectiveDefaultConfigDataLoader.HIDE_HUD);
        setConfig("allow_april_fools", PerspectiveDefaultConfigDataLoader.ALLOW_APRIL_FOOLS);
        setConfig("force_april_fools", PerspectiveDefaultConfigDataLoader.FORCE_APRIL_FOOLS);
        setConfig("show_development_warning", PerspectiveDefaultConfigDataLoader.SHOW_DEVELOPMENT_WARNING);
        setConfig("textured_named_entity", PerspectiveDefaultConfigDataLoader.TEXTURED_NAMED_ENTITY);
        setConfig("textured_random_entity", PerspectiveDefaultConfigDataLoader.TEXTURED_RANDOM_ENTITY);
        // Experimental
    }
    public static void setConfig(String ID, Object VALUE) {
        switch (ID) {
            case "zoom_level" -> PerspectiveConfig.ZOOM_LEVEL = (int)VALUE;
            case "overlay_delay" -> PerspectiveConfig.OVERLAY_DELAY = (int)VALUE;
            case "super_secret_settings" -> PerspectiveConfig.SUPER_SECRET_SETTINGS = (int)VALUE;
            case "super_secret_settings_mode" -> PerspectiveConfig.SUPER_SECRET_SETTINGS_MODE = (boolean)VALUE;
            case "super_secret_settings_enabled" -> PerspectiveConfig.SUPER_SECRET_SETTINGS_ENABLED = (boolean)VALUE;
            case "hide_hud" -> PerspectiveConfig.HIDE_HUD = (boolean)VALUE;
            case "allow_april_fools" -> PerspectiveConfig.ALLOW_APRIL_FOOLS = (boolean)VALUE;
            case "force_april_fools" -> PerspectiveConfig.FORCE_APRIL_FOOLS = (boolean)VALUE;
            case "show_development_warning" -> PerspectiveConfig.SHOW_DEVELOPMENT_WARNING = (boolean)VALUE;
            case "config_version" -> PerspectiveConfig.CONFIG_VERSION = (int)VALUE;
            case "textured_named_entity" -> PerspectiveConfig.TEXTURED_NAMED_ENTITY = (boolean)VALUE;
            case "textured_random_entity" -> PerspectiveConfig.TEXTURED_RANDOM_ENTITY = (boolean)VALUE;
            // Experimental
        }
    }
    public static Object getConfig(String ID) {
        switch (ID) {
            case "zoom_level" -> {return PerspectiveConfig.ZOOM_LEVEL;}
            case "overlay_delay" ->  {return PerspectiveConfig.OVERLAY_DELAY;}
            case "super_secret_settings" -> {return PerspectiveConfig.SUPER_SECRET_SETTINGS;}
            case "super_secret_settings_mode" -> {return PerspectiveConfig.SUPER_SECRET_SETTINGS_MODE;}
            case "super_secret_settings_enabled" -> {return PerspectiveConfig.SUPER_SECRET_SETTINGS_ENABLED;}
            case "hide_hud" -> {return PerspectiveConfig.HIDE_HUD;}
            case "allow_april_fools" -> {return PerspectiveConfig.ALLOW_APRIL_FOOLS;}
            case "force_april_fools" -> {return PerspectiveConfig.FORCE_APRIL_FOOLS;}
            case "show_development_warning" -> {return PerspectiveConfig.SHOW_DEVELOPMENT_WARNING;}
            case "config_version" -> {return PerspectiveConfig.CONFIG_VERSION;}
            case "textured_named_entity" -> {return PerspectiveConfig.TEXTURED_NAMED_ENTITY;}
            case "textured_random_entity" -> {return PerspectiveConfig.TEXTURED_RANDOM_ENTITY;}
            // Experimental
        }
        return new Object();
    }
}