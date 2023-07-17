/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.config.PerspectiveExperimentalConfig;
import com.mclegoman.perspective.client.dataloader.PerspectiveTexturedEntityDataLoader;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class PerspectiveTexturedEntityUtils {
    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveTexturedEntityDataLoader());
    }
    public static Identifier getTexture(Entity entity, String entity_type, String suffix, Identifier default_identifier) {
        try {
            List<String> stringReg = getStringRegistry(entity_type);
            List<Text> textReg = getTextRegistry(entity_type);
            String namespace = entity_type.substring(0, entity_type.lastIndexOf(":"));
            String entity_name = entity_type.substring(entity_type.lastIndexOf(":") + 1);
            if (PerspectiveExperimentalConfig.TEXTURED_NAMED_ENTITY && textReg.contains(entity.getCustomName())) {
                if (!stringReg.get(textReg.indexOf(entity.getCustomName())).equalsIgnoreCase("default")) return new Identifier(namespace, "textures/textured_entity/" + entity_name + "/" + stringReg.get(textReg.indexOf(entity.getCustomName())).toLowerCase() + suffix + ".png");
            }
            if (PerspectiveExperimentalConfig.TEXTURED_RANDOM_ENTITY && (!PerspectiveExperimentalConfig.TEXTURED_NAMED_ENTITY || !textReg.contains(entity.getCustomName()))) {
                int index = Math.floorMod(entity.getUuid().getLeastSignificantBits(), stringReg.size());
                if (!stringReg.get(index).equalsIgnoreCase("default")) return new Identifier(namespace, "textures/textured_entity/" + entity_name + "/" + stringReg.get(index).toLowerCase() + suffix + ".png");
            }
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to set Textured Entity texture.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
        return default_identifier;
    }

    private static List<String> getStringRegistry(String entity_type) {
        List<String> entity_registry = new ArrayList<>();
        for (List<String> registry : PerspectiveTexturedEntityDataLoader.REGISTRY) {
            String type = registry.get(0);
            String name = registry.get(1);
            if (type.equalsIgnoreCase(entity_type)) entity_registry.add(name);
        }
        return entity_registry;
    }
    private static List<Text> getTextRegistry(String entity_type) {
        List<Text> entity_registry = new ArrayList<>();
        for (List<String> registry : PerspectiveTexturedEntityDataLoader.REGISTRY) {
            String type = registry.get(0);
            String name = registry.get(1);
            if (type.equalsIgnoreCase(entity_type)) entity_registry.add(Text.literal(name));
        }
        return entity_registry;
    }
}