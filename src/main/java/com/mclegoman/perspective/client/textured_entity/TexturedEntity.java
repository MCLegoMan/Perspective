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
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.ArrayList;
import java.util.List;

public class TexturedEntity {
	public static void init() {
		try {
			TexturedEntityModels.init();
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new TexturedEntityDataLoader());
		} catch (Exception error) {
			Data.version.getLogger().warn("{} Failed to initialize textured entity texture: {}", Data.version.getID(), error);
		}
	}
	public static Identifier getTexture(Entity entity, String entity_type, String suffix, Identifier default_identifier) {
		try {
			List<String> stringReg = getStringRegistry(entity_type);
			List<Text> textReg = getTextRegistry(entity_type);
			String namespace = entity_type.substring(0, entity_type.lastIndexOf(":"));
			String entity_name = entity_type.substring(entity_type.lastIndexOf(":") + 1);
			if ((boolean) ConfigHelper.getConfig("textured_named_entity") && textReg.contains(entity.getCustomName())) {
				if (!stringReg.get(textReg.indexOf(entity.getCustomName())).equalsIgnoreCase("default"))
					return new Identifier(namespace, "textures/textured_entity/" + entity_name + "/" + stringReg.get(textReg.indexOf(entity.getCustomName())).toLowerCase() + suffix + ".png");
			}
			if ((boolean) ConfigHelper.getConfig("textured_random_entity")) {
				if ((!(boolean) ConfigHelper.getConfig("textured_named_entity")) || ((boolean) ConfigHelper.getConfig("textured_named_entity") && !textReg.contains(entity.getCustomName()))) {
					int index = Math.floorMod(entity.getUuid().getLeastSignificantBits(), stringReg.size());
					if (!stringReg.get(index).equalsIgnoreCase("default"))
						return new Identifier(namespace, "textures/textured_entity/" + entity_name + "/" + stringReg.get(index).toLowerCase() + suffix + ".png");
				}
			}
		} catch (Exception error) {
			Data.version.getLogger().warn("{} Failed to set textured entity texture: {}", Data.version.getID(), error);
		}
		return default_identifier;
	}
	private static List<String> getStringRegistry(String entity_type) {
		List<String> entity_registry = new ArrayList<>();
		try {
			for (List<String> registry : TexturedEntityDataLoader.REGISTRY) {
				String type = registry.get(0);
				String name = registry.get(1);
				if (type.equalsIgnoreCase(entity_type)) entity_registry.add(name);
			}
		} catch (Exception error) {
			Data.version.getLogger().warn("{} Failed to get textured entity string registry: {}", Data.version.getID(), error);
		}
		return entity_registry;
	}
	private static List<Text> getTextRegistry(String entity_type) {
		List<Text> entity_registry = new ArrayList<>();
		try {
			for (List<String> registry : TexturedEntityDataLoader.REGISTRY) {
				String type = registry.get(0);
				String name = registry.get(1);
				if (type.equalsIgnoreCase(entity_type)) entity_registry.add(Text.literal(name));
			}
		} catch (Exception error) {
			Data.version.getLogger().warn("{} Failed to get textured entity text registry: {}", Data.version.getID(), error);
		}
		return entity_registry;
	}
}