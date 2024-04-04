/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.config;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hide.Hide;
import com.mclegoman.perspective.client.screen.config.ConfigScreen;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.shaders.ShaderDataLoader;
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.ui.UIBackground;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.client.util.PerspectiveLogo;
import com.mclegoman.perspective.client.util.ResourcePacks;
import com.mclegoman.perspective.client.util.UpdateChecker;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.common.util.Couple;
import com.mclegoman.perspective.common.util.IdentifierHelper;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigHelper {
	public static final boolean EXPERIMENTS_AVAILABLE = true;
	protected static final int SAVE_VIA_TICK_SAVE_TICK = 20;
	protected static final int DEFAULT_CONFIG_VERSION = 17;
	protected static boolean SAVE_VIA_TICK = false;
	protected static int SAVE_VIA_TICK_TICKS = 0;
	private static boolean SEEN_DEVELOPMENT_WARNING = false;
	private static boolean SHOW_DOWNGRADE_WARNING = false;
	private static boolean SEEN_DOWNGRADE_WARNING = false;
	private static boolean SHOW_LICENSE_UPDATE_NOTICE = false;
	private static boolean SEEN_LICENSE_UPDATE_NOTICE = false;
	private static boolean UPDATED_CONFIG = false;
	private static boolean SAVING_CONFIG = false;
	private static final List<ConfigType> saveConfigs = new ArrayList<>();
	public static boolean isSaving() {
		return SAVING_CONFIG;
	}
	public static void init() {
		try {
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ConfigDataLoader());
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to initialize config!: {}", error));
		}
	}
	protected static void loadConfig() {
		try {
			Config.init();
			ExperimentalConfig.init();
			TutorialsConfig.init();
			WarningsConfig.init();
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, "Failed to load configs!");
		}
		if (!ClientData.getFinishedInitializingAfterConfig()) afterConfig();
	}
	protected static void afterConfig() {
		try {
			ResourcePacks.initAfterConfig();
			UpdateChecker.checkForUpdates(Data.VERSION);
			ClientData.setFinishedInitializingAfterConfig(true);
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, "Failed to init afterConfig.");
		}
	}
	public static void tick() {
		try {
			if (!UPDATED_CONFIG) ConfigHelper.updateConfig();
			if (Keybindings.OPEN_CONFIG.wasPressed())
				ClientData.CLIENT.setScreen(new ConfigScreen(ClientData.CLIENT.currentScreen, false, 1));
			if (SAVE_VIA_TICK_TICKS < SAVE_VIA_TICK_SAVE_TICK) SAVE_VIA_TICK_TICKS += 1;
			else {
				if (fixConfig()) saveConfig();
				if (SAVE_VIA_TICK) {
					for (ConfigType configType : saveConfigs) {
						saveConfig(configType);
					}
					saveConfigs.clear();
					SAVE_VIA_TICK = false;
					SAVING_CONFIG = false;
				}
				SAVE_VIA_TICK_TICKS = 0;
			}
			showToasts();
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, "Failed to tick config!");
		}
	}
	private static void showToasts() {
		if (Data.VERSION.isDevelopmentBuild() && !SEEN_DEVELOPMENT_WARNING) {
			Data.VERSION.sendToLog(Helper.LogType.INFO, "Development Build: Please help us improve by submitting bug reports if you encounter any issues.");
			ClientData.CLIENT.getToastManager().add(new Toast(Translation.getTranslation(Data.VERSION.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.VERSION.getID(), "name"), Translation.getTranslation(Data.VERSION.getID(), "toasts.development_warning.title")}), Translation.getTranslation(Data.VERSION.getID(), "toasts.development_warning.description"), 320, Toast.Type.WARNING));
			SEEN_DEVELOPMENT_WARNING = true;
		}
		if (SHOW_DOWNGRADE_WARNING && !SEEN_DOWNGRADE_WARNING) {
			Data.VERSION.sendToLog(Helper.LogType.INFO, "Downgrading is not supported: You may experience configuration related issues.");
			ClientData.CLIENT.getToastManager().add(new Toast(Translation.getTranslation(Data.VERSION.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.VERSION.getID(), "name"), Translation.getTranslation(Data.VERSION.getID(), "toasts.downgrade_warning.title")}), Translation.getTranslation(Data.VERSION.getID(), "toasts.downgrade_warning.description"), 320, Toast.Type.WARNING));
			SEEN_DOWNGRADE_WARNING = true;
		}
		if (SHOW_LICENSE_UPDATE_NOTICE && !SEEN_LICENSE_UPDATE_NOTICE) {
			Data.VERSION.sendToLog(Helper.LogType.INFO, "License Update: Perspective is now licensed under LGPL-3.0-or-later.");
			ClientData.CLIENT.getToastManager().add(new Toast(Translation.getTranslation(Data.VERSION.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.VERSION.getID(), "name"), Translation.getTranslation(Data.VERSION.getID(), "toasts.license_update.title")}), Translation.getTranslation(Data.VERSION.getID(), "toasts.license_update.description"), 320, Toast.Type.INFO));
			SEEN_LICENSE_UPDATE_NOTICE = true;
		}
	}
	public static void updateConfig() {
		try {
			boolean shouldSaveConfig = false;
			if (Config.config.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) != DEFAULT_CONFIG_VERSION) {
				if (Config.config.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < DEFAULT_CONFIG_VERSION) {
					Data.VERSION.getLogger().info("{} Attempting to update config to the latest version.", Data.VERSION.getLoggerPrefix());
					if (Config.config.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 3) {
						setConfig(ConfigType.NORMAL, "zoom_level", 100 - (int) getConfig(ConfigType.NORMAL, "zoom_level"));
					}
					if (Config.config.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 7) {
						String SUPER_SECRET_SETTINGS_MODE = Config.config.getOrDefault("super_secret_settings_mode", ConfigDataLoader.superSecretSettingsMode);
						if (SUPER_SECRET_SETTINGS_MODE.equals("false")) setConfig(ConfigType.NORMAL, "super_secret_settings_mode", "game");
						else if (SUPER_SECRET_SETTINGS_MODE.equals("true"))
							setConfig(ConfigType.NORMAL, "super_secret_settings_mode", "screen");
						else setConfig(ConfigType.NORMAL, "super_secret_settings_mode", "game");
						Boolean HIDE_HUD = Config.config.getOrDefault("hide_hud", true);
						setConfig(ConfigType.NORMAL, "zoom_hide_hud", HIDE_HUD);
						setConfig(ConfigType.NORMAL, "hold_perspective_hide_hud", HIDE_HUD);
					}
					if (Config.config.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 8) {
						SHOW_LICENSE_UPDATE_NOTICE = true;
					}
					if (Config.config.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 11) {
						setConfig(ConfigType.NORMAL, "zoom_transition", Config.config.getOrDefault("zoom_mode", ConfigDataLoader.zoomTransition));
						setConfig(ConfigType.NORMAL, "zoom_show_percentage", Config.config.getOrDefault("zoom_overlay_message", ConfigDataLoader.zoomShowPercentage));
						setConfig(ConfigType.NORMAL, "super_secret_settings_show_name", Config.config.getOrDefault("super_secret_settings_overlay_message", ConfigDataLoader.superSecretSettingsShowName));
					}
					if (Config.config.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 14) {
						Shader.updateLegacyConfig = true;
						Shader.legacyIndex = Config.config.getOrDefault("super_secret_settings", 0);
					}
					if (Config.config.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 16) {
						String cameraMode = Config.config.getOrDefault("zoom_camera_mode", ConfigDataLoader.zoomScaleMode);
						switch (cameraMode) {
							case "default" -> setConfig(ConfigType.NORMAL, "zoom_scale_mode", "vanilla");
							case "spyglass" -> setConfig(ConfigType.NORMAL, "zoom_scale_mode", "scaled");
						}
					}
					if (Config.config.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 17) {
						setConfig(ConfigType.NORMAL, "zoom_type", Data.VERSION.getID() + ":" + Config.config.getOrDefault("zoom_type", ConfigDataLoader.zoomType.replace((Data.VERSION.getID() + ":"), "")));
						setConfig(ConfigType.NORMAL, "title_screen", Config.config.getOrDefault("dirt_title_screen", false) ? "dirt" : "default");
					}
					setConfig(ConfigType.NORMAL, "config_version", DEFAULT_CONFIG_VERSION);
					Data.VERSION.getLogger().info("{} Successfully updated config to the latest version.", Data.VERSION.getLoggerPrefix());
				} else if (Config.config.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) > DEFAULT_CONFIG_VERSION) {
					if (!SEEN_DOWNGRADE_WARNING) {
						Data.VERSION.getLogger().warn("{} Downgrading is not supported. You may experience configuration related issues.", Data.VERSION.getLoggerPrefix());
						SHOW_DOWNGRADE_WARNING = true;
					}
				}
				shouldSaveConfig = true;
			}
			if (fixConfig()) shouldSaveConfig = true;
			if (shouldSaveConfig) saveConfig();
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, "Failed to update config!");
		}
		UPDATED_CONFIG = true;
	}
	private static void saveConfig(ConfigType config) {
		try {
			SAVING_CONFIG = true;
			fixConfig();
			switch (config) {
				case NORMAL -> Config.save();
				case EXPERIMENTAL -> ExperimentalConfig.save();
				case TUTORIAL -> TutorialsConfig.save();
				case WARNING -> WarningsConfig.save();
			}
			SAVING_CONFIG = false;
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, "Failed to save config!");
		}
	}
	private static void addSaveConfig(ConfigType config) {
		try {
			switch (config) {
				case NORMAL -> {if (!saveConfigs.contains(ConfigType.NORMAL)) saveConfigs.add(ConfigType.NORMAL);}
				case EXPERIMENTAL -> {if (!saveConfigs.contains(ConfigType.EXPERIMENTAL)) saveConfigs.add(ConfigType.EXPERIMENTAL);}
				case TUTORIAL -> {if (!saveConfigs.contains(ConfigType.TUTORIAL)) saveConfigs.add(ConfigType.TUTORIAL);}
				case WARNING -> {if (!saveConfigs.contains(ConfigType.NORMAL)) saveConfigs.add(ConfigType.WARNING);}
			}
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, "Failed to add save config!");
		}
	}
	public static void saveConfig() {
		try {
			SAVE_VIA_TICK = true;
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, "Failed to set config tick save!");
		}
	}
	public static boolean fixConfig() {
		if (ClientData.isFinishedInitializing()) {
			boolean hasFixedConfig = false;
			if ((int) getConfig(ConfigType.NORMAL, "zoom_level") < 0 || (int) getConfig(ConfigType.NORMAL, "zoom_level") > 100) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: zoom_level was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "zoom_level") + ")");
				hasFixedConfig = setConfig(ConfigType.NORMAL, "zoom_level", ConfigDataLoader.zoomLevel);
			}
			if ((int) getConfig(ConfigType.NORMAL, "zoom_increment_size") < 0 || (int) getConfig(ConfigType.NORMAL, "zoom_increment_size") > 10) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: zoom_increment_size was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "zoom_increment_size") + ")");
				hasFixedConfig = setConfig(ConfigType.NORMAL, "zoom_increment_size", ConfigDataLoader.zoomIncrementSize);
			}
			if (!Arrays.stream(Zoom.zoomTransitions).toList().contains((String) getConfig(ConfigType.NORMAL, "zoom_transition"))) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: zoom_transition was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "zoom_transition") + ")");
				hasFixedConfig = setConfig(ConfigType.NORMAL, "zoom_transition", ConfigDataLoader.zoomTransition);
			}
			if (!Arrays.stream(Zoom.zoomScaleModes).toList().contains((String) getConfig(ConfigType.NORMAL, "zoom_scale_mode"))) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: zoom_scale_mode was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "zoom_scale_mode") + ")");
				hasFixedConfig = setConfig(ConfigType.NORMAL, "zoom_scale_mode", ConfigDataLoader.zoomScaleMode);
			}
			if (!Zoom.isValidZoomType(IdentifierHelper.identifierFromString((String) ConfigHelper.getConfig(ConfigType.NORMAL, "zoom_type")))) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: zoom_type was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "zoom_type") + ")");
				hasFixedConfig = setConfig(ConfigType.NORMAL, "zoom_type", ConfigDataLoader.zoomType);
			}
			if (!Shader.isShaderAvailable((String) ConfigHelper.getConfig(ConfigType.NORMAL, "super_secret_settings_shader"))) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: super_secret_settings_shader was invalid and have been reset to prevent any unexpected issues. (" + ConfigHelper.getConfig(ConfigType.NORMAL, "super_secret_settings_shader") + ")");
				if (!Shader.isShaderAvailable(ConfigDataLoader.superSecretSettingsShader)) {
					Shader.superSecretSettingsIndex = Math.min(Shader.superSecretSettingsIndex, ShaderDataLoader.REGISTRY.size() - 1);
					hasFixedConfig = setConfig(ConfigType.NORMAL, "super_secret_settings_shader", Shader.getFullShaderName(Shader.superSecretSettingsIndex));
				} else Shader.superSecretSettingsIndex = Shader.getShaderValue(ConfigDataLoader.superSecretSettingsShader);
			}
			if (!Arrays.stream(Shader.shaderModes).toList().contains((String) getConfig(ConfigType.NORMAL, "super_secret_settings_mode"))) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: super_secret_settings_mode was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "super_secret_settings_mode") + ")");
				hasFixedConfig = setConfig(ConfigType.NORMAL, "super_secret_settings_mode", ConfigDataLoader.superSecretSettingsMode);
			}
			if ((int) getConfig(ConfigType.NORMAL, "force_pride_type_index") < 0 || (int) getConfig(ConfigType.NORMAL, "force_pride_type_index") > PerspectiveLogo.pride_types.length) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: force_pride_type_index was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "force_pride_type_index") + ")");
				hasFixedConfig = setConfig(ConfigType.NORMAL, "force_pride_type_index", ConfigDataLoader.forcePrideTypeIndex);
			}
			if (!Arrays.stream(Hide.hideCrosshairModes).toList().contains((String) getConfig(ConfigType.NORMAL, "hide_crosshair"))) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: hide_crosshair was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "hide_crosshair") + ")");
				hasFixedConfig = setConfig(ConfigType.NORMAL, "hide_crosshair", ConfigDataLoader.hideCrosshair);
			}
			if (!Arrays.stream(UpdateChecker.detectUpdateChannels).toList().contains((String) getConfig(ConfigType.NORMAL, "detect_update_channel"))) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: detect_update_channel was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "detect_update_channel") + ")");
				hasFixedConfig = setConfig(ConfigType.NORMAL, "detect_update_channel", ConfigDataLoader.detectUpdateChannel);
			}
			if (!UIBackground.isValidTitleScreenBackgroundType((String) getConfig(ConfigType.NORMAL, "title_screen"))) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: title_screen was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "title_screen") + ")");
				hasFixedConfig = setConfig(ConfigType.NORMAL, "title_screen", UIBackground.isValidTitleScreenBackgroundType(ConfigDataLoader.titleScreen) ? ConfigDataLoader.titleScreen : "default");
			}
			if (!UIBackground.isValidUIBackgroundType((String) getConfig(ConfigType.NORMAL, "ui_background"))) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: ui_background was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "ui_background") + ")");
				hasFixedConfig = setConfig(ConfigType.NORMAL, "ui_background", UIBackground.isValidUIBackgroundType(ConfigDataLoader.uiBackground) ? ConfigDataLoader.uiBackground : "default");
			}
			return hasFixedConfig;
		}
		return false;
	}
	public static boolean resetConfig() {
		boolean configChanged = false;
		try {
			configChanged = setConfig(ConfigType.NORMAL, "zoom_level", MathHelper.clamp(ConfigDataLoader.zoomLevel, 0, 100));
			configChanged = setConfig(ConfigType.NORMAL, "zoom_increment_size", MathHelper.clamp(ConfigDataLoader.zoomIncrementSize, 1, 10));
			configChanged = setConfig(ConfigType.NORMAL, "zoom_transition", ConfigDataLoader.zoomTransition);
			configChanged = setConfig(ConfigType.NORMAL, "zoom_scale_mode", ConfigDataLoader.zoomScaleMode);
			configChanged = setConfig(ConfigType.NORMAL, "zoom_hide_hud", ConfigDataLoader.zoomHideHud);
			configChanged = setConfig(ConfigType.NORMAL, "zoom_show_percentage", ConfigDataLoader.zoomShowPercentage);
			configChanged = setConfig(ConfigType.NORMAL, "zoom_type", ConfigDataLoader.zoomType);
			configChanged = setConfig(ConfigType.NORMAL, "hold_perspective_hide_hud", ConfigDataLoader.holdPerspectiveHideHud);
			configChanged = setConfig(ConfigType.NORMAL, "super_secret_settings_shader", ConfigDataLoader.superSecretSettingsShader);
			if (Shader.isShaderAvailable((String) ConfigHelper.getConfig(ConfigType.NORMAL, "super_secret_settings_shader"))) Shader.superSecretSettingsIndex = Shader.getShaderValue((String) ConfigHelper.getConfig(ConfigType.NORMAL, "super_secret_settings_shader"));
			else Shader.superSecretSettingsIndex = Math.min(Shader.superSecretSettingsIndex, ShaderDataLoader.REGISTRY.size() - 1);
			configChanged = setConfig(ConfigType.NORMAL, "super_secret_settings_mode", ConfigDataLoader.superSecretSettingsMode);
			configChanged = setConfig(ConfigType.NORMAL, "super_secret_settings_enabled", ConfigDataLoader.superSecretSettingsEnabled);
			if ((boolean) ConfigHelper.getConfig(ConfigType.NORMAL, "super_secret_settings_enabled")) Shader.set(true, false, false, false);
			configChanged = setConfig(ConfigType.NORMAL, "super_secret_settings_sound", ConfigDataLoader.superSecretSettingsSound);
			configChanged = setConfig(ConfigType.NORMAL, "super_secret_settings_show_name", ConfigDataLoader.superSecretSettingsShowName);
			configChanged = setConfig(ConfigType.NORMAL, "super_secret_settings_selection_blur", ConfigDataLoader.superSecretSettingsSelectionBlur);
			configChanged = setConfig(ConfigType.NORMAL, "textured_named_entity", ConfigDataLoader.texturedNamedEntity);
			configChanged = setConfig(ConfigType.NORMAL, "textured_random_entity", ConfigDataLoader.texturedRandomEntity);
			configChanged = setConfig(ConfigType.NORMAL, "allow_april_fools", ConfigDataLoader.allowAprilFools);
			configChanged = setConfig(ConfigType.NORMAL, "force_april_fools", ConfigDataLoader.forceAprilFools);
			configChanged = setConfig(ConfigType.NORMAL, "position_overlay", ConfigDataLoader.positionOverlay);
			configChanged = setConfig(ConfigType.NORMAL, "version_overlay", ConfigDataLoader.versionOverlay);
			configChanged = setConfig(ConfigType.NORMAL, "force_pride", ConfigDataLoader.forcePride);
			configChanged = setConfig(ConfigType.NORMAL, "force_pride_type", ConfigDataLoader.forcePrideType);
			configChanged = setConfig(ConfigType.NORMAL, "force_pride_type_index", MathHelper.clamp(ConfigDataLoader.forcePrideTypeIndex, 0, PerspectiveLogo.pride_types.length));
			configChanged = setConfig(ConfigType.NORMAL, "show_death_coordinates", ConfigDataLoader.showDeathCoordinates);
			configChanged = setConfig(ConfigType.NORMAL, "title_screen", ConfigDataLoader.titleScreen);
			configChanged = setConfig(ConfigType.NORMAL, "ui_background", ConfigDataLoader.uiBackground);
			configChanged = setConfig(ConfigType.NORMAL, "ui_background_texture", ConfigDataLoader.uiBackgroundTexture);
			configChanged = setConfig(ConfigType.NORMAL, "hide_block_outline", ConfigDataLoader.hideBlockOutline);
			configChanged = setConfig(ConfigType.NORMAL, "hide_crosshair", ConfigDataLoader.hideCrosshair);
			configChanged = setConfig(ConfigType.NORMAL, "hide_armor", ConfigDataLoader.hideArmor);
			configChanged = setConfig(ConfigType.NORMAL, "hide_nametags", ConfigDataLoader.hideNametags);
			configChanged = setConfig(ConfigType.NORMAL, "hide_players", ConfigDataLoader.hidePlayers);
			configChanged = setConfig(ConfigType.NORMAL, "hide_show_message", ConfigDataLoader.hideShowMessage);
			configChanged = setConfig(ConfigType.NORMAL, "tutorials", ConfigDataLoader.tutorials);
			configChanged = setConfig(ConfigType.NORMAL, "detect_update_channel", ConfigDataLoader.detectUpdateChannel);
			configChanged = setConfig(ConfigType.NORMAL, "debug", ConfigDataLoader.debug);
			configChanged = setConfig(ConfigType.NORMAL, "test_resource_pack", ConfigDataLoader.testResourcePack);
			Shader.superSecretSettingsIndex = Shader.getShaderValue((String) getConfig(ConfigType.NORMAL, "super_secret_settings_shader"));
			fixConfig();
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, "Failed to reset config!");
		}
		return configChanged;
	}
	public static boolean resetExperiments() {
		boolean configChanged = false;
		try {
			configChanged = setConfig(ConfigType.EXPERIMENTAL, "override_hand_renderer", false);
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, "Failed to reset experiments!");
		}
		return configChanged;
	}
	public static boolean setConfig(ConfigType CONFIG_TYPE, String ID, Object VALUE) {
		boolean configChanged = false;
		try {
			switch (CONFIG_TYPE) {
				case NORMAL -> {
					switch (ID) {
						case "zoom_level" -> {
							Config.zoomLevel = MathHelper.clamp((int) VALUE, 0, 100);
							configChanged = true;
						}
						case "zoom_increment_size" -> {
							Config.zoomIncrementSize = MathHelper.clamp((int) VALUE, 1, 10);
							configChanged = true;
						}
						case "zoom_transition" -> {
							Config.zoomTransition = (String) VALUE;
							configChanged = true;
						}
						case "zoom_scale_mode" -> {
							Config.zoomScaleMode = (String) VALUE;
							configChanged = true;
						}
						case "zoom_hide_hud" -> {
							Config.zoomHideHud = (boolean) VALUE;
							configChanged = true;
						}
						case "zoom_show_percentage" -> {
							Config.zoomShowPercentage = (boolean) VALUE;
							configChanged = true;
						}
						case "zoom_type" -> {
							Config.zoomType = (String) VALUE;
							configChanged = true;
						}
						case "hold_perspective_hide_hud" -> {
							Config.holdPerspectiveHideHud = (boolean) VALUE;
							configChanged = true;
						}
						case "super_secret_settings_shader" -> {
							Config.superSecretSettingsShader = (String) VALUE;
							configChanged = true;
						}
						case "super_secret_settings_mode" -> {
							Config.superSecretSettingsMode = (String) VALUE;
							configChanged = true;
						}
						case "super_secret_settings_enabled" -> {
							Config.superSecretSettingsEnabled = (boolean) VALUE;
							configChanged = true;
						}
						case "super_secret_settings_sound" -> {
							Config.superSecretSettingsSound = (boolean) VALUE;
							configChanged = true;
						}
						case "super_secret_settings_show_name" -> {
							Config.superSecretSettingsShowName = (boolean) VALUE;
							configChanged = true;
						}
						case "super_secret_settings_selection_blur" -> {
							Config.superSecretSettingsSelectionBlur = (boolean) VALUE;
							configChanged = true;
						}
						case "textured_named_entity" -> {
							Config.texturedNamedEntity = (boolean) VALUE;
							configChanged = true;
						}
						case "textured_random_entity" -> {
							Config.texturedRandomEntity = (boolean) VALUE;
							configChanged = true;
						}
						case "allow_april_fools" -> {
							Config.allowAprilFools = (boolean) VALUE;
							configChanged = true;
						}
						case "force_april_fools" -> {
							Config.forceAprilFools = (boolean) VALUE;
							configChanged = true;
						}
						case "position_overlay" -> {
							Config.positionOverlay = (boolean) VALUE;
							configChanged = true;
						}
						case "version_overlay" -> {
							Config.versionOverlay = (boolean) VALUE;
							configChanged = true;
						}
						case "force_pride" -> {
							Config.forcePride = (boolean) VALUE;
							configChanged = true;
						}
						case "force_pride_type" -> {
							Config.forcePrideType = (boolean) VALUE;
							configChanged = true;
						}
						case "force_pride_type_index" -> {
							Config.forcePrideTypeIndex = MathHelper.clamp((int) VALUE, 0, PerspectiveLogo.pride_types.length);
							configChanged = true;
						}
						case "show_death_coordinates" -> {
							Config.showDeathCoordinates = (boolean) VALUE;
							configChanged = true;
						}
						case "title_screen" -> {
							Config.titleScreen = (String) VALUE;
							configChanged = true;
						}
						case "ui_background" -> {
							Config.uiBackground = (String) VALUE;
							configChanged = true;
						}
						case "ui_background_texture" -> {
							Config.uiBackgroundTexture = VALUE instanceof Identifier ? IdentifierHelper.stringFromIdentifier((Identifier) VALUE) : (String) VALUE;
							configChanged = true;
						}
						case "hide_block_outline" -> {
							Config.hideBlockOutline = (boolean) VALUE;
							configChanged = true;
						}
						case "hide_crosshair" -> {
							Config.hideCrosshair = (String) VALUE;
							configChanged = true;
						}
						case "hide_armor" -> {
							Config.hideArmor = (boolean) VALUE;
							configChanged = true;
						}
						case "hide_nametags" -> {
							Config.hideNametags = (boolean) VALUE;
							configChanged = true;
						}
						case "hide_players" -> {
							Config.hidePlayers = (boolean) VALUE;
							configChanged = true;
						}
						case "hide_show_message" -> {
							Config.hideShowMessage = (boolean) VALUE;
							configChanged = true;
						}
						case "tutorials" -> {
							Config.tutorials = (boolean) VALUE;
							configChanged = true;
						}
						case "detect_update_channel" -> {
							Config.detectUpdateChannel = (String) VALUE;
							configChanged = true;
						}
						case "debug" -> {
							Config.debug = (boolean) VALUE;
							configChanged = true;
						}
						case "test_resource_pack" -> {
							Config.testResourcePack = (boolean) VALUE;
							configChanged = true;
						}
						case "config_version" -> {
							Config.configVersion = (int) VALUE;
							configChanged = true;
						}
						default -> {
							Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to set {} config value!: Invalid Key", ID));
							return false;
						}
					}
					if (configChanged) addSaveConfig(ConfigType.NORMAL);
				}
				case EXPERIMENTAL -> {
					switch (ID) {
						case "override_hand_renderer" -> {
							ExperimentalConfig.overrideHandRenderer = (boolean) VALUE;
							configChanged = true;
						}
						default -> {
							Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to set experimental {} config value!: Invalid Key", ID));
							return false;
						}
					}
					if (configChanged) addSaveConfig(ConfigType.EXPERIMENTAL);
				}
				case TUTORIAL -> {
					switch (ID) {
						case "super_secret_settings" -> {
							TutorialsConfig.SUPER_SECRET_SETTINGS = (Boolean) VALUE;
							configChanged = true;
						}
						default -> {
							Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to set tutorial {} config value!: Invalid Key", ID));
							return false;
						}
					}
					if (configChanged) addSaveConfig(ConfigType.TUTORIAL);
				}
				case WARNING -> {
					switch (ID) {
						case "photosensitivity" -> {
							WarningsConfig.PHOTOSENSITIVITY = (boolean) VALUE;
							configChanged = true;
						}
						case "prank" -> {
							WarningsConfig.PRANK = (boolean) VALUE;
							configChanged = true;
						}
						default -> {
							Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to set warning {} config value!: Invalid Key", ID));
							return false;
						}
					}
					if (configChanged) addSaveConfig(ConfigType.WARNING);
				}
				default -> {
					Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to set {} config value!: Invalid Config", ID));
					return false;
				}
			}
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to set {} config value!: {}", ID, error));
		}
		return configChanged;
	}
	public static Object getConfig(ConfigType CONFIG_TYPE, String ID) {
		switch (CONFIG_TYPE) {
			case NORMAL -> {
				switch (ID) {
					case "zoom_level" -> {
						return MathHelper.clamp(Config.zoomLevel, 0, 100);
					}
					case "zoom_increment_size" -> {
						return MathHelper.clamp(Config.zoomIncrementSize, 1, 10);
					}
					case "zoom_transition" -> {
						return Config.zoomTransition;
					}
					case "zoom_scale_mode" -> {
						return Config.zoomScaleMode;
					}
					case "zoom_hide_hud" -> {
						return Config.zoomHideHud;
					}
					case "zoom_show_percentage" -> {
						return Config.zoomShowPercentage;
					}
					case "zoom_type" -> {
						return Config.zoomType;
					}
					case "hold_perspective_hide_hud" -> {
						return Config.holdPerspectiveHideHud;
					}
					case "super_secret_settings_shader" -> {
						return Config.superSecretSettingsShader;
					}
					case "super_secret_settings_mode" -> {
						return Config.superSecretSettingsMode;
					}
					case "super_secret_settings_enabled" -> {
						return Config.superSecretSettingsEnabled;
					}
					case "super_secret_settings_sound" -> {
						return Config.superSecretSettingsSound;
					}
					case "super_secret_settings_show_name" -> {
						return Config.superSecretSettingsShowName;
					}
					case "super_secret_settings_selection_blur" -> {
						return Config.superSecretSettingsSelectionBlur;
					}
					case "textured_named_entity" -> {
						return Config.texturedNamedEntity;
					}
					case "textured_random_entity" -> {
						return Config.texturedRandomEntity;
					}
					case "allow_april_fools" -> {
						return Config.allowAprilFools;
					}
					case "force_april_fools" -> {
						return Config.forceAprilFools;
					}
					case "position_overlay" -> {
						return Config.positionOverlay;
					}
					case "version_overlay" -> {
						return Config.versionOverlay;
					}
					case "force_pride" -> {
						return Config.forcePride;
					}
					case "force_pride_type" -> {
						return Config.forcePrideType;
					}
					case "force_pride_type_index" -> {
						return MathHelper.clamp(Config.forcePrideTypeIndex, 0, PerspectiveLogo.pride_types.length);
					}
					case "show_death_coordinates" -> {
						return Config.showDeathCoordinates;
					}
					case "title_screen" -> {
						return Config.titleScreen;
					}
					case "ui_background" -> {
						return Config.uiBackground;
					}
					case "ui_background_texture" -> {
						return Config.uiBackgroundTexture;
					}
					case "hide_block_outline" -> {
						return Config.hideBlockOutline;
					}
					case "hide_crosshair" -> {
						return Config.hideCrosshair;
					}
					case "hide_armor" -> {
						return Config.hideArmor;
					}
					case "hide_nametags" -> {
						return Config.hideNametags;
					}
					case "hide_players" -> {
						return Config.hidePlayers;
					}
					case "hide_show_message" -> {
						return Config.hideShowMessage;
					}
					case "tutorials" -> {
						return Config.tutorials;
					}
					case "detect_update_channel" -> {
						return Config.detectUpdateChannel;
					}
					case "debug" -> {
						return Config.debug;
					}
					case "test_resource_pack" -> {
						return Config.testResourcePack;
					}
					case "config_version" -> {
						return Config.configVersion;
					}
					default -> {
						Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to get {} config value!: Invalid Key", ID));
						return new Object();
					}
				}
			}
			case EXPERIMENTAL -> {
				switch (ID) {
					case "override_hand_renderer" -> {
						return ExperimentalConfig.overrideHandRenderer;
					}
					default -> {
						Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to get experimental {} config value!: Invalid Key", ID));
						return new Object();
					}
				}
			}
			case TUTORIAL -> {
				switch (ID) {
					case "super_secret_settings" -> {return TutorialsConfig.SUPER_SECRET_SETTINGS;}
					default -> {
						Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to get tutorial {} config value!: Invalid Key", ID));
						return new Object();
					}
				}
			}
			case WARNING -> {
				switch (ID) {
					case "photosensitivity" -> {
						return WarningsConfig.PHOTOSENSITIVITY;
					}
					case "prank" -> {
						return WarningsConfig.PRANK;
					}
					default -> {
						Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to get warning {} config value!: Invalid Key", ID));
						return new Object();
					}
				}
			}
			default -> {
				Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to get {} config value!: Invalid Config", ID));
				return new Object();
			}
		}
	}
	public static List<Object> getDebugConfigText(ConfigType... types) {
		List<Object> text = new ArrayList<>();
		int typeAmount = 0;
		if (Arrays.stream(types).toList().contains(ConfigType.NORMAL)) {
			typeAmount += 1;
			text.add(Text.literal("Normal Config").formatted(Formatting.BOLD));
			for (Couple<String, ?> couple : Config.configProvider.getConfigList())
				text.add(Text.literal(couple.getFirst() + ": " + couple.getSecond()));
		}
		if (Arrays.stream(types).toList().contains(ConfigType.EXPERIMENTAL)) {
			if (EXPERIMENTS_AVAILABLE) {
				typeAmount += 1;
				if (typeAmount > 1) text.add("\n");
				text.add(Text.literal("Experimental Config").formatted(Formatting.BOLD));
				for (Couple<String, ?> couple : ExperimentalConfig.CONFIG_PROVIDER.getConfigList())
					text.add(Text.literal(couple.getFirst() + ": " + couple.getSecond()));
			}
		}
		if (Arrays.stream(types).toList().contains(ConfigType.TUTORIAL)) {
			typeAmount += 1;
			if (typeAmount > 1) text.add("\n");
			text.add(Text.literal("Tutorial Config").formatted(Formatting.BOLD));
			for (Couple<String, ?> couple : TutorialsConfig.CONFIG_PROVIDER.getConfigList())
				text.add(Text.literal(couple.getFirst() + ": " + couple.getSecond()));
		}
		if (Arrays.stream(types).toList().contains(ConfigType.WARNING)) {
			typeAmount += 1;
			if (typeAmount > 1) text.add("\n");
			text.add(Text.literal("Warning Config").formatted(Formatting.BOLD));
			for (Couple<String, ?> couple : WarningsConfig.CONFIG_PROVIDER.getConfigList())
				text.add(Text.literal(couple.getFirst() + ": " + couple.getSecond()));
		}
		return text;
	}
	public enum ConfigType {
		NORMAL,
		EXPERIMENTAL,
		TUTORIAL,
		WARNING
	}
}