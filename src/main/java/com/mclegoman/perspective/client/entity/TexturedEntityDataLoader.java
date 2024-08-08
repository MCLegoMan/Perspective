/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.entity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TexturedEntityDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	public static final List<TexturedEntityData> registry = new ArrayList<>();
	public static final String identifier = "textured_entity";
	public static boolean isReady;

	public TexturedEntityDataLoader() {
		super(new Gson(), identifier);
	}

	private void add(String namespace, String type, String name, JsonObject entity_specific, JsonArray overrides, Boolean enabled) {
		try {
			TexturedEntityData texturedEntity = new TexturedEntityData(namespace, type, name, entity_specific, overrides);
			if (enabled) registry.add(texturedEntity);
			else registry.remove(texturedEntity);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to add textured entity to registry: {}", error));
		}
	}

	private void reset() {
		try {
			registry.clear();
			addDefaultTexturedEntities("minecraft", new String[]{
					"allay",
					"armor_stand",
					"arrow",
					"axolotl",
					"bat",
					"bee",
					"blaze",
					"chest_boat",
					"boat",
					"breeze",
					"breeze_wind_charge",
					"camel",
					"cat",
					"cave_spider",
					"chicken",
					"cod",
					"cow",
					"creeper",
					"dolphin",
					"mule",
					"donkey",
					"dragon_fireball",
					"drowned",
					"elder_guardian",
					"end_crystal",
					"enderman",
					"endermite",
					"evoker",
					"evoker_fangs",
					"experience_orb",
					"firework_rocket",
					"fox",
					"frog",
					"ghast",
					"giant",
					"glow_squid",
					"goat",
					"guardian",
					"hoglin",
					"horse",
					"husk",
					"illusioner",
					"iron_golem",
					"leash_knot",
					"trader_llama",
					"llama",
					"llama_spit",
					"magma_cube",
					"chest_minecart",
					"command_block_minecart",
					"furnace_minecart",
					"hopper_minecart",
					"spawner_minecart",
					"tnt_minecart",
					"minecart",
					"mooshroom",
					"ocelot",
					"panda",
					"parrot",
					"phantom",
					"pig",
					"piglin",
					"zombified_piglin",
					"piglin_brute",
					"pillager",
					"polar_bear",
					"pufferfish",
					"rabbit",
					"ravager",
					"salmon",
					"sheep",
					"shulker_bullet",
					"shulker",
					"silverfish",
					"skeleton",
					"slime",
					"sniffer",
					"snow_golem",
					"spectral_arrow",
					"spider",
					"squid",
					"stray",
					"strider",
					"tadpole",
					"tnt",
					"trident",
					"tropical_fish",
					"turtle",
					"vex",
					"villager",
					"vindicator",
					"wandering_trader",
					"warden",
					"wind_charge",
					"witch",
					"wither",
					"wither_skeleton",
					"wither_skull",
					"wolf",
					"zoglin",
					"zombie",
					"skeleton_horse",
					"zombie_horse",
					"zombie_villager"
			});
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to reset Textured Entity registry: {}", error));
		}
	}
	public void addDefaultTexturedEntities(String namespace, String[] entityTypes) {
		for (String entity : entityTypes) {
			add(namespace, entity, "default", new JsonObject(), new JsonArray(), true);
		}
	}
	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			isReady = false;
			reset();
			prepared.forEach(this::layout$perspective);
			isReady = true;
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to apply textured entity dataloader: {}", error));
		}
	}

	@Override
	public Identifier getFabricId() {
		return Identifier.of(Data.version.getID(), identifier);
	}

	private void layout$perspective(Identifier identifier, JsonElement jsonElement) {
		try {
			JsonObject reader = jsonElement.getAsJsonObject();
			String entity = JsonHelper.getString(reader, "entity");
			String namespace = entity.contains(":") ? entity.substring(0, entity.lastIndexOf(":")) : "minecraft";
			String type = entity.contains(":") ? entity.substring(entity.lastIndexOf(":") + 1) : entity;
			String name = JsonHelper.getString(reader, "name");
			JsonObject entity_specific = JsonHelper.getObject(reader, "entity_specific", new JsonObject());
			JsonArray overrides = JsonHelper.getArray(reader, "overrides", new JsonArray());
			Boolean enabled = JsonHelper.getBoolean(reader, "enabled", true);
			add(namespace, type, name, entity_specific, overrides, enabled);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to load perspective textured entity: {}", error));
		}
	}
}