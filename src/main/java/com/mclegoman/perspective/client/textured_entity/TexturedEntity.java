/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.textured_entity;

import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.common.data.Data;
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
	public static Identifier getTexture(Entity entity, String entity_type, Affix affix_type, String affix, Identifier default_identifier) {
		try {
			List<String> registry = getRegistry(entity_type);
			String namespace = entity_type.substring(0, entity_type.lastIndexOf(":"));
			String entity_name = entity_type.substring(entity_type.lastIndexOf(":") + 1);
			if (entity.hasCustomName()) {
				if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "textured_named_entity") && registry.contains(Objects.requireNonNull(entity.getCustomName()).getString())) {
					if (!registry.get(registry.indexOf(entity.getCustomName().getString())).equalsIgnoreCase("default"))
						if (affix_type.equals(Affix.SUFFIX))
							return new Identifier(namespace, "textures/textured_entity/" + entity_name + "/" + registry.get(registry.indexOf(entity.getCustomName().getString())).toLowerCase() + affix + ".png");
						else if (affix_type.equals(Affix.PREFIX))
							return new Identifier(namespace, "textures/textured_entity/" + entity_name + "/" + affix + registry.get(registry.indexOf(entity.getCustomName().getString())).toLowerCase() + ".png");
				}
			}
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "textured_random_entity")) {
				if ((!(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "textured_named_entity")) || ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "textured_named_entity") && !registry.contains(entity.getCustomName()))) {
					int index = Math.floorMod(entity.getUuid().getLeastSignificantBits(), registry.size());
					if (!registry.get(index).equalsIgnoreCase("default"))
						if (affix_type.equals(Affix.SUFFIX)) return new Identifier(namespace, "textures/textured_entity/" + entity_name + "/" + registry.get(index).toLowerCase() + affix + ".png");
						else if (affix_type.equals(Affix.PREFIX)) return new Identifier(namespace, "textures/textured_entity/" + entity_name + "/" + affix + registry.get(index).toLowerCase() + ".png");
				}
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to set textured entity texture: {}", Data.VERSION.getID(), error);
		}
		return default_identifier;
	}
	public static Identifier getTexture(Entity entity, String entity_type, Identifier default_identifier) {
		return getTexture(entity, entity_type, Affix.SUFFIX, "", default_identifier);
	}
	private static List<String> getRegistry(String entity_type) {
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
	public enum Affix {
		PREFIX,
		SUFFIX;
	}
}