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
import com.mclegoman.perspective.common.util.IdentifierHelper;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import java.util.*;

public class ShaderDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	protected static boolean isReloading;
	public static final List<List<Object>> registry = new ArrayList<>();
	public static final List<List<Object>> entityLinkRegistry = new ArrayList<>();
	public static final List<String> duplicatedNames = new ArrayList<>();
	public static final String ID = "shaders/shaders";
	public ShaderDataLoader() {
		super(new Gson(), ID);
	}
	public static int getShaderAmount() {
		return registry.size();
	}
	public static boolean isDuplicatedShaderName(String name) {
		return duplicatedNames.contains(name);
	}
	protected static List<Object> getFallbackShader() {
		List<Object> shaderMap = new ArrayList<>();
		String namespace = "minecraft";
		String shaderName = "blit";
		shaderMap.add(namespace.toLowerCase() + ":" + shaderName.toLowerCase());
		shaderMap.add(namespace.toLowerCase());
		shaderMap.add(shaderName.toLowerCase());
		shaderMap.add(true);
		shaderMap.add(false);
		shaderMap.add(new ArrayList<>());
		return shaderMap;
	}
	protected static Object get(int SHADER, RegistryValue VALUE) {
		List<Object> SHADER_MAP = SHADER <= registry.size() ? registry.get(SHADER) : getFallbackShader();
		if (VALUE.equals(RegistryValue.id)) return SHADER_MAP.get(0);
		if (VALUE.equals(RegistryValue.namespace)) return SHADER_MAP.get(1);
		if (VALUE.equals(RegistryValue.shaderName)) return SHADER_MAP.get(2);
		if (VALUE.equals(RegistryValue.disableScreenMode)) return SHADER_MAP.get(3);
		if (VALUE.equals(RegistryValue.translatable)) return SHADER_MAP.get(4);
		if (VALUE.equals(RegistryValue.disableSoup)) return SHADER_MAP.get(5);
		if (VALUE.equals(RegistryValue.entityLinks)) return SHADER_MAP.get(6);
		if (VALUE.equals(RegistryValue.custom)) return SHADER_MAP.get(7);
		return null;
	}
	protected static JsonObject getCustom(int shaderIndex, String namespace) {
		JsonObject customDatas = (JsonObject) get(shaderIndex, RegistryValue.custom);
		if (customDatas != null) {
			if (customDatas.has(namespace)) {
				return JsonHelper.getObject(customDatas, namespace);
			}
		}
		return null;
	}
	public static String guessPostShaderNamespace(String id) {
		// If the shader registry contains at least one shader with the name, the first detected instance will be used.
		if (!id.contains(":")) {
			for (List<Object> registry : ShaderDataLoader.registry) {
				if (id.equalsIgnoreCase((String)registry.get(2))) return (String)registry.get(1);
			}
		}
		// This will fall back to the minecraft namespace if no namespace is present.
		return IdentifierHelper.getStringPart(IdentifierHelper.Type.NAMESPACE, id, "minecraft");
	}
	public static Identifier getPostShader(String id) {
		String namespace = IdentifierHelper.getStringPart(IdentifierHelper.Type.NAMESPACE, id, "minecraft");
		String shader = IdentifierHelper.getStringPart(IdentifierHelper.Type.KEY, id);
		return getPostShader(namespace, shader);
	}
	public static Identifier getPostShader(String namespace, String shader) {
		if (namespace != null && shader != null) {
			shader = shader.replace("\"", "").toLowerCase();
			return new Identifier(namespace.toLowerCase(), ("shaders/post/" + shader + ".json"));
		}
		return null;
	}
	private void add(String namespace, String shaderName, boolean disableScreenMode, boolean translatable, boolean disableSoup, JsonObject custom, ResourceManager manager) {
		try {
			String id = namespace.toLowerCase() + ":" + shaderName.toLowerCase();
			manager.getResourceOrThrow(getPostShader(id));
			List<Object> shaderMap = new ArrayList<>();
			shaderMap.add(id);
			shaderMap.add(namespace.toLowerCase());
			shaderMap.add(shaderName.toLowerCase());
			shaderMap.add(disableScreenMode);
			shaderMap.add(translatable);
			shaderMap.add(disableSoup);
			shaderMap.add(custom);
			boolean alreadyRegistered = false;
			for (List<Object> SHADER_MAP_IN_REGISTRY : registry) {
				if (SHADER_MAP_IN_REGISTRY.contains(id)) {
					alreadyRegistered = true;
					break;
				}
			}
			if (!alreadyRegistered) registry.add(shaderMap);
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, "Failed to add shader to registry: " + error);
		}
	}
	private void reset(ResourceManager manager) {
		try {
			registry.clear();
			entityLinkRegistry.clear();
			duplicatedNames.clear();
			add$default(manager);
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to reset shaders registry: {}", Data.VERSION.getID(), error);
		}
	}
	private void clearNamespace(String namespace) {
		try {
			registry.removeIf((shader) -> shader.get(1).toString().equalsIgnoreCase(namespace));
			entityLinkRegistry.removeIf((shader) -> shader.get(0).toString().equalsIgnoreCase(namespace));
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to remove {} namespace shaders from the shaders registry: {}", Data.VERSION.getID(), namespace, error);
		}
	}
	private void removeShader(String namespace, String name) {
		try {
			registry.removeIf((shader) -> shader.get(1).toString().equalsIgnoreCase(namespace) && shader.get(2).toString().equalsIgnoreCase(name));
			entityLinkRegistry.removeIf((shader) -> shader.get(1).toString().equalsIgnoreCase(namespace) && shader.get(2).toString().equalsIgnoreCase(name));
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to remove {} namespace shaders from the shaders registry: {}", Data.VERSION.getID(), namespace, error);
		}
	}
	private void add$default(ResourceManager manager) {
		try {
			add("minecraft", "blur", true, true, false, new JsonObject(), manager);
			add("minecraft", "creeper", true, true, false, new JsonObject(), manager);
			add("minecraft", "invert", false, true, false, new JsonObject(), manager);
			add("minecraft", "spider", true, true, false, new JsonObject(), manager);
			add("perspective", "gaussian", true, true, false, new JsonObject(), manager);
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to add default shaders to registry: {}", Data.VERSION.getID(), error);
		}
	}
	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset(manager);
			Shader.releaseShaders();
			prepared.forEach((identifier, jsonElement) -> layout$perspective(jsonElement, manager));
			layout$souper_secret_settings(manager);
			layout$souper_secret_settings$entity_links(manager);
			List<String> ALL_NAMES = new ArrayList<>();
			for (List<Object> registry : registry) {
				if (!ALL_NAMES.contains((String) registry.get(1))) ALL_NAMES.add((String) registry.get(1));
				else {
					if (!duplicatedNames.contains((String) registry.get(1)))
						duplicatedNames.add((String) registry.get(1));
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
	private void layout$perspective(JsonElement jsonElement, ResourceManager manager) {
		try {
			JsonObject reader = jsonElement.getAsJsonObject();
			String namespace = JsonHelper.getString(reader, "namespace", Data.VERSION.getID());
			String shader = JsonHelper.getString(reader, "shader");
			boolean disableScreenMode = JsonHelper.getBoolean(reader, "disable_screen_mode", false);
			boolean translatable = JsonHelper.getBoolean(reader, "translatable", false);
			boolean disableSoup = JsonHelper.getBoolean(reader, "disable_soup", false);
			JsonArray entityLinks = JsonHelper.getArray(reader, "entity_links", new JsonArray());
			JsonObject custom = JsonHelper.getObject(reader, "custom", new JsonObject());
			boolean enabled = JsonHelper.getBoolean(reader, "enabled");
			if (enabled) {
				add(namespace, shader, disableScreenMode, translatable, disableSoup, custom, manager);
				for (JsonElement entity : entityLinks) {
					List<Object> entityLink = new ArrayList<>();
					entityLink.add(entity.getAsString());
					entityLink.add(namespace + ":" + shader);
					entityLinkRegistry.add(entityLink);
				}
			}
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
					JsonObject reader = JsonHelper.deserialize(resource.getReader());
					if (JsonHelper.getBoolean(reader, "replace", false)) reset(manager);
					if (JsonHelper.hasArray(reader, "namespaces") && !JsonHelper.getArray(reader, "namespaces").isEmpty()) {
						for (JsonElement namespaces : reader.getAsJsonArray("namespaces")) {
							JsonObject namespaceList = JsonHelper.asObject(namespaces, "namespacelist");
							String namespace = JsonHelper.getString(namespaceList, "namespace", "minecraft");
							boolean replace = JsonHelper.getBoolean(namespaceList, "replace", false);
							boolean translatable = JsonHelper.getBoolean(namespaceList, "translatable", false);
							if (replace) clearNamespace(namespace);
							List<String> disableScreenMode = new ArrayList<>();
							for (JsonElement shader : JsonHelper.getArray(namespaceList, "disable_screen_mode", new JsonArray()))
								disableScreenMode.add(shader.getAsString());
							List<String> disableSoup = new ArrayList<>();
							for (JsonElement shader : JsonHelper.getArray(namespaceList, "disable_soup", new JsonArray()))
								disableSoup.add(shader.getAsString());
							JsonObject custom = JsonHelper.getObject(namespaceList, "custom", new JsonObject());
							for (JsonElement shader : JsonHelper.getArray(namespaceList, "shaders", new JsonArray()))
								add(namespace, shader.getAsString(), disableScreenMode.contains(shader.getAsString()), translatable, disableSoup.contains(shader.getAsString()), JsonHelper.getObject(custom, shader.getAsString(), new JsonObject()), manager);
						}
					}
				} catch (Exception error) {
					Data.VERSION.getLogger().warn("{} Failed to load souper secret settings shader list: {}", Data.VERSION.getID(), error);
				}
			}
		}
	}
	private void layout$souper_secret_settings$entity_links(ResourceManager manager) {
		for (String NAMESPACE : manager.getAllNamespaces()) {
			List<Resource> SHADER_LISTS = manager.getAllResources(new Identifier(NAMESPACE, "shaders.json"));
			for (Resource resource : SHADER_LISTS) {
				try {
					JsonObject reader = JsonHelper.deserialize(resource.getReader());
					if (JsonHelper.hasJsonObject(reader, "entity_links")) {
						JsonObject entityLinks = JsonHelper.getObject(reader, "entity_links");
						Set<Map.Entry<String, JsonElement>> entitySet = entityLinks.entrySet();
						for (Map.Entry<String, JsonElement> entry: entitySet) {
							List<Object> entityLink = new ArrayList<>();
							entityLink.add(entry.getKey());
							entityLink.add(JsonHelper.asString(entry.getValue(), "shader"));
							entityLinkRegistry.add(entityLink);
						}
					}
				} catch (Exception error) {
					Data.VERSION.getLogger().warn("{} Failed to load souper secret settings shader entity links: {}", Data.VERSION.getID(), error);
				}
			}
		}
	}
	public enum RegistryValue {
		id,
		namespace,
		shaderName,
		disableScreenMode,
		translatable,
		disableSoup,
		entityLinks,
		custom
	}
}