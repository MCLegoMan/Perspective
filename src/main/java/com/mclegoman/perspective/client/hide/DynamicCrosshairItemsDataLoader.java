/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.hide;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.item.Item;
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

public class DynamicCrosshairItemsDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	public static final List<Item> registry = new ArrayList<>();
	public static final String ID = "hide/dynamic_crosshair";

	public DynamicCrosshairItemsDataLoader() {
		super(new Gson(), ID);
	}

	private void add(Item value) {
		try {
			if (!registry.contains(value)) registry.add(value);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to add dynamic crosshair item to registry: {}", error));
		}
	}

	private void reset() {
		try {
			registry.clear();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to reset dynamic crosshair item registry: {}", error));
		}
	}

	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset();
			layout$perspective(manager);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to apply dynamic crosshair item dataloader: {}", Data.version.getID(), error));
		}
	}

	@Override
	public Identifier getFabricId() {
		return Identifier.of(Data.version.getID(), ID);
	}

	private void layout$perspective(ResourceManager manager) {
		List<Resource> hideLists = manager.getAllResources(Identifier.of("perspective", "dynamic_crosshair.json"));
		for (Resource resource : hideLists) {
			try {
				for (JsonElement value : JsonHelper.deserialize(resource.getReader()).getAsJsonArray("values")) {
					add(Registries.ITEM.get(Identifier.of(value.getAsString())));
				}
			} catch (Exception error) {
				Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to load perspective dynamic crosshair item list: {}", error));
			}
		}
	}
}