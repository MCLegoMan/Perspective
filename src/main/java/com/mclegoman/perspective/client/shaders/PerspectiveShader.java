/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.shaders;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.client.translation.PerspectiveTranslationType;
import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Environment(EnvType.CLIENT)
public class PerspectiveShader {
    public static Framebuffer DEPTH_FRAME_BUFFER;
    public static boolean DEPTH_FIX;
    @Nullable
    public static PostEffectProcessor postProcessor;
    private static final Formatting[] COLORS = new Formatting[]{Formatting.DARK_BLUE, Formatting.DARK_GREEN, Formatting.DARK_AQUA, Formatting.DARK_RED, Formatting.DARK_PURPLE, Formatting.GOLD, Formatting.BLUE, Formatting.GREEN, Formatting.AQUA, Formatting.RED, Formatting.LIGHT_PURPLE, Formatting.YELLOW};
    private static Formatting LAST_COLOR;
    private static final List<Identifier> SOUND_EVENTS = new ArrayList<>();
    public static void init() {
        try {
            ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveShaderDataLoader());
            for (Identifier id : Registries.SOUND_EVENT.getIds()) {
                if (!id.toString().contains("music")) {
                    SOUND_EVENTS.add(id);
                }
            }
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "Caught an error whilst initializing Super Secret Settings");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    public static void tick(MinecraftClient client) {
        if (PerspectiveKeybindings.CYCLE_SHADERS.wasPressed()) cycle(client, client.options.sneakKey.isPressed(), false);
        if (PerspectiveKeybindings.TOGGLE_SHADERS.wasPressed()) toggle(client, false);
    }
    public static void toggle(MinecraftClient client, boolean SILENT) {
        PerspectiveConfigHelper.setConfig("super_secret_settings_enabled", !(boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_enabled"));
        if (!SILENT) setOverlay(client, PerspectiveTranslation.getVariableTranslation((boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_enabled"), PerspectiveTranslationType.ENDISABLE));
        if ((boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_enabled")) set(client, true);
        else {
            if (postProcessor != null) {
                postProcessor.close();
                postProcessor = null;
            }
        }
        PerspectiveConfigHelper.saveConfig(true);
    }
    public static void cycle(MinecraftClient client, Boolean forwards, boolean SILENT) {
        try {
            if (forwards) {
                if ((int)PerspectiveConfigHelper.getConfig("super_secret_settings") < PerspectiveShaderDataLoader.getShaderAmount()) PerspectiveConfigHelper.setConfig("super_secret_settings", (int)PerspectiveConfigHelper.getConfig("super_secret_settings") + 1);
                else PerspectiveConfigHelper.setConfig("super_secret_settings", 0);
            } else {
                if ((int)PerspectiveConfigHelper.getConfig("super_secret_settings") > 0) PerspectiveConfigHelper.setConfig("super_secret_settings", (int)PerspectiveConfigHelper.getConfig("super_secret_settings") - 1);
                else PerspectiveConfigHelper.setConfig("super_secret_settings", PerspectiveShaderDataLoader.getShaderAmount());
            }
            if (!(boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_enabled")) toggle(client, SILENT);
            else set(client, SILENT);
            PerspectiveConfigHelper.saveConfig(true);
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to cycle Super Secret Settings.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }
    public static void set(MinecraftClient client, boolean SILENT) {
        try {
            DEPTH_FIX = true;
            if (postProcessor != null) postProcessor.close();
            postProcessor = new PostEffectProcessor(client.getTextureManager(), client.getResourceManager(), client.getFramebuffer(), PerspectiveShaderDataLoader.SHADERS.get((int)PerspectiveConfigHelper.getConfig("super_secret_settings")));
            postProcessor.setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
            if (!SILENT) setOverlay(client, Text.of(PerspectiveShaderDataLoader.SHADERS_NAME.get((int)PerspectiveConfigHelper.getConfig("super_secret_settings"))));
            try {
                if (!SILENT && client.world != null && client.player != null) client.world.playSound(client.player, client.player.getBlockPos(), SoundEvent.of(SOUND_EVENTS.get(new Random().nextInt(SOUND_EVENTS.size() - 1))), SoundCategory.MASTER);
            } catch (Exception e) {
                PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to play random Super Secret Settings sound.");
                PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
            }
            DEPTH_FIX = false;
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to set Super Secret Settings.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
            PerspectiveConfigHelper.setConfig("super_secret_settings", 0);
            try {
                if (postProcessor != null) postProcessor.close();
                postProcessor = new PostEffectProcessor(client.getTextureManager(), client.getResourceManager(), client.getFramebuffer(), PerspectiveShaderDataLoader.SHADERS.get((int)PerspectiveConfigHelper.getConfig("super_secret_settings")));
                postProcessor.setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
            } catch (Exception ignored) {}
            PerspectiveConfigHelper.saveConfig(true);
        }
    }
    private static void setOverlay(MinecraftClient client, Text message) {
        try {
            Objects.requireNonNull(client.player).sendMessage(Text.translatable("gui.perspective.message.shader", message).formatted(getRandomColor()), true);
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to set overlay: {}", (Object)error);
        }
    }
    public static Formatting getRandomColor() {
        Random random = new Random();
        Formatting COLOR = LAST_COLOR;
        while (COLOR == LAST_COLOR) COLOR = COLORS[(random.nextInt(COLORS.length))];
        LAST_COLOR = COLOR;
        return COLOR;
    }
}