/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.shaders;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.overlay.PerspectiveOverlay;
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
        if (PerspectiveKeybindings.CYCLE_SHADERS.wasPressed()) cycle(client, !client.options.sneakKey.isPressed(), false);
        if (PerspectiveKeybindings.TOGGLE_SHADERS.wasPressed()) toggle(client, false, false);
        if (PerspectiveKeybindings.SET_RANDOM_SHADER.wasPressed()) random();
    }
    public static void toggle(MinecraftClient client, boolean SILENT, boolean SHOW_SHADER_NAME) {
        PerspectiveConfigHelper.setConfig("super_secret_settings_enabled", !(boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_enabled"));
        if (!SILENT) {
            if (SHOW_SHADER_NAME) setOverlay(Text.literal((String)PerspectiveShaderDataLoader.get((int)PerspectiveConfigHelper.getConfig("super_secret_settings"), PerspectiveShaderRegistryValue.NAME)));
            else setOverlay(PerspectiveTranslation.getVariableTranslation((boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_enabled"), PerspectiveTranslationType.ENDISABLE));
        }
        if ((boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_enabled")) set(client, true, true);
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
            set(client, forwards, SILENT);
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to cycle Super Secret Settings.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }
    public static void random() {
        try {
            int SHADER = (int)PerspectiveConfigHelper.getConfig("super_secret_settings");
            while (SHADER == (int)PerspectiveConfigHelper.getConfig("super_secret_settings")) SHADER = Math.max(1, new Random().nextInt(PerspectiveShaderDataLoader.getShaderAmount()));
            PerspectiveConfigHelper.setConfig("super_secret_settings", SHADER);
            PerspectiveShader.set(PerspectiveClientData.CLIENT, true, false);
        } catch (Exception error) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to randomize Super Secret Settings.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + error.getLocalizedMessage());
        }
    }
    public static void set(MinecraftClient client, Boolean forwards, boolean SILENT) {
        try {
            DEPTH_FIX = true;
            if (postProcessor != null) postProcessor.close();
            postProcessor = new PostEffectProcessor(client.getTextureManager(), client.getResourceManager(), client.getFramebuffer(), (Identifier)Objects.requireNonNull(PerspectiveShaderDataLoader.get((int) PerspectiveConfigHelper.getConfig("super_secret_settings"), PerspectiveShaderRegistryValue.ID)));
            postProcessor.setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
            if (!SILENT) setOverlay(Text.literal((String)PerspectiveShaderDataLoader.get((int)PerspectiveConfigHelper.getConfig("super_secret_settings"), PerspectiveShaderRegistryValue.NAME)));
            try {
                if (!SILENT && client.world != null && client.player != null && (boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_sound")) client.world.playSound(client.player, client.player.getBlockPos(), SoundEvent.of(SOUND_EVENTS.get(new Random().nextInt(SOUND_EVENTS.size() - 1))), SoundCategory.MASTER);
            } catch (Exception e) {
                PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to play random Super Secret Settings sound.");
                PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
            }
            DEPTH_FIX = false;
            if (!(boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_enabled")) toggle(client, true, false);
            PerspectiveConfigHelper.saveConfig(true);
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to set Super Secret Settings.");
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
            try {
                cycle(client, forwards, false);
            } catch (Exception error) {
                PerspectiveConfigHelper.setConfig("super_secret_settings", 0);
                try {
                    if (postProcessor != null) postProcessor.close();
                    postProcessor = new PostEffectProcessor(client.getTextureManager(), client.getResourceManager(), client.getFramebuffer(), (Identifier)Objects.requireNonNull(PerspectiveShaderDataLoader.get((int) PerspectiveConfigHelper.getConfig("super_secret_settings"), PerspectiveShaderRegistryValue.ID)));
                    postProcessor.setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
                    if ((boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_enabled")) toggle(client, true, false);
                    PerspectiveConfigHelper.saveConfig(true);
                } catch (Exception ignored) {}
            }
            PerspectiveConfigHelper.saveConfig(true);
        }
    }
    private static void setOverlay(Text message) {
        PerspectiveOverlay.setOverlay(Text.translatable("gui.perspective.message.shader", message).formatted(getRandomColor()));
    }
    public static Formatting getRandomColor() {
        Random random = new Random();
        Formatting COLOR = LAST_COLOR;
        while (COLOR == LAST_COLOR) COLOR = COLORS[(random.nextInt(COLORS.length))];
        LAST_COLOR = COLOR;
        return COLOR;
    }
    public static boolean shouldRenderShader() {
        return postProcessor != null && (boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_enabled");
    }
    public static boolean shouldDisableScreenMode() {
        if (!(boolean)PerspectiveConfigHelper.getConfig("bypass_limits")) return (Boolean)Objects.requireNonNull(PerspectiveShaderDataLoader.get((int)PerspectiveConfigHelper.getConfig("super_secret_settings"), PerspectiveShaderRegistryValue.DISABLE_SCREEN_MODE));
        else return false;
    }
    public static void render(float tickDelta) {
        if (postProcessor != null) postProcessor.render(tickDelta);
    }
}