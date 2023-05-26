package com.mclegoman.perspective.dataloader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.perspective.PerspectiveClientMain;
import com.mclegoman.perspective.data.PerspectiveData;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.ArrayList;
import java.util.Map;

public class PerspectiveSSSDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static final ArrayList<Identifier> SHADERS = new ArrayList<>();
    public static final ArrayList<String> SHADERS_NAME = new ArrayList<>();
    public static final String ID = "shaders/shaders";
    public PerspectiveSSSDataLoader() {
        super(new Gson(), ID);
    }

    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        clearArrayLists();
        prepared.forEach((identifier, jsonElement) -> {
            try{
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String NAMESPACE = JsonHelper.getString(jsonObject, "namespace");
                String SHADER = JsonHelper.getString(jsonObject, "shader");
                Boolean ENABLED = JsonHelper.getBoolean(jsonObject, "enabled");
                addToArrayList(new Identifier(NAMESPACE, ("shaders/post/" + SHADER + ".json")), ENABLED);
                addToArrayList((NAMESPACE + ":" + SHADER), ENABLED);
            } catch (Exception e) {
                PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
            }
        });
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier("perspective", ID);
    }

    private void addToArrayList(Identifier id, Boolean enabled) {
        if (enabled && !SHADERS.contains(id)) SHADERS.add(id);
    }
    private void addToArrayList(String name, Boolean enabled) {
        if (enabled && !SHADERS_NAME.contains(name)) SHADERS_NAME.add(name);
    }

    private void clearArrayLists() {
        SHADERS.clear();
    }
}