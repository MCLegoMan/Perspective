/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.config;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.ConfigScreen;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class ConfigHelper {
	protected static final int SAVE_VIA_TICK_SAVE_TICK = 20;
	protected static final int DEFAULT_CONFIG_VERSION = 14;
	public static final boolean EXPERIMENTS_AVAILABLE = true;
	protected static boolean SAVE_VIA_TICK = false;
	protected static int SAVE_VIA_TICK_TICKS = 0;
	private static boolean SEEN_DEVELOPMENT_WARNING = false;
	private static boolean SHOW_DOWNGRADE_WARNING = false;
	private static boolean SEEN_DOWNGRADE_WARNING = false;
	private static boolean SHOW_LICENSE_UPDATE_NOTICE = false;
	private static boolean SEEN_LICENSE_UPDATE_NOTICE = false;
	private static boolean SAVING = false;
	public static boolean isSaving() {
		return SAVING;
	}
	public static void init() {
		try {
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ConfigDataLoader());
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to initialize config: {}", error));
		}
	}
	protected static void loadConfig() {
		try {
			Config.init();
			ExperimentalConfig.init();
			TutorialsConfig.init();
			WarningsConfig.init();
			ConfigHelper.updateConfig();
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to load configs: {}", error));
		}
	}
	public static void tick() {
		try {
			if (Keybindings.OPEN_CONFIG.wasPressed())
				ClientData.minecraft.setScreen(new ConfigScreen(ClientData.minecraft.currentScreen, false, 1));
			if (SAVE_VIA_TICK_TICKS < SAVE_VIA_TICK_SAVE_TICK) SAVE_VIA_TICK_TICKS += 1;
			else {
				if (SAVE_VIA_TICK) {
					saveConfig(false);
					SAVE_VIA_TICK = false;
					SAVING = false;
				}
				SAVE_VIA_TICK_TICKS = 0;
			}
			showToasts();
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to tick configs: {}", error));
		}
	}
	private static void showToasts() {
		if (Data.version.isDevelopmentBuild() && !SEEN_DEVELOPMENT_WARNING) {
			Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Development Build. Please help us improve by submitting bug reports if you encounter any issues."));
			//ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.development_warning.title")}), Translation.getTranslation(Data.version.getID(), "toasts.development_warning.description"), 320, Toast.Type.WARNING));
			SEEN_DEVELOPMENT_WARNING = true;
		}
		if (SHOW_DOWNGRADE_WARNING && !SEEN_DOWNGRADE_WARNING) {
			Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Downgrading is not supported. You may experience configuration related issues."));
			//ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.downgrade_warning.title")}), Translation.getTranslation(Data.version.getID(), "toasts.downgrade_warning.description"), 320, Toast.Type.WARNING));
			SEEN_DOWNGRADE_WARNING = true;
		}
		if (SHOW_LICENSE_UPDATE_NOTICE && !SEEN_LICENSE_UPDATE_NOTICE) {
			Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("License Update. {} is now licensed under LGPL-3.0-or-later.", Data.version.getName()));
			//ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.license_update.title")}), Translation.getTranslation(Data.version.getID(), "toasts.license_update.description"), 320, Toast.Type.INFO));
			SEEN_LICENSE_UPDATE_NOTICE = true;
		}
	}
	public static void updateConfig() {
		try {
			if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) != DEFAULT_CONFIG_VERSION) {
				if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < DEFAULT_CONFIG_VERSION) {
					Data.version.sendToLog(Helper.LogType.INFO, Translation.getString("Attempting to update config to the latest version."));
					if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 3) {
						setConfig("zoom_level", 100 - (int) getConfig("zoom_level"));
					}
					if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 7) {
						String SUPER_SECRET_SETTINGS_MODE = Config.CONFIG.getOrDefault("super_secret_settings_mode", ConfigDataLoader.SUPER_SECRET_SETTINGS_MODE);
						if (SUPER_SECRET_SETTINGS_MODE.equals("false")) setConfig("super_secret_settings_mode", "game");
						else if (SUPER_SECRET_SETTINGS_MODE.equals("true"))
							setConfig("super_secret_settings_mode", "screen");
						else setConfig("super_secret_settings_mode", "game");
						Boolean HIDE_HUD = Config.CONFIG.getOrDefault("hide_hud", true);
						setConfig("zoom_hide_hud", HIDE_HUD);
						setConfig("hold_perspective_hide_hud", HIDE_HUD);
					}
					if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 8) {
						SHOW_LICENSE_UPDATE_NOTICE = true;
					}
					if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 11) {
						setConfig("zoom_transition", Config.CONFIG.getOrDefault("zoom_mode", ConfigDataLoader.ZOOM_TRANSITION));
						setConfig("zoom_show_percentage", Config.CONFIG.getOrDefault("zoom_overlay_message", ConfigDataLoader.ZOOM_SHOW_PERCENTAGE));
						setConfig("super_secret_settings_show_name", Config.CONFIG.getOrDefault("super_secret_settings_overlay_message", ConfigDataLoader.SUPER_SECRET_SETTINGS_SHOW_NAME));
					}
					if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) < 14) {
						Shader.updateLegacyConfig = true;
						Shader.legacyIndex = Config.CONFIG.getOrDefault("super_secret_settings", 0);
					}
					setConfig("config_version", DEFAULT_CONFIG_VERSION);
					Data.version.sendToLog(Helper.LogType.INFO, Translation.getString("Successfully updated config to the latest version."));
				} else if (Config.CONFIG.getOrDefault("config_version", DEFAULT_CONFIG_VERSION) > DEFAULT_CONFIG_VERSION) {
					if (!SEEN_DOWNGRADE_WARNING) {
						Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Downgrading is not supported. You may experience configuration related issues."));
						SHOW_DOWNGRADE_WARNING = true;
					}
				}
				saveConfig(false);
			}
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to update config: {}", error));
		}
	}
	public static void saveConfig(boolean onTick) {
		try {
			if (onTick) {
				SAVING = true;
				SAVE_VIA_TICK = true;
			} else {
				SAVING = true;
				Config.save();
				ExperimentalConfig.save();
				TutorialsConfig.save();
				WarningsConfig.save();
				SAVING = false;
			}
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to save config: {}", error));
		}
	}
	public static void resetConfig() {
		try {
			// Main Config
			setConfig("zoom_level", MathHelper.clamp(ConfigDataLoader.ZOOM_LEVEL, 0, 100));
			setConfig("zoom_increment_size", MathHelper.clamp(ConfigDataLoader.ZOOM_INCREMENT_SIZE, 1, 10));
			setConfig("zoom_transition", ConfigDataLoader.ZOOM_TRANSITION);
			setConfig("zoom_camera_mode", ConfigDataLoader.ZOOM_CAMERA_MODE);
			setConfig("zoom_hide_hud", ConfigDataLoader.ZOOM_HIDE_HUD);
			setConfig("zoom_show_percentage", ConfigDataLoader.ZOOM_SHOW_PERCENTAGE);
			setConfig("hold_perspective_hide_hud", ConfigDataLoader.HOLD_PERSPECTIVE_HIDE_HUD);
			setConfig("super_secret_settings_shader", ConfigDataLoader.SUPER_SECRET_SETTINGS_SHADER);
			setConfig("super_secret_settings_mode", ConfigDataLoader.SUPER_SECRET_SETTINGS_MODE);
			setConfig("super_secret_settings_enabled", ConfigDataLoader.SUPER_SECRET_SETTINGS_ENABLED);
			setConfig("super_secret_settings_sound", ConfigDataLoader.SUPER_SECRET_SETTINGS_SOUND);
			setConfig("super_secret_settings_options_screen", ConfigDataLoader.SUPER_SECRET_SETTINGS_OPTIONS_SCREEN);
			setConfig("super_secret_settings_show_name", ConfigDataLoader.SUPER_SECRET_SETTINGS_SHOW_NAME);
			setConfig("textured_named_entity", ConfigDataLoader.TEXTURED_NAMED_ENTITY);
			setConfig("textured_random_entity", ConfigDataLoader.TEXTURED_RANDOM_ENTITY);
			setConfig("allow_april_fools", ConfigDataLoader.ALLOW_APRIL_FOOLS);
			setConfig("force_april_fools", ConfigDataLoader.FORCE_APRIL_FOOLS);
			setConfig("position_overlay", ConfigDataLoader.POSITION_OVERLAY);
			setConfig("version_overlay", ConfigDataLoader.VERSION_OVERLAY);
			setConfig("force_pride", ConfigDataLoader.FORCE_PRIDE);
			setConfig("force_pride_type", ConfigDataLoader.FORCE_PRIDE_TYPE);
			setConfig("force_pride_type_index", MathHelper.clamp(ConfigDataLoader.FORCE_PRIDE_TYPE_INDEX, 0, ClientData.PRIDE_LOGOS.length));
			setConfig("show_death_coordinates", ConfigDataLoader.SHOW_DEATH_COORDINATES);
			setConfig("dirt_title_screen", ConfigDataLoader.DIRT_TITLE_SCREEN);
			setConfig("hide_block_outline", ConfigDataLoader.HIDE_BLOCK_OUTLINE);
			setConfig("hide_crosshair", ConfigDataLoader.HIDE_CROSSHAIR);
			setConfig("hide_armor", ConfigDataLoader.HIDE_ARMOR);
			setConfig("hide_nametags", ConfigDataLoader.HIDE_NAMETAGS);
			setConfig("hide_players", ConfigDataLoader.HIDE_PLAYERS);
			setConfig("hide_show_message", ConfigDataLoader.HIDE_SHOW_MESSAGE);
			setConfig("tutorials", ConfigDataLoader.TUTORIALS);
			setConfig("detect_update_channel", ConfigDataLoader.DETECT_UPDATE_CHANNEL);
			setConfig("debug", ConfigDataLoader.DEBUG);
			// Experimental Config
			if (EXPERIMENTS_AVAILABLE) {
				setExperimentalConfig("super_secret_settings_list", false);
			}
			Shader.superSecretSettingsIndex = Shader.getShaderValue((String) getConfig("super_secret_settings_shader"));
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to reset config: {}", error));
		}
	}
	public static void setConfig(String ID, Object VALUE) {
		try {
			switch (ID) {
				case "zoom_level" -> Config.ZOOM_LEVEL = MathHelper.clamp((int)VALUE, 0, 100);
				case "zoom_increment_size" -> Config.ZOOM_INCREMENT_SIZE = MathHelper.clamp((int)VALUE, 1, 10);
				case "zoom_transition" -> Config.ZOOM_TRANSITION = (String) VALUE;
				case "zoom_camera_mode" -> Config.ZOOM_CAMERA_MODE = (String) VALUE;
				case "zoom_hide_hud" -> Config.ZOOM_HIDE_HUD = (boolean) VALUE;
				case "zoom_show_percentage" -> Config.ZOOM_SHOW_PERCENTAGE = (boolean) VALUE;
				case "hold_perspective_hide_hud" -> Config.HOLD_PERSPECTIVE_HIDE_HUD = (boolean) VALUE;
				case "super_secret_settings_shader" ->
						Config.SUPER_SECRET_SETTINGS_SHADER = (String) VALUE;
				case "super_secret_settings_mode" -> Config.SUPER_SECRET_SETTINGS_MODE = (String) VALUE;
				case "super_secret_settings_enabled" -> Config.SUPER_SECRET_SETTINGS_ENABLED = (boolean) VALUE;
				case "super_secret_settings_sound" -> Config.SUPER_SECRET_SETTINGS_SOUND = (boolean) VALUE;
				case "super_secret_settings_options_screen" ->
						Config.SUPER_SECRET_SETTINGS_OPTIONS_SCREEN = (boolean) VALUE;
				case "super_secret_settings_show_name" -> Config.SUPER_SECRET_SETTINGS_SHOW_NAME = (boolean) VALUE;
				case "textured_named_entity" -> Config.TEXTURED_NAMED_ENTITY = (boolean) VALUE;
				case "textured_random_entity" -> Config.TEXTURED_RANDOM_ENTITY = (boolean) VALUE;
				case "allow_april_fools" -> Config.ALLOW_APRIL_FOOLS = (boolean) VALUE;
				case "force_april_fools" -> Config.FORCE_APRIL_FOOLS = (boolean) VALUE;
				case "position_overlay" -> Config.POSITION_OVERLAY = (boolean) VALUE;
				case "version_overlay" -> Config.VERSION_OVERLAY = (boolean) VALUE;
				case "force_pride" -> Config.FORCE_PRIDE = (boolean) VALUE;
				case "force_pride_type" -> Config.FORCE_PRIDE_TYPE = (boolean) VALUE;
				case "force_pride_type_index" ->
						Config.FORCE_PRIDE_TYPE_INDEX = MathHelper.clamp((int)VALUE, 0, ClientData.PRIDE_LOGOS.length);
				case "show_death_coordinates" -> Config.SHOW_DEATH_COORDINATES = (boolean) VALUE;
				case "dirt_title_screen" -> Config.DIRT_TITLE_SCREEN = (boolean) VALUE;
				case "hide_block_outline" -> Config.HIDE_BLOCK_OUTLINE = (boolean) VALUE;
				case "hide_crosshair" -> Config.HIDE_CROSSHAIR = (String) VALUE;
				case "hide_armor" -> Config.HIDE_ARMOR = (boolean) VALUE;
				case "hide_nametags" -> Config.HIDE_NAMETAGS = (boolean) VALUE;
				case "hide_players" -> Config.HIDE_PLAYERS = (boolean) VALUE;
				case "hide_show_message" -> Config.HIDE_SHOW_MESSAGE = (boolean) VALUE;
				case "tutorials" -> Config.TUTORIALS = (boolean) VALUE;
				case "detect_update_channel" -> Config.DETECT_UPDATE_CHANNEL = (String) VALUE;
				case "debug" -> Config.DEBUG = (boolean) VALUE;
				case "config_version" -> Config.CONFIG_VERSION = (int) VALUE;
				default ->
						Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to set {} config value: Invalid Key", ID));
			}
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to set {} config value: {}", ID, error));
		}
	}
	public static void setExperimentalConfig(String ID, Object VALUE) {
		try {
			switch (ID) {
				case "super_secret_settings_list" -> ExperimentalConfig.SUPER_SECRET_SETTINGS_LIST = (boolean) VALUE;
				case "super_secret_settings_notice" ->
						ExperimentalConfig.SUPER_SECRET_SETTINGS_NOTICE = (boolean) VALUE;
				default ->
						Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to set {} experimental config value: Invalid Key", ID));
			}
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to set {} experimental config value: {}", ID, error));
		}
	}
	public static void setTutorialConfig(String ID, Object VALUE) {
		try {
			if (ID.equals("super_secret_settings")) {
				TutorialsConfig.SUPER_SECRET_SETTINGS = (Boolean) VALUE;
			} else {
				Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to set {} tutorial config value: Invalid Key", ID));
			}
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to set {} tutorial config value: {}", ID, error));
		}
	}
	public static void setWarningConfig(String ID, Object VALUE) {
		try {
			switch (ID) {
				case "photosensitivity" -> WarningsConfig.PHOTOSENSITIVITY = (boolean) VALUE;
				case "prank" -> WarningsConfig.PRANK = (boolean) VALUE;
				default ->
						Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to set {} warning config value: Invalid Key", ID));
			}
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to set {} warning config value: {}", ID, error));
		}
	}
	public static Object getConfig(String ID) {
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
			case "zoom_camera_mode" -> {
				return Config.ZOOM_CAMERA_MODE;
			}
			case "zoom_hide_hud" -> {
				return Config.ZOOM_HIDE_HUD;
			}
			case "zoom_show_percentage" -> {
				return Config.ZOOM_SHOW_PERCENTAGE;
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
				return MathHelper.clamp(Config.FORCE_PRIDE_TYPE_INDEX, 0, ClientData.PRIDE_LOGOS.length);
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
			case "config_version" -> {
				return Config.CONFIG_VERSION;
			}
			default -> {
				Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to get {} config value: Invalid Key", ID));
				return new Object();
			}
		}
	}
	public static Object getExperimentalConfig(String ID) {
		switch (ID) {
			case "super_secret_settings_list" -> {
				return ExperimentalConfig.SUPER_SECRET_SETTINGS_LIST;
			}
			case "super_secret_settings_notice" -> {
				return ExperimentalConfig.SUPER_SECRET_SETTINGS_NOTICE;
			}
			default -> {
				Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to get {} experimental value: Invalid Key", ID));
				return new Object();
			}
		}
	}
	public static Object getTutorialConfig(String ID) {
		if (ID.equals("super_secret_settings")) {
			return TutorialsConfig.SUPER_SECRET_SETTINGS;
		} else {
			Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to get {} tutorial config value: Invalid Key", ID));
		}
		return new Object();
	}
	public static Object getWarningConfig(String ID) {
		switch (ID) {
			case "photosensitivity" -> {
				return WarningsConfig.PHOTOSENSITIVITY;
			}
			case "prank" -> {
				return WarningsConfig.PRANK;
			}
			default -> {
				Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to get {} warning config value: Invalid Key", ID));
				return new Object();
			}
		}
	}
	public static List<Object> getDebugConfigText() {
		List<Object> text = new ArrayList<>();
		text.add(Text.literal(Config.ID).formatted(Formatting.BOLD));
		for (Pair<String, ?> pair : Config.CONFIG_PROVIDER.getConfigList())
			text.add(Text.literal(pair.getFirst() + ": " + pair.getSecond()));
		if (EXPERIMENTS_AVAILABLE) {
			text.add("\n");
			text.add(Text.literal(ExperimentalConfig.ID).formatted(Formatting.BOLD));
			for (Pair<String, ?> pair : ExperimentalConfig.CONFIG_PROVIDER.getConfigList())
				text.add(Text.literal(pair.getFirst() + ": " + pair.getSecond()));
		}
		text.add("\n");
		text.add(Text.literal(TutorialsConfig.ID).formatted(Formatting.BOLD));
		for (Pair<String, ?> pair : TutorialsConfig.CONFIG_PROVIDER.getConfigList())
			text.add(Text.literal(pair.getFirst() + ": " + pair.getSecond()));
		text.add("\n");
		text.add(Text.literal(WarningsConfig.ID).formatted(Formatting.BOLD));
		for (Pair<String, ?> pair : WarningsConfig.CONFIG_PROVIDER.getConfigList())
			text.add(Text.literal(pair.getFirst() + ": " + pair.getSecond()));
		return text;
	}
}