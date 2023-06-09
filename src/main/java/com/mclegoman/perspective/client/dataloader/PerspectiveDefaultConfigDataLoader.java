/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.dataloader;

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
    public static int ZOOM_LEVEL = 20;
    public static boolean ZOOM_SHOW_HUD = true;
    public static int OVERLAY_DELAY = 20;
    public static int SUPER_SECRET_SETTINGS = 0;
    public static boolean PERSPECTIVE_HOLD = true;
    public static boolean PERSPECTIVE_HOLD_SHOW_HUD = true;
    public static String DEFAULT_SUPER_SECRET_SETTINGS_SOUND = "minecraft:block.pointed_dripstone.drip_water";
    public static boolean TEXTURED_NAMED_ENTITY = true;
    public static boolean TEXTURED_RANDOM_ENTITY = false;
    public static boolean ALLOW_APRIL_FOOLS = true;
    public static boolean FORCE_APRIL_FOOLS = false;
    public static boolean SHOW_DEVELOPMENT_WARNING = true;
    public static int CONFIG_VERSION = 3;
    public static final String ID = "config";
    public PerspectiveDefaultConfigDataLoader() {
        super(new Gson(), ID);
    }

    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        try {
            for (Resource resource : manager.getAllResources(new Identifier(PerspectiveData.ID, ID + ".json"))) {
                ZOOM_LEVEL = JsonHelper.getInt(JsonHelper.deserialize(resource.getReader()), "zoom_level");
                ZOOM_SHOW_HUD = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "zoom_show_hud");
                OVERLAY_DELAY = JsonHelper.getInt(JsonHelper.deserialize(resource.getReader()), "overlay_delay");
                SUPER_SECRET_SETTINGS = JsonHelper.getInt(JsonHelper.deserialize(resource.getReader()), "super_secret_settings");
                PERSPECTIVE_HOLD = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "perspective_hold");
                PERSPECTIVE_HOLD_SHOW_HUD = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "perspective_hold_show_hud");
                TEXTURED_NAMED_ENTITY = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "textured_named_entity");
                TEXTURED_RANDOM_ENTITY = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "textured_random_entity");
                ALLOW_APRIL_FOOLS = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "allow_april_fools");
                FORCE_APRIL_FOOLS = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "force_april_fools");
                SHOW_DEVELOPMENT_WARNING = JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "show_development_warning");
                CONFIG_VERSION = JsonHelper.getInt(JsonHelper.deserialize(resource.getReader()), "config_version");
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