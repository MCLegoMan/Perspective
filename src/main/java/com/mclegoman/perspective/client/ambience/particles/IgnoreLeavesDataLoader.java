/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.ambience.particles;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.luminance.common.util.IdentifierHelper;
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

public class IgnoreLeavesDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	public static final String id = "ignore_leaves";
	public static List<Identifier> registry = new ArrayList<>();
	public IgnoreLeavesDataLoader() {
		super(new Gson(), id);
	}
	private void add(Identifier id) {
		if (!registry.contains(id)) registry.add(id);
	}
	private void reset() {
		registry.clear();
	}
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset();
			prepared.forEach(this::layout$perspective);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to load ignore leaves values: {}", error));
		}
	}
	private void layout$perspective(Identifier identifier, JsonElement jsonElement) {
		try {
			JsonObject reader = jsonElement.getAsJsonObject();
			for (JsonElement blockId : JsonHelper.getArray(reader, "values")) add(IdentifierHelper.identifierFromString(blockId.getAsString()));
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to load ignore leaves data from dataloader: {}", error));
		}
	}
	public Identifier getFabricId() {
		return Identifier.of(Data.version.getID(), id);
	}
}