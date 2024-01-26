/*
    Perspective
    Contributor(s): MCLegoMan, Nettakrim
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.shaders;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.gl.ShaderStage;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShaderDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	protected static boolean isReloading;
	public static final List<List<Object>> REGISTRY = new ArrayList<>();
	public static final List<String> DUPLICATED_NAMES = new ArrayList<>();
	public static final String ID = "shaders/shaders";
	public ShaderDataLoader() {
		super(new Gson(), ID);
	}
	public static int getShaderAmount() {
		return REGISTRY.size() - 1;
	}
	public static boolean isDuplicatedShaderName(String name) {
		return DUPLICATED_NAMES.contains(name);
	}
	public static Object get(int SHADER, ShaderRegistryValue VALUE) {
		List<Object> SHADER_MAP = REGISTRY.get(SHADER);
		if (VALUE.equals(ShaderRegistryValue.ID)) return SHADER_MAP.get(0);
		if (VALUE.equals(ShaderRegistryValue.NAMESPACE)) return SHADER_MAP.get(1);
		if (VALUE.equals(ShaderRegistryValue.SHADER_NAME)) return SHADER_MAP.get(2);
		if (VALUE.equals(ShaderRegistryValue.DISABLE_SCREEN_MODE)) return SHADER_MAP.get(3);
		return null;
	}
	private void add(String NAMESPACE, String SHADER_NAME, boolean DISABLE_SCREEN_MODE, ResourceManager manager) {
		try {
			SHADER_NAME = SHADER_NAME.replace("\"", "").toLowerCase();
			Identifier ID = new Identifier(NAMESPACE.toLowerCase(), ("shaders/post/" + SHADER_NAME + ".json"));
			try {
				manager.getResourceOrThrow(ID);
				List<Object> SHADER_MAP = new ArrayList<>();
				SHADER_MAP.add(ID);
				SHADER_MAP.add(NAMESPACE.toLowerCase());
				SHADER_MAP.add(SHADER_NAME);
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
				if (!ALREADY_REGISTERED) REGISTRY.add(SHADER_MAP);
			} catch (FileNotFoundException error) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Failed to register shader: " + error);
			}
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, "Failed to add shader to registry: " + error);
		}
	}
	private void reset(ResourceManager manager) {
		try {
			REGISTRY.clear();
			DUPLICATED_NAMES.clear();
			add$default(manager);
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to reset shaders registry: {}", Data.VERSION.getID(), error);
		}
	}
	private void clearNamespace(String namespace) {
		try {
			REGISTRY.removeIf((SHADER) -> SHADER.get(1).toString().equalsIgnoreCase(namespace));
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to remove {} namespace shaders from the shaders registry: {}", Data.VERSION.getID(), namespace, error);
		}
	}
	private void removeShader(String namespace, String shader) {
		try {
			REGISTRY.removeIf((SHADER) -> SHADER.get(1).toString().equalsIgnoreCase(namespace) && SHADER.get(2).toString().equalsIgnoreCase(shader));
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to remove {} namespace shaders from the shaders registry: {}", Data.VERSION.getID(), namespace, error);
		}
	}
	private void add$default(ResourceManager manager) {
		try {
			add("minecraft", "antialias", false, manager);
			add("minecraft", "art", true, manager);
			add("minecraft", "bits", true, manager);
			add("minecraft", "blobs", true, manager);
			add("minecraft", "blobs2", true, manager);
			add("minecraft", "blur", true, manager);
			add("minecraft", "bumpy", false, manager);
			add("minecraft", "color_convolve", false, manager);
			add("minecraft", "creeper", true, manager);
			add("minecraft", "deconverge", false, manager);
			add("minecraft", "desaturate", false, manager);
			add("minecraft", "flip", true, manager);
			add("minecraft", "fxaa", false, manager);
			add("minecraft", "green", true, manager);
			add("minecraft", "invert", false, manager);
			add("minecraft", "notch", false, manager);
			add("minecraft", "ntsc", true, manager);
			add("minecraft", "outline", false, manager);
			add("minecraft", "pencil", true, manager);
			add("minecraft", "phosphor", false, manager);
			add("minecraft", "scan_pincushion", false, manager);
			add("minecraft", "sobel", false, manager);
			add("minecraft", "spider", true, manager);
			add("minecraft", "wobble", false, manager);
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to add default shaders to registry: {}", Data.VERSION.getID(), error);
		}
	}
	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset(manager);
			releaseShaders();
			prepared.forEach((identifier, jsonElement) -> layout$perspective(identifier, jsonElement, manager));
			layout$souper_secret_settings(manager);
			List<String> ALL_NAMES = new ArrayList<>();
			for (List<Object> registry : REGISTRY) {
				if (!ALL_NAMES.contains((String) registry.get(1))) ALL_NAMES.add((String) registry.get(1));
				else {
					if (!DUPLICATED_NAMES.contains((String) registry.get(1)))
						DUPLICATED_NAMES.add((String) registry.get(1));
				}
			}
			isReloading = true;
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to apply shaders dataloader: {}", Data.VERSION.getID(), error);
		}
	}
	@Override
	public Identifier getFabricId() {
		return new Identifier(Data.VERSION.getID(), ID);
	}
	private void layout$perspective(Identifier identifier, JsonElement jsonElement, ResourceManager manager) {
		try {
			JsonObject READER = jsonElement.getAsJsonObject();
			String NAMESPACE = JsonHelper.getString(READER, "namespace", Data.VERSION.getID());
			String SHADER = JsonHelper.getString(READER, "shader");
			boolean DISABLE_SCREEN_MODE = JsonHelper.getBoolean(READER, "disable_screen_mode", false);
			boolean ENABLED = JsonHelper.getBoolean(READER, "enabled");
			if (ENABLED) add(NAMESPACE, SHADER, DISABLE_SCREEN_MODE, manager);
			else removeShader(NAMESPACE, SHADER);
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to load perspective shader: {}", Data.VERSION.getID(), error);
		}
	}
	private void layout$souper_secret_settings(ResourceManager manager) {
		for (String NAMESPACE : manager.getAllNamespaces()) {
			List<Resource> SHADER_LISTS = manager.getAllResources(new Identifier(NAMESPACE, "shaders.json"));
			for (Resource resource : SHADER_LISTS) {
				try {
					if (JsonHelper.getBoolean(JsonHelper.deserialize(resource.getReader()), "replace", false)) reset(manager);
					for (JsonElement namespaces : JsonHelper.deserialize(resource.getReader()).getAsJsonArray("namespaces")) {
						JsonObject NAMESPACE_LIST = JsonHelper.asObject(namespaces, "namespacelist");
						String SHADER_NAMESPACE = JsonHelper.getString(NAMESPACE_LIST, "namespace", Data.VERSION.getID());
						boolean REPLACE = JsonHelper.getBoolean(NAMESPACE_LIST, "replace", false);
						if (REPLACE) clearNamespace(NAMESPACE);
						List<String> DISABLE_SCREEN_MODE_SHADERS = new ArrayList<>();
						for (JsonElement SHADER : JsonHelper.getArray(NAMESPACE_LIST, "disable_screen_mode", new JsonArray()))
							DISABLE_SCREEN_MODE_SHADERS.add(SHADER.getAsString());
						for (JsonElement SHADER : JsonHelper.getArray(NAMESPACE_LIST, "shaders", new JsonArray()))
							add(SHADER_NAMESPACE, SHADER.getAsString(), DISABLE_SCREEN_MODE_SHADERS.contains(SHADER.getAsString()), manager);
					}
				} catch (Exception error) {
					Data.VERSION.getLogger().warn("{} Failed to load souper secret settings shader list: {}", Data.VERSION.getID(), error);
				}
			}
		}
	}
	// Nettakrim:Souper-Secret-Settings:ShaderResourceLoader.releaseFromType(ShaderStage.Type type);
	// https://github.com/Nettakrim/Souper-Secret-Settings/blob/main/src/main/java/com/nettakrim/souper_secret_settings/ShaderResourceLoader.java
	private void releaseShaders() {
		try {
			List<ShaderStage.Type> shaderTypes = new ArrayList<>();
			shaderTypes.add(ShaderStage.Type.VERTEX);
			shaderTypes.add(ShaderStage.Type.FRAGMENT);
			for (ShaderStage.Type type : shaderTypes) {
				List<Map.Entry<String, ShaderStage>> loadedShaders = type.getLoadedShaders().entrySet().stream().toList();
				for (int index = loadedShaders.size() - 1; index > -1; index--) {
					Map.Entry<String, ShaderStage> loadedShader = loadedShaders.get(index);
					String name = loadedShader.getKey();
					if (name.startsWith("rendertype_")) continue;
					if (name.startsWith("position_")) continue;
					if (name.equals("position") || name.equals("particle")) continue;
					loadedShader.getValue().release();
				}
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to release shaders: {}", Data.VERSION.getID(), error);
		}
	}
}