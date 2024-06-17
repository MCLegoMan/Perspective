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
	public static final List<List<String>> REGISTRY = new ArrayList<>();
	public static final String ID = "textured_entity";
	public TexturedEntityDataLoader() {
		super(new Gson(), ID);
	}
	private void add(String ENTITY, String NAME, Boolean ENABLED) {
		try {
			List<String> VALUES = new ArrayList<>();
			VALUES.add(ENTITY);
			VALUES.add(NAME);
			if (ENABLED) REGISTRY.add(VALUES);
			else REGISTRY.remove(VALUES);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to add textured entity to registry: {}", error));
		}
	}
	private void reset() {
		try {
			REGISTRY.clear();
			add$default();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to reset textured entity registry: {}", error));
		}
	}
	private void add$default() {
		try {
			String[] ENTITIES = new String[]{
					"minecraft:allay",
					"minecraft:armor_stand",
					"minecraft:arrow",
					"minecraft:axolotl",
					"minecraft:bat",
					"minecraft:bee",
					"minecraft:blaze",
					"minecraft:chest_boat",
					"minecraft:boat",
					"minecraft:breeze",
					"minecraft:camel",
					"minecraft:cat",
					"minecraft:cave_spider",
					"minecraft:chicken",
					"minecraft:cod",
					"minecraft:cow",
					"minecraft:creeper",
					"minecraft:dolphin",
					"minecraft:mule",
					"minecraft:donkey",
					"minecraft:dragon_fireball",
					"minecraft:drowned",
					"minecraft:elder_guardian",
					"minecraft:end_crystal",
					"minecraft:enderman",
					"minecraft:endermite",
					"minecraft:evoker",
					"minecraft:evoker_fangs",
					"minecraft:experience_orb",
					"minecraft:firework_rocket",
					"minecraft:fox",
					"minecraft:frog",
					"minecraft:ghast",
					"minecraft:giant",
					"minecraft:glow_squid",
					"minecraft:goat",
					"minecraft:guardian",
					"minecraft:hoglin",
					"minecraft:horse",
					"minecraft:husk",
					"minecraft:illusioner",
					"minecraft:iron_golem",
					"minecraft:leash_knot",
					"minecraft:trader_llama",
					"minecraft:llama",
					"minecraft:llama_spit",
					"minecraft:magma_cube",
					"minecraft:chest_minecart",
					"minecraft:command_block_minecart",
					"minecraft:furnace_minecart",
					"minecraft:hopper_minecart",
					"minecraft:spawner_minecart",
					"minecraft:tnt_minecart",
					"minecraft:minecart",
					"minecraft:mooshroom",
					"minecraft:ocelot",
					"minecraft:panda",
					"minecraft:parrot",
					"minecraft:phantom",
					"minecraft:pig",
					"minecraft:piglin",
					"minecraft:zombified_piglin",
					"minecraft:piglin_brute",
					"minecraft:pillager",
					"minecraft:polar_bear",
					"minecraft:pufferfish",
					"minecraft:rabbit",
					"minecraft:ravager",
					"minecraft:salmon",
					"minecraft:sheep",
					"minecraft:shulker_bullet",
					"minecraft:shulker",
					"minecraft:silverfish",
					"minecraft:skeleton",
					"minecraft:slime",
					"minecraft:sniffer",
					"minecraft:snow_golem",
					"minecraft:spectral_arrow",
					"minecraft:spider",
					"minecraft:squid",
					"minecraft:stray",
					"minecraft:strider",
					"minecraft:tadpole",
					"minecraft:tnt",
					"minecraft:trident",
					"minecraft:tropical_fish",
					"minecraft:turtle",
					"minecraft:vex",
					"minecraft:villager",
					"minecraft:vindicator",
					"minecraft:wandering_trader",
					"minecraft:warden",
					"minecraft:wind_charge",
					"minecraft:witch",
					"minecraft:wither",
					"minecraft:wither_skeleton",
					"minecraft:wither_skull",
					"minecraft:wolf",
					"minecraft:zoglin",
					"minecraft:zombie",
					"minecraft:skeleton_horse",
					"minecraft:zombie_horse",
					"minecraft:zombie_villager"
			};
			for (String ENTITY : ENTITIES) {
				add(ENTITY, "default", true);
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to add default textured entities to registry: {}", error));
		}
	}
	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset();
			prepared.forEach(this::layout$perspective);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to apply textured entity registry: {}", error));
		}
	}
	@Override
	public Identifier getFabricId() {
		return Identifier.of(Data.version.getID(), ID);
	}
	private void layout$perspective(Identifier identifier, JsonElement jsonElement) {
		try {
			JsonObject READER = jsonElement.getAsJsonObject();
			String ENTITY = JsonHelper.getString(READER, "entity");
			String NAME = JsonHelper.getString(READER, "name");
			Boolean ENABLED = JsonHelper.getBoolean(READER, "enabled", true);
			add(ENTITY, NAME, ENABLED);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to load textured entity to registry: {}", error));
		}
	}
}