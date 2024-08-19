/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.logo;

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
import java.util.Random;

public class PrideLogoDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	public static final List<LogoData> registry = new ArrayList<>();
	public static final String identifier = "pride_logos";
	private static LogoData logo;
	public static LogoData getLogo() {
		return logo;
	}
	public static void randomizeLogo() {
		if (registry.size() > 1) {
			List<LogoData> logos = new ArrayList<>(registry);
			if (getLogo() != null) logos.remove(getLogo());
			logo = logos.get(new Random().nextInt(logos.size()));
		} else {
			if (registry.size() == 1) logo = registry.getFirst();
			else logo = PerspectiveLogo.getDefaultLogo();
		}
	}
	public static List<String> getLogoNames() {
		List<String> logos = new ArrayList<>();
		for (LogoData logoData : registry) logos.add(logoData.getId());
		return logos;
	}
	public PrideLogoDataLoader() {
		super(new Gson(), identifier);
	}
	private void add(String id, Identifier logoTexture, Identifier iconTexture) {
		registry.removeIf(logoData -> logoData.getId().equals(id));
		registry.add(new LogoData("pride", id, logoTexture, iconTexture));
	}
	private void reset() {
		registry.clear();
	}
	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset();
			prepared.forEach(this::layout$perspective);
			randomizeLogo();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to apply pride logo dataloader: {}", error));
		}
	}
	@Override
	public Identifier getFabricId() {
		return Identifier.of(Data.version.getID(), identifier);
	}
	private void layout$perspective(Identifier identifier, JsonElement jsonElement) {
		try {
			JsonObject reader = jsonElement.getAsJsonObject();
			String id = JsonHelper.getString(reader, "id", identifier.getPath());
			String logoTexture = JsonHelper.getString(reader, "logo_texture", getDefaultLogoTexture(id));
			String iconTexture = JsonHelper.getString(reader, "icon_texture", getDefaultIconTexture(id));
			add(id, IdentifierHelper.identifierFromString(logoTexture.endsWith(".png") ? logoTexture : logoTexture + ".png"), IdentifierHelper.identifierFromString(iconTexture.endsWith(".png") ? iconTexture : iconTexture + ".png"));
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to load perspective pride logo: {}", error));
		}
	}
	private String getDefaultLogoTexture(String id) {
		return PerspectiveLogo.getLogoTexture("pride", id);
	}
	private String getDefaultIconTexture(String id) {
		return PerspectiveLogo.getIconTexture("pride", id);
	}
}