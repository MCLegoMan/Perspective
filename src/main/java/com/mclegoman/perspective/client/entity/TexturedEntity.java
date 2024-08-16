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
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

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
	public static void tick() {
		TexturedEntityModels.tick();
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
	public static Identifier getTexture(Entity entity, String namespace, String entity_type, String prefix, String suffix, Identifier default_identifier) {
		try {
			if (TexturedEntityDataLoader.isReady) {
				TexturedEntityData entityData = getEntity(entity, namespace, entity_type);
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
					if (shouldReplaceTexture) return getOverrideTexture(prefix, suffix, entityData.getOverrides(), Identifier.of(default_identifier.getNamespace(), "textures/textured_entity/" + IdentifierHelper.getStringPart(IdentifierHelper.Type.KEY, entity_type) + "/" + (prefix + entityData.getName().toLowerCase() + suffix) + ".png"));
				}
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to set textured entity texture: {}", error));
		}
		return default_identifier;
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
	public static TexturedEntityData getEntity(Entity entity, String namespace, String entity_type) {
		try {
			List<TexturedEntityData> registry = getRegistry(IdentifierHelper.getStringPart(IdentifierHelper.Type.NAMESPACE, entity_type), IdentifierHelper.getStringPart(IdentifierHelper.Type.KEY, entity_type));
			if (TexturedEntityDataLoader.isReady) {
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
	public enum Affix {
		PREFIX,
		SUFFIX
	}
}