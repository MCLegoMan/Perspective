/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.april_fools;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class PerspectiveAprilFoolsDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static final List<String> REGISTRY = new ArrayList<>();
    private void add(String NAME, Boolean ENABLED) {
        if (ENABLED) REGISTRY.add(NAME);
        else REGISTRY.remove(NAME);
    }
    private void clear() {
        REGISTRY.clear();
    }
    public static final String ID = "april_fools";
    public PerspectiveAprilFoolsDataLoader() {
        super(new Gson(), ID);
    }
    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        try {
            clear();
            // Perspective Resource Pack Layout
            prepared.forEach((identifier, jsonElement) -> {
                JsonObject READER = jsonElement.getAsJsonObject();
                String SKIN = JsonHelper.getString(READER, "skin");
                Boolean ENABLED = JsonHelper.getBoolean(READER, "enabled", true);
                add(SKIN, ENABLED);
            });
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst loading April Fools data.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }
    @Override
    public Identifier getFabricId() {
        return new Identifier(PerspectiveData.ID, ID);
    }
}