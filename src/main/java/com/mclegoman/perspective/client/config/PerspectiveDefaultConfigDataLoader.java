/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class PerspectiveDefaultConfigDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static boolean INIT;
    // Normal Config
    public static int ZOOM_LEVEL;
    public static int OVERLAY_DELAY;
    public static int SUPER_SECRET_SETTINGS;
    public static boolean SUPER_SECRET_SETTINGS_MODE;
    public static boolean SUPER_SECRET_SETTINGS_ENABLED;
    public static boolean HIDE_HUD;
    public static boolean ALLOW_APRIL_FOOLS;
    public static boolean FORCE_APRIL_FOOLS;
    public static boolean SHOW_DEVELOPMENT_WARNING;
    // Experimental Config
    public static boolean TEXTURED_NAMED_ENTITY;
    public static boolean TEXTURED_RANDOM_ENTITY;
    public static final String ID = "config";
    public PerspectiveDefaultConfigDataLoader() {
        super(new Gson(), ID);
    }

    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        try {
            for (Resource resource : manager.getAllResources(new Identifier(PerspectiveData.ID, ID + ".json"))) {
                // Normal Config
                ZOOM_LEVEL = JsonHelper.getInt(JsonHelper.deserialize(resource.getReader()), "zoom_level", 80);
                OVERLAY_DELAY = JsonHelper.getInt(JsonHelper.deserialize(resource.getReader()), "overlay_delay", 1);
                SUPER_SECRET_SETTINGS = JsonHelper.getInt(JsonHelper.deserialize(resource.getReader()), "super_secret_settings", 0);
                SUPER_SECRET_SETTINGS_MODE = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "super_secret_settings_mode", false);
                SUPER_SECRET_SETTINGS_ENABLED = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "super_secret_settings_enabled", false);
                HIDE_HUD = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "hide_hud", true);
                ALLOW_APRIL_FOOLS = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "allow_april_fools", true);
                FORCE_APRIL_FOOLS = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "force_april_fools", false);
                SHOW_DEVELOPMENT_WARNING = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "show_development_warning", true);
                TEXTURED_NAMED_ENTITY = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "textured_named_entity", true);
                TEXTURED_RANDOM_ENTITY = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "textured_random_entity", false);
                // Experimental Config
            }
            if (!INIT) {
                PerspectiveConfig.init();
                PerspectiveExperimentalConfig.init();
                PerspectiveConfigHelper.updateConfig();
                INIT = true;
            }
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst loading Default Config data.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(PerspectiveData.ID, ID);
    }
}