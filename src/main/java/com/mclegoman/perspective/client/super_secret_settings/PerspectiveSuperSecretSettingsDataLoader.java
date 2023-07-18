/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.super_secret_settings;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class PerspectiveSuperSecretSettingsDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static final List<Identifier> SHADERS = new ArrayList<>();
    public static final List<String> SHADERS_NAME = new ArrayList<>();
    public static int getShaderAmount() {
        return SHADERS.size() - 1;
    }
    private void add(String NAMESPACE, String SHADER, Boolean ENABLED) {
        try {
            SHADER = SHADER.replace("\"", "");
            Identifier ID = new Identifier(NAMESPACE, ("shaders/post/" + SHADER + ".json"));
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
        } catch (Exception error) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "Failed to add super secret settings to registry: {}", (Object)error);
        }
    }
    private void reset() {
        try {
            SHADERS.clear();
            SHADERS_NAME.clear();
            add$default();
        } catch (Exception error) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "Failed to reset super secret settings registry: {}", (Object)error);
        }
    }
    private void add$default() {
        try {
            add("minecraft", "none", true);
        } catch (Exception error) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "Failed to add default super secret settings values to registry: {}", (Object)error);
        }
    }
    public static final String ID = "shaders/shaders";
    public PerspectiveSuperSecretSettingsDataLoader() {
        super(new Gson(), ID);
    }
    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        try {
            reset();
            prepared.forEach(this::layout$perspective);
            layout$souper_secret_settings(manager);
            PerspectiveConfigHelper.setConfig("super_secret_settings", Math.min((int)PerspectiveConfigHelper.getConfig("super_secret_settings"), SHADERS.size() - 1));
            PerspectiveSuperSecretSettingsUtil.set(MinecraftClient.getInstance(), true);
        } catch (Exception error) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "Failed to apply super secret settings dataloader: {}", (Object)error);
        }
    }
    @Override
    public Identifier getFabricId() {
        return new Identifier(PerspectiveData.ID, ID);
    }
    private void layout$perspective(Identifier identifier, JsonElement jsonElement) {
        try {
            JsonObject READER = jsonElement.getAsJsonObject();
            String NAMESPACE = JsonHelper.getString(READER, "namespace", PerspectiveData.ID);
            String SHADER = JsonHelper.getString(READER, "shader");
            Boolean ENABLED = JsonHelper.getBoolean(READER, "enabled", true);
            add(NAMESPACE, SHADER, ENABLED);
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to load perspective shader: {}", (Object)error);
        }
    }
    private void layout$souper_secret_settings(ResourceManager manager) {
        for (Resource resource : manager.getAllResources(new Identifier("souper_secret_settings", "shaders.json"))) {
            try {
                JsonObject READER = JsonHelper.deserialize(resource.getReader());
                for (JsonElement namespaces : READER.getAsJsonArray("namespaces")) {
                    JsonObject namespacelist = JsonHelper.asObject(namespaces, "namespacelist");
                    String NAMESPACE = JsonHelper.getString(namespacelist, "namespace", PerspectiveData.ID);
                    JsonArray SHADERS = JsonHelper.getArray(namespacelist, "shaders");
                    Boolean ENABLED = JsonHelper.getBoolean(namespacelist, "enabled", true);
                    for (JsonElement SHADER : SHADERS) add(NAMESPACE, SHADER.getAsString(), ENABLED);
                }
            } catch (Exception error) {
                PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to load souper secret settings shader list: {}", (Object)error);
            }
        }
    }
}