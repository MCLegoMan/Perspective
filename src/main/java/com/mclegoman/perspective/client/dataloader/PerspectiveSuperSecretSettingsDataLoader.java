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

public class PerspectiveSuperSecretSettingsDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static final ArrayList<Identifier> SHADERS = new ArrayList<>();
    public static final ArrayList<String> SHADERS_NAME = new ArrayList<>();
    public static int getShaderAmount() {
        return SHADERS.size() - 1;
    }
    public static final String ID = "shaders/shaders";
    public PerspectiveSuperSecretSettingsDataLoader() {
        super(new Gson(), ID);
    }

    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        try {
            clearArrayLists();
            prepared.forEach((identifier, jsonElement) -> {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String NAMESPACE = JsonHelper.getString(jsonObject, "namespace");
                    String SHADER = JsonHelper.getString(jsonObject, "shader");
                    Boolean ENABLED = JsonHelper.getBoolean(jsonObject, "enabled");
                    addToArrayList(new Identifier(NAMESPACE, ("shaders/post/" + SHADER + ".json")), ENABLED);
                    addToArrayList((NAMESPACE + ":" + SHADER), ENABLED);
            });
            parseSouperSecretSettingsShader(manager);
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst loading Super Secret Settings data.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
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
        SHADERS_NAME.clear();
    }

    // Souper Secret Settings Resource Pack Compatibility
    // https://github.com/Nettakrim/Souper-Secret-Settings
    private void parseSouperSecretSettingsShader(ResourceManager resourceManager) {
        try {
            for (Resource resource : resourceManager.getAllResources(new Identifier("souper_secret_settings", "shaders.json"))) {
                JsonObject souper_shaders = JsonHelper.deserialize(resource.getReader());
                for (JsonElement shaderspaces : souper_shaders.getAsJsonArray("namespaces")) {
                    JsonObject shaderspaces_object = JsonHelper.asObject(shaderspaces, "namespacelist");
                    String NAMESPACE = JsonHelper.getString(shaderspaces_object, "namespace");
                    JsonArray shaders = JsonHelper.getArray(shaderspaces_object, "shaders");
                    for (JsonElement shader : shaders) {
                        String SHADER = shader.getAsString().replace("\"", "");
                        addToArrayList(new Identifier(NAMESPACE, ("shaders/post/" + SHADER + ".json")), true);
                        addToArrayList((NAMESPACE + ":" + SHADER), true);
                    }
                }
            }
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst loading 'Souper Secret Settings' Super Secret Settings data.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }
}