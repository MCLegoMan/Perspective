/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.displaynames;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.common.util.Couple;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DisplayNamesDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	public static final List<Couple<UUID, Text>> REGISTRY = new ArrayList<>();
	public static final String ID = "displaynames";
	public DisplayNamesDataLoader() {
		super(new Gson(), ID);
	}
	private void add(Couple<UUID, Text> value) {
		try {
			if (!REGISTRY.contains(value)) REGISTRY.add(value);
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to add displayname to registry: {}", Data.VERSION.getID(), error);
		}
	}
	private void reset() {
		try {
			REGISTRY.clear();
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to reset displayname registry: {}", Data.VERSION.getID(), error);
		}
	}
	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset();
			prepared.forEach((identifier, jsonElement) -> layout$perspective(identifier, jsonElement, manager));
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to apply displayname dataloader: {}", Data.VERSION.getID(), error);
		}
	}
	@Override
	public Identifier getFabricId() {
		return new Identifier(Data.VERSION.getID(), ID);
	}
	private void layout$perspective(Identifier identifier, JsonElement jsonElement, ResourceManager manager) {
		try {
			JsonObject READER = jsonElement.getAsJsonObject();
			UUID ID = UUID.fromString(JsonHelper.getString(READER, "uuid"));
			String DISPLAYNAME = JsonHelper.getString(READER, "displayname");
			if (DISPLAYNAME != null && !DISPLAYNAME.equals("")) add(new Couple<>(ID, Text.of(DISPLAYNAME)));
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to load displayname: {}", Data.VERSION.getID(), error);
		}
	}
}