/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
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
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class PerspectiveShaderDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static final List<List<Object>> REGISTRY = new ArrayList<>();
    public static int getShaderAmount() {
        return REGISTRY.size() - 1;
    }
    public static Object get(int SHADER, PerspectiveShaderRegistryValue VALUE) {
        List<Object> SHADER_MAP = REGISTRY.get(SHADER);
        if (VALUE.equals(PerspectiveShaderRegistryValue.ID)) return SHADER_MAP.get(0);
        if (VALUE.equals(PerspectiveShaderRegistryValue.NAME)) return SHADER_MAP.get(1);
        if (VALUE.equals(PerspectiveShaderRegistryValue.HIDE_ARMOR)) return SHADER_MAP.get(2);
        if (VALUE.equals(PerspectiveShaderRegistryValue.HIDE_NAMETAGS)) return SHADER_MAP.get(3);
        if (VALUE.equals(PerspectiveShaderRegistryValue.DISABLE_SCREEN_MODE)) return SHADER_MAP.get(4);
        return null;
    }
    private void add(String NAMESPACE, String SHADER, Boolean HIDE_ARMOR, Boolean HIDE_NAMETAGS, Boolean DISABLE_SCREEN_MODE, Boolean ENABLED) {
        try {
            SHADER = SHADER.replace("\"", "");
            Identifier ID = new Identifier(NAMESPACE, ("shaders/post/" + SHADER + ".json"));
            String NAME = NAMESPACE + ":" + SHADER;
            List<Object> SHADER_MAP = new ArrayList<>();
            SHADER_MAP.add(ID);
            SHADER_MAP.add(NAME);
            SHADER_MAP.add(HIDE_ARMOR);
            SHADER_MAP.add(HIDE_NAMETAGS);
            SHADER_MAP.add(DISABLE_SCREEN_MODE);
            boolean ALREADY_REGISTERED = false;
            List<Object> REGISTRY_MAP = SHADER_MAP;
            for (List<Object> SHADER_MAP_IN_REGISTRY : REGISTRY) {
                if (SHADER_MAP_IN_REGISTRY.contains(ID)) {
                    ALREADY_REGISTERED = true;
                    REGISTRY_MAP = SHADER_MAP_IN_REGISTRY;
                    break;
                }
            }
            if (ENABLED) {
                if (!ALREADY_REGISTERED) REGISTRY.add(SHADER_MAP);
            } else REGISTRY.remove(REGISTRY_MAP);
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to add shader to registry: {}", PerspectiveData.PERSPECTIVE_VERSION.getID(), error);
        }
    }
    private void reset() {
        try {
            REGISTRY.clear();
            add$default();
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to reset shaders registry: {}", PerspectiveData.PERSPECTIVE_VERSION.getID(), error);
        }
    }
    private void add$default() {
        try {
            add("minecraft", "none",false, false, false, true);
            add("minecraft", "antialias",false, false, false, true);
            add("minecraft", "art",false, false, false, true);
            add("minecraft", "bits",false, false, false, true);
            add("minecraft", "blobs",false, false, false, true);
            add("minecraft", "blobs2",false, false, false, true);
            add("minecraft", "blur",false, false, false, true);
            add("minecraft", "bumpy",false, false, false, true);
            add("minecraft", "color_convolve",false, false, false, true);
            add("minecraft", "creeper",false, false, false, true);
            add("minecraft", "deconverge",false, false, false, true);
            add("minecraft", "desaturate",false, false, false, true);
            add("minecraft", "flip",false, false, true, true);
            add("minecraft", "fxaa",false, false, false, true);
            add("minecraft", "green",false, false, false, true);
            add("minecraft", "invert",false, false, false, true);
            add("minecraft", "notch",false, false, false, true);
            add("minecraft", "ntsc",false, false, false, true);
            add("minecraft", "outline",false, false, false, true);
            add("minecraft", "pencil",false, false, false, true);
            add("minecraft", "phosphor",false, false, false, true);
            add("minecraft", "scan_pincushion",false, false, false, true);
            add("minecraft", "sobel",false, false, false, true);
            add("minecraft", "spider",false, false, true, true);
            add("minecraft", "wobble",false, false, false, true);
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to add default shaders to registry: {}", PerspectiveData.PERSPECTIVE_VERSION.getID(), error);
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
            if ((boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_enabled")) PerspectiveShader.set(MinecraftClient.getInstance(), true, true, true);
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to apply shaders dataloader: {}", PerspectiveData.PERSPECTIVE_VERSION.getID(), error);
        }
    }
    @Override
    public Identifier getFabricId() {
        return new Identifier(PerspectiveData.PERSPECTIVE_VERSION.getID(), ID);
    }
    private void layout$perspective(Identifier identifier, JsonElement jsonElement) {
        try {
            JsonObject READER = jsonElement.getAsJsonObject();
            String NAMESPACE = JsonHelper.getString(READER, "namespace", PerspectiveData.PERSPECTIVE_VERSION.getID());
            String SHADER = JsonHelper.getString(READER, "shader");
            Boolean HIDE_ARMOR = JsonHelper.getBoolean(READER, "hide_armor", false);
            Boolean HIDE_NAMETAGS = JsonHelper.getBoolean(READER, "hide_nametags", false);
            Boolean DISABLE_SCREEN_MODE = JsonHelper.getBoolean(READER, "disable_screen_mode", false);
            Boolean ENABLED = JsonHelper.getBoolean(READER, "enabled", true);
            add(NAMESPACE, SHADER, HIDE_ARMOR, HIDE_NAMETAGS, DISABLE_SCREEN_MODE, ENABLED);
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to load perspective shader: {}", PerspectiveData.PERSPECTIVE_VERSION.getID(), error);
        }
    }
    private void layout$souper_secret_settings(ResourceManager manager) {
        List<Resource> SHADER_LISTS = manager.getAllResources(new Identifier("souper_secret_settings", "shaders.json"));
        for (Resource resource : SHADER_LISTS) {
            try {
                JsonObject READER = JsonHelper.deserialize(resource.getReader());
                for (JsonElement namespaces : READER.getAsJsonArray("namespaces")) {
                    JsonObject namespacelist = JsonHelper.asObject(namespaces, "namespacelist");
                    String NAMESPACE = JsonHelper.getString(namespacelist, "namespace", PerspectiveData.PERSPECTIVE_VERSION.getID());
                    JsonArray SHADERS = JsonHelper.getArray(namespacelist, "shaders", new JsonArray());
                    JsonArray HIDE_ARMOR = JsonHelper.getArray(namespacelist, "hide_armor", new JsonArray());
                    JsonArray HIDE_NAMETAGS = JsonHelper.getArray(namespacelist, "hide_nametags", new JsonArray());
                    JsonArray DISABLE_SCREEN_MODE = JsonHelper.getArray(namespacelist, "disable_screen_mode", new JsonArray());
                    Boolean ENABLED = JsonHelper.getBoolean(namespacelist, "enabled", true);
                    List<String> HIDE_ARMOR_SHADERS = new ArrayList<>();
                    for (JsonElement SHADER : HIDE_ARMOR) HIDE_ARMOR_SHADERS.add(SHADER.getAsString());
                    List<String> HIDE_NAMETAGS_SHADERS = new ArrayList<>();
                    for (JsonElement SHADER : HIDE_NAMETAGS) HIDE_NAMETAGS_SHADERS.add(SHADER.getAsString());
                    List<String> DISABLE_SCREEN_MODE_SHADERS = new ArrayList<>();
                    for (JsonElement SHADER : DISABLE_SCREEN_MODE) DISABLE_SCREEN_MODE_SHADERS.add(SHADER.getAsString());
                    for (JsonElement SHADER : SHADERS) add(NAMESPACE, SHADER.getAsString(), HIDE_ARMOR_SHADERS.contains(SHADER.getAsString()), HIDE_NAMETAGS_SHADERS.contains(SHADER.getAsString()), DISABLE_SCREEN_MODE_SHADERS.contains(SHADER.getAsString()), ENABLED);
                }
            } catch (Exception error) {
                PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to load souper secret settings shader list: {}", PerspectiveData.PERSPECTIVE_VERSION.getID(), error);
            }
        }
    }
}