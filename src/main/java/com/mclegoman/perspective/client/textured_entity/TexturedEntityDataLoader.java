/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.textured_entity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
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
	public static final List<List<Object>> REGISTRY = new ArrayList<>();
	public static final String ID = "textured_entity";

	public TexturedEntityDataLoader() {
		super(new Gson(), ID);
	}

	private void add(String namespace, String type, String name, Boolean enabled) {
		try {
			List<Object> texturedEntity = new ArrayList<>();
			texturedEntity.add(namespace);
			texturedEntity.add(type);
			texturedEntity.add(name);
			if (enabled) REGISTRY.add(texturedEntity);
			else REGISTRY.remove(texturedEntity);
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to add textured entity to registry: {}", Data.VERSION.getID(), error);
		}
	}

	private void reset() {
		try {
			REGISTRY.clear();
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
			Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to reset Textured Entity registry: {}", error));
		}
	}
	public void addDefaultTexturedEntities(String namespace, String[] entityTypes) {
		for (String entity : entityTypes) {
			add(namespace, entity, "default", true);
		}
	}
	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset();
			prepared.forEach(this::layout$perspective);
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to apply textured entity dataloader: {}", Data.VERSION.getID(), error);
		}
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier(Data.VERSION.getID(), ID);
	}

	private void layout$perspective(Identifier identifier, JsonElement jsonElement) {
		try {
			JsonObject reader = jsonElement.getAsJsonObject();
			String entity = JsonHelper.getString(reader, "entity");
			String namespace = entity.contains(":") ? entity.substring(0, entity.lastIndexOf(":")) : "minecraft";
			String type = entity.contains(":") ? entity.substring(entity.lastIndexOf(":") + 1) : entity;
			String name = JsonHelper.getString(reader, "name");
			Boolean enabled = JsonHelper.getBoolean(reader, "enabled", true);
			add(namespace, type, name, enabled);
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to load perspective textured entity: {}", Data.VERSION.getID(), error);
		}
	}
}