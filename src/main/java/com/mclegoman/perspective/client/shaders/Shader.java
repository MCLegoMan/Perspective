/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.shaders;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.overlays.HUDOverlays;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.translation.TranslationType;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Shader {
    public static Framebuffer DEPTH_FRAME_BUFFER;
    public static boolean DEPTH_FIX;
    @Nullable
    public static ShaderEffect postProcessor;
    private static final Formatting[] COLORS = new Formatting[]{Formatting.DARK_BLUE, Formatting.DARK_GREEN, Formatting.DARK_AQUA, Formatting.DARK_RED, Formatting.DARK_PURPLE, Formatting.GOLD, Formatting.BLUE, Formatting.GREEN, Formatting.AQUA, Formatting.RED, Formatting.LIGHT_PURPLE, Formatting.YELLOW};
    private static Formatting LAST_COLOR;
    private static final List<Identifier> SOUND_EVENTS = new ArrayList<>();
    public static void init() {
        try {
            ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ShaderDataLoader());
            for (Identifier id : Registry.SOUND_EVENT.getIds()) {
                if (!id.toString().contains("music")) {
                    SOUND_EVENTS.add(id);
                }
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Caught an error whilst initializing Super Secret Settings", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    public static void tick(MinecraftClient client) {
        if (Keybindings.CYCLE_SHADERS.wasPressed()) cycle(client, !client.options.sneakKey.isPressed(), false, true);
        if (Keybindings.TOGGLE_SHADERS.wasPressed()) toggle(client, false, false, true);
        if (Keybindings.RANDOM_SHADER.wasPressed()) random(false, true);
    }
    public static void toggle(MinecraftClient client, boolean SILENT, boolean SHOW_SHADER_NAME, boolean SAVE_CONFIG) {
        ConfigHelper.setConfig("super_secret_settings_enabled", !(boolean) ConfigHelper.getConfig("super_secret_settings_enabled"));
        if (!SILENT) {
            if (SHOW_SHADER_NAME) setOverlay(Text.literal(ShaderDataLoader.getShaderName((int) ConfigHelper.getConfig("super_secret_settings"))));
            else setOverlay(Translation.getVariableTranslation((boolean) ConfigHelper.getConfig("super_secret_settings_enabled"), TranslationType.ENDISABLE));
        }
        if ((boolean) ConfigHelper.getConfig("super_secret_settings_enabled")) set(client, true, true, true);
        else {
            if (postProcessor != null) {
                postProcessor.close();
                postProcessor = null;
            }
        }
        if (SAVE_CONFIG) ConfigHelper.saveConfig(true);
    }
    public static void cycle(MinecraftClient client, boolean FORWARDS, boolean SILENT, boolean SAVE_CONFIG) {
        try {
            if (FORWARDS) {
                if ((int) ConfigHelper.getConfig("super_secret_settings") < ShaderDataLoader.getShaderAmount()) ConfigHelper.setConfig("super_secret_settings", (int) ConfigHelper.getConfig("super_secret_settings") + 1);
                else ConfigHelper.setConfig("super_secret_settings", 0);
            } else {
                if ((int) ConfigHelper.getConfig("super_secret_settings") > 0) ConfigHelper.setConfig("super_secret_settings", (int) ConfigHelper.getConfig("super_secret_settings") - 1);
                else ConfigHelper.setConfig("super_secret_settings", ShaderDataLoader.getShaderAmount());
            }
            set(client, FORWARDS, SILENT, SAVE_CONFIG);
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to cycle Super Secret Settings.", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    public static void random(boolean SILENT, boolean SAVE_CONFIG) {
        try {
            int SHADER = (int) ConfigHelper.getConfig("super_secret_settings");
            while (SHADER == (int) ConfigHelper.getConfig("super_secret_settings")) SHADER = Math.max(1, new Random().nextInt(ShaderDataLoader.getShaderAmount()));
            ConfigHelper.setConfig("super_secret_settings", SHADER);
            Shader.set(ClientData.CLIENT, true, SILENT, SAVE_CONFIG);
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to randomize Super Secret Settings.", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    public static void set(MinecraftClient client, Boolean forwards, boolean SILENT, boolean SAVE_CONFIG) {
        try {
            DEPTH_FIX = true;
            if (postProcessor != null) postProcessor.close();
            postProcessor = new ShaderEffect(client.getTextureManager(), client.getResourceManager(), client.getFramebuffer(), (Identifier)Objects.requireNonNull(ShaderDataLoader.get((int) ConfigHelper.getConfig("super_secret_settings"), ShaderRegistryValue.ID)));
            postProcessor.setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
            if (!SILENT) setOverlay(Text.literal(ShaderDataLoader.getShaderName((int) ConfigHelper.getConfig("super_secret_settings"))));
            DEPTH_FIX = false;
            if (!(boolean) ConfigHelper.getConfig("super_secret_settings_enabled")) toggle(client, true, false, false);
            if (SAVE_CONFIG) ConfigHelper.saveConfig(true);
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to set Super Secret Settings.", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
            try {
                cycle(client, forwards, false, SAVE_CONFIG);
            } catch (Exception ignored) {
                ConfigHelper.setConfig("super_secret_settings", 0);
                try {
                    if (postProcessor != null) postProcessor.close();
                    postProcessor = new ShaderEffect(client.getTextureManager(), client.getResourceManager(), client.getFramebuffer(), (Identifier)Objects.requireNonNull(ShaderDataLoader.get((int) ConfigHelper.getConfig("super_secret_settings"), ShaderRegistryValue.ID)));
                    postProcessor.setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
                    if ((boolean) ConfigHelper.getConfig("super_secret_settings_enabled")) toggle(client, true, false, false);
                } catch (Exception ignored2) {}
            }
            if (SAVE_CONFIG) ConfigHelper.saveConfig(true);
        }
    }
    private static void setOverlay(Text message) {
        if ((boolean) ConfigHelper.getConfig("super_secret_settings_show_name")) HUDOverlays.setOverlay(Text.translatable("gui.perspective.message.shader", message).formatted(getRandomColor()));
    }
    public static Formatting getRandomColor() {
        Random random = new Random();
        Formatting COLOR = LAST_COLOR;
        while (COLOR == LAST_COLOR) COLOR = COLORS[(random.nextInt(COLORS.length))];
        LAST_COLOR = COLOR;
        return COLOR;
    }
    public static boolean shouldRenderShader() {
        return postProcessor != null && (boolean) ConfigHelper.getConfig("super_secret_settings_enabled");
    }
    public static void render(float tickDelta) {
        if (postProcessor != null) postProcessor.render(tickDelta);
    }

    public static Object getShaderData(ShaderRegistryValue key) {
        return ShaderDataLoader.get((int) ConfigHelper.getConfig("super_secret_settings"), key);
    }
    public static void cycleShaderModes() {
        if (ConfigHelper.getConfig("super_secret_settings_mode").equals("game")) ConfigHelper.setConfig("super_secret_settings_mode", "screen");
        else if (ConfigHelper.getConfig("super_secret_settings_mode").equals("screen")) ConfigHelper.setConfig("super_secret_settings_mode", "game");
        else ConfigHelper.setConfig("super_secret_settings_mode", "game");
    }
}