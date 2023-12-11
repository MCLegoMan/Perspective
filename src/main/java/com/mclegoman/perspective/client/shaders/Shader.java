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
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.translation.TranslationType;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Shader {
	private static final Formatting[] COLORS = new Formatting[]{Formatting.DARK_BLUE, Formatting.DARK_GREEN, Formatting.DARK_AQUA, Formatting.DARK_RED, Formatting.DARK_PURPLE, Formatting.GOLD, Formatting.BLUE, Formatting.GREEN, Formatting.AQUA, Formatting.RED, Formatting.LIGHT_PURPLE, Formatting.YELLOW};
	private static final List<Identifier> SOUND_EVENTS = new ArrayList<>();
	public static Framebuffer DEPTH_FRAME_BUFFER;
	public static boolean DEPTH_FIX;
	public static boolean USE_DEPTH;
	public static String RENDER_TYPE;
	@Nullable
	public static PostEffectProcessor postProcessor;
	private static Formatting LAST_COLOR;
	public static void init() {
		try {
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ShaderDataLoader());
			for (Identifier id : Registries.SOUND_EVENT.getIds()) {
				if (!id.toString().contains("music")) {
					SOUND_EVENTS.add(id);
				}
			}
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Caught an error whilst initializing Super Secret Settings", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
		}
	}
	public static void tick() {
		if (shouldRenderShader()) {
			if (ConfigHelper.getConfig("super_secret_settings_mode").equals("screen")) showToasts();
			else {
				if (ClientData.CLIENT.world != null) showToasts();
			}
		}
		checkKeybindings();
	}
	public static void checkKeybindings() {
		if (Keybindings.CYCLE_SHADERS.wasPressed())
			cycle(true, !ClientData.CLIENT.options.sneakKey.isPressed(), true, true, true, true);
		if (Keybindings.TOGGLE_SHADERS.wasPressed()) toggle(true, true, true, true);
		if (Keybindings.RANDOM_SHADER.wasPressed()) random(true, true, true);
	}
	private static void showToasts() {
		boolean save = false;
		if ((boolean) ConfigHelper.getConfig("tutorials")) {
			if (!(boolean) ConfigHelper.getTutorialConfig("super_secret_settings")) {
				ClientData.CLIENT.getToastManager().add(new Toast(Translation.getTranslation("toasts.tutorial.title", new Object[]{Translation.getTranslation("name"), Translation.getTranslation("toasts.tutorial.super_secret_settings.title")}), Translation.getTranslation("toasts.tutorial.super_secret_settings.description", new Object[]{KeyBindingHelper.getBoundKeyOf(Keybindings.CYCLE_SHADERS).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(Keybindings.TOGGLE_SHADERS).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(Keybindings.OPEN_CONFIG).getLocalizedText()}), 280, Toast.Type.TUTORIAL));
				ConfigHelper.setTutorialConfig("super_secret_settings", true);
				save = true;
			}
		}
		if (!(boolean) ConfigHelper.getWarningConfig("photosensitivity")) {
			ClientData.CLIENT.getToastManager().add(new Toast(Translation.getTranslation("toasts.warning.title", new Object[]{Translation.getTranslation("name"), Translation.getTranslation("toasts.warning.photosensitivity.title")}), Translation.getTranslation("toasts.warning.photosensitivity.description"), 280, Toast.Type.TUTORIAL));
			ConfigHelper.setWarningConfig("photosensitivity", true);
			save = true;
		}
		if (save) ConfigHelper.saveConfig(true);
	}
	public static void toggle(boolean playSound, boolean showShaderName, boolean skipDisableScreenModeWhenWorldNull, boolean SAVE_CONFIG) {
		ConfigHelper.setConfig("super_secret_settings_enabled", !(boolean) ConfigHelper.getConfig("super_secret_settings_enabled"));
		if ((boolean) ConfigHelper.getConfig("super_secret_settings_enabled")) {
			set(true, playSound, showShaderName, true);
			if (skipDisableScreenModeWhenWorldNull && (ClientData.CLIENT.world == null && (USE_DEPTH || !shouldDisableScreenMode())))
				cycle(true, true, false, true, true, SAVE_CONFIG);
		} else {
			if (postProcessor != null) {
				postProcessor.close();
				postProcessor = null;
			}
		}
		if (showShaderName) {
			setOverlay(Translation.getVariableTranslation((boolean) ConfigHelper.getConfig("super_secret_settings_enabled"), TranslationType.ENDISABLE));
		}
		if (SAVE_CONFIG) ConfigHelper.saveConfig(true);
	}
	public static void cycle(boolean shouldCycle, boolean forwards, boolean playSound, boolean showShaderName, boolean skipDisableScreenModeWhenWorldNull, boolean SAVE_CONFIG) {
		try {
			boolean shouldLoop = true;
			while (shouldLoop) {
				if (shouldCycle) {
					if (forwards) {
						if ((int) ConfigHelper.getConfig("super_secret_settings") < ShaderDataLoader.getShaderAmount())
							ConfigHelper.setConfig("super_secret_settings", (int) ConfigHelper.getConfig("super_secret_settings") + 1);
						else ConfigHelper.setConfig("super_secret_settings", 0);
					} else {
						if ((int) ConfigHelper.getConfig("super_secret_settings") > 0)
							ConfigHelper.setConfig("super_secret_settings", (int) ConfigHelper.getConfig("super_secret_settings") - 1);
						else ConfigHelper.setConfig("super_secret_settings", ShaderDataLoader.getShaderAmount());
					}
				}
				set(forwards, playSound, showShaderName, SAVE_CONFIG);
				if (skipDisableScreenModeWhenWorldNull) {
					if (ClientData.CLIENT.world == null && (USE_DEPTH || !shouldDisableScreenMode())) shouldLoop = false;
					else shouldLoop = false;
				} else {
					shouldLoop = false;
				}
			}
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to cycle Super Secret Settings.", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
		}
	}
	public static void random(boolean playSound, boolean showShaderName, boolean SAVE_CONFIG) {
		try {
			int SHADER = (int) ConfigHelper.getConfig("super_secret_settings");
			while (SHADER == (int) ConfigHelper.getConfig("super_secret_settings"))
				SHADER = Math.max(1, new Random().nextInt(ShaderDataLoader.getShaderAmount()));
			ConfigHelper.setConfig("super_secret_settings", SHADER);
			Shader.set(true, playSound, showShaderName, SAVE_CONFIG);
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to randomize Super Secret Settings.", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
		}
	}
	public static void set(Boolean forwards, boolean playSound, boolean showShaderName, boolean SAVE_CONFIG) {
		USE_DEPTH = false;
		DEPTH_FIX = true;
		try {
			if (postProcessor != null) postProcessor.close();
			postProcessor = new PostEffectProcessor(ClientData.CLIENT.getTextureManager(), ClientData.CLIENT.getResourceManager(), ClientData.CLIENT.getFramebuffer(), (Identifier) Objects.requireNonNull(ShaderDataLoader.get((int) ConfigHelper.getConfig("super_secret_settings"), ShaderRegistryValue.ID)));
			postProcessor.setupDimensions(ClientData.CLIENT.getWindow().getFramebufferWidth(), ClientData.CLIENT.getWindow().getFramebufferHeight());
			if (showShaderName)
				setOverlay(Text.literal(ShaderDataLoader.getShaderName((int) ConfigHelper.getConfig("super_secret_settings"))));
			try {
				if (playSound && (boolean) ConfigHelper.getConfig("super_secret_settings_sound"))
					ClientData.CLIENT.getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(SOUND_EVENTS.get(new Random().nextInt(SOUND_EVENTS.size() - 1))), 1.0F));

			} catch (Exception error) {
				Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to play random Super Secret Settings sound.", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
			}
			if (!(boolean) ConfigHelper.getConfig("super_secret_settings_enabled"))
				toggle(true, false, true, false);
			if (SAVE_CONFIG) ConfigHelper.saveConfig(true);
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to set Super Secret Settings.", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
			try {
				cycle(true, forwards, false, true, true, SAVE_CONFIG);
			} catch (Exception ignored) {
				ConfigHelper.setConfig("super_secret_settings", 0);
				try {
					if (postProcessor != null) postProcessor.close();
					postProcessor = new PostEffectProcessor(ClientData.CLIENT.getTextureManager(), ClientData.CLIENT.getResourceManager(), ClientData.CLIENT.getFramebuffer(), (Identifier) Objects.requireNonNull(ShaderDataLoader.get((int) ConfigHelper.getConfig("super_secret_settings"), ShaderRegistryValue.ID)));
					postProcessor.setupDimensions(ClientData.CLIENT.getWindow().getFramebufferWidth(), ClientData.CLIENT.getWindow().getFramebufferHeight());
					if ((boolean) ConfigHelper.getConfig("super_secret_settings_enabled"))
						toggle(false, true, true, false);
				} catch (Exception ignored2) {
				}
			}
			if (SAVE_CONFIG) ConfigHelper.saveConfig(true);
		}
		DEPTH_FIX = false;
	}
	private static void setOverlay(Text message) {
		if ((boolean) ConfigHelper.getConfig("super_secret_settings_show_name"))
			HUDOverlays.setOverlay(Text.translatable("gui.perspective.message.shader", message).formatted(getRandomColor()));
	}
	public static Formatting getRandomColor() {
		Random random = new Random();
		Formatting COLOR = LAST_COLOR;
		while (COLOR == LAST_COLOR) COLOR = COLORS[(random.nextInt(COLORS.length))];
		LAST_COLOR = COLOR;
		return COLOR;
	}
	public static boolean shouldRenderShader() {
		return (postProcessor != null && (boolean) ConfigHelper.getConfig("super_secret_settings_enabled")) || ClientData.CLIENT.gameRenderer.isRenderingPanorama();
	}
	public static void render(float tickDelta, String renderType) {
		RENDER_TYPE = renderType;
		if (postProcessor != null) postProcessor.render(tickDelta);
	}
	public static boolean shouldDisableScreenMode() {
		return (boolean) Shader.getShaderData(ShaderRegistryValue.DISABLE_SCREEN_MODE) || USE_DEPTH;
	}
	public static Object getShaderData(ShaderRegistryValue key) {
		return ShaderDataLoader.get((int) ConfigHelper.getConfig("super_secret_settings"), key);
	}
	public static void cycleShaderModes() {
		if (ConfigHelper.getConfig("super_secret_settings_mode").equals("game"))
			ConfigHelper.setConfig("super_secret_settings_mode", "screen");
		else if (ConfigHelper.getConfig("super_secret_settings_mode").equals("screen"))
			ConfigHelper.setConfig("super_secret_settings_mode", "game");
		else ConfigHelper.setConfig("super_secret_settings_mode", "game");
	}
	public static void saveDepth(boolean renderFastFancy, boolean renderFabulous, boolean beginWrite) {
		boolean currentGraphics = ClientData.CLIENT.options.getGraphicsMode().getValue().equals(GraphicsMode.FABULOUS);
		boolean shouldSaveDepth = false;
		if (!currentGraphics && renderFastFancy) shouldSaveDepth = true;
		else if (currentGraphics && renderFabulous) shouldSaveDepth = true;
		if (shouldSaveDepth) {
			if (Shader.USE_DEPTH) {
				Shader.DEPTH_FRAME_BUFFER.copyDepthFrom(ClientData.CLIENT.getFramebuffer());
				if (beginWrite) ClientData.CLIENT.getFramebuffer().beginWrite(false);
			}
		}
	}
}