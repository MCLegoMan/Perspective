/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.april_fools_prank;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

@Environment(EnvType.CLIENT)
public class PerspectiveAprilFoolsPrankDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static final List<String> REGISTRY = new ArrayList<>();
    public static boolean shouldFlipUpsideDown;
    public static boolean shouldDisplayCape;
    private void add(String NAME) {
        try {
            if (!REGISTRY.contains(NAME)) REGISTRY.add(NAME);
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().error("{} Failed to add april fools prank to registry: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    private void reset() {
        try {
            REGISTRY.clear();
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().error("{} Failed to reset april fools prank registry: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    public static final String ID = "prank";
    public PerspectiveAprilFoolsPrankDataLoader() {
        super(new Gson(), ID);
    }
    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        try {
            reset();
            for (Resource resource : manager.getAllResources(new Identifier(PerspectiveData.PERSPECTIVE_VERSION.getID(), ID + ".json"))) {
                JsonObject reader = JsonHelper.deserialize(resource.getReader());
                JsonArray skins = reader.getAsJsonArray("skins");
                skins.forEach((skin) -> add(skin.getAsString()));
                shouldFlipUpsideDown = JsonHelper.getBoolean(reader, "shouldFlipUpsideDown");
                shouldDisplayCape = JsonHelper.getBoolean(reader, "shouldDisplayCape");
            }
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to load prank values: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    @Override
    public Identifier getFabricId() {
        return new Identifier(PerspectiveData.PERSPECTIVE_VERSION.getID(), ID);
    }
}