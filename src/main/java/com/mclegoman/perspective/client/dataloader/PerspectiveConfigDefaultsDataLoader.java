/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.dataloader;

import com.google.gson.Gson;
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

import java.util.Map;

public class PerspectiveConfigDefaultsDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static int ZOOM_LEVEL = 20;
    public static int OVERLAY_DELAY = 20;
    public static int SUPER_SECRET_SETTINGS = 0;
    public static int PERSPECTIVE = 0;
    private static final String ID = "config.json";
    public PerspectiveConfigDefaultsDataLoader() {
        super(new Gson(), ID);
    }
    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        try {
            for (Resource resource : manager.getAllResources(new Identifier(PerspectiveData.ID, "config.json"))) {
                JsonObject default_settings = JsonHelper.deserialize(resource.getReader());
                ZOOM_LEVEL = JsonHelper.getInt(default_settings, "zoom_level", 20);
                OVERLAY_DELAY = JsonHelper.getInt(default_settings, "overlay_delay", 20);
                SUPER_SECRET_SETTINGS = JsonHelper.getInt(default_settings, "super_secret_settings", 0);
                PERSPECTIVE = JsonHelper.getInt(default_settings, "perspective", 0);
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