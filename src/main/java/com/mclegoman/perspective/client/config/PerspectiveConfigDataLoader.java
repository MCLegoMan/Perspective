/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.developmentwarning.screen.PerspectiveDevelopmentWarningScreen;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class PerspectiveConfigDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static boolean INIT;
    public static int ZOOM_LEVEL;
    public static boolean HIDE_HUD;
    public static int SUPER_SECRET_SETTINGS;
    public static boolean SUPER_SECRET_SETTINGS_MODE;
    public static boolean SUPER_SECRET_SETTINGS_ENABLED;
    public static boolean NAMED_TEXTURED_ENTITY;
    public static boolean RANDOM_TEXTURED_ENTITY;
    public static boolean ALLOW_APRIL_FOOLS;
    public static boolean FORCE_APRIL_FOOLS;
    public static boolean FORCE_PRIDE;
    public static boolean VERSION_OVERLAY;
    public static boolean SHOW_DEVELOPMENT_WARNING;
    public static boolean HIDE_ARMOR;
    public static boolean HIDE_NAMETAGS;
    public static final String ID = "config";
    public PerspectiveConfigDataLoader() {
        super(new Gson(), ID);
    }

    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        try {
            for (Resource resource : manager.getAllResources(new Identifier(PerspectiveData.ID, ID + ".json"))) {
                ZOOM_LEVEL = JsonHelper.getInt(JsonHelper.deserialize(resource.getReader()), "zoom_level", 80);
                HIDE_HUD = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "hide_hud", true);
                SUPER_SECRET_SETTINGS = JsonHelper.getInt(JsonHelper.deserialize(resource.getReader()), "super_secret_settings", 0);
                SUPER_SECRET_SETTINGS_MODE = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "super_secret_settings_mode", false);
                SUPER_SECRET_SETTINGS_ENABLED = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "super_secret_settings_enabled", false);
                NAMED_TEXTURED_ENTITY = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "named_textured_entity", true);
                RANDOM_TEXTURED_ENTITY = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "random_textured_entity", false);
                ALLOW_APRIL_FOOLS = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "allow_april_fools", true);
                FORCE_APRIL_FOOLS = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "force_april_fools", false);
                FORCE_PRIDE = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "force_pride", false);
                VERSION_OVERLAY = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "version_overlay", false);
                SHOW_DEVELOPMENT_WARNING = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "show_development_warning", true);
                HIDE_ARMOR = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "hide_armor", false);
                HIDE_NAMETAGS = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "hide_nametags", false);
            }
            if (!INIT) {
                PerspectiveConfig.init();
                PerspectiveExperimentalConfig.init();
                PerspectiveConfigHelper.updateConfig();
                if (PerspectiveData.IS_DEVELOPMENT) {
                    if ((boolean)PerspectiveConfigHelper.getConfig("show_development_warning") && PerspectiveClientData.CLIENT.currentScreen instanceof TitleScreen) {
                        PerspectiveClientData.CLIENT.setScreen(new PerspectiveDevelopmentWarningScreen(PerspectiveClientData.CLIENT.currentScreen, 200, true));
                    }
                }
                INIT = true;
            }
        } catch (Exception error) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "Failed to load default config values: {}", (Object)error);
        }
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(PerspectiveData.ID, ID);
    }
}