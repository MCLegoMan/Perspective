/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.april_fools_prank;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
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
    private void add(String NAME, Boolean ENABLED) {
        try {
            if (ENABLED) REGISTRY.add(NAME);
            else REGISTRY.remove(NAME);
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
    public static final String ID = "april_fools_prank";
    public PerspectiveAprilFoolsPrankDataLoader() {
        super(new Gson(), ID);
    }
    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        try {
            reset();
            prepared.forEach(this::layout$perspective);
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().error("{} Failed to apply april fools prank dataloader: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    @Override
    public Identifier getFabricId() {
        return new Identifier(PerspectiveData.PERSPECTIVE_VERSION.getID(), ID);
    }
    private void layout$perspective(Identifier identifier, JsonElement jsonElement) {
        try {
            JsonObject READER = jsonElement.getAsJsonObject();
            String SKIN = JsonHelper.getString(READER, "name");
            Boolean ENABLED = JsonHelper.getBoolean(READER, "enabled", true);
            add(SKIN, ENABLED);
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().error("{} Failed to load april fools prank: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
}