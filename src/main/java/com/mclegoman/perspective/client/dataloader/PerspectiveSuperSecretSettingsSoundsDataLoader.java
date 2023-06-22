/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.dataloader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Environment(EnvType.CLIENT)
public class PerspectiveSuperSecretSettingsSoundsDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public static final List<SoundEvent> SOUNDS = new ArrayList<>();
    public static SoundEvent LAST_SOUND_USED;
    private void add(String NAMESPACE, String SOUND, Boolean ENABLED) {
        SOUND = SOUND.replace("\"", "");
        SoundEvent SOUND_EVENT = SoundEvent.of(new Identifier(NAMESPACE + ":" + SOUND));
        if (ENABLED) {
            if (!SOUNDS.contains(SOUND_EVENT)) {
                SOUNDS.add(SOUND_EVENT);
            }
        } else {
            SOUNDS.remove(SOUND_EVENT);
        }
    }
    private void clear() {
        SOUNDS.clear();
    }
    public static final String ID = "shaders/sounds";
    public PerspectiveSuperSecretSettingsSoundsDataLoader() {
        super(new Gson(), ID);
    }
    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        try {
            clear();
            // Perspective Resource Pack Layout
            prepared.forEach((identifier, jsonElement) -> {
                JsonObject READER = jsonElement.getAsJsonObject();
                String NAMESPACE = JsonHelper.getString(READER, "namespace", PerspectiveData.ID);
                String SOUND = JsonHelper.getString(READER, "sound");
                Boolean ENABLED = JsonHelper.getBoolean(READER, "enabled", true);
                add(NAMESPACE, SOUND, ENABLED);
            });
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst loading Super Secret Settings Sounds data.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }
    @Override
    public Identifier getFabricId() {
        return new Identifier(PerspectiveData.ID, ID);
    }

    public static SoundEvent getRandomSound() {
        try {
            Random RANDOM = new Random();
            SoundEvent OUTPUT;
            if (LAST_SOUND_USED != null) OUTPUT = LAST_SOUND_USED;
            else OUTPUT = SOUNDS.get(0);
            while (OUTPUT.equals(LAST_SOUND_USED)) OUTPUT = SOUNDS.get(RANDOM.nextInt(SOUNDS.size()));
            LAST_SOUND_USED = OUTPUT;
            return OUTPUT;
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst getting random Super Secret Settings Sound.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
            return SoundEvent.of(new Identifier(PerspectiveConfig.DEFAULT_SUPER_SECRET_SETTINGS_SOUND));
        }
    }
}