/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.textured_entity;

import com.google.gson.JsonObject;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TexturedEntity {
	public static void init() {
		try {
			TexturedEntityModels.init();
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new TexturedEntityDataLoader());
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to initialize textured entity texture: {}", Data.VERSION.getID(), error);
		}
	}
	public static Identifier getTexture(Entity entity, String entity_type, String prefix, String suffix, Identifier default_identifier) {
		try {
			List<String> registry = getNameRegistry(entity_type);
			String namespace = entity_type.substring(0, entity_type.lastIndexOf(":"));
			String entity_name = entity_type.substring(entity_type.lastIndexOf(":") + 1);
			if (entity.hasCustomName()) {
				if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "textured_named_entity") && registry.contains(Objects.requireNonNull(entity.getCustomName()).getString())) {
					if (!registry.get(registry.indexOf(entity.getCustomName().getString())).equalsIgnoreCase("default")) {
						String texture = prefix + registry.get(registry.indexOf(entity.getCustomName().getString())).toLowerCase() + suffix;
						return new Identifier(namespace, "textures/textured_entity/" + entity_name + "/" + texture + ".png");
					}
				}
			}
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "textured_random_entity")) {
				if ((!(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "textured_named_entity")) || ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "textured_named_entity") && !registry.contains(String.valueOf(entity.getCustomName())))) {
					int index = Math.floorMod(entity.getUuid().getLeastSignificantBits(), registry.size());
					if (!registry.get(index).equalsIgnoreCase("default")) {
						String texture = prefix + registry.get(index).toLowerCase() + suffix;
						return new Identifier(namespace, "textures/textured_entity/" + entity_name + "/" + texture + ".png");
					}
				}
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to set textured entity texture: {}", Data.VERSION.getID(), error);
		}
		return default_identifier;
	}
	public static Identifier getTexture(Entity entity, String entity_type, Identifier default_identifier) {
		return getTexture(entity, entity_type, "", "", default_identifier);
	}
	public static Identifier getTexture(Entity entity, String entity_type, Affix affixType, String affix, Identifier default_identifier) {
		return getTexture(entity, entity_type, affixType.equals(Affix.PREFIX) ? affix : "", affixType.equals(Affix.SUFFIX) ? affix : "", default_identifier);
	}
	private static List<String> getNameRegistry(String entity_type) {
		List<String> entity_registry = new ArrayList<>();
		try {
			for (List<Object> registry : TexturedEntityDataLoader.REGISTRY) {
				String entityNamespace = (String) registry.get(0);
				String entityType = (String) registry.get(1);
				String name = (String) registry.get(2);
				if ((entityNamespace + ":" + entityType).equalsIgnoreCase(entity_type)) entity_registry.add(name);
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to get textured entity string registry: {}", Data.VERSION.getID(), error);
		}
		return entity_registry;
	}
	public static JsonObject getEntitySpecific(Entity entity, String type) {
		try {
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "textured_named_entity")) {
				if (entity.hasCustomName() && !entity.getCustomName().getString().equalsIgnoreCase("default")) {
					return (JsonObject)TexturedEntity.getRegistry(type, entity.getCustomName().getString(), 3);
				}
			}
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "textured_random_entity")) {
				List<String> registry = getNameRegistry(type);
				int index = Math.floorMod(entity.getUuid().getLeastSignificantBits(), registry.size());
				if (!registry.get(index).equalsIgnoreCase("default")) {
					return (JsonObject)TexturedEntity.getRegistry(type, registry.get(index), 3);
				}
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to get textured entity entity specific data: {}", Data.VERSION.getID(), error);
		}
		return null;
	}
	public static Object getRegistry(String type, String name, int index) {
		try {
			List<Object> registry = getRegistry(type, name);
			return registry.size() >= index ? registry.get(index) : null;
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to get textured entity registry (via index): {}", Data.VERSION.getID(), error);
		}
		return null;
	}
	public static List<Object> getRegistry(String type, String name) {
		List<Object> entityRegistry = new ArrayList<>();
		try {
			for (List<Object> registry : TexturedEntityDataLoader.REGISTRY) {
				String entityNamespace = (String) registry.get(0);
				String entityType = (String) registry.get(1);
				String entityName = (String) registry.get(2);
				JsonObject entitySpecific = (JsonObject) registry.get(3);
				if ((entityNamespace + ":" + entityType).equalsIgnoreCase(type) && entityName.equals(name)) {
					entityRegistry.add(entityNamespace);
					entityRegistry.add(entityType);
					entityRegistry.add(entityName);
					entityRegistry.add(entitySpecific);
				}

			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to get textured entity registry: {}", Data.VERSION.getID(), error);
		}
		return entityRegistry;
	}
	public enum Affix {
		PREFIX,
		SUFFIX
	}
}