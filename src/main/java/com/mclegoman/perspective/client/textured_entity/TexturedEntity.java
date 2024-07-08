/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.textured_entity;

import com.google.gson.JsonObject;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.luminance.common.util.IdentifierHelper;
import com.mclegoman.perspective.config.ConfigHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class TexturedEntity {
	public static void init() {
		try {
			TexturedEntityModels.init();
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new TexturedEntityDataLoader());
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize textured entity texture: {}", error));
		}
	}
	public static Identifier getTexture(Entity entity, String namespace, String entity_type, String prefix, String suffix, String textureNamespace, Identifier default_identifier) {
		try {
			if (TexturedEntityDataLoader.isReady) {
				List<String> registry = getNameRegistry(namespace, entity_type);
				if (!registry.isEmpty()) {
					String typeName = IdentifierHelper.getStringPart(IdentifierHelper.Type.KEY, entity_type);
					if (entity.hasCustomName() && entity.getCustomName() != null) {
						if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "textured_named_entity") && registry.contains(entity.getCustomName().getString())) {
							if (!registry.get(registry.indexOf(entity.getCustomName().getString())).equalsIgnoreCase("default")) {
								String texture = prefix + registry.get(registry.indexOf(entity.getCustomName().getString())).toLowerCase() + suffix;
								return Identifier.of(textureNamespace, "textures/textured_entity/" + typeName + "/" + texture + ".png");
							}
						}
					}
					if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "textured_random_entity")) {
						if ((!(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "textured_named_entity")) || ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "textured_named_entity") && !registry.contains(String.valueOf(entity.getCustomName())))) {
							int index = Math.floorMod(entity.getUuid().getLeastSignificantBits(), registry.size());
							if (!registry.get(index).equalsIgnoreCase("default")) {
								String texture = prefix + registry.get(index).toLowerCase() + suffix;
								return Identifier.of(textureNamespace, "textures/textured_entity/" + typeName + "/" + texture + ".png");
							}
						}
					}
				}
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to set textured entity texture: {}", error));
		}
		return default_identifier;
	}
	public static Identifier getTexture(Entity entity, String namespace, String entity_type, String prefix, String suffix, Identifier default_identifier) {
		return getTexture(entity, namespace, entity_type, prefix, suffix, default_identifier.getNamespace(), default_identifier);
	}
	public static Identifier getTexture(Entity entity, String entity_type, String prefix, String suffix, Identifier default_identifier) {
		return getTexture(entity, IdentifierHelper.getStringPart(IdentifierHelper.Type.NAMESPACE, entity_type), IdentifierHelper.getStringPart(IdentifierHelper.Type.KEY, entity_type), prefix, suffix, default_identifier);
	}
	public static Identifier getTexture(Entity entity, String entity_type, Identifier default_identifier) {
		return getTexture(entity, entity_type, "", "", default_identifier);
	}
	public static Identifier getTexture(Entity entity, String namespace, String entity_type, Identifier default_identifier) {
		return getTexture(entity, namespace, entity_type, "", "", default_identifier);
	}
	public static Identifier getTexture(Entity entity, String entity_type, Affix affixType, String affix, Identifier default_identifier) {
		return getTexture(entity, entity_type, affixType.equals(Affix.PREFIX) ? affix : "", affixType.equals(Affix.SUFFIX) ? affix : "", default_identifier);
	}
	public static Identifier getTexture(Entity entity, String namespace, String entity_type, Affix affixType, String affix, Identifier default_identifier) {
		return getTexture(entity, namespace, entity_type, affixType.equals(Affix.PREFIX) ? affix : "", affixType.equals(Affix.SUFFIX) ? affix : "", default_identifier);
	}
	private static List<String> getNameRegistry(String namespace, String entity_type) {
		List<String> entityRegistry = new ArrayList<>();
		try {
			for (List<Object> registry : TexturedEntityDataLoader.registry) {
				String entityNamespace = (String) registry.get(0);
				String entityType = (String) registry.get(1);
				String name = (String) registry.get(2);
				if (entityNamespace.equals(namespace) && entityType.equals(entity_type)) entityRegistry.add(name);
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to get textured entity string registry: {}", error));
		}
		return entityRegistry;
	}
	public static JsonObject getEntitySpecific(Entity entity, String namespace, String entity_type) {
		try {
			if (TexturedEntityDataLoader.isReady) {
				if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "textured_named_entity")) {
					if (entity.hasCustomName() && !entity.getCustomName().getString().equalsIgnoreCase("default")) {
						return (JsonObject)TexturedEntity.getRegistry(namespace, entity_type, entity.getCustomName().getString(), 3);
					}
				}
				if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "textured_random_entity")) {
					List<String> registry = getNameRegistry(IdentifierHelper.getStringPart(IdentifierHelper.Type.NAMESPACE, entity_type), IdentifierHelper.getStringPart(IdentifierHelper.Type.KEY, entity_type));
					int index = Math.floorMod(entity.getUuid().getLeastSignificantBits(), registry.size());
					if (!registry.get(index).equalsIgnoreCase("default")) {
						return (JsonObject)TexturedEntity.getRegistry(namespace, entity_type, registry.get(index), 3);
					}
				}
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to get textured entity entity specific data: {}", error));
		}
		return null;
	}
	public static Object getRegistry(String namespace, String type, String name, int index) {
		try {
			List<Object> registry = getRegistry(namespace, type, name);
			return registry.size() >= index ? registry.get(index) : null;
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to get textured entity registry (via index): {}", error));
		}
		return null;
	}
	public static List<Object> getRegistry(String namespace, String entity_type, String name) {
		List<Object> entityRegistry = new ArrayList<>();
		try {
			for (List<Object> registry : TexturedEntityDataLoader.registry) {
				String entityNamespace = (String) registry.get(0);
				String entityType = (String) registry.get(1);
				String entityName = (String) registry.get(2);
				JsonObject entitySpecific = (JsonObject) registry.get(3);
				if (entityNamespace.equals(namespace) && entityType.equals(entity_type) && entityName.equals(name)) {
					entityRegistry.add(entityNamespace);
					entityRegistry.add(entityType);
					entityRegistry.add(entityName);
					entityRegistry.add(entitySpecific);
				}

			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to get textured entity registry: {}", error));
		}
		return entityRegistry;
	}
	public enum Affix {
		PREFIX,
		SUFFIX
	}
}