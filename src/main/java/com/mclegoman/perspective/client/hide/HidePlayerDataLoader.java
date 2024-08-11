/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.hide;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HidePlayerDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	public static final List<String> REGISTRY = new ArrayList<>();
	public static final String ID = "hide/player";

	public HidePlayerDataLoader() {
		super(new Gson(), ID);
	}

	private void add(String value) {
		try {
			if (!REGISTRY.contains(value)) REGISTRY.add(value);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to add hide player to registry: {}", error));
		}
	}

	private void reset() {
		try {
			REGISTRY.clear();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to reset hide player registry: {}", error));
		}
	}

	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset();
			layout$perspective(manager);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to apply hide player dataloader: {}", error));
		}
	}

	@Override
	public Identifier getFabricId() {
		return Identifier.of(Data.version.getID(), ID);
	}

	private void layout$perspective(ResourceManager manager) {
		List<Resource> hideLists = manager.getAllResources(Identifier.of("perspective", "hide_player.json"));
		for (Resource resource : hideLists) {
			try {
				JsonObject reader = JsonHelper.deserialize(resource.getReader());
				if (JsonHelper.getBoolean(reader, "replace")) reset();
				for (JsonElement value : JsonHelper.getArray(reader, "values", new JsonArray())) add(value.getAsString());
			} catch (Exception error) {
				Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to load perspective hide player list: {}", error));
			}
		}
	}
}