/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.shaders;

import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hud.MessageOverlay;
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.translation.TranslationType;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.ShaderStage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.resource.ResourceType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.*;

public class Shader {
	public static final String[] shaderModes = new String[]{"game", "screen"};
	private static final Formatting[] COLORS = new Formatting[]{Formatting.DARK_BLUE, Formatting.DARK_GREEN, Formatting.DARK_AQUA, Formatting.DARK_RED, Formatting.DARK_PURPLE, Formatting.GOLD, Formatting.BLUE, Formatting.GREEN, Formatting.AQUA, Formatting.RED, Formatting.LIGHT_PURPLE, Formatting.YELLOW};
	public static int superSecretSettingsIndex;
	public static Framebuffer depthFramebuffer;
	public static Framebuffer translucentFramebuffer;
	public static Framebuffer entityFramebuffer;
	public static Framebuffer particlesFramebuffer;
	public static Framebuffer weatherFramebuffer;
	public static Framebuffer cloudsFramebuffer;
	public static boolean DEPTH_FIX;
	public static boolean USE_DEPTH;
	public static String RENDER_TYPE;
	@Nullable
	public static PostEffectProcessor postProcessor;
	public static boolean updateLegacyConfig;
	public static int legacyIndex;
	private static Formatting LAST_COLOR;
	public static Camera handfix_camera;
	public static Matrix4f handfix_matrix4f;
	public static void init() {
		try {
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ShaderDataLoader());
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ShaderSoundsDataLoader());
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Caught an error whilst initializing Super Secret Settings", Data.VERSION.getLoggerPrefix(), error);
		}
	}
	public static void tick() {
		if (shouldRenderShader()) {
			if (ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_mode").equals("screen")) showToasts();
			else {
				if (ClientData.CLIENT.world != null) showToasts();
			}
		}
		checkKeybindings();
		if (ShaderDataLoader.isReloading && ClientData.isFinishedInitializing()) {
			boolean saveConfig;
			saveConfig = ConfigHelper.fixConfig();
			if (Shader.updateLegacyConfig) {
				if (Shader.getFullShaderName(Shader.legacyIndex) != null && Shader.isShaderAvailable(Shader.legacyIndex)) {
					ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_shader", Shader.getFullShaderName(Shader.legacyIndex));
				}
				Shader.updateLegacyConfig = false;
			}
			Shader.superSecretSettingsIndex = Shader.getShaderValue((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_shader"));
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_enabled"))
				Shader.set(true, false, false, false);
			if (saveConfig) ConfigHelper.saveConfig();
			ShaderDataLoader.isReloading = false;
		}
	}

	public static void checkKeybindings() {
		if (Keybindings.CYCLE_SHADERS.wasPressed())
			cycle(true, !ClientData.CLIENT.options.sneakKey.isPressed(), true, true, true);
		if (Keybindings.TOGGLE_SHADERS.wasPressed()) toggle(true, true, true, true);
		if (Keybindings.RANDOM_SHADER.wasPressed()) random(true, true, true);
	}

	private static void showToasts() {
		boolean save = false;
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "tutorials")) {
			if (!(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.TUTORIAL, "super_secret_settings")) {
				ClientData.CLIENT.getToastManager().add(new Toast(Translation.getTranslation(Data.VERSION.getID(), "toasts.tutorial.title", new Object[]{Translation.getTranslation(Data.VERSION.getID(), "name"), Translation.getTranslation(Data.VERSION.getID(), "toasts.tutorial.super_secret_settings.title")}), Translation.getTranslation(Data.VERSION.getID(), "toasts.tutorial.super_secret_settings.description", new Object[]{KeyBindingHelper.getBoundKeyOf(Keybindings.CYCLE_SHADERS).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(Keybindings.TOGGLE_SHADERS).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(Keybindings.OPEN_CONFIG).getLocalizedText()}), 280, Toast.Type.TUTORIAL));
				ConfigHelper.setConfig(ConfigHelper.ConfigType.TUTORIAL, "super_secret_settings", true);
				save = true;
			}
		}
		if (!(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.WARNING, "photosensitivity")) {
			ClientData.CLIENT.getToastManager().add(new Toast(Translation.getTranslation(Data.VERSION.getID(), "toasts.warning.title", new Object[]{Translation.getTranslation(Data.VERSION.getID(), "name"), Translation.getTranslation(Data.VERSION.getID(), "toasts.warning.photosensitivity.title")}), Translation.getTranslation(Data.VERSION.getID(), "toasts.warning.photosensitivity.description"), 280, Toast.Type.TUTORIAL));
			ConfigHelper.setConfig(ConfigHelper.ConfigType.WARNING, "photosensitivity", true);
			save = true;
		}
		if (save) ConfigHelper.saveConfig();
	}
	public static Text getTranslatedShaderName(int shaderIndex) {
		if ((boolean)get(shaderIndex, ShaderDataLoader.RegistryValue.TRANSLATABLE)) {
			String namespace = (String) get(shaderIndex, ShaderDataLoader.RegistryValue.NAMESPACE);
			String shaderName = (String) get(shaderIndex, ShaderDataLoader.RegistryValue.SHADER_NAME);
			if (namespace != null && shaderName != null)
				return Translation.getShaderTranslation(namespace, shaderName);
		} else {
			return Text.literal(Objects.requireNonNull(getShaderName(shaderIndex)));
		}
		return null;
	}
	public static String getShaderName(int shaderIndex) {
		String namespace = (String) get(shaderIndex, ShaderDataLoader.RegistryValue.NAMESPACE);
		String shaderName = (String) get(shaderIndex, ShaderDataLoader.RegistryValue.SHADER_NAME);
		if (namespace != null && shaderName != null)
			return ShaderDataLoader.isDuplicatedShaderName(shaderName) ? namespace + ":" + shaderName : shaderName;
		return null;
	}
	public static String getFullShaderName(int SHADER) {
		String NAMESPACE = (String) ShaderDataLoader.get(SHADER, ShaderDataLoader.RegistryValue.NAMESPACE);
		String SHADER_NAME = (String) ShaderDataLoader.get(SHADER, ShaderDataLoader.RegistryValue.SHADER_NAME);
		if (NAMESPACE != null && SHADER_NAME != null) return NAMESPACE + ":" + SHADER_NAME;
		return null;
	}

	public static boolean isShaderAvailable(String id) {
		for (int shader = 0; shader < ShaderDataLoader.REGISTRY.size(); shader++) {
			if (id.contains(":") && id.equals(getFullShaderName(shader))) return true;
			else if ((!id.contains(":")) && id.equals(getShaderName(shader))) return true;
		}
		return false;
	}
	public static boolean isShaderAvailable(int id) {
		return id <= (ShaderDataLoader.getShaderAmount() - 1);
	}

	public static int getShaderValue(String id) {
		for (int shader = 0; shader < ShaderDataLoader.REGISTRY.size(); shader++) {
			if (id.contains(":") && id.equals(getFullShaderName(shader))) return shader;
			else if ((!id.contains(":")) && id.equals(getShaderName(shader))) return shader;
		}
		return 0;
	}
	public static Object get(ShaderDataLoader.RegistryValue shaderRegistryValue) {
		return ShaderDataLoader.get(superSecretSettingsIndex, shaderRegistryValue);
	}
	public static Object get(int shaderIndex, ShaderDataLoader.RegistryValue shaderRegistryValue) {
		return ShaderDataLoader.get(shaderIndex, shaderRegistryValue);
	}
	@SuppressWarnings("unused")
	public static JsonObject getCustom(String namespace) {
		return ShaderDataLoader.getCustom(superSecretSettingsIndex, namespace);
	}
	@SuppressWarnings("unused")
	public static JsonObject getCustom(int shaderIndex, String namespace) {
		return ShaderDataLoader.getCustom(shaderIndex, namespace);
	}
	public static void toggle(boolean playSound, boolean showShaderName, boolean skipDisableScreenModeWhenWorldNull, boolean SAVE_CONFIG) {
		ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_enabled", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_enabled"));
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_enabled")) {
			set(true, playSound, showShaderName, true);
			if (skipDisableScreenModeWhenWorldNull && (ClientData.CLIENT.world == null && (USE_DEPTH || !shouldDisableScreenMode())))
				cycle(true, true, false, true, SAVE_CONFIG);
		} else {
			if (postProcessor != null) {
				postProcessor.close();
				postProcessor = null;
			}
		}
		if (showShaderName) {
			setOverlay(Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_enabled"), TranslationType.ENDISABLE));
		}
		if (SAVE_CONFIG) ConfigHelper.saveConfig();
	}

	public static void cycle(boolean shouldCycle, boolean forwards, boolean playSound, boolean showShaderName, boolean SAVE_CONFIG) {
		try {
			if (shouldCycle && isShaderButtonsEnabled()) {
				if (forwards) {
					if (superSecretSettingsIndex < (ShaderDataLoader.getShaderAmount() - 1))
						superSecretSettingsIndex++;
					else superSecretSettingsIndex = 0;
				} else {
					if (superSecretSettingsIndex > 0)
						superSecretSettingsIndex--;
					else superSecretSettingsIndex = (ShaderDataLoader.getShaderAmount() - 1);
				}
			}
			set(forwards, false, showShaderName, SAVE_CONFIG);
			try {
				if (playSound && (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_sound"))
					ClientData.CLIENT.getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(ShaderSoundsDataLoader.REGISTRY.get(new Random().nextInt(ShaderSoundsDataLoader.REGISTRY.size()))), new Random().nextFloat(0.5F, 1.5F), 1.0F));

			} catch (Exception error) {
				Data.VERSION.getLogger().warn("{} An error occurred whilst trying to play random Super Secret Settings sound.", Data.VERSION.getLoggerPrefix(), error);
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} An error occurred whilst trying to cycle Super Secret Settings.", Data.VERSION.getLoggerPrefix(), error);
		}
	}

	public static void random(boolean playSound, boolean showShaderName, boolean SAVE_CONFIG) {
		try {
			if (isShaderButtonsEnabled()) {
				int SHADER = superSecretSettingsIndex;
				while (SHADER == superSecretSettingsIndex)
					SHADER = Math.max(1, new Random().nextInt(ShaderDataLoader.getShaderAmount()));
				superSecretSettingsIndex = SHADER - 1;
				Shader.set(true, playSound, showShaderName, SAVE_CONFIG);
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} An error occurred whilst trying to randomize Super Secret Settings.", Data.VERSION.getLoggerPrefix(), error);
		}
	}
	public static void set(Boolean forwards, boolean playSound, boolean showShaderName, boolean SAVE_CONFIG) {
		set(forwards, playSound, showShaderName, SAVE_CONFIG, ClientData.CLIENT.getFramebuffer(), ClientData.CLIENT.getWindow().getFramebufferWidth(), ClientData.CLIENT.getWindow().getFramebufferHeight());
	}
	public static void set(Boolean forwards, boolean playSound, boolean showShaderName, boolean SAVE_CONFIG, Framebuffer framebuffer, int framebufferWidth, int framebufferHeight) {
		USE_DEPTH = false;
		DEPTH_FIX = true;
		try {
			if (postProcessor != null) postProcessor.close();
			postProcessor = new PostEffectProcessor(ClientData.CLIENT.getTextureManager(), ClientData.CLIENT.getResourceManager(), framebuffer, (Identifier) Objects.requireNonNull(get(ShaderDataLoader.RegistryValue.ID)));
			postProcessor.setupDimensions(framebufferWidth, framebufferHeight);
			try {
				if (postProcessor != null) {
					if (translucentFramebuffer != null) translucentFramebuffer.delete();
					translucentFramebuffer = postProcessor.getSecondaryTarget("translucent");
					if (entityFramebuffer != null) entityFramebuffer.delete();
					entityFramebuffer = postProcessor.getSecondaryTarget("itemEntity");
					if (particlesFramebuffer != null) particlesFramebuffer.delete();
					particlesFramebuffer = postProcessor.getSecondaryTarget("particles");
					if (weatherFramebuffer != null) weatherFramebuffer.delete();
					weatherFramebuffer = postProcessor.getSecondaryTarget("weather");
					if (cloudsFramebuffer != null) cloudsFramebuffer.delete();
					cloudsFramebuffer = postProcessor.getSecondaryTarget("clouds");
				}
			} catch (Exception error) {
				Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Error setting shader framebuffers: {}", error));
			}
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_shader", getFullShaderName(superSecretSettingsIndex));
			if (showShaderName)
				setOverlay(getTranslatedShaderName(superSecretSettingsIndex));
			try {
				if (playSound && (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_sound"))
					ClientData.CLIENT.getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(ShaderSoundsDataLoader.REGISTRY.get(new Random().nextInt(ShaderSoundsDataLoader.REGISTRY.size()))), new Random().nextFloat(0.5F, 1.5F), 1.0F));

			} catch (Exception error) {
				Data.VERSION.getLogger().warn("{} An error occurred whilst trying to play random Super Secret Settings sound.", Data.VERSION.getLoggerPrefix(), error);
			}
			if (!(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_enabled"))
				toggle(true, false, true, false);
			if (SAVE_CONFIG) ConfigHelper.saveConfig();

		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} An error occurred whilst trying to set Super Secret Settings.", Data.VERSION.getLoggerPrefix(), error);
			try {
				cycle(true, forwards, false, true, SAVE_CONFIG);
			} catch (Exception ignored) {
				superSecretSettingsIndex = 0;
				try {
					if (postProcessor != null) postProcessor.close();
					postProcessor = new PostEffectProcessor(ClientData.CLIENT.getTextureManager(), ClientData.CLIENT.getResourceManager(), ClientData.CLIENT.getFramebuffer(), (Identifier) Objects.requireNonNull(get(ShaderDataLoader.RegistryValue.ID)));
					postProcessor.setupDimensions(ClientData.CLIENT.getWindow().getFramebufferWidth(), ClientData.CLIENT.getWindow().getFramebufferHeight());
					if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_enabled"))
						toggle(false, true, true, false);
				} catch (Exception ignored2) {
				}
			}
			if (SAVE_CONFIG) ConfigHelper.saveConfig();
		}
		DEPTH_FIX = false;
	}

	private static void setOverlay(Text message) {
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_show_name"))
			MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.shader", message).formatted(getRandomColor()));
	}

	public static Formatting getRandomColor() {
		Random random = new Random();
		Formatting COLOR = LAST_COLOR;
		while (COLOR == LAST_COLOR) COLOR = COLORS[(random.nextInt(COLORS.length))];
		LAST_COLOR = COLOR;
		return COLOR;
	}

	public static boolean shouldRenderShader() {
		return postProcessor != null && (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_enabled");
	}

	public static void render(float tickDelta, String renderType) {
		RENDER_TYPE = renderType + (Shader.USE_DEPTH ? ":depth" : "");
		if (postProcessor != null) {
			RenderSystem.enableBlend();
			postProcessor.render(tickDelta);
			RenderSystem.disableBlend();
			RenderSystem.defaultBlendFunc();
		}
	}
	public static boolean shouldDisableScreenMode() {
		return (boolean) Shader.get(ShaderDataLoader.RegistryValue.DISABLE_SCREEN_MODE) || USE_DEPTH;
	}
	public static void cycleShaderModes() {
		List<String> shaderRenderModes = Arrays.stream(shaderModes).toList();
		ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_mode", shaderRenderModes.contains((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_mode")) ? shaderModes[(shaderRenderModes.indexOf((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_mode")) + 1) % shaderModes.length] : shaderModes[0]);
	}
	public static boolean isShaderButtonsEnabled() {
		return ShaderDataLoader.getShaderAmount() > 1;
	}
	// Nettakrim:Souper-Secret-Settings:ShaderResourceLoader.releaseFromType(ShaderStage.Type type);
	// https://github.com/Nettakrim/Souper-Secret-Settings/blob/main/src/main/java/com/nettakrim/souper_secret_settings/ShaderResourceLoader.java
	protected static void releaseShaders() {
		try {
			List<ShaderStage.Type> shaderTypes = new ArrayList<>();
			shaderTypes.add(ShaderStage.Type.VERTEX);
			shaderTypes.add(ShaderStage.Type.FRAGMENT);
			for (ShaderStage.Type type : shaderTypes) {
				List<Map.Entry<String, ShaderStage>> loadedShaders = type.getLoadedShaders().entrySet().stream().toList();
				for (int index = loadedShaders.size() - 1; index > -1; index--) {
					Map.Entry<String, ShaderStage> loadedShader = loadedShaders.get(index);
					String name = loadedShader.getKey();
					if (name.startsWith("rendertype_")) continue;
					if (name.startsWith("position_")) continue;
					if (name.equals("position") || name.equals("particle")) continue;
					loadedShader.getValue().release();
				}
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to release shaders: {}", Data.VERSION.getID(), error);
		}
	}
}