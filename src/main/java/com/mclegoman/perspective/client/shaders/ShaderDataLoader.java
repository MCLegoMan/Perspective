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
	protected static Object get(int SHADER, RegistryValue VALUE) {
		if (SHADER <= REGISTRY.size()) {
			List<Object> SHADER_MAP = REGISTRY.get(SHADER);
			if (VALUE.equals(RegistryValue.ID)) return SHADER_MAP.get(0);
			if (VALUE.equals(RegistryValue.NAMESPACE)) return SHADER_MAP.get(1);
			if (VALUE.equals(RegistryValue.SHADER_NAME)) return SHADER_MAP.get(2);
			if (VALUE.equals(RegistryValue.DISABLE_SCREEN_MODE)) return SHADER_MAP.get(3);
			if (VALUE.equals(RegistryValue.TRANSLATABLE)) return SHADER_MAP.get(4);
			if (VALUE.equals(RegistryValue.CUSTOM)) return SHADER_MAP.get(5);
		}
		return null;
	}
	protected static JsonObject getCustom(int shaderIndex, String namespace) {
		JsonObject customDatas = (JsonObject) get(shaderIndex, RegistryValue.CUSTOM);
		if (customDatas != null) {
			if (customDatas.has(namespace)) {
				return JsonHelper.getObject(customDatas, namespace);
			}
		}
		return null;
	}
	private void add(String namespace, String shaderName, boolean disableScreenMode, boolean translatable, JsonObject custom, ResourceManager manager) {
		try {
			shaderName = shaderName.replace("\"", "").toLowerCase();
			Identifier ID = new Identifier(namespace.toLowerCase(), ("shaders/post/" + shaderName + ".json"));
			try {
				manager.getResourceOrThrow(ID);
				List<Object> SHADER_MAP = new ArrayList<>();
				SHADER_MAP.add(ID);
				SHADER_MAP.add(namespace.toLowerCase());
				SHADER_MAP.add(shaderName);
				SHADER_MAP.add(disableScreenMode);
				SHADER_MAP.add(translatable);
				SHADER_MAP.add(custom);
				boolean ALREADY_REGISTERED = false;
				for (List<Object> SHADER_MAP_IN_REGISTRY : REGISTRY) {
					if (SHADER_MAP_IN_REGISTRY.contains(ID)) {
						ALREADY_REGISTERED = true;
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
			add("minecraft", "antialias", false, true, new JsonObject(), manager);
			add("minecraft", "art", true, true, new JsonObject(), manager);
			add("minecraft", "bits", true, true, new JsonObject(), manager);
			add("minecraft", "blobs", true, true, new JsonObject(), manager);
			add("minecraft", "blobs2", true, true, new JsonObject(), manager);
			add("minecraft", "blur", true, true, new JsonObject(), manager);
			add("minecraft", "bumpy", false, true, new JsonObject(), manager);
			add("minecraft", "color_convolve", false, true, new JsonObject(), manager);
			add("minecraft", "creeper", true, true, new JsonObject(), manager);
			add("minecraft", "deconverge", false, true, new JsonObject(), manager);
			add("minecraft", "desaturate", false, true, new JsonObject(), manager);
			add("minecraft", "flip", true, true, new JsonObject(), manager);
			add("minecraft", "fxaa", false, true, new JsonObject(), manager);
			add("minecraft", "green", true, true, new JsonObject(), manager);
			add("minecraft", "invert", false, true, new JsonObject(), manager);
			add("minecraft", "notch", false, true, new JsonObject(), manager);
			add("minecraft", "ntsc", true, true, new JsonObject(), manager);
			add("minecraft", "outline", false, true, new JsonObject(), manager);
			add("minecraft", "pencil", true, true, new JsonObject(), manager);
			add("minecraft", "phosphor", false, true, new JsonObject(), manager);
			add("minecraft", "scan_pincushion", false, true, new JsonObject(), manager);
			add("minecraft", "sobel", false, true, new JsonObject(), manager);
			add("minecraft", "spider", true, true, new JsonObject(), manager);
			add("minecraft", "wobble", false, true, new JsonObject(), manager);
			add("minecraft", "love", false, true, new JsonObject(), manager);
			add("perspective", "gaussian", true, true, new JsonObject(), manager);
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to add default shaders to registry: {}", Data.VERSION.getID(), error);
		}
	}
	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset(manager);
			Shader.releaseShaders();
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
			JsonObject reader = jsonElement.getAsJsonObject();
			String namespace = JsonHelper.getString(reader, "namespace", Data.VERSION.getID());
			String shader = JsonHelper.getString(reader, "shader");
			boolean disableScreenMode = JsonHelper.getBoolean(reader, "disable_screen_mode", false);
			boolean translatable = JsonHelper.getBoolean(reader, "translatable", false);
			JsonObject custom = JsonHelper.getObject(reader, "custom", new JsonObject());
			boolean enabled = JsonHelper.getBoolean(reader, "enabled");
			if (enabled) add(namespace, shader, disableScreenMode, translatable, custom, manager);
			else removeShader(namespace, shader);
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
						JsonObject namespaceList = JsonHelper.asObject(namespaces, "namespacelist");
						String namespace = JsonHelper.getString(namespaceList, "namespace", Data.VERSION.getID());
						boolean replace = JsonHelper.getBoolean(namespaceList, "replace", false);
						boolean translatable = JsonHelper.getBoolean(namespaceList, "translatable", false);
						if (replace) clearNamespace(namespace);
						List<String> disableScreenMode = new ArrayList<>();
						for (JsonElement shader : JsonHelper.getArray(namespaceList, "disable_screen_mode", new JsonArray()))
							disableScreenMode.add(shader.getAsString());
						for (JsonElement shader : JsonHelper.getArray(namespaceList, "shaders", new JsonArray()))
							add(namespace, shader.getAsString(), disableScreenMode.contains(shader.getAsString()), translatable, JsonHelper.getObject(namespaceList, "custom", new JsonObject()), manager);
					}
				} catch (Exception error) {
					Data.VERSION.getLogger().warn("{} Failed to load souper secret settings shader list: {}", Data.VERSION.getID(), error);
				}
			}
		}
	}
	public enum RegistryValue {
		ID,
		NAMESPACE,
		SHADER_NAME,
		DISABLE_SCREEN_MODE,
		TRANSLATABLE,
		CUSTOM
	}
}