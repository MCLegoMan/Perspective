/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.contributor;

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

public class ContributorDataloader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	public static final List<List<Object>> registry = new ArrayList<>();
	public static final String id = "contributors";
	public ContributorDataloader() {
		super(new Gson(), id);
	}
	private void add(String uuid, String type, boolean shouldFlipUpsideDown, boolean shouldReplaceCape, String capeTexture) {
		try {
			if (Contributor.isAllowedUuid(uuid)) {
				List<Object> contributor = new ArrayList<>();
				contributor.add(uuid);
				contributor.add(type);
				contributor.add(shouldFlipUpsideDown);
				contributor.add(shouldReplaceCape);
				contributor.add(capeTexture);
				if (!registry.contains(contributor)) registry.add(contributor);
			} else {
				Data.version.sendToLog(LogType.WARN, Translation.getString("{} is not permitted to use contributor dataloader!", uuid));
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to add contributor to contributor registry: {}", error));
		}
	}
	private void reset() {
		try {
			registry.clear();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to reset contributor registry: {}", error));
		}
	}
	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset();
			prepared.forEach(this::layout$perspective);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to apply contributor dataloader: {}", error));
		}
	}
	private void layout$perspective(Identifier identifier, JsonElement jsonElement) {
		try {
			JsonObject reader = jsonElement.getAsJsonObject();
			JsonArray uuids = JsonHelper.getArray(reader, "uuids");
			String type = JsonHelper.getString(reader, "type");
			boolean shouldFlipUpsideDown = JsonHelper.getBoolean(reader, "shouldFlipUpsideDown", false);
			boolean shouldReplaceCape = JsonHelper.getBoolean(reader, "shouldReplaceCape", false);
			String capeTexture = JsonHelper.getString(reader, "capeTexture", "perspective:developer");
			for (JsonElement uuid : uuids) add(uuid.getAsString(), type, shouldFlipUpsideDown, shouldReplaceCape, capeTexture);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to load contributor from dataloader: {}", error));
		}
	}
	@Override
	public Identifier getFabricId() {
		return Identifier.of(Data.version.getID(), id);
	}
}