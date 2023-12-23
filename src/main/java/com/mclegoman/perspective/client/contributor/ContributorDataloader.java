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
	public static final List<List<Object>> REGISTRY = new ArrayList<>();
	public static final String ID = "contributors";

	public ContributorDataloader() {
		super(new Gson(), ID);
	}

	private void add(List<Object> value) {
		try {
			if (!REGISTRY.contains(value)) REGISTRY.add(value);
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to add contributor to registry: {}", Data.PERSPECTIVE_VERSION.getID(), error);
		}
	}

	private void reset() {
		try {
			REGISTRY.clear();
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to reset contributor registry: {}", Data.PERSPECTIVE_VERSION.getID(), error);
		}
	}

	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset();
			prepared.forEach(this::layout$perspective);
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to apply contributor dataloader: {}", Data.PERSPECTIVE_VERSION.getID(), error);
		}
	}

	private void layout$perspective(Identifier identifier, JsonElement jsonElement) {
		try {
			JsonObject READER = jsonElement.getAsJsonObject();
			List<Object> DEVELOPER = new ArrayList<>();
			DEVELOPER.add(JsonHelper.getString(READER, "uuid"));
			DEVELOPER.add(JsonHelper.getString(READER, "type"));
			DEVELOPER.add(JsonHelper.getBoolean(READER, "shouldFlipUpsideDown", false));
			DEVELOPER.add(JsonHelper.getBoolean(READER, "shouldDisplayCape", false));
			DEVELOPER.add(JsonHelper.getString(READER, "capeTexture", "perspective:developer"));
			add(DEVELOPER);
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to load perspective contributor: {}", Data.PERSPECTIVE_VERSION.getID(), error);
		}
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier(Data.PERSPECTIVE_VERSION.getID(), ID);
	}
}