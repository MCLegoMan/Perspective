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
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HideNameTagsDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	public static final List<String> REGISTRY = new ArrayList<>();
	public static final String ID = "hide/nametags";
	public HideNameTagsDataLoader() {
		super(new Gson(), ID);
	}
	private void add(String value) {
		try {
			if (!REGISTRY.contains(value)) REGISTRY.add(value);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to add hide nametag to registry: {}", error));
		}
	}
	private void reset() {
		try {
			REGISTRY.clear();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to reset hide nametags registry: {}", error));
		}
	}
	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset();
			layout$perspective(manager);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to apply hide name tags dataloader: {}", error));
		}
	}
	@Override
	public Identifier getFabricId() {
		return new Identifier(Data.version.getID(), ID);
	}
	private void layout$perspective(ResourceManager manager) {
		List<Resource> HIDE_LISTS = manager.getAllResources(new Identifier("perspective", "hide_nametags.json"));
		for (Resource resource : HIDE_LISTS) {
			try {
				for (JsonElement value : JsonHelper.deserialize(resource.getReader()).getAsJsonArray("values")) {
					add(value.getAsString());
				}
			} catch (Exception error) {
				Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to load perspective hide name tags list: {}", error));
			}
		}
	}
}