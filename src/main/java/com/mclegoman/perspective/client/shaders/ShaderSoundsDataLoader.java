/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.shaders;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.registry.Registries;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShaderSoundsDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	private static final List<String> SOUNDS = new ArrayList<>();
	public static final List<Identifier> REGISTRY = new ArrayList<>();
	public static final String ID = "shaders/sounds";

	public ShaderSoundsDataLoader() {
		super(new Gson(), ID);
	}

	private void add(String SOUND) {
		try {
			if (!SOUNDS.contains(SOUND)) SOUNDS.add(SOUND);
		} catch (Exception error) {
			Data.VERSION.getLogger().error("{} Failed to add shader sound to registry: {}", Data.VERSION.getLoggerPrefix(), error);
		}
	}

	private void reset() {
		try {
			SOUNDS.clear();
			REGISTRY.clear();
		} catch (Exception error) {
			Data.VERSION.getLogger().error("{} Failed to reset shader sound registry: {}", Data.VERSION.getLoggerPrefix(), error);
		}
	}

	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset();
			for (Resource resource : manager.getAllResources(new Identifier(Data.VERSION.getID(), ID + ".json"))) {
				JsonObject reader = JsonHelper.deserialize(resource.getReader());
				if (JsonHelper.getBoolean(reader, "replace", false)) reset();
				JsonArray defaultSounds = new JsonArray();
				for (Identifier id : Registries.SOUND_EVENT.getIds()) {
					if (!id.toString().contains("music") && !id.toString().contains("ambient") && !id.toString().contains("music_disc")) defaultSounds.add(id.toString());
				}
				JsonArray sounds = JsonHelper.getArray(reader, "values", defaultSounds);
				sounds.forEach((sound) -> add(sound.getAsString()));
			}
			if (!SOUNDS.isEmpty()) {
				for (Identifier id : Registries.SOUND_EVENT.getIds()) {
					if (SOUNDS.contains(id.toString())) {
						if (!REGISTRY.contains(id)) REGISTRY.add(id);
					}
				}
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to load shader sound values: {}", Data.VERSION.getLoggerPrefix(), error);
		}
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier(Data.VERSION.getID(), ID);
	}
}