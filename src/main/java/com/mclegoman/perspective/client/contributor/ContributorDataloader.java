/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.contributor;

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
			}
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to add contributor to contributor registry: {}", error));
		}
	}
	private void reset() {
		try {
			registry.clear();
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to reset contributor registry: {}", error));
		}
	}
	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset();
			prepared.forEach(this::layout$perspective);
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to apply contributor dataloader: {}", error));
		}
	}
	private void layout$perspective(Identifier identifier, JsonElement jsonElement) {
		try {
			JsonObject READER = jsonElement.getAsJsonObject();
			String uuid = JsonHelper.getString(READER, "uuid");
			String type = JsonHelper.getString(READER, "type");
			boolean shouldFlipUpsideDown = JsonHelper.getBoolean(READER, "shouldFlipUpsideDown", false);
			boolean shouldReplaceCape = JsonHelper.getBoolean(READER, "shouldReplaceCape", false);
			String capeTexture = JsonHelper.getString(READER, "capeTexture", "perspective:developer");
			add(uuid, type, shouldFlipUpsideDown, shouldReplaceCape, capeTexture);
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to load contributor from dataloader: {}", error));
		}
	}
	@Override
	public Identifier getFabricId() {
		return new Identifier(Data.VERSION.getID(), id);
	}
}