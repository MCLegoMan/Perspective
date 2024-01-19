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

public class AprilFoolsPrankDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	public static final List<String> REGISTRY = new ArrayList<>();
	public static final String ID = "prank";
	public static boolean isSlim;
	public static boolean shouldFlipUpsideDown;
	public static boolean shouldDisplayCape;
	public static String contributor;

	public AprilFoolsPrankDataLoader() {
		super(new Gson(), ID);
	}

	private void add(String NAME) {
		try {
			if (!REGISTRY.contains(NAME)) REGISTRY.add(NAME);
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().error("{} Failed to add april fools prank to registry: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
		}
	}

	private void reset() {
		try {
			REGISTRY.clear();
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().error("{} Failed to reset april fools prank registry: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
		}
	}

	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset();
			for (Resource resource : manager.getAllResources(new Identifier(Data.PERSPECTIVE_VERSION.getID(), ID + ".json"))) {
				JsonObject reader = JsonHelper.deserialize(resource.getReader());
				JsonArray defaultSkins = new JsonArray();
				defaultSkins.add("mclegoman_normal");
				defaultSkins.add("mclegoman_pride");
				defaultSkins.add("mclegoman_trans");
				defaultSkins.add("mclegoman_sheep");
				defaultSkins.add("mclegoman_orange_sheep");
				defaultSkins.add("mclegoman_suit");
				defaultSkins.add("mclegoblock_normal");
				defaultSkins.add("mclegoblock_pride");
				defaultSkins.add("mclegoblock_trans");
				defaultSkins.add("mclegoblock_sheep");
				defaultSkins.add("mclegoblock_orange_sheep");
				JsonArray skins = JsonHelper.getArray(reader, "skins", defaultSkins);
				skins.forEach((skin) -> add(skin.getAsString()));
				isSlim = JsonHelper.getBoolean(reader, "isSlim", true);
				shouldFlipUpsideDown = JsonHelper.getBoolean(reader, "shouldFlipUpsideDown", true);
				shouldDisplayCape = JsonHelper.getBoolean(reader, "shouldDisplayCape", true);
				contributor = JsonHelper.getString(reader, "contributor", "772eb47b-a24e-4d43-a685-6ca9e9e132f7");
			}
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to load prank values: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
		}
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier(Data.PERSPECTIVE_VERSION.getID(), ID);
	}
}