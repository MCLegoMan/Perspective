/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.shaders;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShaderDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
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

	private void add(String NAMESPACE, String SHADER_NAME, boolean DISABLE_SCREEN_MODE, boolean ENABLED, ResourceManager manager) {
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
				if (ENABLED) {
					if (!ALREADY_REGISTERED) REGISTRY.add(SHADER_MAP);
				} else REGISTRY.remove(REGISTRY_MAP);
			} catch (FileNotFoundException error) {
				Data.PERSPECTIVE_VERSION.sendToLog(Helper.LogType.WARN, "Failed to register shader: " + error);
			}
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.sendToLog(Helper.LogType.WARN, "Failed to add shader to registry: " + error);
		}
	}

	private void reset(ResourceManager manager) {
		try {
			REGISTRY.clear();
			DUPLICATED_NAMES.clear();
			add$default(manager);
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to reset shaders registry: {}", Data.PERSPECTIVE_VERSION.getID(), error);
		}
	}

	private void add$default(ResourceManager manager) {
		try {
			add("minecraft", "antialias", false, true, manager);
			add("minecraft", "art", true, true, manager);
			add("minecraft", "bits", true, true, manager);
			add("minecraft", "blobs", true, true, manager);
			add("minecraft", "blobs2", true, true, manager);
			add("minecraft", "blur", true, true, manager);
			add("minecraft", "bumpy", false, true, manager);
			add("minecraft", "color_convolve", false, true, manager);
			add("minecraft", "creeper", true, true, manager);
			add("minecraft", "deconverge", false, true, manager);
			add("minecraft", "desaturate", false, true, manager);
			add("minecraft", "flip", true, true, manager);
			add("minecraft", "fxaa", false, true, manager);
			add("minecraft", "green", true, true, manager);
			add("minecraft", "invert", false, true, manager);
			add("minecraft", "notch", false, true, manager);
			add("minecraft", "ntsc", true, true, manager);
			add("minecraft", "outline", false, true, manager);
			add("minecraft", "pencil", true, true, manager);
			add("minecraft", "phosphor", false, true, manager);
			add("minecraft", "scan_pincushion", false, true, manager);
			add("minecraft", "sobel", false, true, manager);
			add("minecraft", "spider", true, true, manager);
			add("minecraft", "wobble", false, true, manager);
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to add default shaders to registry: {}", Data.PERSPECTIVE_VERSION.getID(), error);
		}
	}

	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset(manager);
			prepared.forEach((identifier, jsonElement) -> layout$perspective(identifier, jsonElement, manager));
			layout$souper_secret_settings(manager);
			boolean saveConfig;
			if (Shader.updateLegacyConfig) {
				if (Shader.getFullShaderName(Shader.legacyIndex) != null && Shader.isShaderAvailable(Shader.legacyIndex)) {
					ConfigHelper.setConfig("super_secret_settings_shader", Shader.getFullShaderName(Shader.legacyIndex));
				}
				Shader.updateLegacyConfig = false;
			}
			if (Shader.isShaderAvailable((String) ConfigHelper.getConfig("super_secret_settings_shader"))) {
				Shader.superSecretSettingsIndex = Shader.getShaderValue((String) ConfigHelper.getConfig("super_secret_settings_shader"));
				saveConfig = false;
			} else {
				Shader.superSecretSettingsIndex = Math.min(Shader.superSecretSettingsIndex, REGISTRY.size() - 1);
				saveConfig = true;
			}
			if ((boolean) ConfigHelper.getConfig("super_secret_settings_enabled"))
				Shader.set(true, false, false, saveConfig);
			List<String> ALL_NAMES = new ArrayList<>();
			for (List<Object> registry : REGISTRY) {
				if (!ALL_NAMES.contains((String) registry.get(1))) ALL_NAMES.add((String) registry.get(1));
				else {
					if (!DUPLICATED_NAMES.contains((String) registry.get(1)))
						DUPLICATED_NAMES.add((String) registry.get(1));
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

	private void layout$perspective(Identifier identifier, JsonElement jsonElement, ResourceManager manager) {
		try {
			JsonObject READER = jsonElement.getAsJsonObject();
			String NAMESPACE = JsonHelper.getString(READER, "namespace", Data.PERSPECTIVE_VERSION.getID());
			String SHADER = JsonHelper.getString(READER, "shader");
			boolean DISABLE_SCREEN_MODE = JsonHelper.getBoolean(READER, "disable_screen_mode", false);
			boolean ENABLED = JsonHelper.getBoolean(READER, "enabled", true);
			add(NAMESPACE, SHADER, DISABLE_SCREEN_MODE, ENABLED, manager);
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
					List<String> DISABLE_SCREEN_MODE_SHADERS = new ArrayList<>();
					for (JsonElement SHADER : JsonHelper.getArray(NAMESPACES, "disable_screen_mode", new JsonArray()))
						DISABLE_SCREEN_MODE_SHADERS.add(SHADER.getAsString());
					for (JsonElement SHADER : JsonHelper.getArray(NAMESPACES, "shaders", new JsonArray()))
						add(JsonHelper.getString(NAMESPACES, "namespace", Data.PERSPECTIVE_VERSION.getID()), SHADER.getAsString(), DISABLE_SCREEN_MODE_SHADERS.contains(SHADER.getAsString()), JsonHelper.getBoolean(NAMESPACES, "enabled", true), manager);
				}
			} catch (Exception error) {
				Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to load souper secret settings shader list: {}", Data.PERSPECTIVE_VERSION.getID(), error);
			}
		}
	}
}