/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.april_fools_prank;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.common.util.Couple;
import com.mclegoman.perspective.common.util.IdentifierHelper;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AprilFoolsPrankDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	public static final List<Couple<Identifier, Boolean>> registry = new ArrayList<>();
	public static final String ID = "prank";
	public static String contributor;
	public AprilFoolsPrankDataLoader() {
		super(new Gson(), ID);
	}
	private void add(Identifier id, Boolean isSlim) {
		try {
			Couple<Identifier, Boolean> skin = new Couple<>(id, isSlim);
			if (!registry.contains(skin)) registry.add(skin);
		} catch (Exception error) {
			Data.VERSION.getLogger().error("{} Failed to add april fools prank to registry: {}", Data.VERSION.getLoggerPrefix(), error);
		}
	}
	private void addSkin(String id, boolean isSlim) {
		String namespace = IdentifierHelper.getStringPart(IdentifierHelper.Type.NAMESPACE, id, Data.VERSION.getID());
		String texture = IdentifierHelper.getStringPart(IdentifierHelper.Type.KEY, id);
		if (namespace != null && texture != null) {
			texture = texture.toLowerCase();
			texture = !texture.startsWith("textures/") ? "textures/" + texture : texture;
			texture = !texture.endsWith(".png") ? texture + ".png" : texture;
			add(new Identifier(namespace, texture), isSlim);
		}
	}
	private void reset() {
		try {
			registry.clear();
		} catch (Exception error) {
			Data.VERSION.getLogger().error("{} Failed to reset april fools prank registry: {}", Data.VERSION.getLoggerPrefix(), error);
		}
	}
	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset();
			for (Resource resource : manager.getAllResources(new Identifier(Data.VERSION.getID(), ID + ".json"))) {
				JsonObject reader = JsonHelper.deserialize(resource.getReader());
				if (JsonHelper.getBoolean(reader, "replace", false)) reset();
				JsonObject skins = JsonHelper.getObject(reader, "skins", new JsonObject());
				JsonArray slimSkins = JsonHelper.getArray(skins, "slim", new JsonArray());
				slimSkins.forEach((skin) -> addSkin(skin.getAsString(), true));
				JsonArray wideSkins = JsonHelper.getArray(skins, "wide", new JsonArray());
				wideSkins.forEach((skin) -> addSkin(skin.getAsString(), false));
				contributor = JsonHelper.getString(reader, "contributor", "772eb47b-a24e-4d43-a685-6ca9e9e132f7");
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to load prank values: {}", Data.VERSION.getLoggerPrefix(), error);
		}
	}
	@Override
	public Identifier getFabricId() {
		return new Identifier(Data.VERSION.getID(), ID);
	}
}