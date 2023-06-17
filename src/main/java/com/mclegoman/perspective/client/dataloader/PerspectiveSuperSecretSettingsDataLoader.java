/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.dataloader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.legacyfabric.fabric.api.resource.IdentifiableResourceReloadListener;
import net.legacyfabric.fabric.api.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.JsonHelper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;

public class PerspectiveSuperSecretSettingsDataLoader implements IdentifiableResourceReloadListener {
    public static final ArrayList<net.minecraft.util.Identifier> SHADERS = new ArrayList<>();
    public static final ArrayList<String> SHADERS_NAME = new ArrayList<>();
    public static int getShaderAmount() {
        return SHADERS.size() - 1;
    }
    private void add(String NAMESPACE, String SHADER, Boolean ENABLED) {
        SHADER = SHADER.replace("\"", "");
        net.minecraft.util.Identifier ID = new net.minecraft.util.Identifier(NAMESPACE, ("shaders/post/" + SHADER + ".json"));
        String NAME = NAMESPACE + ":" + SHADER;
        if (ENABLED) {
            if (!SHADERS.contains(ID) && !SHADERS_NAME.contains(NAME)) {
                SHADERS.add(ID);
                SHADERS_NAME.add(NAME);
            }
        } else {
            if (SHADERS.contains(ID) && SHADERS_NAME.contains(NAME)) {
                SHADERS.remove(ID);
                SHADERS_NAME.remove(NAME);
            }
        }
    }
    private void clear() {
        SHADERS.clear();
        SHADERS_NAME.clear();
    }
    public static final String ID = "shaders/shaders";
    public PerspectiveSuperSecretSettingsDataLoader() {
        super();
    }
    @Override
    public Identifier getFabricId() {
        return new Identifier(PerspectiveData.ID, ID);
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        try {
            clear();
//            // Perspective Resource Pack Layout
//            prepared.forEach((identifier, jsonElement) -> {
//                JsonObject READER = jsonElement.getAsJsonObject();
//                String NAMESPACE = JsonHelper.getString(READER, "namespace", PerspectiveData.ID);
//                String SHADER = JsonHelper.getString(READER, "shader");
//                Boolean ENABLED = JsonHelper.getBoolean(READER, "enabled", true);
//                add(NAMESPACE, SHADER, ENABLED);
//            });
            // Souper Secret Settings Resource Pack Layout Compatibility
            // https://github.com/Nettakrim/Souper-Secret-Settings
//            for (Resource resource : resourceManager.getAllResources(new net.minecraft.util.Identifier("souper_secret_settings", "shaders.json"))) {
//                try {
//                    BufferedReader reader = new BufferedReader(new FileReader(MinecraftClient.getInstance().runDirectory.getPath() + "shaders.json"));
//                    JsonHelper.
//                    for (JsonElement namespaces : reader.getAsJsonArray("namespaces")) {
//                        JsonObject namespacelist = JsonHelper.asObject(namespaces, "namespacelist");
//                        String NAMESPACE = JsonHelper.getString(namespacelist, "namespace", PerspectiveData.ID);
//                        JsonArray SHADERS = JsonHelper.getArray(namespacelist, "shaders");
//                        Boolean ENABLED = JsonHelper.getBoolean(namespacelist, "enabled", true);
//                        for (JsonElement SHADER : SHADERS) add(NAMESPACE, SHADER.getAsString(), ENABLED);
//                    }
//                } catch(Exception e) {
//                    PerspectiveData.LOGGER.error("Error occurred while loading resource json " + resource.getId().toString(), e);
//                }
//            }
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst loading Super Secret Settings data.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }
}