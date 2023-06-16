/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.dataloader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.ArrayList;
import java.util.Map;

public class PerspectiveDefaultConfigDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static int ZOOM_LEVEL = 20;
    public static int OVERLAY_DELAY = 20;
    public static int SUPER_SECRET_SETTINGS = 0;
    public static int PERSPECTIVE = 0;
    public static boolean SHOW_DEVELOPMENT_WARNING = true;
    public static int CONFIG_VERSION = 2;
    public static final String ID = "config";
    public PerspectiveDefaultConfigDataLoader() {
        super(new Gson(), ID);
    }

    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        try {
            for (Resource resource : manager.getAllResources(new Identifier(PerspectiveData.ID, ID + ".json"))) {
                ZOOM_LEVEL = JsonHelper.getInt(JsonHelper.deserialize(resource.getReader()), "zoom_level");
                OVERLAY_DELAY = JsonHelper.getInt(JsonHelper.deserialize(resource.getReader()), "overlay_delay");
                SUPER_SECRET_SETTINGS = JsonHelper.getInt(JsonHelper.deserialize(resource.getReader()), "super_secret_settings");
                PERSPECTIVE = JsonHelper.getInt(JsonHelper.deserialize(resource.getReader()), "perspective");
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