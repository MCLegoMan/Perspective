/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.ui;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.april_fools_prank.AprilFoolsPrank;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.luminance.common.util.Couple;
import com.mclegoman.releasetypeutils.common.version.Helper;
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
import java.util.Random;

public class SplashesDataloader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	public static final List<Couple<String, Boolean>> registry = new ArrayList<>();
	public static final String id = "splashes";
	private static Couple<String, Boolean> splashText;
	public static Couple<String, Boolean> getSplashText() {
		if (PerspectiveLogo.isPride() && !PerspectiveLogo.isForcePride()) return new Couple<>("splashes.perspective.special.pride_month", true);
		else if (AprilFoolsPrank.isAprilFools() && !AprilFoolsPrank.isForceAprilFools()) return new Couple<>("splashes.perspective.special.april_fools", true);
		else return splashText;
	}
	public static void randomizeSplashText() {
		List<Couple<String, Boolean>> splashes = new ArrayList<>(registry);
		if (getSplashText() != null) splashes.remove(getSplashText());
		splashText = splashes.get(new Random().nextInt(splashes.size()));
	}
	public SplashesDataloader() {
		super(new Gson(), id);
	}
	private void add(String text, Boolean translatable) {
		try {
			Couple<String, Boolean> splash = new Couple<>(text, translatable);
			if (!registry.contains(splash)) registry.add(splash);
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to add splash text to registry: {}", error));
		}
	}
	private void reset() {
		try {
			registry.clear();
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to reset splash text registry: {}", error));
		}
	}
	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset();
			for (Resource resource : manager.getAllResources(new Identifier(Data.version.getID(), id + ".json"))) {
				JsonObject reader = JsonHelper.deserialize(resource.getReader());
				if (JsonHelper.getBoolean(reader, "replace", false)) reset();
				JsonArray translatableTexts = JsonHelper.getArray(reader, "translatable");
				for (JsonElement splashText : translatableTexts) add(splashText.getAsString(), true);
				JsonArray literalTexts = JsonHelper.getArray(reader, "literal");
				for (JsonElement splashText : literalTexts) add(splashText.getAsString(), false);
			}
			randomizeSplashText();
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to load splash text from dataloader: {}", error));
		}
	}
	@Override
	public Identifier getFabricId() {
		return new Identifier(Data.version.getID(), id);
	}
}