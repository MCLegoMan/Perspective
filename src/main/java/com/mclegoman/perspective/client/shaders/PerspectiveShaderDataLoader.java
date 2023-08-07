/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.shaders;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class PerspectiveShaderDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static final List<Map<Identifier, String>> REGISTRY = new ArrayList<>();
    public static int getShaderAmount() {
        return REGISTRY.size() - 1;
    }
    public static Object get(int SHADER, boolean NAME) {
        for (Map.Entry<Identifier, String> SHADER_MAP : REGISTRY.get(SHADER).entrySet()) {
            if (!NAME) return SHADER_MAP.getKey();
            else return SHADER_MAP.getValue();
        }
        return null;
    }
    private void add(String NAMESPACE, String SHADER, Boolean ENABLED) {
        try {
            SHADER = SHADER.replace("\"", "");
            Identifier ID = new Identifier(NAMESPACE, ("shaders/post/" + SHADER + ".json"));
            String NAME = NAMESPACE + ":" + SHADER;
            Map<Identifier, String> SHADER_MAP = new HashMap<>();
            SHADER_MAP.put(ID, NAME);
            if (ENABLED) {
                if (!REGISTRY.contains(SHADER_MAP)) REGISTRY.add(SHADER_MAP);
            } else REGISTRY.remove(SHADER_MAP);
        } catch (Exception error) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "Failed to add shader to registry: {}", (Object)error);
        }
    }
    private void reset() {
        try {
            REGISTRY.clear();
            add$default();
        } catch (Exception error) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "Failed to reset shaders registry: {}", (Object)error);
        }
    }
    private void add$default() {
        try {
            add("minecraft", "none", true);
        } catch (Exception error) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "Failed to add default shaders to registry: {}", (Object)error);
        }
    }
    public static final String ID = "shaders/shaders";
    public PerspectiveShaderDataLoader() {
        super(new Gson(), ID);
    }
    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        try {
            reset();
            prepared.forEach(this::layout$perspective);
            layout$souper_secret_settings(manager);
            PerspectiveConfigHelper.setConfig("super_secret_settings", Math.min((int)PerspectiveConfigHelper.getConfig("super_secret_settings"), REGISTRY.size() - 1));
            PerspectiveShader.set(MinecraftClient.getInstance(), true);
        } catch (Exception error) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "Failed to apply shaders dataloader: {}", (Object)error);
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