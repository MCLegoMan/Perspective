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
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
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

	private void add(String NAMESPACE, String SHADER_NAME, boolean DISABLE_SCREEN_MODE, boolean ENABLED) {
		try {
			SHADER_NAME = SHADER_NAME.replace("\"", "").toLowerCase();
			Identifier ID = new Identifier(NAMESPACE.toLowerCase(), ("shaders/post/" + SHADER_NAME + ".json"));
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
			add("minecraft", "antialias", false, true);
			add("minecraft", "art", true, true);
			add("minecraft", "bits", true, true);
			add("minecraft", "blobs", true, true);
			add("minecraft", "blobs2", true, true);
			add("minecraft", "blur", true, true);
			add("minecraft", "bumpy", false, true);
			add("minecraft", "color_convolve", false, true);
			add("minecraft", "creeper", true, true);
			add("minecraft", "deconverge", false, true);
			add("minecraft", "desaturate", false, true);
			add("minecraft", "flip", true, true);
			add("minecraft", "fxaa", false, true);
			add("minecraft", "green", true, true);
			add("minecraft", "invert", false, true);
			add("minecraft", "notch", false, true);
			add("minecraft", "ntsc", true, true);
			add("minecraft", "outline", false, true);
			add("minecraft", "pencil", true, true);
			add("minecraft", "phosphor", false, true);
			add("minecraft", "scan_pincushion", false, true);
			add("minecraft", "sobel", false, true);
			add("minecraft", "spider", true, true);
			add("minecraft", "wobble", false, true);
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to add default shaders to registry: {}", Data.PERSPECTIVE_VERSION.getID(), error);
		}
	}

	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset();
			prepared.forEach(this::layout$perspective);
			layout$souper_secret_settings(manager);
			boolean saveConfig;
			if (Shader.updateLegacyConfig) {
				if (Shader.getFullShaderName(Shader.legacyIndex) != null) {
					ConfigHelper.setConfig("super_secret_settings_shader", Shader.getFullShaderName(Shader.legacyIndex));
				}
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

	private void layout$perspective(Identifier identifier, JsonElement jsonElement) {
		try {
			JsonObject READER = jsonElement.getAsJsonObject();
			String NAMESPACE = JsonHelper.getString(READER, "namespace", Data.PERSPECTIVE_VERSION.getID());
			String SHADER = JsonHelper.getString(READER, "shader");
			boolean DISABLE_SCREEN_MODE = JsonHelper.getBoolean(READER, "disable_screen_mode", false);
			boolean ENABLED = JsonHelper.getBoolean(READER, "enabled", true);
			add(NAMESPACE, SHADER, DISABLE_SCREEN_MODE, ENABLED);
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
						add(JsonHelper.getString(NAMESPACES, "namespace", Data.PERSPECTIVE_VERSION.getID()), SHADER.getAsString(), DISABLE_SCREEN_MODE_SHADERS.contains(SHADER.getAsString()), JsonHelper.getBoolean(NAMESPACES, "enabled", true));
				}
			} catch (Exception error) {
				Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to load souper secret settings shader list: {}", Data.PERSPECTIVE_VERSION.getID(), error);
			}
		}
	}
}