/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.config;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.PerspectiveClient;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hide.Hide;
import com.mclegoman.perspective.client.hud.Overlays;
import com.mclegoman.perspective.client.logo.PrideLogoDataLoader;
import com.mclegoman.perspective.client.screen.config.ConfigScreen;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.shaders.Shaders;
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.ui.UIBackground;
import com.mclegoman.perspective.client.keybindings.Keybindings;
import com.mclegoman.perspective.client.util.Update;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.luminance.common.util.Couple;
import com.mclegoman.luminance.common.util.IdentifierHelper;
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
	public static final boolean experimentsAvailable = ExperimentalConfig.options.length > 0;
	public static boolean showReloadOverlay = false;
	public static int showReloadOverlayTicks = 20;
	protected static final int saveViaTickSaveTick = 20;
	protected static final int defaultConfigVersion = 22;
	protected static final boolean defaultDebug = false;
	protected static boolean saveViaTick = false;
	protected static int saveViaTickTicks = 0;
	private static boolean seenDevelopmentWarning = false;
	private static boolean showDowngradeWarning = false;
	private static boolean seenDowngradeWarning = false;
	private static boolean showLicenseUpdateNotice = false;
	private static boolean seenLicenseUpdateNotice = false;
	private static boolean updatedConfig = false;
	private static boolean savingConfig = false;
	private static final List<ConfigType> saveConfigs = new ArrayList<>();
	public static boolean isSaving() {
		return savingConfig;
	}
	public static void init() {
		try {
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ConfigDataLoader());
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to initialize config!: {}", error));
		}
	}
	protected static void loadConfig() {
		try {
			reloadConfig(false);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, "Failed to load configs!");
		}
		if (!ClientData.getFinishedInitializingAfterConfig()) PerspectiveClient.afterInitializeConfig();
	}
	public static void reloadConfig(boolean log) {
		if (log) {
			Data.version.sendToLog(LogType.INFO, Translation.getString("Reloading Config!"));
			showReloadOverlay = true;
			showReloadOverlayTicks = 20;
		}
		Config.init();
		ExperimentalConfig.init();
		TutorialsConfig.init();
		WarningsConfig.init();
		fixConfig();
	}
	public static void tick() {
		try {
			if (!updatedConfig) ConfigHelper.updateConfig();
			if (Keybindings.openConfig.wasPressed())
				ClientData.minecraft.setScreen(new ConfigScreen(ClientData.minecraft.currentScreen, false, true, 1));
			if (saveViaTickTicks < saveViaTickSaveTick) saveViaTickTicks += 1;
			else {
				if (saveViaTick) {
					for (ConfigType configType : saveConfigs) saveConfigs(configType);
					saveConfigs.clear();
					saveViaTick = false;
					savingConfig = false;
				}
				saveViaTickTicks = 0;
			}
			if (showReloadOverlay) {
				if (showReloadOverlayTicks < 1) {
					showReloadOverlay = false;
					showReloadOverlayTicks = 20;
				} else {
					showReloadOverlayTicks -= 1;
				}
			}
			showToasts();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, "Failed to tick config!");
		}
	}
	private static void showToasts() {
		if (Data.version.isDevelopmentBuild() && !seenDevelopmentWarning) {
			Data.version.sendToLog(LogType.INFO, "Development Build: Please help us improve by submitting bug reports if you encounter any issues.");
			ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.development_warning.title")}), Translation.getTranslation(Data.version.getID(), "toasts.development_warning.description"), 320, Toast.Type.WARNING));
			seenDevelopmentWarning = true;
		}
		if (showDowngradeWarning && !seenDowngradeWarning) {
			Data.version.sendToLog(LogType.INFO, "Downgrading is not supported: You may experience configuration related issues.");
			ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.downgrade_warning.title")}), Translation.getTranslation(Data.version.getID(), "toasts.downgrade_warning.description"), 320, Toast.Type.WARNING));
			seenDowngradeWarning = true;
		}
		if (showLicenseUpdateNotice && !seenLicenseUpdateNotice) {
			Data.version.sendToLog(LogType.INFO, "License Update: Perspective is now licensed under LGPL-3.0-or-later.");
			ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.license_update.title")}), Translation.getTranslation(Data.version.getID(), "toasts.license_update.description"), 320, Toast.Type.INFO));
			seenLicenseUpdateNotice = true;
		}
	}
	public static void updateConfig() {
		try {
			boolean shouldSaveConfig = false;
			if (Config.config.getOrDefault("config_version", defaultConfigVersion) != defaultConfigVersion) {
				if (Config.config.getOrDefault("config_version", defaultConfigVersion) < defaultConfigVersion) {
					Data.version.sendToLog(LogType.INFO, Translation.getString("Attempting to update config to the latest version."));
					if (Config.config.getOrDefault("config_version", defaultConfigVersion) < 3) {
						setConfig(ConfigType.normal, "zoom_level", 100 - (int) getConfig(ConfigType.normal, "zoom_level"));
					}
					if (Config.config.getOrDefault("config_version", defaultConfigVersion) < 7) {
						String SUPER_SECRET_SETTINGS_MODE = Config.config.getOrDefault("super_secret_settings_mode", ConfigDataLoader.superSecretSettingsMode);
						if (SUPER_SECRET_SETTINGS_MODE.equals("false")) setConfig(ConfigType.normal, "super_secret_settings_mode", "game");
						else if (SUPER_SECRET_SETTINGS_MODE.equals("true"))
							setConfig(ConfigType.normal, "super_secret_settings_mode", "screen");
						else setConfig(ConfigType.normal, "super_secret_settings_mode", "game");
						Boolean HIDE_HUD = Config.config.getOrDefault("hide_hud", true);
						setConfig(ConfigType.normal, "zoom_hide_hud", HIDE_HUD);
						setConfig(ConfigType.normal, "hold_perspective_back_hide_hud", HIDE_HUD);
						setConfig(ConfigType.normal, "hold_perspective_front_hide_hud", HIDE_HUD);
					}
					if (Config.config.getOrDefault("config_version", defaultConfigVersion) < 8) {
						showLicenseUpdateNotice = true;
					}
					if (Config.config.getOrDefault("config_version", defaultConfigVersion) < 11) {
						setConfig(ConfigType.normal, "zoom_transition", Config.config.getOrDefault("zoom_mode", ConfigDataLoader.zoomTransition));
						setConfig(ConfigType.normal, "zoom_show_percentage", Config.config.getOrDefault("zoom_overlay_message", ConfigDataLoader.zoomShowPercentage));
						setConfig(ConfigType.normal, "super_secret_settings_show_name", Config.config.getOrDefault("super_secret_settings_overlay_message", ConfigDataLoader.superSecretSettingsShowName));
					}
					if (Config.config.getOrDefault("config_version", defaultConfigVersion) < 14) {
						Shader.updateLegacyConfig = true;
						Shader.legacyIndex = Config.config.getOrDefault("super_secret_settings", 0);
					}
					if (Config.config.getOrDefault("config_version", defaultConfigVersion) < 16) {
						String cameraMode = Config.config.getOrDefault("zoom_camera_mode", ConfigDataLoader.zoomScaleMode);
						switch (cameraMode) {
							case "default" -> setConfig(ConfigType.normal, "zoom_scale_mode", "vanilla");
							case "spyglass" -> setConfig(ConfigType.normal, "zoom_scale_mode", "scaled");
						}
					}
					if (Config.config.getOrDefault("config_version", defaultConfigVersion) < 17) setConfig(ConfigType.normal, "zoom_type", Data.version.getID() + ":" + Config.config.getOrDefault("zoom_type", ConfigDataLoader.zoomType.replace((Data.version.getID() + ":"), "")));
					if (Config.config.getOrDefault("config_version", defaultConfigVersion) < 19) {
						boolean forcePrideType = Config.config.getOrDefault("force_pride_type", false);
						int prideIndex = Config.config.getOrDefault("force_pride_type_index", 0);
						setConfig(ConfigType.normal, "force_pride_type", forcePrideType ? (prideIndex == 0 ? "rainbow" : (prideIndex == 1 ? "bi" : "trans")) : "none");
					}
					if (Config.config.getOrDefault("config_version", defaultConfigVersion) < 20) {
						String crosshairType = Config.config.getOrDefault("hide_crosshair", ConfigDataLoader.crosshairType);
						if (crosshairType.equals("false")) crosshairType = "vanilla";
						else if (crosshairType.equals("true")) crosshairType = "hidden";
						setConfig(ConfigType.normal, "crosshair_type", crosshairType);
					}
					if (Config.config.getOrDefault("config_version", defaultConfigVersion) < 22) {
						setConfig(ConfigType.normal, "hold_perspective_back_hide_hud", Config.config.getOrDefault("hold_perspective_hide_hud", ConfigDataLoader.holdPerspectiveBackHideHud));
						setConfig(ConfigType.normal, "hold_perspective_front_hide_hud", Config.config.getOrDefault("hold_perspective_hide_hud", ConfigDataLoader.holdPerspectiveFrontHideHud));
					}
					setConfig(ConfigType.normal, "config_version", defaultConfigVersion);
					Data.version.sendToLog(LogType.INFO, Translation.getString("Successfully updated config to the latest version."));
				} else if (Config.config.getOrDefault("config_version", defaultConfigVersion) > defaultConfigVersion) {
					if (!seenDowngradeWarning) {
						Data.version.sendToLog(LogType.ERROR, Translation.getString("Downgrading is not supported. You may experience configuration related issues."));
						showDowngradeWarning = true;
					}
				}
				shouldSaveConfig = true;
			}
			if (fixConfig()) shouldSaveConfig = true;
			if (shouldSaveConfig) addSaveConfig();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, "Failed to update config!");
		}
		updatedConfig = true;
	}
	private static void saveConfigs(ConfigType config) {
		try {
			savingConfig = true;
			fixConfig();
			switch (config) {
				case normal -> Config.save();
				case experimental -> ExperimentalConfig.save();
				case tutorial -> TutorialsConfig.save();
				case warning -> WarningsConfig.save();
			}
			savingConfig = false;
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, "Failed to save config!");
		}
	}
	public static void saveConfig() {
		saveViaTick = true;
	}
	private static void addSaveConfig(ConfigType... configTypes) {
		try {
			if (configTypes.length != 0) {
				for (ConfigType configType : configTypes) {
					switch (configType) {
						case normal -> {if (!saveConfigs.contains(ConfigType.normal)) saveConfigs.add(ConfigType.normal);}
						case experimental -> {if (!saveConfigs.contains(ConfigType.experimental)) saveConfigs.add(ConfigType.experimental);}
						case tutorial -> {if (!saveConfigs.contains(ConfigType.tutorial)) saveConfigs.add(ConfigType.tutorial);}
						case warning -> {if (!saveConfigs.contains(ConfigType.normal)) saveConfigs.add(ConfigType.warning);}
					}
				}
			} else {
				if (!saveConfigs.contains(ConfigType.normal)) saveConfigs.add(ConfigType.normal);
				if (!saveConfigs.contains(ConfigType.experimental)) saveConfigs.add(ConfigType.experimental);
				if (!saveConfigs.contains(ConfigType.tutorial)) saveConfigs.add(ConfigType.tutorial);
				if (!saveConfigs.contains(ConfigType.warning)) saveConfigs.add(ConfigType.warning);
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, "Failed to add save config!");
		}
	}
	public static boolean fixConfig() {
		if (ClientData.isFinishedInitializing()) {
			ArrayList<Boolean> hasFixedConfig = new ArrayList<>();
			if ((int) getConfig(ConfigType.normal, "zoom_level") < 0 || (int) getConfig(ConfigType.normal, "zoom_level") > 100) {
				Data.version.sendToLog(LogType.WARN, "Config: zoom_level was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.normal, "zoom_level") + ")");
				hasFixedConfig.add(setConfig(ConfigType.normal, "zoom_level", ConfigDataLoader.zoomLevel));
			}
			if ((int) getConfig(ConfigType.normal, "zoom_increment_size") < 0 || (int) getConfig(ConfigType.normal, "zoom_increment_size") > 10) {
				Data.version.sendToLog(LogType.WARN, "Config: zoom_increment_size was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.normal, "zoom_increment_size") + ")");
				hasFixedConfig.add(setConfig(ConfigType.normal, "zoom_increment_size", ConfigDataLoader.zoomIncrementSize));
			}
			if ((double) getConfig(ConfigType.normal, "zoom_smooth_speed_in") < 0.001D || (double) getConfig(ConfigType.normal, "zoom_smooth_speed_in") > 2.0D) {
				Data.version.sendToLog(LogType.WARN, "Config: zoom_smooth_speed_in was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.normal, "zoom_smooth_speed_in") + ")");
				hasFixedConfig.add(setConfig(ConfigType.normal, "zoom_smooth_speed_in", ConfigDataLoader.zoomSmoothSpeedIn));
			}
			if ((double) getConfig(ConfigType.normal, "zoom_smooth_speed_out") < 0.001D || (double) getConfig(ConfigType.normal, "zoom_smooth_speed_out") > 2.0D) {
				Data.version.sendToLog(LogType.WARN, "Config: zoom_smooth_speed_out was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.normal, "zoom_smooth_speed_out") + ")");
				hasFixedConfig.add(setConfig(ConfigType.normal, "zoom_smooth_speed_out", ConfigDataLoader.zoomSmoothSpeedOut));
			}
			if (!Arrays.stream(Zoom.zoomTransitions).toList().contains((String) getConfig(ConfigType.normal, "zoom_transition"))) {
				Data.version.sendToLog(LogType.WARN, "Config: zoom_transition was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.normal, "zoom_transition") + ")");
				hasFixedConfig.add(setConfig(ConfigType.normal, "zoom_transition", ConfigDataLoader.zoomTransition));
			}
			if (!Arrays.stream(Zoom.zoomScaleModes).toList().contains((String) getConfig(ConfigType.normal, "zoom_scale_mode"))) {
				Data.version.sendToLog(LogType.WARN, "Config: zoom_scale_mode was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.normal, "zoom_scale_mode") + ")");
				hasFixedConfig.add(setConfig(ConfigType.normal, "zoom_scale_mode", ConfigDataLoader.zoomScaleMode));
			}
			if (!Zoom.isValidZoomType(IdentifierHelper.identifierFromString((String) ConfigHelper.getConfig(ConfigType.normal, "zoom_type")))) {
				Data.version.sendToLog(LogType.WARN, "Config: zoom_type was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.normal, "zoom_type") + ")");
				hasFixedConfig.add(setConfig(ConfigType.normal, "zoom_type", ConfigDataLoader.zoomType));
			}
			if ((double) getConfig(ConfigType.normal, "hold_perspective_back_multiplier") < 0.5D || (double) getConfig(ConfigType.normal, "hold_perspective_back_multiplier") > 4.0D) {
				Data.version.sendToLog(LogType.WARN, "Config: hold_perspective_back_multiplier was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.normal, "hold_perspective_back_multiplier") + ")");
				hasFixedConfig.add(setConfig(ConfigType.normal, "hold_perspective_back_multiplier", ConfigDataLoader.holdPerspectiveBackMultiplier));
			}
			if ((double) getConfig(ConfigType.normal, "hold_perspective_front_multiplier") < 0.5D || (double) getConfig(ConfigType.normal, "hold_perspective_front_multiplier") > 4.0D) {
				Data.version.sendToLog(LogType.WARN, "Config: hold_perspective_front_multiplier was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.normal, "hold_perspective_front_multiplier") + ")");
				hasFixedConfig.add(setConfig(ConfigType.normal, "hold_perspective_front_multiplier", ConfigDataLoader.holdPerspectiveBackMultiplier));
			}
			//if (!Shader.isShaderAvailable((String) ConfigHelper.getConfig(ConfigType.NORMAL, "super_secret_settings_shader"))) {
				//Data.version.sendToLog(LogType.WARN, "Config: super_secret_settings_shader was invalid and have been reset to prevent any unexpected issues. (" + ConfigHelper.getConfig(ConfigType.NORMAL, "super_secret_settings_shader") + ")");
				//Shader.superSecretSettingsIndex = (!Shader.isShaderAvailable(ConfigDataLoader.superSecretSettingsShader)) ? Math.min(Shader.superSecretSettingsIndex, ShaderDataLoader.registry.size() - 1) : Shader.getShaderValue(ConfigDataLoader.superSecretSettingsShader);
				//hasFixedConfig.add(setConfig(ConfigType.NORMAL, "super_secret_settings_shader", Shader.getFullShaderName(Shader.superSecretSettingsIndex)));
				// We also disable super secret settings as it's unlikely to be a shader they want anyway (e.g blur).
				//hasFixedConfig.add(setConfig(ConfigType.NORMAL, "super_secret_settings_enabled", false));
			//}
			if (!Arrays.stream(Shader.shaderModes).toList().contains((String) getConfig(ConfigType.normal, "super_secret_settings_mode"))) {
				Data.version.sendToLog(LogType.WARN, "Config: super_secret_settings_mode was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.normal, "super_secret_settings_mode") + ")");
				hasFixedConfig.add(setConfig(ConfigType.normal, "super_secret_settings_mode", ConfigDataLoader.superSecretSettingsMode));
			}
			if (!getConfig(ConfigType.normal, "force_pride_type").equals("random")) {
				if (!PrideLogoDataLoader.getLogoNames().contains((String) getConfig(ConfigType.normal, "force_pride_type"))) {
					Data.version.sendToLog(LogType.WARN, "Config: force_pride_type was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.normal, "force_pride_type") + ")");
					hasFixedConfig.add(setConfig(ConfigType.normal, "force_pride_type", ConfigDataLoader.forcePrideType));
				}
			}
			if (!Arrays.stream(Hide.hideCrosshairModes).toList().contains((String) getConfig(ConfigType.normal, "crosshair_type"))) {
				Data.version.sendToLog(LogType.WARN, "Config: crosshair_type was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.normal, "crosshair_type") + ")");
				hasFixedConfig.add(setConfig(ConfigType.normal, "crosshair_type", ConfigDataLoader.crosshairType));
			}
			if (!Arrays.stream(Update.detectUpdateChannels).toList().contains((String) getConfig(ConfigType.normal, "detect_update_channel"))) {
				Data.version.sendToLog(LogType.WARN, "Config: detect_update_channel was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.normal, "detect_update_channel") + ")");
				hasFixedConfig.add(setConfig(ConfigType.normal, "detect_update_channel", ConfigDataLoader.detectUpdateChannel));
			}
			if (!UIBackground.isRegisteredUIBackgroundType((String) getConfig(ConfigType.normal, "ui_background"))) {
				Data.version.sendToLog(LogType.WARN, "Config: ui_background was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.normal, "ui_background") + ")");
				hasFixedConfig.add(setConfig(ConfigType.normal, "ui_background", UIBackground.isRegisteredUIBackgroundType(ConfigDataLoader.uiBackground) ? ConfigDataLoader.uiBackground : "default"));
			}
			if ((int) getConfig(ConfigType.normal, "block_outline") < 0 || (int) getConfig(ConfigType.normal, "block_outline") > 100) {
				Data.version.sendToLog(LogType.WARN, "Config: block_outline was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.normal, "block_outline") + ")");
				hasFixedConfig.add(setConfig(ConfigType.normal, "block_outline", ConfigDataLoader.blockOutline));
			}
			if (!Overlays.isValidTimeOverlay((String) getConfig(ConfigType.normal, "time_overlay"))) {
				Data.version.sendToLog(LogType.WARN, "Config: time_overlay was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.normal, "time_overlay") + ")");
				hasFixedConfig.add(setConfig(ConfigType.normal, "time_overlay", ConfigDataLoader.timeOverlay));
			}
			return hasFixedConfig.contains(true);
		}
		return false;
	}
	public static boolean resetConfig() {
		List<Boolean> configChanged = new ArrayList<>();
		try {
			configChanged.add(setConfig(ConfigType.normal, "zoom_enabled", ConfigDataLoader.zoomEnabled));
			configChanged.add(setConfig(ConfigType.normal, "zoom_level", MathHelper.clamp(ConfigDataLoader.zoomLevel, 0, 100)));
			configChanged.add(setConfig(ConfigType.normal, "zoom_increment_size", MathHelper.clamp(ConfigDataLoader.zoomIncrementSize, 1, 10)));
			configChanged.add(setConfig(ConfigType.normal, "zoom_transition", ConfigDataLoader.zoomTransition));
			configChanged.add(setConfig(ConfigType.normal, "zoom_smooth_speed_in", MathHelper.clamp(ConfigDataLoader.zoomSmoothSpeedIn, 0.001D, 2.0D)));
			configChanged.add(setConfig(ConfigType.normal, "zoom_smooth_speed_out", MathHelper.clamp(ConfigDataLoader.zoomSmoothSpeedOut, 0.001D, 2.0D)));
			configChanged.add(setConfig(ConfigType.normal, "zoom_scale_mode", ConfigDataLoader.zoomScaleMode));
			configChanged.add(setConfig(ConfigType.normal, "zoom_hide_hud", ConfigDataLoader.zoomHideHud));
			configChanged.add(setConfig(ConfigType.normal, "zoom_show_percentage", ConfigDataLoader.zoomShowPercentage));
			configChanged.add(setConfig(ConfigType.normal, "zoom_type", ConfigDataLoader.zoomType));
			configChanged.add(setConfig(ConfigType.normal, "zoom_reset", ConfigDataLoader.zoomReset));
			configChanged.add(setConfig(ConfigType.normal, "zoom_cinematic", ConfigDataLoader.zoomCinematic));
			configChanged.add(setConfig(ConfigType.normal, "hold_perspective_back_multiplier", MathHelper.clamp(ConfigDataLoader.holdPerspectiveBackMultiplier, 0.5D, 4.0D)));
			configChanged.add(setConfig(ConfigType.normal, "hold_perspective_front_multiplier", MathHelper.clamp(ConfigDataLoader.holdPerspectiveFrontMultiplier, 0.5D, 4.0D)));
			configChanged.add(setConfig(ConfigType.normal, "hold_perspective_back_hide_hud", ConfigDataLoader.holdPerspectiveBackHideHud));
			configChanged.add(setConfig(ConfigType.normal, "hold_perspective_front_hide_hud", ConfigDataLoader.holdPerspectiveFrontHideHud));
			configChanged.add(setConfig(ConfigType.normal, "super_secret_settings_shader", ConfigDataLoader.superSecretSettingsShader));
			configChanged.add(setConfig(ConfigType.normal, "super_secret_settings_mode", ConfigDataLoader.superSecretSettingsMode));
			configChanged.add(setConfig(ConfigType.normal, "super_secret_settings_enabled", ConfigDataLoader.superSecretSettingsEnabled));
			configChanged.add(setConfig(ConfigType.normal, "super_secret_settings_sound", ConfigDataLoader.superSecretSettingsSound));
			configChanged.add(setConfig(ConfigType.normal, "super_secret_settings_show_name", ConfigDataLoader.superSecretSettingsShowName));
			configChanged.add(setConfig(ConfigType.normal, "super_secret_settings_selection_blur", ConfigDataLoader.superSecretSettingsSelectionBlur));
			Shaders.setSuperSecretSettings();
			configChanged.add(setConfig(ConfigType.normal, "textured_named_entity", ConfigDataLoader.texturedNamedEntity));
			configChanged.add(setConfig(ConfigType.normal, "textured_random_entity", ConfigDataLoader.texturedRandomEntity));
			configChanged.add(setConfig(ConfigType.normal, "allow_april_fools", ConfigDataLoader.allowAprilFools));
			configChanged.add(setConfig(ConfigType.normal, "force_april_fools", ConfigDataLoader.forceAprilFools));
			configChanged.add(setConfig(ConfigType.normal, "allow_halloween", ConfigDataLoader.allowHalloween));
			configChanged.add(setConfig(ConfigType.normal, "force_halloween", ConfigDataLoader.forceHalloween));
			configChanged.add(setConfig(ConfigType.normal, "version_overlay", ConfigDataLoader.versionOverlay));
			configChanged.add(setConfig(ConfigType.normal, "position_overlay", ConfigDataLoader.positionOverlay));
			configChanged.add(setConfig(ConfigType.normal, "time_overlay", ConfigDataLoader.timeOverlay));
			configChanged.add(setConfig(ConfigType.normal, "day_overlay", ConfigDataLoader.dayOverlay));
			configChanged.add(setConfig(ConfigType.normal, "biome_overlay", ConfigDataLoader.biomeOverlay));
			configChanged.add(setConfig(ConfigType.normal, "force_pride", ConfigDataLoader.forcePride));
			configChanged.add(setConfig(ConfigType.normal, "force_pride_type", ConfigDataLoader.forcePrideType));
			configChanged.add(setConfig(ConfigType.normal, "show_death_coordinates", ConfigDataLoader.showDeathCoordinates));
			configChanged.add(setConfig(ConfigType.normal, "ui_background", ConfigDataLoader.uiBackground));
			UIBackground.loadShader(UIBackground.getCurrentUIBackground());
			configChanged.add(setConfig(ConfigType.normal, "ui_background_texture", ConfigDataLoader.uiBackgroundTexture));
			configChanged.add(setConfig(ConfigType.normal, "hide_block_outline", ConfigDataLoader.hideBlockOutline));
			configChanged.add(setConfig(ConfigType.normal, "block_outline", MathHelper.clamp(ConfigDataLoader.blockOutline, 0, 100)));
			configChanged.add(setConfig(ConfigType.normal, "rainbow_block_outline", ConfigDataLoader.rainbowBlockOutline));
			configChanged.add(setConfig(ConfigType.normal, "crosshair_type", ConfigDataLoader.crosshairType));
			configChanged.add(setConfig(ConfigType.normal, "hide_armor", ConfigDataLoader.hideArmor));
			configChanged.add(setConfig(ConfigType.normal, "hide_nametags", ConfigDataLoader.hideNametags));
			configChanged.add(setConfig(ConfigType.normal, "hide_players", ConfigDataLoader.hidePlayers));
			configChanged.add(setConfig(ConfigType.normal, "hide_show_message", ConfigDataLoader.hideShowMessage));
			configChanged.add(setConfig(ConfigType.normal, "tutorials", ConfigDataLoader.tutorials));
			configChanged.add(setConfig(ConfigType.normal, "detect_update_channel", ConfigDataLoader.detectUpdateChannel));
			configChanged.add(setConfig(ConfigType.normal, "ripple_density", ConfigDataLoader.rippleDensity));
			fixConfig();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, "Failed to reset config!");
		}
		return configChanged.contains(true);
	}
	public static boolean resetExperiments() {
		ArrayList<Boolean> configChanged = new ArrayList<>();
		try {
			configChanged.add(setConfig(ConfigType.experimental, "ambience", false));
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, "Failed to reset experiments!");
		}
		return configChanged.contains(true);
	}
	public static boolean setConfig(ConfigType type, String key, Object value) {
		boolean configChanged = false;
		try {
			switch (type) {
				case normal -> {
					switch (key) {
						case "zoom_enabled" -> {
							Config.zoomEnabled = (boolean) value;
							configChanged = true;
						}
						case "zoom_level" -> {
							Config.zoomLevel = MathHelper.clamp((int) value, 0, 100);
							configChanged = true;
						}
						case "zoom_increment_size" -> {
							Config.zoomIncrementSize = MathHelper.clamp((int) value, 1, 10);
							configChanged = true;
						}
						case "zoom_transition" -> {
							Config.zoomTransition = (String) value;
							configChanged = true;
						}
						case "zoom_smooth_speed_in" -> {
							Config.zoomSmoothSpeedIn = MathHelper.clamp((double) value, 0.001D, 2.0D);
							configChanged = true;
						}
						case "zoom_smooth_speed_out" -> {
							Config.zoomSmoothSpeedOut = (double) value;
							configChanged = true;
						}
						case "zoom_scale_mode" -> {
							Config.zoomScaleMode = (String) value;
							configChanged = true;
						}
						case "zoom_hide_hud" -> {
							Config.zoomHideHud = (boolean) value;
							configChanged = true;
						}
						case "zoom_show_percentage" -> {
							Config.zoomShowPercentage = (boolean) value;
							configChanged = true;
						}
						case "zoom_type" -> {
							Config.zoomType = (String) value;
							configChanged = true;
						}
						case "zoom_reset" -> {
							Config.zoomReset = (boolean) value;
							configChanged = true;
						}
						case "zoom_cinematic" -> {
							Config.zoomCinematic = (boolean) value;
							configChanged = true;
						}
						case "hold_perspective_back_multiplier" -> {
							Config.holdPerspectiveBackMultiplier = MathHelper.clamp((double) value, 0.5D, 4.0D);
							configChanged = true;
						}
						case "hold_perspective_front_multiplier" -> {
							Config.holdPerspectiveFrontMultiplier = MathHelper.clamp((double) value, 0.5D, 4.0D);
							configChanged = true;
						}
						case "hold_perspective_back_hide_hud" -> {
							Config.holdPerspectiveBackHideHud = (boolean) value;
							configChanged = true;
						}
						case "hold_perspective_front_hide_hud" -> {
							Config.holdPerspectiveFrontHideHud = (boolean) value;
							configChanged = true;
						}
						case "super_secret_settings_shader" -> {
							Config.superSecretSettingsShader = (String) value;
							configChanged = true;
						}
						case "super_secret_settings_mode" -> {
							Config.superSecretSettingsMode = (String) value;
							configChanged = true;
						}
						case "super_secret_settings_enabled" -> {
							Config.superSecretSettingsEnabled = (boolean) value;
							configChanged = true;
						}
						case "super_secret_settings_sound" -> {
							Config.superSecretSettingsSound = (boolean) value;
							configChanged = true;
						}
						case "super_secret_settings_show_name" -> {
							Config.superSecretSettingsShowName = (boolean) value;
							configChanged = true;
						}
						case "super_secret_settings_selection_blur" -> {
							Config.superSecretSettingsSelectionBlur = (boolean) value;
							configChanged = true;
						}
						case "textured_named_entity" -> {
							Config.texturedNamedEntity = (boolean) value;
							configChanged = true;
						}
						case "textured_random_entity" -> {
							Config.texturedRandomEntity = (boolean) value;
							configChanged = true;
						}
						case "allow_april_fools" -> {
							Config.allowAprilFools = (boolean) value;
							configChanged = true;
						}
						case "force_april_fools" -> {
							Config.forceAprilFools = (boolean) value;
							configChanged = true;
						}
						case "allow_halloween" -> {
							Config.allowHalloween = (boolean) value;
							configChanged = true;
						}
						case "force_halloween" -> {
							Config.forceHalloween = (boolean) value;
							configChanged = true;
						}
						case "version_overlay" -> {
							Config.versionOverlay = (boolean) value;
							configChanged = true;
						}
						case "position_overlay" -> {
							Config.positionOverlay = (boolean) value;
							configChanged = true;
						}
						case "time_overlay" -> {
							Config.timeOverlay = (String) value;
							configChanged = true;
						}
						case "day_overlay" -> {
							Config.dayOverlay = (boolean) value;
							configChanged = true;
						}
						case "biome_overlay" -> {
							Config.biomeOverlay = (boolean) value;
							configChanged = true;
						}
						case "force_pride" -> {
							Config.forcePride = (boolean) value;
							configChanged = true;
						}
						case "force_pride_type" -> {
							Config.forcePrideType = (String) value;
							configChanged = true;
						}
						case "show_death_coordinates" -> {
							Config.showDeathCoordinates = (boolean) value;
							configChanged = true;
						}
						case "ui_background" -> {
							Config.uiBackground = (String) value;
							configChanged = true;
						}
						case "ui_background_texture" -> {
							Config.uiBackgroundTexture = value instanceof Identifier ? IdentifierHelper.stringFromIdentifier((Identifier) value) : (String) value;
							configChanged = true;
						}
						case "hide_block_outline" -> {
							Config.hideBlockOutline = (boolean) value;
							configChanged = true;
						}
						case "block_outline" -> {
							Config.blockOutline = MathHelper.clamp((int) value, 0, 100);
							configChanged = true;
						}
						case "rainbow_block_outline" -> {
							Config.rainbowBlockOutline = (boolean)value;
							configChanged = true;
						}
						case "crosshair_type" -> {
							Config.crosshairType = (String) value;
							configChanged = true;
						}
						case "hide_armor" -> {
							Config.hideArmor = (boolean) value;
							configChanged = true;
						}
						case "hide_nametags" -> {
							Config.hideNametags = (boolean) value;
							configChanged = true;
						}
						case "hide_players" -> {
							Config.hidePlayers = (boolean) value;
							configChanged = true;
						}
						case "hide_show_message" -> {
							Config.hideShowMessage = (boolean) value;
							configChanged = true;
						}
						case "tutorials" -> {
							Config.tutorials = (boolean) value;
							configChanged = true;
						}
						case "detect_update_channel" -> {
							Config.detectUpdateChannel = (String) value;
							configChanged = true;
						}
						case "ripple_density" -> {
							Config.rippleDensity = (int) value;
							configChanged = true;
						}
						case "debug" -> {
							Config.debug = (boolean) value;
							configChanged = true;
						}
						case "config_version" -> {
							Config.configVersion = (int) value;
							configChanged = true;
						}
						default -> {
							Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to set {} config value!: Invalid Key", key));
						}
					}
					if (configChanged) addSaveConfig(ConfigType.normal);
				}
				case experimental -> {
					switch (key) {
						case "ambience" -> {
							ExperimentalConfig.ambience = (Boolean) value;
							configChanged = true;
						}
						default -> {
							Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to set experimental {} config value!: Invalid Key", key));
						}
					}
					if (configChanged) addSaveConfig(ConfigType.experimental);
				}
				case tutorial -> {
					switch (key) {
						case "super_secret_settings" -> {
							TutorialsConfig.superSecretSettings = (Boolean) value;
							configChanged = true;
						}
						default -> {
							Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to set tutorial {} config value!: Invalid Key", key));
						}
					}
					if (configChanged) addSaveConfig(ConfigType.tutorial);
				}
				case warning -> {
					switch (key) {
						case "photosensitivity" -> {
							WarningsConfig.photosensitivity = (boolean) value;
							configChanged = true;
						}
						case "prank" -> {
							WarningsConfig.prank = (boolean) value;
							configChanged = true;
						}
						case "halloween" -> {
							WarningsConfig.halloween = (boolean) value;
							configChanged = true;
						}
						default -> {
							Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to set warning {} config value!: Invalid Key", key));
						}
					}
					if (configChanged) addSaveConfig(ConfigType.warning);
				}
				default -> {
					Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to set {} config value!: Invalid Config", key));
					return false;
				}
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to set {} config value!: {}", key, error));
		}
		return configChanged;
	}
	public static Object getConfig(ConfigType type, String key) {
		switch (type) {
			case normal -> {
				switch (key) {
					case "zoom_enabled" -> {
						return Config.zoomEnabled;
					}
					case "zoom_level" -> {
						return MathHelper.clamp(Config.zoomLevel, 0, 100);
					}
					case "zoom_increment_size" -> {
						return MathHelper.clamp(Config.zoomIncrementSize, 1, 10);
					}
					case "zoom_transition" -> {
						return Config.zoomTransition;
					}
					case "zoom_smooth_speed_in" -> {
						return MathHelper.clamp(Config.zoomSmoothSpeedIn, 0.001D, 2.0D);
					}
					case "zoom_smooth_speed_out" -> {
						return MathHelper.clamp(Config.zoomSmoothSpeedOut, 0.001D, 2.0D);
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
					case "zoom_reset" -> {
						return Config.zoomReset;
					}
					case "zoom_cinematic" -> {
						return Config.zoomCinematic;
					}
					case "hold_perspective_back_multiplier" -> {
						return MathHelper.clamp(Config.holdPerspectiveBackMultiplier, 0.5D, 4.0D);
					}
					case "hold_perspective_front_multiplier" -> {
						return MathHelper.clamp(Config.holdPerspectiveFrontMultiplier, 0.5D, 4.0D);
					}
					case "hold_perspective_back_hide_hud" -> {
						return Config.holdPerspectiveBackHideHud;
					}
					case "hold_perspective_front_hide_hud" -> {
						return Config.holdPerspectiveFrontHideHud;
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
					case "allow_halloween" -> {
						return Config.allowHalloween;
					}
					case "force_halloween" -> {
						return Config.forceHalloween;
					}
					case "version_overlay" -> {
						return Config.versionOverlay;
					}
					case "position_overlay" -> {
						return Config.positionOverlay;
					}
					case "time_overlay" -> {
						return Config.timeOverlay;
					}
					case "day_overlay" -> {
						return Config.dayOverlay;
					}
					case "biome_overlay" -> {
						return Config.biomeOverlay;
					}
					case "force_pride" -> {
						return Config.forcePride;
					}
					case "force_pride_type" -> {
						return Config.forcePrideType;
					}
					case "show_death_coordinates" -> {
						return Config.showDeathCoordinates;
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
					case "block_outline" -> {
						return MathHelper.clamp(Config.blockOutline, 0, 100);
					}
					case "rainbow_block_outline" -> {
						return Config.rainbowBlockOutline;
					}
					case "crosshair_type" -> {
						String crosshairType = Config.crosshairType;
						if (crosshairType.equals("true")) return "hidden";
						else if (crosshairType.equals("false")) return "vanilla";
						return crosshairType;
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
					case "ripple_density" -> {
						return Config.rippleDensity;
					}
					case "debug" -> {
						return Config.debug;
					}
					case "config_version" -> {
						return Config.configVersion;
					}
					default -> {
						Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to get {} config value!: Invalid Key", key));
						return new Object();
					}
				}
			}
			case experimental -> {
				switch (key) {
					case "ambience" -> {
						return ExperimentalConfig.ambience;
					}
					default -> {
						Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to get experimental {} config value!: Invalid Key", key));
						return new Object();
					}
				}
			}
			case tutorial -> {
				switch (key) {
					case "super_secret_settings" -> {return TutorialsConfig.superSecretSettings;}
					default -> {
						Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to get tutorial {} config value!: Invalid Key", key));
						return new Object();
					}
				}
			}
			case warning -> {
				switch (key) {
					case "photosensitivity" -> {
						return WarningsConfig.photosensitivity;
					}
					case "prank" -> {
						return WarningsConfig.prank;
					}
					case "halloween" -> {
						return WarningsConfig.halloween;
					}
					default -> {
						Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to get warning {} config value!: Invalid Key", key));
						return new Object();
					}
				}
			}
			default -> {
				Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to get {} config value!: Invalid Config", key));
				return new Object();
			}
		}
	}
	public static List<Text> getDebugConfigText(ConfigType... types) {
		List<Text> text = new ArrayList<>();
		int typeAmount = 0;
		if (Arrays.stream(types).toList().contains(ConfigType.normal)) {
			typeAmount += 1;
			text.add(Translation.getTranslation(Data.version.getID(), "debug.config.normal", new Formatting[]{Formatting.BOLD}));
			for (Couple<String, ?> couple : Config.configProvider.getConfigList())
				text.add(Text.literal(couple.getFirst() + ": " + couple.getSecond()));
		}
		if (Arrays.stream(types).toList().contains(ConfigType.experimental)) {
			if (experimentsAvailable) {
				typeAmount += 1;
				if (typeAmount > 1) text.add(Text.empty());
				text.add(Translation.getTranslation(Data.version.getID(), "debug.config.experimental", new Formatting[]{Formatting.BOLD}));
				for (Couple<String, ?> couple : ExperimentalConfig.configProvider.getConfigList())
					text.add(Text.literal(couple.getFirst() + ": " + couple.getSecond()));
			}
		}
		if (Arrays.stream(types).toList().contains(ConfigType.tutorial)) {
			typeAmount += 1;
			if (typeAmount > 1) text.add(Text.empty());
			text.add(Translation.getTranslation(Data.version.getID(), "debug.config.tutorial", new Formatting[]{Formatting.BOLD}));
			for (Couple<String, ?> couple : TutorialsConfig.configProvider.getConfigList())
				text.add(Text.literal(couple.getFirst() + ": " + couple.getSecond()));
		}
		if (Arrays.stream(types).toList().contains(ConfigType.warning)) {
			typeAmount += 1;
			if (typeAmount > 1) text.add(Text.empty());
			text.add(Translation.getTranslation(Data.version.getID(), "debug.config.warning", new Formatting[]{Formatting.BOLD}));
			for (Couple<String, ?> couple : WarningsConfig.configProvider.getConfigList())
				text.add(Text.literal(couple.getFirst() + ": " + couple.getSecond()));
		}
		return text;
	}
	public enum ConfigType {
		normal,
		experimental,
		tutorial,
		warning
	}
}