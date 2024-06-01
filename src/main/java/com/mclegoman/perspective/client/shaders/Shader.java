/*
    Perspective
    Contributor(s): MCLegoMan, Nettakrim
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.shaders;

import com.mclegoman.luminance.client.shaders.ShaderDataloader;
import com.mclegoman.luminance.client.shaders.Shaders;
import com.mclegoman.luminance.client.util.CompatHelper;
import com.mclegoman.luminance.client.util.MessageOverlay;
import com.mclegoman.luminance.common.util.IdentifierHelper;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.keybindings.Keybindings;
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.resource.ResourceType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/* This class will be removed in a future version, after everything that is required has been moved to the Shaders class. */
public class Shader {
	public static final String[] shaderModes = new String[]{"game", "screen"};
	private static final List<String> improvedDepthRendererIncompatibleMods = new ArrayList<>();
	private static final Formatting[] colors = new Formatting[]{Formatting.DARK_BLUE, Formatting.DARK_GREEN, Formatting.DARK_AQUA, Formatting.DARK_RED, Formatting.DARK_PURPLE, Formatting.GOLD, Formatting.BLUE, Formatting.GREEN, Formatting.AQUA, Formatting.RED, Formatting.LIGHT_PURPLE, Formatting.YELLOW};
	public static int superSecretSettingsIndex;
	public static boolean depthFix;
	public static boolean useDepth;
	public static boolean entityDepthFix;
	public static boolean entityUseDepth;
	public static String renderType;
	public static boolean updateLegacyConfig;
	public static int legacyIndex;
	private static Formatting lastColor;
	public static Framebuffer translucentFramebuffer;
	public static Framebuffer entityFramebuffer;
	public static Framebuffer particlesFramebuffer;
	public static Framebuffer weatherFramebuffer;
	public static Framebuffer cloudsFramebuffer;
	@Nullable
	public static List<PostEffectProcessor> entityPostProcessor;
	public static List<Framebuffer> entityTranslucentFramebuffer;
	public static List<Framebuffer> entityEntityFramebuffer;
	public static List<Framebuffer> entityParticlesFramebuffer;
	public static List<Framebuffer> entityWeatherFramebuffer;
	public static List<Framebuffer> entityCloudsFramebuffer;
	public static Entity entityShaderEntityPrev;
	public static Entity entityShaderEntity;
	public static boolean finishedAfterInit;
	public static void init() {
		try {
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ShaderSoundsDataLoader());
			addImprovedDepthRendererIncompatibleMod("canvas");
			Uniforms.init();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Caught an error whilst initializing Super Secret Settings: {}", error));
		}
	}
	public static void tick() {
		Uniforms.tick();
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_enabled")) {
			if (ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_mode").equals("screen")) showToasts();
			else {
				if (ClientData.minecraft.world != null) showToasts();
			}
		}
		checkKeybindings();
		if (ClientData.isFinishedInitializing() && !finishedAfterInit) {
			boolean saveConfig;
			saveConfig = ConfigHelper.fixConfig();
			if (updateLegacyConfig) {
				if (Shaders.getShaderName(legacyIndex) != null && Shaders.get(legacyIndex) != null) {
					ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_shader", Shaders.getShaderName(legacyIndex, true));
				}
				updateLegacyConfig = false;
			}
			superSecretSettingsIndex = Shaders.getShaderIndex((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_shader"));
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_enabled"))
				set(true, false, false, false);
			prepEntityShader(ClientData.minecraft.getCameraEntity());
			if (saveConfig) ConfigHelper.saveConfig();
			finishedAfterInit = true;
		}
		entityShaderEntityPrev = entityShaderEntity;
		entityShaderEntity = ClientData.minecraft.getCameraEntity();
		if (entityShaderEntity != entityShaderEntityPrev) prepEntityShader(entityShaderEntity);
	}
	public static void addImprovedDepthRendererIncompatibleMod(String modID) {
		if (!improvedDepthRendererIncompatibleMods.contains(modID)) improvedDepthRendererIncompatibleMods.add(modID);
	}
	public static List<String> getImprovedDepthRendererIncompatibleMods() {
		List<String> incompatibleModsFound = new ArrayList<>();
		for (String modID : improvedDepthRendererIncompatibleMods) {
			if (Data.isModInstalled(modID)) {
				incompatibleModsFound.add(Data.getModContainer(modID).getMetadata().getName());
			}
		}
		return incompatibleModsFound;
	}
	public static boolean shouldDisableImprovedDepthRenderer() {
		return !getImprovedDepthRendererIncompatibleMods().isEmpty() && CompatHelper.isIrisShadersEnabled();
	}
	public static void checkKeybindings() {
		if (Keybindings.cycleShaders.wasPressed())
			cycle(true, !ClientData.minecraft.options.sneakKey.isPressed(), true, true, true);
		if (Keybindings.toggleShaders.wasPressed()) toggle(true, true, true, true);
		if (Keybindings.randomizeShader.wasPressed()) random(true, true, true);
	}

	private static void showToasts() {
		boolean save = false;
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "tutorials")) {
			if (!(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.tutorial, "super_secret_settings")) {
				ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.tutorial.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.tutorial.super_secret_settings.title")}), Translation.getTranslation(Data.version.getID(), "toasts.tutorial.super_secret_settings.description", new Object[]{KeyBindingHelper.getBoundKeyOf(Keybindings.cycleShaders).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(Keybindings.toggleShaders).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(Keybindings.openConfig).getLocalizedText()}), 280, Toast.Type.TUTORIAL));
				ConfigHelper.setConfig(ConfigHelper.ConfigType.tutorial, "super_secret_settings", true);
				save = true;
			}
		}
		if (!(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.warning, "photosensitivity")) {
			ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.warning.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.warning.photosensitivity.title")}), Translation.getTranslation(Data.version.getID(), "toasts.warning.photosensitivity.description"), 280, Toast.Type.TUTORIAL));
			ConfigHelper.setConfig(ConfigHelper.ConfigType.warning, "photosensitivity", true);
			save = true;
		}
		if (save) ConfigHelper.saveConfig();
	}
	public static void toggle(boolean playSound, boolean showShaderName, boolean skipDisableScreenModeWhenWorldNull, boolean SAVE_CONFIG) {
		ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_enabled", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_enabled"));
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_enabled")) {
			set(true, playSound, showShaderName, true);
			if (skipDisableScreenModeWhenWorldNull && ClientData.minecraft.world == null)
				cycle(true, true, false, true, SAVE_CONFIG);
		}
		if (showShaderName) {
			setOverlay(Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_enabled"), Translation.Type.ENDISABLE));
		}
		if (SAVE_CONFIG) ConfigHelper.saveConfig();
	}

	public static void cycle(boolean shouldCycle, boolean forwards, boolean playSound, boolean showShaderName, boolean saveConfig) {
		try {
			if (shouldCycle && isShaderButtonsEnabled()) {
				if (forwards) {
					if (superSecretSettingsIndex < (ShaderDataloader.getShaderAmount() - 1))
						superSecretSettingsIndex++;
					else superSecretSettingsIndex = 0;
				} else {
					if (superSecretSettingsIndex > 0)
						superSecretSettingsIndex--;
					else superSecretSettingsIndex = (ShaderDataloader.getShaderAmount() - 1);
				}
			}
			set(forwards, false, showShaderName, saveConfig);
			try {
				if (playSound && (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_sound"))
					ClientData.minecraft.getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(ShaderSoundsDataLoader.REGISTRY.get(new Random().nextInt(ShaderSoundsDataLoader.REGISTRY.size()))), new Random().nextFloat(0.5F, 1.5F), 1.0F));

			} catch (Exception error) {
				Data.version.sendToLog(LogType.ERROR, Translation.getString("An error occurred whilst trying to play random Super Secret Settings sound: {}", error));
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("An error occurred whilst trying to cycle Super Secret Settings: {}", error));
		}
	}

	public static void random(boolean playSound, boolean showShaderName, boolean saveConfig) {
		try {
			if (isShaderButtonsEnabled()) {
				int SHADER = superSecretSettingsIndex;
				while (SHADER == superSecretSettingsIndex)
					SHADER = Math.max(1, new Random().nextInt(ShaderDataloader.getShaderAmount()));
				superSecretSettingsIndex = SHADER - 1;
				set(true, playSound, showShaderName, saveConfig);
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("An error occurred whilst trying to randomize Super Secret Settings: {}", error));
		}
	}
	public static void set(Boolean forwards, boolean playSound, boolean showShaderName, boolean saveConfig) {
		set(forwards, playSound, showShaderName, saveConfig, ClientData.minecraft.getFramebuffer(), ClientData.minecraft.getWindow().getFramebufferWidth(), ClientData.minecraft.getWindow().getFramebufferHeight());
	}
	public static void set(Boolean forwards, boolean playSound, boolean showShaderName, boolean saveConfig, Framebuffer framebuffer, int framebufferWidth, int framebufferHeight) {
		useDepth = false;
		depthFix = true;
		try {
			Data.version.sendToLog(LogType.INFO, Translation.getString("Shadering shader: {}", ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_shader")));
			try {
//				if (postProcessor != null) {
//					if (translucentFramebuffer != null) translucentFramebuffer.delete();
//					translucentFramebuffer = postProcessor.getSecondaryTarget("translucent");
//					if (entityFramebuffer != null) entityFramebuffer.delete();
//					entityFramebuffer = postProcessor.getSecondaryTarget("itemEntity");
//					if (particlesFramebuffer != null) particlesFramebuffer.delete();
//					particlesFramebuffer = postProcessor.getSecondaryTarget("particles");
//					if (weatherFramebuffer != null) weatherFramebuffer.delete();
//					weatherFramebuffer = postProcessor.getSecondaryTarget("weather");
//					if (cloudsFramebuffer != null) cloudsFramebuffer.delete();
//					cloudsFramebuffer = postProcessor.getSecondaryTarget("clouds");
//				}
			} catch (Exception error) {
				Data.version.sendToLog(LogType.ERROR, Translation.getString("Error setting shader framebuffers: {}", error));
			}
			if (ShaderDataloader.isValidIndex(superSecretSettingsIndex)) ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_shader", Shaders.get(superSecretSettingsIndex).getId());
			if (showShaderName)
				setOverlay((MutableText) Shaders.getShaderName(superSecretSettingsIndex));
			try {
				if (playSound && (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_sound"))
					ClientData.minecraft.getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(ShaderSoundsDataLoader.REGISTRY.get(new Random().nextInt(ShaderSoundsDataLoader.REGISTRY.size()))), new Random().nextFloat(0.5F, 1.5F), 1.0F));

			} catch (Exception error) {
				Data.version.sendToLog(LogType.ERROR, Translation.getString("An error occurred whilst trying to play random Super Secret Settings sound: {}", error));
			}
			if (!(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_enabled"))
				toggle(true, false, true, false);
			if (saveConfig) ConfigHelper.saveConfig();

		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("An error occurred whilst trying to set Super Secret Settings: {}", error));
			try {
				// If there's an issue, we always save the config.
				cycle(true, forwards, false, true, true);
			} catch (Exception ignored) {
				superSecretSettingsIndex = 0;
				try {
					if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_enabled"))
						toggle(false, true, true, true);
				} catch (Exception ignored2) {
				}
			}
		}
		com.mclegoman.perspective.client.shaders.Shaders.set();
		depthFix = false;
	}
	public static void setEntityShader(Framebuffer framebuffer, int framebufferWidth, int framebufferHeight, String entityType) {
		entityUseDepth = false;
		entityDepthFix = true;
		try {
			if (entityPostProcessor != null) {
				if (!entityPostProcessor.isEmpty()) entityPostProcessor.clear();
//				for (List<Object> shader : ShaderDataLoader.entityLinkRegistry) {
//					if (entityType.equalsIgnoreCase((String) shader.get(0))) {
//						try {
//							Identifier shaderId = ((String) shader.get(1)).contains(":") ? ShaderDataLoader.getPostShader((String) shader.get(1)): ShaderDataLoader.getPostShader(ShaderDataLoader.guessPostShaderNamespace((String) shader.get(1)), (String) shader.get(1));
//							PostEffectProcessor shaderProcessor = new PostEffectProcessor(ClientData.minecraft.getTextureManager(), ClientData.minecraft.getResourceManager(), framebuffer, shaderId);
//							shaderProcessor.setupDimensions(framebufferWidth, framebufferHeight);
//							entityPostProcessor.add(shaderProcessor);
//							try {
//								if (entityTranslucentFramebuffer == null) entityTranslucentFramebuffer = new ArrayList<>();
//								if (entityEntityFramebuffer == null) entityEntityFramebuffer = new ArrayList<>();
//								if (entityParticlesFramebuffer == null) entityParticlesFramebuffer = new ArrayList<>();
//								if (entityWeatherFramebuffer == null) entityWeatherFramebuffer = new ArrayList<>();
//								if (entityCloudsFramebuffer == null) entityCloudsFramebuffer = new ArrayList<>();
//								for (PostEffectProcessor postProcessor : entityPostProcessor) {
//									entityTranslucentFramebuffer.add(postProcessor.getSecondaryTarget("translucent"));
//									entityEntityFramebuffer.add(postProcessor.getSecondaryTarget("itemEntity"));
//									entityParticlesFramebuffer.add(postProcessor.getSecondaryTarget("particles"));
//									entityWeatherFramebuffer.add(postProcessor.getSecondaryTarget("weather"));
//									entityCloudsFramebuffer.add(postProcessor.getSecondaryTarget("clouds"));
//								}
//							} catch (Exception error) {
//								Data.version.sendToLog(LogType.ERROR, Translation.getString("Error setting entity link shader framebuffers: {}", error));
//							}
//						} catch (FileNotFoundException ignored) {
//							// We ignore this error as we would have already caught errors with the post json on load.
//							// Errors related to the shader program will still be logged.
//						}
//					}
//				}
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("An error occurred whilst trying to set entity link shader: {}", error));
		}
		entityDepthFix = false;
	}
	private static void setOverlay(MutableText message) {
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_show_name"))
			MessageOverlay.setOverlay(Translation.getText("gui.perspective.message.shader", true, new Object[]{message}, new Formatting[]{getRandomColor()}));
	}

	public static Formatting getRandomColor() {
		Random random = new Random();
		Formatting COLOR = lastColor;
		while (COLOR == lastColor) COLOR = colors[(random.nextInt(colors.length))];
		lastColor = COLOR;
		return COLOR;
	}
	public static void prepEntityShader(@Nullable Entity entity) {
		String entityType = entity != null ? entity.getType().toString() : EntityType.PLAYER.toString();
		entityPostProcessor = new ArrayList<>();
		setEntityShader(ClientData.minecraft.getFramebuffer(), ClientData.minecraft.getWindow().getFramebufferWidth(), ClientData.minecraft.getWindow().getFramebufferHeight(), entityType);
	}
	public static void render(PostEffectProcessor postEffectProcessor, float tickDelta, String type) {
		renderType = type + (useDepth ? ":depth" : "");
		//render(postEffectProcessor, tickDelta);
	}
	public static void render(PostEffectProcessor postEffectProcessor, float tickDelta) {
		try {
			if (postEffectProcessor != null) {
				ClientData.minecraft.getResourceManager().getResourceOrThrow(IdentifierHelper.identifierFromString(postEffectProcessor.getName()));
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				//postEffectProcessor.render(tickDelta);
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
				//render(postProcessor, tickDelta);
			}
		}
	}
	public static boolean shouldUseGameRenderer() {
		return String.valueOf(ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_mode")).equalsIgnoreCase("game");
	}
	public static boolean shouldUseScreenRenderer() {
		return String.valueOf(ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_mode")).equalsIgnoreCase("screen");
	}
	public static boolean shouldUseDepthGameRenderer() {
		return shouldUseGameRenderer() && !shouldDisableImprovedDepthRenderer() && useDepth;
	}
	public static boolean shouldUseEntityLink() {
		return canUseEntityLink() && entityPostProcessor != null && !entityPostProcessor.isEmpty();
	}
	public static boolean shouldUseDepthEntityLink() {
		return shouldUseEntityLink() && useDepth;
	}
	public static boolean canUseEntityLink() {
		return !Data.isModInstalled("souper_secret_settings");
	}
	public static void cycleShaderModes() {
		List<String> shaderRenderModes = Arrays.stream(shaderModes).toList();
		ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_mode", shaderRenderModes.contains((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_mode")) ? shaderModes[(shaderRenderModes.indexOf((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_mode")) + 1) % shaderModes.length] : shaderModes[0]);
	}
	public static boolean isShaderButtonsEnabled() {
		return ShaderDataloader.getShaderAmount() > 1;
	}

}