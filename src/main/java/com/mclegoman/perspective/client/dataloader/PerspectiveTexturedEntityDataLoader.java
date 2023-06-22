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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.*;

@Environment(EnvType.CLIENT)
public class PerspectiveTexturedEntityDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static final List<List<String>> REGISTRY = new ArrayList<>();
    private void add(String ENTITY, String NAME, Boolean ENABLED) {
        List<String> VALUES = new ArrayList<>();
        VALUES.add(ENTITY);
        VALUES.add(NAME);
        if (ENABLED) REGISTRY.add(VALUES);
        else REGISTRY.remove(VALUES);
    }
    private void clear() {
        REGISTRY.clear();
    }
    public static final String ID = "textured_entity";
    public PerspectiveTexturedEntityDataLoader() {
        super(new Gson(), ID);
    }
    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        try {
            clear();
            // Perspective Resource Pack Layout
            prepared.forEach((identifier, jsonElement) -> {
                JsonObject READER = jsonElement.getAsJsonObject();
                String ENTITY = JsonHelper.getString(READER, "entity");
                String NAME = JsonHelper.getString(READER, "name");
                Boolean ENABLED = JsonHelper.getBoolean(READER, "enabled", true);
                add(ENTITY, NAME, ENABLED);
            });
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst loading Textured Entity data.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }
    @Override
    public Identifier getFabricId() {
        return new Identifier(PerspectiveData.ID, ID);
    }
}