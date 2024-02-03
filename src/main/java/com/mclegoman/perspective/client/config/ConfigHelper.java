/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.client.PerspectiveClient;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hide.Hide;
import com.mclegoman.perspective.client.screen.config.ConfigScreen;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.shaders.ShaderDataLoader;
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.client.util.PerspectiveLogo;
import com.mclegoman.perspective.client.util.UpdateChecker;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.common.util.Couple;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
		PerspectiveClient.onInitializeClientAfterConfig();
	}
	public static void tick() {
		try {
			if (!UPDATED_CONFIG) ConfigHelper.updateConfig();
			if (Keybindings.OPEN_CONFIG.wasPressed())
				ClientData.CLIENT.setScreen(new ConfigScreen(ClientData.CLIENT.currentScreen, false, 1, false));
			if (SAVE_VIA_TICK_TICKS < SAVE_VIA_TICK_SAVE_TICK) SAVE_VIA_TICK_TICKS += 1;
			else {
				fixConfig(false);
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
			if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) != DEFAULT_CONFIG_VERSION) {
				if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < DEFAULT_CONFIG_VERSION) {
					Data.VERSION.getLogger().info("{} Attempting to update config to the latest version.", Data.VERSION.getLoggerPrefix());
					if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 3) {
						setConfig(ConfigType.NORMAL, "zoom_level", 100 - (int) getConfig(ConfigType.NORMAL, "zoom_level"));
					}
					if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 7) {
						String SUPER_SECRET_SETTINGS_MODE = Config.CONFIG.getOrDefault("super_secret_settings_mode", ConfigDataLoader.SUPER_SECRET_SETTINGS_MODE);
						if (SUPER_SECRET_SETTINGS_MODE.equals("false")) setConfig(ConfigType.NORMAL, "super_secret_settings_mode", "game");
						else if (SUPER_SECRET_SETTINGS_MODE.equals("true"))
							setConfig(ConfigType.NORMAL, "super_secret_settings_mode", "screen");
						else setConfig(ConfigType.NORMAL, "super_secret_settings_mode", "game");
						Boolean HIDE_HUD = Config.CONFIG.getOrDefault("hide_hud", true);
						setConfig(ConfigType.NORMAL, "zoom_hide_hud", HIDE_HUD);
						setConfig(ConfigType.NORMAL, "hold_perspective_hide_hud", HIDE_HUD);
					}
					if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 8) {
						SHOW_LICENSE_UPDATE_NOTICE = true;
					}
					if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 11) {
						setConfig(ConfigType.NORMAL, "zoom_transition", Config.CONFIG.getOrDefault("zoom_mode", ConfigDataLoader.ZOOM_TRANSITION));
						setConfig(ConfigType.NORMAL, "zoom_show_percentage", Config.CONFIG.getOrDefault("zoom_overlay_message", ConfigDataLoader.ZOOM_SHOW_PERCENTAGE));
						setConfig(ConfigType.NORMAL, "super_secret_settings_show_name", Config.CONFIG.getOrDefault("super_secret_settings_overlay_message", ConfigDataLoader.SUPER_SECRET_SETTINGS_SHOW_NAME));
					}
					if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 14) {
						Shader.updateLegacyConfig = true;
						Shader.legacyIndex = Config.CONFIG.getOrDefault("super_secret_settings", 0);
					}
					if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 16) {
						String cameraMode = Config.CONFIG.getOrDefault("zoom_camera_mode", ConfigDataLoader.ZOOM_SCALE_MODE);
						switch (cameraMode) {
							case "default" -> setConfig(ConfigType.NORMAL, "zoom_scale_mode", "vanilla");
							case "spyglass" -> setConfig(ConfigType.NORMAL, "zoom_scale_mode", "scaled");
						}
					}
					if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 17) {
						setConfig(ConfigType.NORMAL, "zoom_type", Data.VERSION.getID() + ":" + Config.CONFIG.getOrDefault("zoom_type", ConfigDataLoader.ZOOM_TYPE.replace((Data.VERSION.getID() + ":"), "")));
					}
					setConfig(ConfigType.NORMAL, "config_version", DEFAULT_CONFIG_VERSION);
					Data.VERSION.getLogger().info("{} Successfully updated config to the latest version.", Data.VERSION.getLoggerPrefix());
				} else if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) > DEFAULT_CONFIG_VERSION) {
					if (!SEEN_DOWNGRADE_WARNING) {
						Data.VERSION.getLogger().warn("{} Downgrading is not supported. You may experience configuration related issues.", Data.VERSION.getLoggerPrefix());
						SHOW_DOWNGRADE_WARNING = true;
					}
				}
				saveConfig();
			}
			fixConfig(true);
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, "Failed to update config!");
		}
		UPDATED_CONFIG = true;
	}
	private static void saveConfig(ConfigType config) {
		try {
			SAVING_CONFIG = true;
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
	public static boolean fixConfig(boolean saveConfig) {
		if (ClientData.isFinishedInitializing()) {
			if ((int) getConfig(ConfigType.NORMAL, "zoom_level") < 0 || (int) getConfig(ConfigType.NORMAL, "zoom_level") > 100) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: zoom_level was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "zoom_level") + ")");
				setConfig(ConfigType.NORMAL, "zoom_level", ConfigDataLoader.ZOOM_LEVEL);
			}
			if ((int) getConfig(ConfigType.NORMAL, "zoom_increment_size") < 0 || (int) getConfig(ConfigType.NORMAL, "zoom_increment_size") > 10) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: zoom_increment_size was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "zoom_increment_size") + ")");
				setConfig(ConfigType.NORMAL, "zoom_increment_size", ConfigDataLoader.ZOOM_INCREMENT_SIZE);
			}
			if (!Arrays.stream(Zoom.zoomTransitions).toList().contains((String) getConfig(ConfigType.NORMAL, "zoom_transition"))) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: zoom_transition was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "zoom_transition") + ")");
				setConfig(ConfigType.NORMAL, "zoom_transition", ConfigDataLoader.ZOOM_TRANSITION);
			}
			if (!Arrays.stream(Zoom.zoomScaleModes).toList().contains((String) getConfig(ConfigType.NORMAL, "zoom_scale_mode"))) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: zoom_scale_mode was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "zoom_scale_mode") + ")");
				setConfig(ConfigType.NORMAL, "zoom_scale_mode", ConfigDataLoader.ZOOM_SCALE_MODE);
			}
			if (!Zoom.zoomTypes.contains(Zoom.getZoomType())) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: zoom_type was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "zoom_type") + ")");
				setConfig(ConfigType.NORMAL, "zoom_type", ConfigDataLoader.ZOOM_TYPE);
			}
			if (!Shader.isShaderAvailable((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_shader"))) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: super_secret_settings_shader was invalid and have been reset to prevent any unexpected issues. (" + ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_shader") + ")");
				if (!Shader.isShaderAvailable(ConfigDataLoader.SUPER_SECRET_SETTINGS_SHADER)) {
					Shader.superSecretSettingsIndex = Math.min(Shader.superSecretSettingsIndex, ShaderDataLoader.REGISTRY.size() - 1);
					ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_shader", Shader.getFullShaderName(Shader.superSecretSettingsIndex));
				} else Shader.superSecretSettingsIndex = Shader.getShaderValue(ConfigDataLoader.SUPER_SECRET_SETTINGS_SHADER);
			}
			if (!Arrays.stream(Shader.shaderModes).toList().contains((String) getConfig(ConfigType.NORMAL, "super_secret_settings_mode"))) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: super_secret_settings_mode was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "super_secret_settings_mode") + ")");
				setConfig(ConfigType.NORMAL, "super_secret_settings_mode", ConfigDataLoader.SUPER_SECRET_SETTINGS_MODE);
			}
			if ((int) getConfig(ConfigType.NORMAL, "force_pride_type_index") < 0 || (int) getConfig(ConfigType.NORMAL, "force_pride_type_index") > PerspectiveLogo.pride_types.length) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: force_pride_type_index was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "force_pride_type_index") + ")");
				setConfig(ConfigType.NORMAL, "force_pride_type_index", ConfigDataLoader.FORCE_PRIDE_TYPE_INDEX);
			}
			if (!Arrays.stream(Hide.hideCrosshairModes).toList().contains((String) getConfig(ConfigType.NORMAL, "hide_crosshair"))) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: hide_crosshair was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "hide_crosshair") + ")");
				setConfig(ConfigType.NORMAL, "hide_crosshair", ConfigDataLoader.HIDE_CROSSHAIR);
			}
			if (!Arrays.stream(UpdateChecker.detectUpdateChannels).toList().contains((String) getConfig(ConfigType.NORMAL, "detect_update_channel"))) {
				Data.VERSION.sendToLog(Helper.LogType.WARN, "Config: detect_update_channel was invalid and have been reset to prevent any unexpected issues. (" + getConfig(ConfigType.NORMAL, "detect_update_channel") + ")");
				setConfig(ConfigType.NORMAL, "detect_update_channel", ConfigDataLoader.DETECT_UPDATE_CHANNEL);
			}
			if (saveConfig) {
				saveConfig();
				return true;
			}
		}
		return false;
	}
	public static boolean resetConfig() {
		boolean configChanged = false;
		try {
			configChanged = setConfig(ConfigType.NORMAL, "zoom_level", MathHelper.clamp(ConfigDataLoader.ZOOM_LEVEL, 0, 100));
			configChanged = setConfig(ConfigType.NORMAL, "zoom_increment_size", MathHelper.clamp(ConfigDataLoader.ZOOM_INCREMENT_SIZE, 1, 10));
			configChanged = setConfig(ConfigType.NORMAL, "zoom_transition", ConfigDataLoader.ZOOM_TRANSITION);
			configChanged = setConfig(ConfigType.NORMAL, "zoom_scale_mode", ConfigDataLoader.ZOOM_SCALE_MODE);
			configChanged = setConfig(ConfigType.NORMAL, "zoom_hide_hud", ConfigDataLoader.ZOOM_HIDE_HUD);
			configChanged = setConfig(ConfigType.NORMAL, "zoom_show_percentage", ConfigDataLoader.ZOOM_SHOW_PERCENTAGE);
			configChanged = setConfig(ConfigType.NORMAL, "zoom_type", ConfigDataLoader.ZOOM_TYPE);
			configChanged = setConfig(ConfigType.NORMAL, "hold_perspective_hide_hud", ConfigDataLoader.HOLD_PERSPECTIVE_HIDE_HUD);
			configChanged = setConfig(ConfigType.NORMAL, "super_secret_settings_shader", ConfigDataLoader.SUPER_SECRET_SETTINGS_SHADER);
			if (Shader.isShaderAvailable((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_shader"))) Shader.superSecretSettingsIndex = Shader.getShaderValue((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_shader"));
			else Shader.superSecretSettingsIndex = Math.min(Shader.superSecretSettingsIndex, ShaderDataLoader.REGISTRY.size() - 1);
			configChanged = setConfig(ConfigType.NORMAL, "super_secret_settings_mode", ConfigDataLoader.SUPER_SECRET_SETTINGS_MODE);
			configChanged = setConfig(ConfigType.NORMAL, "super_secret_settings_enabled", ConfigDataLoader.SUPER_SECRET_SETTINGS_ENABLED);
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_enabled")) Shader.set(true, false, false, false);
			configChanged = setConfig(ConfigType.NORMAL, "super_secret_settings_sound", ConfigDataLoader.SUPER_SECRET_SETTINGS_SOUND);
			configChanged = setConfig(ConfigType.NORMAL, "super_secret_settings_options_screen", ConfigDataLoader.SUPER_SECRET_SETTINGS_OPTIONS_SCREEN);
			configChanged = setConfig(ConfigType.NORMAL, "super_secret_settings_show_name", ConfigDataLoader.SUPER_SECRET_SETTINGS_SHOW_NAME);
			configChanged = setConfig(ConfigType.NORMAL, "textured_named_entity", ConfigDataLoader.TEXTURED_NAMED_ENTITY);
			configChanged = setConfig(ConfigType.NORMAL, "textured_random_entity", ConfigDataLoader.TEXTURED_RANDOM_ENTITY);
			configChanged = setConfig(ConfigType.NORMAL, "allow_april_fools", ConfigDataLoader.ALLOW_APRIL_FOOLS);
			configChanged = setConfig(ConfigType.NORMAL, "force_april_fools", ConfigDataLoader.FORCE_APRIL_FOOLS);
			configChanged = setConfig(ConfigType.NORMAL, "position_overlay", ConfigDataLoader.POSITION_OVERLAY);
			configChanged = setConfig(ConfigType.NORMAL, "version_overlay", ConfigDataLoader.VERSION_OVERLAY);
			configChanged = setConfig(ConfigType.NORMAL, "force_pride", ConfigDataLoader.FORCE_PRIDE);
			configChanged = setConfig(ConfigType.NORMAL, "force_pride_type", ConfigDataLoader.FORCE_PRIDE_TYPE);
			configChanged = setConfig(ConfigType.NORMAL, "force_pride_type_index", MathHelper.clamp(ConfigDataLoader.FORCE_PRIDE_TYPE_INDEX, 0, PerspectiveLogo.pride_types.length));
			configChanged = setConfig(ConfigType.NORMAL, "show_death_coordinates", ConfigDataLoader.SHOW_DEATH_COORDINATES);
			configChanged = setConfig(ConfigType.NORMAL, "dirt_title_screen", ConfigDataLoader.DIRT_TITLE_SCREEN);
			configChanged = setConfig(ConfigType.NORMAL, "hide_block_outline", ConfigDataLoader.HIDE_BLOCK_OUTLINE);
			configChanged = setConfig(ConfigType.NORMAL, "hide_crosshair", ConfigDataLoader.HIDE_CROSSHAIR);
			configChanged = setConfig(ConfigType.NORMAL, "hide_armor", ConfigDataLoader.HIDE_ARMOR);
			configChanged = setConfig(ConfigType.NORMAL, "hide_nametags", ConfigDataLoader.HIDE_NAMETAGS);
			configChanged = setConfig(ConfigType.NORMAL, "hide_players", ConfigDataLoader.HIDE_PLAYERS);
			configChanged = setConfig(ConfigType.NORMAL, "hide_show_message", ConfigDataLoader.HIDE_SHOW_MESSAGE);
			configChanged = setConfig(ConfigType.NORMAL, "tutorials", ConfigDataLoader.TUTORIALS);
			configChanged = setConfig(ConfigType.NORMAL, "detect_update_channel", ConfigDataLoader.DETECT_UPDATE_CHANNEL);
			configChanged = setConfig(ConfigType.NORMAL, "debug", ConfigDataLoader.DEBUG);
			configChanged = setConfig(ConfigType.NORMAL, "test_resource_pack", ConfigDataLoader.TEST_RESOURCE_PACK);
			Shader.superSecretSettingsIndex = Shader.getShaderValue((String) getConfig(ConfigType.NORMAL, "super_secret_settings_shader"));
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.WARN, "Failed to reset config!");
		}
		return configChanged;
	}
	public static boolean resetExperiments() {
		boolean configChanged = false;
		try {
			configChanged = setConfig(ConfigType.EXPERIMENTAL, "displaynames", false);
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
							Config.ZOOM_LEVEL = MathHelper.clamp((int) VALUE, 0, 100);
							configChanged = true;
						}
						case "zoom_increment_size" -> {
							Config.ZOOM_INCREMENT_SIZE = MathHelper.clamp((int) VALUE, 1, 10);
							configChanged = true;
						}
						case "zoom_transition" -> {
							Config.ZOOM_TRANSITION = (String) VALUE;
							configChanged = true;
						}
						case "zoom_scale_mode" -> {
							Config.ZOOM_SCALE_MODE = (String) VALUE;
							configChanged = true;
						}
						case "zoom_hide_hud" -> {
							Config.ZOOM_HIDE_HUD = (boolean) VALUE;
							configChanged = true;
						}
						case "zoom_show_percentage" -> {
							Config.ZOOM_SHOW_PERCENTAGE = (boolean) VALUE;
							configChanged = true;
						}
						case "zoom_type" -> {
							Config.ZOOM_TYPE = (String) VALUE;
							configChanged = true;
						}
						case "hold_perspective_hide_hud" -> {
							Config.HOLD_PERSPECTIVE_HIDE_HUD = (boolean) VALUE;
							configChanged = true;
						}
						case "super_secret_settings_shader" -> {
							Config.SUPER_SECRET_SETTINGS_SHADER = (String) VALUE;
							configChanged = true;
						}
						case "super_secret_settings_mode" -> {
							Config.SUPER_SECRET_SETTINGS_MODE = (String) VALUE;
							configChanged = true;
						}
						case "super_secret_settings_enabled" -> {
							Config.SUPER_SECRET_SETTINGS_ENABLED = (boolean) VALUE;
							configChanged = true;
						}
						case "super_secret_settings_sound" -> {
							Config.SUPER_SECRET_SETTINGS_SOUND = (boolean) VALUE;
							configChanged = true;
						}
						case "super_secret_settings_options_screen" -> {
							Config.SUPER_SECRET_SETTINGS_OPTIONS_SCREEN = (boolean) VALUE;
							configChanged = true;
						}
						case "super_secret_settings_show_name" -> {
							Config.SUPER_SECRET_SETTINGS_SHOW_NAME = (boolean) VALUE;
							configChanged = true;
						}
						case "textured_named_entity" -> {
							Config.TEXTURED_NAMED_ENTITY = (boolean) VALUE;
							configChanged = true;
						}
						case "textured_random_entity" -> {
							Config.TEXTURED_RANDOM_ENTITY = (boolean) VALUE;
							configChanged = true;
						}
						case "allow_april_fools" -> {
							Config.ALLOW_APRIL_FOOLS = (boolean) VALUE;
							configChanged = true;
						}
						case "force_april_fools" -> {
							Config.FORCE_APRIL_FOOLS = (boolean) VALUE;
							configChanged = true;
						}
						case "position_overlay" -> {
							Config.POSITION_OVERLAY = (boolean) VALUE;
							configChanged = true;
						}
						case "version_overlay" -> {
							Config.VERSION_OVERLAY = (boolean) VALUE;
							configChanged = true;
						}
						case "force_pride" -> {
							Config.FORCE_PRIDE = (boolean) VALUE;
							configChanged = true;
						}
						case "force_pride_type" -> {
							Config.FORCE_PRIDE_TYPE = (boolean) VALUE;
							configChanged = true;
						}
						case "force_pride_type_index" -> {
							Config.FORCE_PRIDE_TYPE_INDEX = MathHelper.clamp((int) VALUE, 0, PerspectiveLogo.pride_types.length);
							configChanged = true;
						}
						case "show_death_coordinates" -> {
							Config.SHOW_DEATH_COORDINATES = (boolean) VALUE;
							configChanged = true;
						}
						case "dirt_title_screen" -> {
							Config.DIRT_TITLE_SCREEN = (boolean) VALUE;
							configChanged = true;
						}
						case "hide_block_outline" -> {
							Config.HIDE_BLOCK_OUTLINE = (boolean) VALUE;
							configChanged = true;
						}
						case "hide_crosshair" -> {
							Config.HIDE_CROSSHAIR = (String) VALUE;
							configChanged = true;
						}
						case "hide_armor" -> {
							Config.HIDE_ARMOR = (boolean) VALUE;
							configChanged = true;
						}
						case "hide_nametags" -> {
							Config.HIDE_NAMETAGS = (boolean) VALUE;
							configChanged = true;
						}
						case "hide_players" -> {
							Config.HIDE_PLAYERS = (boolean) VALUE;
							configChanged = true;
						}
						case "hide_show_message" -> {
							Config.HIDE_SHOW_MESSAGE = (boolean) VALUE;
							configChanged = true;
						}
						case "tutorials" -> {
							Config.TUTORIALS = (boolean) VALUE;
							configChanged = true;
						}
						case "detect_update_channel" -> {
							Config.DETECT_UPDATE_CHANNEL = (String) VALUE;
							configChanged = true;
						}
						case "debug" -> {
							Config.DEBUG = (boolean) VALUE;
							configChanged = true;
						}
						case "test_resource_pack" -> {
							Config.TEST_RESOURCE_PACK = (boolean) VALUE;
							configChanged = true;
						}
						case "config_version" -> {
							Config.CONFIG_VERSION = (int) VALUE;
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
						case "displaynames" -> {
							ExperimentalConfig.DISPLAYNAMES = (Boolean) VALUE;
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
						return MathHelper.clamp(Config.ZOOM_LEVEL, 0, 100);
					}
					case "zoom_increment_size" -> {
						return MathHelper.clamp(Config.ZOOM_INCREMENT_SIZE, 1, 10);
					}
					case "zoom_transition" -> {
						return Config.ZOOM_TRANSITION;
					}
					case "zoom_scale_mode" -> {
						return Config.ZOOM_SCALE_MODE;
					}
					case "zoom_hide_hud" -> {
						return Config.ZOOM_HIDE_HUD;
					}
					case "zoom_show_percentage" -> {
						return Config.ZOOM_SHOW_PERCENTAGE;
					}
					case "zoom_type" -> {
						return Config.ZOOM_TYPE;
					}
					case "hold_perspective_hide_hud" -> {
						return Config.HOLD_PERSPECTIVE_HIDE_HUD;
					}
					case "super_secret_settings_shader" -> {
						return Config.SUPER_SECRET_SETTINGS_SHADER;
					}
					case "super_secret_settings_mode" -> {
						return Config.SUPER_SECRET_SETTINGS_MODE;
					}
					case "super_secret_settings_enabled" -> {
						return Config.SUPER_SECRET_SETTINGS_ENABLED;
					}
					case "super_secret_settings_sound" -> {
						return Config.SUPER_SECRET_SETTINGS_SOUND;
					}
					case "super_secret_settings_options_screen" -> {
						return Config.SUPER_SECRET_SETTINGS_OPTIONS_SCREEN;
					}
					case "super_secret_settings_show_name" -> {
						return Config.SUPER_SECRET_SETTINGS_SHOW_NAME;
					}
					case "textured_named_entity" -> {
						return Config.TEXTURED_NAMED_ENTITY;
					}
					case "textured_random_entity" -> {
						return Config.TEXTURED_RANDOM_ENTITY;
					}
					case "allow_april_fools" -> {
						return Config.ALLOW_APRIL_FOOLS;
					}
					case "force_april_fools" -> {
						return Config.FORCE_APRIL_FOOLS;
					}
					case "position_overlay" -> {
						return Config.POSITION_OVERLAY;
					}
					case "version_overlay" -> {
						return Config.VERSION_OVERLAY;
					}
					case "force_pride" -> {
						return Config.FORCE_PRIDE;
					}
					case "force_pride_type" -> {
						return Config.FORCE_PRIDE_TYPE;
					}
					case "force_pride_type_index" -> {
						return MathHelper.clamp(Config.FORCE_PRIDE_TYPE_INDEX, 0, PerspectiveLogo.pride_types.length);
					}
					case "show_death_coordinates" -> {
						return Config.SHOW_DEATH_COORDINATES;
					}
					case "dirt_title_screen" -> {
						return Config.DIRT_TITLE_SCREEN;
					}
					case "hide_block_outline" -> {
						return Config.HIDE_BLOCK_OUTLINE;
					}
					case "hide_crosshair" -> {
						return Config.HIDE_CROSSHAIR;
					}
					case "hide_armor" -> {
						return Config.HIDE_ARMOR;
					}
					case "hide_nametags" -> {
						return Config.HIDE_NAMETAGS;
					}
					case "hide_players" -> {
						return Config.HIDE_PLAYERS;
					}
					case "hide_show_message" -> {
						return Config.HIDE_SHOW_MESSAGE;
					}
					case "tutorials" -> {
						return Config.TUTORIALS;
					}
					case "detect_update_channel" -> {
						return Config.DETECT_UPDATE_CHANNEL;
					}
					case "debug" -> {
						return Config.DEBUG;
					}
					case "test_resource_pack" -> {
						return Config.TEST_RESOURCE_PACK;
					}
					case "config_version" -> {
						return Config.CONFIG_VERSION;
					}
					default -> {
						Data.VERSION.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to get {} config value!: Invalid Key", ID));
						return new Object();
					}
				}
			}
			case EXPERIMENTAL -> {
				switch (ID) {
					case "displaynames" -> {return ExperimentalConfig.DISPLAYNAMES;}
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
	public static List<Object> getDebugConfigText() {
		List<Object> text = new ArrayList<>();
		text.add(Text.literal(Config.ID).formatted(Formatting.BOLD));
		for (Couple<String, ?> couple : Config.CONFIG_PROVIDER.getConfigList())
			text.add(Text.literal(couple.getFirst() + ": " + couple.getSecond()));
		if (EXPERIMENTS_AVAILABLE) {
			text.add("\n");
			text.add(Text.literal(ExperimentalConfig.ID).formatted(Formatting.BOLD));
			for (Couple<String, ?> couple : ExperimentalConfig.CONFIG_PROVIDER.getConfigList())
				text.add(Text.literal(couple.getFirst() + ": " + couple.getSecond()));
		}
		text.add("\n");
		text.add(Text.literal(TutorialsConfig.ID).formatted(Formatting.BOLD));
		for (Couple<String, ?> couple : TutorialsConfig.CONFIG_PROVIDER.getConfigList())
			text.add(Text.literal(couple.getFirst() + ": " + couple.getSecond()));
		text.add("\n");
		text.add(Text.literal(WarningsConfig.ID).formatted(Formatting.BOLD));
		for (Couple<String, ?> couple : WarningsConfig.CONFIG_PROVIDER.getConfigList())
			text.add(Text.literal(couple.getFirst() + ": " + couple.getSecond()));
		return text;
	}
	public enum ConfigType {
		NORMAL,
		EXPERIMENTAL,
		TUTORIAL,
		WARNING
	}
}