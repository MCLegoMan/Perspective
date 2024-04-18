/*
    Perspective
    Contributor(s): MCLegoMan, Nettakrim
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.shaders;

import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hud.MessageOverlay;
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.common.util.IdentifierHelper;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.releasetypeutils.common.version.Helper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.ShaderStage;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.util.*;

public class Shader {
	public static final String[] shaderModes = new String[]{"game", "screen"};
	private static final Formatting[] COLORS = new Formatting[]{Formatting.DARK_BLUE, Formatting.DARK_GREEN, Formatting.DARK_AQUA, Formatting.DARK_RED, Formatting.DARK_PURPLE, Formatting.GOLD, Formatting.BLUE, Formatting.GREEN, Formatting.AQUA, Formatting.RED, Formatting.LIGHT_PURPLE, Formatting.YELLOW};
	public static int superSecretSettingsIndex;
	public static boolean depthFix;
	public static boolean useDepth;
	public static boolean entityDepthFix;
	public static boolean entityUseDepth;
	public static String renderType;
	public static boolean updateLegacyConfig;
	public static int legacyIndex;
	private static Formatting lastColor;
	@Nullable
	public static PostEffectProcessor postProcessor;
	public static Framebuffer depthFramebuffer;
	public static Framebuffer translucentFramebuffer;
	public static Framebuffer entityFramebuffer;
	public static Framebuffer particlesFramebuffer;
	public static Framebuffer weatherFramebuffer;
	public static Framebuffer cloudsFramebuffer;
	@Nullable
	public static List<PostEffectProcessor> entityPostProcessor;
	public static List<Framebuffer> entityDepthFramebuffer;
	public static List<Framebuffer> entityTranslucentFramebuffer;
	public static List<Framebuffer> entityEntityFramebuffer;
	public static List<Framebuffer> entityParticlesFramebuffer;
	public static List<Framebuffer> entityWeatherFramebuffer;
	public static List<Framebuffer> entityCloudsFramebuffer;
	public static Entity entityShaderEntityPrev;
	public static Entity entityShaderEntity;
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
				if (ClientData.minecraft.world != null) showToasts();
			}
		}
		checkKeybindings();
		if (ShaderDataLoader.isReloading && ClientData.isFinishedInitializing()) {
			boolean saveConfig;
			saveConfig = ConfigHelper.fixConfig();
			if (updateLegacyConfig) {
				if (getFullShaderName(legacyIndex) != null && isShaderAvailable(legacyIndex)) {
					ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_shader", getFullShaderName(legacyIndex));
				}
				updateLegacyConfig = false;
			}
			superSecretSettingsIndex = getShaderValue((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_shader"));
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_enabled"))
				set(true, false, false, false);
			if (saveConfig) ConfigHelper.saveConfig();
			ShaderDataLoader.isReloading = false;
		}
		entityShaderEntityPrev = entityShaderEntity;
		entityShaderEntity = ClientData.minecraft.getCameraEntity();
		if (entityShaderEntity != entityShaderEntityPrev) prepEntityShader(entityShaderEntity);
	}

	public static void checkKeybindings() {
		if (Keybindings.cycleShaders.wasPressed())
			cycle(true, !ClientData.minecraft.options.sneakKey.isPressed(), true, true, true);
		if (Keybindings.toggleShaders.wasPressed()) toggle(true, true, true, true);
		if (Keybindings.randomizeShader.wasPressed()) random(true, true, true);
	}

	private static void showToasts() {
		boolean save = false;
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "tutorials")) {
			if (!(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.TUTORIAL, "super_secret_settings")) {
				ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.VERSION.getID(), "toasts.tutorial.title", new Object[]{Translation.getTranslation(Data.VERSION.getID(), "name"), Translation.getTranslation(Data.VERSION.getID(), "toasts.tutorial.super_secret_settings.title")}), Translation.getTranslation(Data.VERSION.getID(), "toasts.tutorial.super_secret_settings.description", new Object[]{KeyBindingHelper.getBoundKeyOf(Keybindings.cycleShaders).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(Keybindings.toggleShaders).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(Keybindings.openConfig).getLocalizedText()}), 280, Toast.Type.TUTORIAL));
				ConfigHelper.setConfig(ConfigHelper.ConfigType.TUTORIAL, "super_secret_settings", true);
				save = true;
			}
		}
		if (!(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.WARNING, "photosensitivity")) {
			ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.VERSION.getID(), "toasts.warning.title", new Object[]{Translation.getTranslation(Data.VERSION.getID(), "name"), Translation.getTranslation(Data.VERSION.getID(), "toasts.warning.photosensitivity.title")}), Translation.getTranslation(Data.VERSION.getID(), "toasts.warning.photosensitivity.description"), 280, Toast.Type.TUTORIAL));
			ConfigHelper.setConfig(ConfigHelper.ConfigType.WARNING, "photosensitivity", true);
			save = true;
		}
		if (save) ConfigHelper.saveConfig();
	}
	public static MutableText getTranslatedShaderName(int shaderIndex) {
		if ((boolean)get(shaderIndex, ShaderDataLoader.RegistryValue.translatable)) {
			String namespace = (String) get(shaderIndex, ShaderDataLoader.RegistryValue.namespace);
			String shaderName = (String) get(shaderIndex, ShaderDataLoader.RegistryValue.shaderName);
			if (namespace != null && shaderName != null)
				return Translation.getShaderTranslation(namespace, shaderName);
		} else {
			return Translation.getText(getShaderName(shaderIndex), false);
		}
		return null;
	}
	public static String getShaderName(int shaderIndex) {
		String namespace = (String) get(shaderIndex, ShaderDataLoader.RegistryValue.namespace);
		String shaderName = (String) get(shaderIndex, ShaderDataLoader.RegistryValue.shaderName);
		if (namespace != null && shaderName != null)
			return ShaderDataLoader.isDuplicatedShaderName(shaderName) ? namespace + ":" + shaderName : shaderName;
		return null;
	}
	public static String getFullShaderName(int SHADER) {
		String NAMESPACE = (String) ShaderDataLoader.get(SHADER, ShaderDataLoader.RegistryValue.namespace);
		String SHADER_NAME = (String) ShaderDataLoader.get(SHADER, ShaderDataLoader.RegistryValue.shaderName);
		if (NAMESPACE != null && SHADER_NAME != null) return NAMESPACE + ":" + SHADER_NAME;
		return null;
	}

	public static boolean isShaderAvailable(String id) {
		for (int shader = 0; shader < ShaderDataLoader.registry.size(); shader++) {
			if (id.contains(":") && id.equals(getFullShaderName(shader))) return true;
			else if ((!id.contains(":")) && id.equals(getShaderName(shader))) return true;
		}
		return false;
	}
	public static boolean isShaderAvailable(int id) {
		return id <= (ShaderDataLoader.getShaderAmount() - 1);
	}

	public static int getShaderValue(String id) {
		for (int shader = 0; shader < ShaderDataLoader.registry.size(); shader++) {
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
			if (skipDisableScreenModeWhenWorldNull && (ClientData.minecraft.world == null && (useDepth || !shouldDisableScreenMode())))
				cycle(true, true, false, true, SAVE_CONFIG);
		} else {
			if (postProcessor != null) {
				postProcessor.close();
				postProcessor = null;
			}
		}
		if (showShaderName) {
			setOverlay(Translation.getVariableTranslation(Data.VERSION.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_enabled"), Translation.Type.ENDISABLE));
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
					ClientData.minecraft.getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(ShaderSoundsDataLoader.REGISTRY.get(new Random().nextInt(ShaderSoundsDataLoader.REGISTRY.size()))), new Random().nextFloat(0.5F, 1.5F), 1.0F));

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
				set(true, playSound, showShaderName, SAVE_CONFIG);
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} An error occurred whilst trying to randomize Super Secret Settings.", Data.VERSION.getLoggerPrefix(), error);
		}
	}
	public static void set(Boolean forwards, boolean playSound, boolean showShaderName, boolean SAVE_CONFIG) {
		set(forwards, playSound, showShaderName, SAVE_CONFIG, ClientData.minecraft.getFramebuffer(), ClientData.minecraft.getWindow().getFramebufferWidth(), ClientData.minecraft.getWindow().getFramebufferHeight());
	}
	public static void set(Boolean forwards, boolean playSound, boolean showShaderName, boolean SAVE_CONFIG, Framebuffer framebuffer, int framebufferWidth, int framebufferHeight) {
		useDepth = false;
		depthFix = true;
		try {
			if (postProcessor != null) postProcessor.close();
			Identifier shaderId = ShaderDataLoader.getPostShader((String) get(ShaderDataLoader.RegistryValue.id));
			try {
				ClientData.minecraft.getResourceManager().getResourceOrThrow(shaderId);
				postProcessor = new PostEffectProcessor(ClientData.minecraft.getTextureManager(), ClientData.minecraft.getResourceManager(), framebuffer, shaderId);
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
			} catch (FileNotFoundException ignored) {
				// We ignore this error as we would have already caught errors with the post json on load.
				// Errors related to the shader program will still be logged.
			}
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_shader", getFullShaderName(superSecretSettingsIndex));
			if (showShaderName)
				setOverlay(getTranslatedShaderName(superSecretSettingsIndex));
			try {
				if (playSound && (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_sound"))
					ClientData.minecraft.getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(ShaderSoundsDataLoader.REGISTRY.get(new Random().nextInt(ShaderSoundsDataLoader.REGISTRY.size()))), new Random().nextFloat(0.5F, 1.5F), 1.0F));

			} catch (Exception error) {
				Data.VERSION.getLogger().warn("{} An error occurred whilst trying to play random Super Secret Settings sound.", Data.VERSION.getLoggerPrefix(), error);
			}
			if (!(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_enabled"))
				toggle(true, false, true, false);
			if (SAVE_CONFIG) ConfigHelper.saveConfig();

		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} An error occurred whilst trying to set Super Secret Settings.", Data.VERSION.getLoggerPrefix(), error);
			try {
				// If there's an issue, we always save the config.
				cycle(true, forwards, false, true, true);
			} catch (Exception ignored) {
				superSecretSettingsIndex = 0;
				try {
					if (postProcessor != null) postProcessor.close();
					postProcessor = new PostEffectProcessor(ClientData.minecraft.getTextureManager(), ClientData.minecraft.getResourceManager(), ClientData.minecraft.getFramebuffer(), (Identifier) Objects.requireNonNull(get(ShaderDataLoader.RegistryValue.id)));
					postProcessor.setupDimensions(ClientData.minecraft.getWindow().getFramebufferWidth(), ClientData.minecraft.getWindow().getFramebufferHeight());
					if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_enabled"))
						toggle(false, true, true, true);
				} catch (Exception ignored2) {
				}
			}
		}
		depthFix = false;
	}
	public static void setEntityShader(Framebuffer framebuffer, int framebufferWidth, int framebufferHeight, Entity entity) {
		entityUseDepth = false;
		entityDepthFix = true;
		try {
			if (entityPostProcessor != null) {
				if (!entityPostProcessor.isEmpty()) entityPostProcessor.clear();
				for (List<Object> shader : ShaderDataLoader.entityLinkRegistry) {
					if (entity.getType().toString().equalsIgnoreCase((String) shader.get(0))) {
						try {
							Identifier shaderId = ((String) shader.get(1)).contains(":") ? ShaderDataLoader.getPostShader((String) shader.get(1)): ShaderDataLoader.getPostShader(ShaderDataLoader.guessPostShaderNamespace((String) shader.get(1)), (String) shader.get(1));
							PostEffectProcessor shaderProcessor = new PostEffectProcessor(ClientData.minecraft.getTextureManager(), ClientData.minecraft.getResourceManager(), framebuffer, shaderId);
							shaderProcessor.setupDimensions(framebufferWidth, framebufferHeight);
							entityPostProcessor.add(shaderProcessor);
							try {
								if (entityTranslucentFramebuffer == null) entityTranslucentFramebuffer = new ArrayList<>();
								if (entityEntityFramebuffer == null) entityEntityFramebuffer = new ArrayList<>();
								if (entityParticlesFramebuffer == null) entityParticlesFramebuffer = new ArrayList<>();
								if (entityWeatherFramebuffer == null) entityWeatherFramebuffer = new ArrayList<>();
								if (entityCloudsFramebuffer == null) entityCloudsFramebuffer = new ArrayList<>();
								for (PostEffectProcessor postProcessor : entityPostProcessor) {
									entityTranslucentFramebuffer.add(postProcessor.getSecondaryTarget("translucent"));
									entityEntityFramebuffer.add(postProcessor.getSecondaryTarget("itemEntity"));
									entityParticlesFramebuffer.add(postProcessor.getSecondaryTarget("particles"));
									entityWeatherFramebuffer.add(postProcessor.getSecondaryTarget("weather"));
									entityCloudsFramebuffer.add(postProcessor.getSecondaryTarget("clouds"));
								}
							} catch (Exception error) {
								Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Error setting entity link shader framebuffers: {}", error));
							}
						} catch (FileNotFoundException ignored) {
							// We ignore this error as we would have already caught errors with the post json on load.
							// Errors related to the shader program will still be logged.
						}
					}
				}
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} An error occurred whilst trying to set entity link shader.", Data.VERSION.getLoggerPrefix(), error);
		}
		entityDepthFix = false;
	}
	private static void setOverlay(MutableText message) {
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_show_name"))
			MessageOverlay.setOverlay(Translation.getText("gui.perspective.message.shader", true, new Object[]{message}, new Formatting[]{getRandomColor()}));
	}

	public static Formatting getRandomColor() {
		Random random = new Random();
		Formatting COLOR = lastColor;
		while (COLOR == lastColor) COLOR = COLORS[(random.nextInt(COLORS.length))];
		lastColor = COLOR;
		return COLOR;
	}
	public static boolean shouldRenderShader() {
		return postProcessor != null && (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_enabled");
	}
	public static void prepEntityShader(@Nullable Entity entity) {
		if (entity != null) {
			entityPostProcessor = new ArrayList<>();
			setEntityShader(ClientData.minecraft.getFramebuffer(), ClientData.minecraft.getWindow().getFramebufferWidth(), ClientData.minecraft.getWindow().getFramebufferHeight(), entity);
		}
	}
	public static void render(PostEffectProcessor postEffectProcessor, float tickDelta, String type) {
		renderType = type + (useDepth ? ":depth" : "");
		render(postEffectProcessor, tickDelta);
	}
	public static void render(PostEffectProcessor postEffectProcessor, float tickDelta) {
		try {
			if (postEffectProcessor != null) {
				ClientData.minecraft.getResourceManager().getResourceOrThrow(IdentifierHelper.identifierFromString(postEffectProcessor.getName()));
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				postEffectProcessor.render(tickDelta);
				RenderSystem.disableBlend();
				ClientData.minecraft.getFramebuffer().beginWrite(true);
			}
		} catch (FileNotFoundException ignored) {
		}
	}
	public static void renderEntityLink(float tickDelta, boolean isDepth) {
		// This could possibly be changed to have isFirstPerson configurable (first person / all perspective)??.
		if ((isDepth ? shouldUseDepthEntityLink() : (shouldUseEntityLink() && !shouldUseDepthEntityLink())) && ClientData.minecraft.options.getPerspective().isFirstPerson()) {
			for (PostEffectProcessor postProcessor : entityPostProcessor) {
				render(postProcessor, tickDelta);
			}
		}
	}
	public static boolean shouldUseGameRenderer() {
		return String.valueOf(ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_mode")).equalsIgnoreCase("game") || shouldDisableScreenMode();
	}
	public static boolean shouldUseScreenRenderer() {
		return String.valueOf(ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_mode")).equalsIgnoreCase("screen") && !shouldDisableScreenMode();
	}
	public static boolean shouldUseDepthGameRenderer() {
		return shouldUseGameRenderer() && !Data.isModInstalled("iris") && ((boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.EXPERIMENTAL, "improved_shader_renderer") && useDepth);
	}
	public static boolean shouldUseEntityLink() {
		return canUseEntityLink() && entityPostProcessor != null && !entityPostProcessor.isEmpty();
	}
	public static boolean shouldUseDepthEntityLink() {
		return shouldUseEntityLink() && useDepth;
	}
	public static boolean canUseEntityLink() {
		return ((boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.EXPERIMENTAL, "improved_shader_renderer")) && !Data.isModInstalled("souper_secret_settings");
	}
	public static boolean shouldDisableScreenMode() {
		return (boolean) get(ShaderDataLoader.RegistryValue.disableScreenMode) || useDepth;
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