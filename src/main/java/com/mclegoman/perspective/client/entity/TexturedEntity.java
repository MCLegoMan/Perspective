/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.luminance.common.util.IdentifierHelper;
import com.mclegoman.perspective.config.ConfigHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class TexturedEntity {
	public static void init() {
		try {
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new TexturedEntityDataLoader());
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize textured entity texture: {}", error));
		}
	}
	private static Identifier getOverrideTexture(String prefix, String suffix, JsonArray overrides, Identifier fallback) {
		if (!overrides.isEmpty()) {
			for (JsonElement element : overrides) {
				String entityPrefix = JsonHelper.getString((JsonObject) element, "prefix", "");
				String entitySuffix = JsonHelper.getString((JsonObject) element, "suffix", "");
				String entityTexture = JsonHelper.getString((JsonObject) element, "texture", IdentifierHelper.stringFromIdentifier(fallback));
				String entityTextureNamespace = entityTexture.contains(":") ? entityTexture.substring(0, entityTexture.lastIndexOf(":")) : "minecraft";
				String entityTexturePath = entityTexture.contains(":") ? entityTexture.substring(entityTexture.lastIndexOf(":") + 1) : entityTexture;
				if (prefix.equals(entityPrefix) && suffix.equals(entitySuffix)) return Identifier.of(entityTextureNamespace, entityTexturePath.endsWith(".png") ? entityTexturePath : entityTexturePath + ".png");
			}
		}
		return fallback;
	}
	private static Identifier getOverrideTexture(JsonArray overrides, Identifier fallback) {
		return getOverrideTexture("", "", overrides, fallback);
	}
	public static Identifier getTexture(Entity entity, Identifier defaultIdentifier) {
		return getTexture(entity, "", "", "", defaultIdentifier);
	}
	public static Identifier getTexture(Entity entity, String overrideNamespace, Identifier defaultIdentifier) {
		return getTexture(entity, overrideNamespace, "", "", defaultIdentifier);
	}
	public static Identifier getTexture(Entity entity, String prefix, String suffix, Identifier defaultIdentifier) {
		return getTexture(entity, "", prefix, suffix, defaultIdentifier);
	}
	public static Identifier getTexture(Entity entity, String overrideNamespace, String prefix, String suffix, Identifier defaultIdentifier) {
		try {
			if (TexturedEntityDataLoader.isReady) {
				Identifier entityType = Registries.ENTITY_TYPE.getId(entity.getType());
				String namespace = defaultIdentifier.getNamespace();
				if (!overrideNamespace.isEmpty()) namespace = overrideNamespace;
				TexturedEntityData entityData = getEntity(entity);
				if (entityData != null) {
					boolean shouldReplaceTexture = true;
						if (entity instanceof LivingEntity) {
							JsonObject entitySpecific = entityData.getEntitySpecific();
							if (entitySpecific != null) {
								if (entitySpecific.has("ages")) {
									JsonObject ages = JsonHelper.getObject(entitySpecific, "ages", new JsonObject());
									if (((LivingEntity)entity).isBaby()) {
										if (ages.has("baby")) {
											JsonObject typeRegistry = JsonHelper.getObject(ages, "baby", new JsonObject());
											shouldReplaceTexture = JsonHelper.getBoolean(typeRegistry, "enabled", true);
										}
									} else {
										if (ages.has("adult")) {
											JsonObject typeRegistry = JsonHelper.getObject(ages, "adult", new JsonObject());
											shouldReplaceTexture = JsonHelper.getBoolean(typeRegistry, "enabled", true);
										}
									}
								}
							}
						}
					if (shouldReplaceTexture) return getOverrideTexture(prefix, suffix, entityData.getOverrides(), Identifier.of(namespace, "textures/textured_entity/" + entityType.getNamespace() + "/" + entityType.getPath() + "/" + (prefix + entityData.getName().toLowerCase() + suffix) + ".png"));
				}
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to set textured entity texture: {}", error));
		}
		return defaultIdentifier;
	}
	private static List<TexturedEntityData> getRegistry(String namespace, String entity_type) {
		List<TexturedEntityData> entityRegistry = new ArrayList<>();
		try {
			for (TexturedEntityData registry : TexturedEntityDataLoader.registry) {
				if (registry.getNamespace().equals(namespace) && registry.getType().equals(entity_type)) entityRegistry.add(registry);
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to get textured entity string registry: {}", error));
		}
		return entityRegistry;
	}
	public static TexturedEntityData getEntity(Entity entity) {
		return getEntity(entity, Registries.ENTITY_TYPE.getId(entity.getType()));
	}
	private static TexturedEntityData getEntity(Entity entity, Identifier entityId) {
		try {
			List<TexturedEntityData> registry = getRegistry(IdentifierHelper.getStringPart(IdentifierHelper.Type.NAMESPACE, IdentifierHelper.stringFromIdentifier(entityId)), IdentifierHelper.getStringPart(IdentifierHelper.Type.KEY, IdentifierHelper.stringFromIdentifier(entityId)));
			if (TexturedEntityDataLoader.isReady && !registry.isEmpty()) {
				if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "textured_named_entity")) {
					if (entity.hasCustomName() && !entity.getCustomName().getString().equalsIgnoreCase("default")) {
						for (TexturedEntityData entityData : registry) {
							if (entity.getCustomName().getString().equals(entityData.getName())) {
								return entityData;
							}
						}
					}
				}
				if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "textured_random_entity")) {
					int index = Math.floorMod(entity.getUuid().getLeastSignificantBits(), registry.size());
					TexturedEntityData entityData = registry.get(index);
					if (!entityData.getName().equalsIgnoreCase("default")) {
						return entityData;
					}
				}
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to get textured entity entity specific data: {}", error));
		}
		return null;
	}
}