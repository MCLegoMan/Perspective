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
import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.common.data.Data;
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

public class ShaderDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static final List<List<Object>> REGISTRY = new ArrayList<>();
    public static final List<String> DUPLICATED_NAMES = new ArrayList<>();
    public static int getShaderAmount() {
        return REGISTRY.size() - 1;
    }
    public static String getShaderName(int SHADER) {
        String NAMESPACE = (String) ShaderDataLoader.get(SHADER, ShaderRegistryValue.NAMESPACE);
        String SHADER_NAME = (String) ShaderDataLoader.get(SHADER, ShaderRegistryValue.SHADER_NAME);
        return isDuplicatedShaderName(SHADER_NAME) ? NAMESPACE + ":" + SHADER_NAME : SHADER_NAME;
    }
    private static boolean isDuplicatedShaderName(String name) {
        return DUPLICATED_NAMES.contains(name);
    }
    public static Object get(int SHADER, ShaderRegistryValue VALUE) {
        List<Object> SHADER_MAP = REGISTRY.get(SHADER);
        if (VALUE.equals(ShaderRegistryValue.ID)) return SHADER_MAP.get(0);
        if (VALUE.equals(ShaderRegistryValue.NAMESPACE)) return SHADER_MAP.get(1);
        if (VALUE.equals(ShaderRegistryValue.SHADER_NAME)) return SHADER_MAP.get(2);
        if (VALUE.equals(ShaderRegistryValue.HIDE_ARMOR)) return SHADER_MAP.get(3);
        if (VALUE.equals(ShaderRegistryValue.HIDE_BLOCK_OUTLINE)) return SHADER_MAP.get(4);
        if (VALUE.equals(ShaderRegistryValue.HIDE_CROSSHAIR)) return SHADER_MAP.get(5);
        if (VALUE.equals(ShaderRegistryValue.HIDE_NAMETAGS)) return SHADER_MAP.get(6);
        if (VALUE.equals(ShaderRegistryValue.DISABLE_SCREEN_MODE)) return SHADER_MAP.get(7);
        return null;
    }
    private void add(String NAMESPACE, String SHADER_NAME, boolean HIDE_ARMOR, boolean HIDE_BLOCK_OUTLINE, boolean HIDE_CROSSHAIR, boolean HIDE_NAMETAGS, boolean DISABLE_SCREEN_MODE, boolean ENABLED) {
        try {
            SHADER_NAME = SHADER_NAME.replace("\"", "").toLowerCase();
            Identifier ID = new Identifier(NAMESPACE.toLowerCase(), ("shaders/post/" + SHADER_NAME + ".json"));
            List<Object> SHADER_MAP = new ArrayList<>();
            SHADER_MAP.add(ID);
            SHADER_MAP.add(NAMESPACE.toLowerCase());
            SHADER_MAP.add(SHADER_NAME);
            SHADER_MAP.add(HIDE_ARMOR);
            SHADER_MAP.add(HIDE_BLOCK_OUTLINE);
            SHADER_MAP.add(HIDE_CROSSHAIR);
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
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to add shader to registry: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
    }
    private void reset() {
        try {
            REGISTRY.clear();
            DUPLICATED_NAMES.clear();
            add$default();
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to reset shaders registry: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
    }
    private void add$default() {
        try {
            add("minecraft", "none", false, false, false, false, false, true);
            add("minecraft", "antialias", false, false, false, false, false, true);
            add("minecraft", "art", false, false, false, false, true, true);
            add("minecraft", "bits", false, false, false, false, true, true);
            add("minecraft", "blobs", false, false, false, false, false, true);
            add("minecraft", "blobs2", false, false, false, false, true, true);
            add("minecraft", "blur", false, false, false, false, true, true);
            add("minecraft", "bumpy", false, false, false, false, false, true);
            add("minecraft", "color_convolve", false, false, false, false, false, true);
            add("minecraft", "creeper", false, false, false, false, false, true);
            add("minecraft", "deconverge", false, false, false, false, false, true);
            add("minecraft", "desaturate", false, false, false, false, false, true);
            add("minecraft", "flip", false, false, false, false, true, true);
            add("minecraft", "fxaa", false, false, false, false, false, true);
            add("minecraft", "green", false, false, false, false, true, true);
            add("minecraft", "invert", false, false, false, false, false, true);
            add("minecraft", "love", false, false, false, false, false, true);
            add("minecraft", "notch", false, false, false, false, false, true);
            add("minecraft", "ntsc", false, false, false, false, true, true);
            add("minecraft", "outline", false, false, false, false, false, true);
            add("minecraft", "pencil", false, false, false, false, false, true);
            add("minecraft", "phosphor", false, false, false, false, false, true);
            add("minecraft", "scan_pincushion", false, false, false, false, false, true);
            add("minecraft", "sobel", false, false, false, false, false, true);
            add("minecraft", "spider", false, false, false, false, true, true);
            add("minecraft", "wobble", false, false, false, false, false, true);
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to add default shaders to registry: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
    }
    public static final String ID = "shaders/shaders";
    public ShaderDataLoader() {
        super(new Gson(), ID);
    }
    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        try {
            reset();
            prepared.forEach(this::layout$perspective);
            layout$souper_secret_settings(manager);
            ConfigHelper.setConfig("super_secret_settings", Math.min((int) ConfigHelper.getConfig("super_secret_settings"), REGISTRY.size() - 1));
            if ((boolean) ConfigHelper.getConfig("super_secret_settings_enabled")) Shader.set(MinecraftClient.getInstance(), true, true, true);


            List<String> ALL_NAMES = new ArrayList<>();
            for (List<Object> registry : REGISTRY) {
                if (!ALL_NAMES.contains((String) registry.get(1))) ALL_NAMES.add((String) registry.get(1));
                else {
                    if (!DUPLICATED_NAMES.contains((String) registry.get(1))) DUPLICATED_NAMES.add((String) registry.get(1));
                }
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to apply shaders dataloader: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
    }
    @Override
    public Identifier getFabricId() {
        return new Identifier(Data.PERSPECTIVE_VERSION.getID(), ID);
    }
    private void layout$perspective(Identifier identifier, JsonElement jsonElement) {
        try {
            JsonObject READER = jsonElement.getAsJsonObject();
            String NAMESPACE = JsonHelper.getString(READER, "namespace", Data.PERSPECTIVE_VERSION.getID());
            String SHADER = JsonHelper.getString(READER, "shader");
            boolean HIDE_ARMOR = JsonHelper.getBoolean(READER, "hide_armor", false);
            boolean HIDE_BLOCK_OUTLINE = JsonHelper.getBoolean(READER, "hide_block_outline", false);
            boolean HIDE_CROSSHAIR = JsonHelper.getBoolean(READER, "hide_crosshair", false);
            boolean HIDE_NAMETAGS = JsonHelper.getBoolean(READER, "hide_nametags", false);
            boolean DISABLE_SCREEN_MODE = JsonHelper.getBoolean(READER, "disable_screen_mode", false);
            boolean ENABLED = JsonHelper.getBoolean(READER, "enabled", true);
            add(NAMESPACE, SHADER, HIDE_ARMOR, HIDE_BLOCK_OUTLINE, HIDE_CROSSHAIR, HIDE_NAMETAGS, DISABLE_SCREEN_MODE, ENABLED);
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to load perspective shader: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
    }
    private void layout$souper_secret_settings(ResourceManager manager) {
        List<Resource> SHADER_LISTS = manager.getAllResources(new Identifier("souper_secret_settings", "shaders.json"));
        for (Resource resource : SHADER_LISTS) {
            try {
                for (JsonElement namespaces : JsonHelper.deserialize(resource.getReader()).getAsJsonArray("namespaces")) {
                    JsonObject NAMESPACES = JsonHelper.asObject(namespaces, "namespacelist");
                    List<String> HIDE_ARMOR_SHADERS = new ArrayList<>();
                    List<String> HIDE_CROSSHAIR_SHADERS = new ArrayList<>();
                    List<String> HIDE_BLOCK_OUTLINE_SHADERS = new ArrayList<>();
                    List<String> HIDE_NAMETAGS_SHADERS = new ArrayList<>();
                    List<String> DISABLE_SCREEN_MODE_SHADERS = new ArrayList<>();
                    for (JsonElement SHADER : JsonHelper.getArray(NAMESPACES, "hide_armor", new JsonArray())) HIDE_ARMOR_SHADERS.add(SHADER.getAsString());
                    for (JsonElement SHADER : JsonHelper.getArray(NAMESPACES, "hide_crosshair", new JsonArray())) HIDE_CROSSHAIR_SHADERS.add(SHADER.getAsString());
                    for (JsonElement SHADER : JsonHelper.getArray(NAMESPACES, "hide_block_outline", new JsonArray())) HIDE_BLOCK_OUTLINE_SHADERS.add(SHADER.getAsString());
                    for (JsonElement SHADER : JsonHelper.getArray(NAMESPACES, "hide_nametags", new JsonArray())) HIDE_NAMETAGS_SHADERS.add(SHADER.getAsString());
                    for (JsonElement SHADER : JsonHelper.getArray(NAMESPACES, "disable_screen_mode", new JsonArray())) DISABLE_SCREEN_MODE_SHADERS.add(SHADER.getAsString());
                    for (JsonElement SHADER : JsonHelper.getArray(NAMESPACES, "shaders", new JsonArray())) add(JsonHelper.getString(NAMESPACES, "namespace", Data.PERSPECTIVE_VERSION.getID()), SHADER.getAsString(), HIDE_ARMOR_SHADERS.contains(SHADER.getAsString()), HIDE_BLOCK_OUTLINE_SHADERS.contains(SHADER.getAsString()), HIDE_CROSSHAIR_SHADERS.contains(SHADER.getAsString()), HIDE_NAMETAGS_SHADERS.contains(SHADER.getAsString()), DISABLE_SCREEN_MODE_SHADERS.contains(SHADER.getAsString()), JsonHelper.getBoolean(NAMESPACES, "enabled", true));
                }
            } catch (Exception error) {
                Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to load souper secret settings shader list: {}", Data.PERSPECTIVE_VERSION.getID(), error);
            }
        }
    }
}