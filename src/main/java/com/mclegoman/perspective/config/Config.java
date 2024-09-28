/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.config;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.luminance.config.ConfigProvider;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.luminance.common.util.Couple;
import net.darktree.simplelibs.config.SimpleConfig;

public class Config {
	protected static final String id = Data.version.getID();
	protected static SimpleConfig config;
	protected static ConfigProvider configProvider;
	protected static boolean zoomEnabled;
	protected static int zoomLevel;
	protected static int zoomIncrementSize;
	protected static String zoomTransition;
	protected static double zoomSmoothSpeedIn;
	protected static double zoomSmoothSpeedOut;
	protected static String zoomScaleMode;
	protected static boolean zoomHideHud;
	protected static boolean zoomShowPercentage;
	protected static String zoomType;
	protected static boolean zoomReset;
	protected static boolean zoomCinematic;
	protected static double holdPerspectiveBackMultiplier;
	protected static double holdPerspectiveFrontMultiplier;
	protected static boolean holdPerspectiveBackHideHud;
	protected static boolean holdPerspectiveFrontHideHud;
	protected static String superSecretSettingsShader;
	protected static String superSecretSettingsMode;
	protected static boolean superSecretSettingsEnabled;
	protected static boolean superSecretSettingsSound;
	protected static boolean superSecretSettingsShowName;
	protected static boolean superSecretSettingsSelectionBlur;
	protected static boolean texturedNamedEntity;
	protected static boolean texturedRandomEntity;
	protected static boolean allowAprilFools;
	protected static boolean forceAprilFools;
	protected static boolean allowHalloween;
	protected static boolean forceHalloween;
	protected static boolean versionOverlay;
	protected static boolean positionOverlay;
	protected static String timeOverlay;
	protected static boolean dayOverlay;
	protected static boolean biomeOverlay;
	protected static boolean forcePride;
	protected static String forcePrideType;
	protected static boolean showDeathCoordinates;
	protected static String uiBackground;
	protected static String uiBackgroundTexture;
	protected static String crosshairType;
	protected static boolean hideBlockOutline;
	protected static int blockOutline;
	protected static boolean rainbowBlockOutline;
	protected static boolean hideArmor;
	protected static boolean hideNametags;
	protected static boolean hidePlayers;
	protected static boolean hideShowMessage;
	protected static String detectUpdateChannel;
	protected static boolean tutorials;
	protected static boolean debug;
	protected static int rippleDensity;
	protected static boolean rippleSound;
	protected static int fallingLeavesDensity;
	protected static int configVersion;
	protected static final Object[] options;

	protected static void init() {
		try {
			configProvider = new ConfigProvider();
			create();
			config = SimpleConfig.of(id).provider(configProvider).request();
			assign();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to initialize {} config: {}", id, error));
		}
	}

	protected static void create() {
		configProvider.add(new Couple<>("zoom_enabled", ConfigDataLoader.zoomEnabled));
		configProvider.add(new Couple<>("zoom_level", ConfigDataLoader.zoomLevel));
		configProvider.add(new Couple<>("zoom_increment_size", ConfigDataLoader.zoomIncrementSize));
		configProvider.add(new Couple<>("zoom_transition", ConfigDataLoader.zoomTransition));
		configProvider.add(new Couple<>("zoom_smooth_speed_in", ConfigDataLoader.zoomSmoothSpeedIn));
		configProvider.add(new Couple<>("zoom_smooth_speed_out", ConfigDataLoader.zoomSmoothSpeedOut));
		configProvider.add(new Couple<>("zoom_scale_mode", ConfigDataLoader.zoomScaleMode));
		configProvider.add(new Couple<>("zoom_hide_hud", ConfigDataLoader.zoomHideHud));
		configProvider.add(new Couple<>("zoom_show_percentage", ConfigDataLoader.zoomShowPercentage));
		configProvider.add(new Couple<>("zoom_type", ConfigDataLoader.zoomType));
		configProvider.add(new Couple<>("zoom_reset", ConfigDataLoader.zoomReset));
		configProvider.add(new Couple<>("zoom_cinematic", ConfigDataLoader.zoomCinematic));
		configProvider.add(new Couple<>("hold_perspective_back_multiplier", ConfigDataLoader.holdPerspectiveBackMultiplier));
		configProvider.add(new Couple<>("hold_perspective_front_multiplier", ConfigDataLoader.holdPerspectiveFrontMultiplier));
		configProvider.add(new Couple<>("hold_perspective_back_hide_hud", ConfigDataLoader.holdPerspectiveBackHideHud));
		configProvider.add(new Couple<>("hold_perspective_front_hide_hud", ConfigDataLoader.holdPerspectiveFrontHideHud));
		configProvider.add(new Couple<>("super_secret_settings_shader", ConfigDataLoader.superSecretSettingsShader));
		configProvider.add(new Couple<>("super_secret_settings_mode", ConfigDataLoader.superSecretSettingsMode));
		configProvider.add(new Couple<>("super_secret_settings_enabled", ConfigDataLoader.superSecretSettingsEnabled));
		configProvider.add(new Couple<>("super_secret_settings_sound", ConfigDataLoader.superSecretSettingsSound));
		configProvider.add(new Couple<>("super_secret_settings_show_name", ConfigDataLoader.superSecretSettingsShowName));
		configProvider.add(new Couple<>("super_secret_settings_selection_blur", ConfigDataLoader.superSecretSettingsSelectionBlur));
		configProvider.add(new Couple<>("textured_named_entity", ConfigDataLoader.texturedNamedEntity));
		configProvider.add(new Couple<>("textured_random_entity", ConfigDataLoader.texturedRandomEntity));
		configProvider.add(new Couple<>("allow_april_fools", ConfigDataLoader.allowAprilFools));
		configProvider.add(new Couple<>("force_april_fools", ConfigDataLoader.forceAprilFools));
		configProvider.add(new Couple<>("allow_halloween", ConfigDataLoader.allowHalloween));
		configProvider.add(new Couple<>("force_halloween", ConfigDataLoader.forceHalloween));
		configProvider.add(new Couple<>("version_overlay", ConfigDataLoader.versionOverlay));
		configProvider.add(new Couple<>("position_overlay", ConfigDataLoader.positionOverlay));
		configProvider.add(new Couple<>("time_overlay", ConfigDataLoader.timeOverlay));
		configProvider.add(new Couple<>("day_overlay", ConfigDataLoader.dayOverlay));
		configProvider.add(new Couple<>("biome_overlay", ConfigDataLoader.biomeOverlay));
		configProvider.add(new Couple<>("force_pride", ConfigDataLoader.forcePride));
		configProvider.add(new Couple<>("force_pride_type", ConfigDataLoader.forcePrideType));
		configProvider.add(new Couple<>("show_death_coordinates", ConfigDataLoader.showDeathCoordinates));
		configProvider.add(new Couple<>("ui_background", ConfigDataLoader.uiBackground));
		configProvider.add(new Couple<>("ui_background_texture", ConfigDataLoader.uiBackgroundTexture));
		configProvider.add(new Couple<>("hide_block_outline", ConfigDataLoader.hideBlockOutline));
		configProvider.add(new Couple<>("block_outline", ConfigDataLoader.blockOutline));
		configProvider.add(new Couple<>("rainbow_block_outline", ConfigDataLoader.rainbowBlockOutline));
		configProvider.add(new Couple<>("crosshair_type", ConfigDataLoader.crosshairType));
		configProvider.add(new Couple<>("hide_armor", ConfigDataLoader.hideArmor));
		configProvider.add(new Couple<>("hide_nametags", ConfigDataLoader.hideNametags));
		configProvider.add(new Couple<>("hide_players", ConfigDataLoader.hidePlayers));
		configProvider.add(new Couple<>("hide_show_message", ConfigDataLoader.hideShowMessage));
		configProvider.add(new Couple<>("tutorials", ConfigDataLoader.tutorials));
		configProvider.add(new Couple<>("detect_update_channel", ConfigDataLoader.detectUpdateChannel));
		configProvider.add(new Couple<>("ripple_density", ConfigDataLoader.rippleDensity));
		configProvider.add(new Couple<>("ripple_sound", ConfigDataLoader.rippleSound));
		configProvider.add(new Couple<>("falling_leaves", ConfigDataLoader.fallingLeavesDensity));
		configProvider.add(new Couple<>("debug", ConfigHelper.defaultDebug));
		configProvider.add(new Couple<>("config_version", ConfigHelper.defaultConfigVersion));
	}

	protected static void assign() {
		zoomEnabled = config.getOrDefault("zoom_enabled", ConfigDataLoader.zoomEnabled);
		zoomLevel = config.getOrDefault("zoom_level", ConfigDataLoader.zoomLevel);
		zoomIncrementSize = config.getOrDefault("zoom_increment_size", ConfigDataLoader.zoomIncrementSize);
		zoomTransition = config.getOrDefault("zoom_transition", ConfigDataLoader.zoomTransition);
		zoomSmoothSpeedIn = config.getOrDefault("zoom_smooth_speed_in", ConfigDataLoader.zoomSmoothSpeedIn);
		zoomSmoothSpeedOut = config.getOrDefault("zoom_smooth_speed_out", ConfigDataLoader.zoomSmoothSpeedOut);
		zoomScaleMode = config.getOrDefault("zoom_scale_mode", ConfigDataLoader.zoomScaleMode);
		zoomHideHud = config.getOrDefault("zoom_hide_hud", ConfigDataLoader.zoomHideHud);
		zoomShowPercentage = config.getOrDefault("zoom_show_percentage", ConfigDataLoader.zoomShowPercentage);
		zoomType = config.getOrDefault("zoom_type", ConfigDataLoader.zoomType);
		zoomReset = config.getOrDefault("zoom_reset", ConfigDataLoader.zoomReset);
		zoomCinematic = config.getOrDefault("zoom_cinematic", ConfigDataLoader.zoomCinematic);
		holdPerspectiveBackHideHud = config.getOrDefault("hold_perspective_back_hide_hud", ConfigDataLoader.holdPerspectiveBackHideHud);
		holdPerspectiveFrontHideHud = config.getOrDefault("hold_perspective_front_hide_hud", ConfigDataLoader.holdPerspectiveFrontHideHud);
		holdPerspectiveBackMultiplier = config.getOrDefault("hold_perspective_back_multiplier", ConfigDataLoader.holdPerspectiveBackMultiplier);
		holdPerspectiveFrontMultiplier = config.getOrDefault("hold_perspective_front_multiplier", ConfigDataLoader.holdPerspectiveFrontMultiplier);
		superSecretSettingsShader = config.getOrDefault("super_secret_settings_shader", ConfigDataLoader.superSecretSettingsShader);
		superSecretSettingsMode = config.getOrDefault("super_secret_settings_mode", ConfigDataLoader.superSecretSettingsMode);
		superSecretSettingsEnabled = config.getOrDefault("super_secret_settings_enabled", ConfigDataLoader.superSecretSettingsEnabled);
		superSecretSettingsSound = config.getOrDefault("super_secret_settings_sound", ConfigDataLoader.superSecretSettingsSound);
		superSecretSettingsShowName = config.getOrDefault("super_secret_settings_show_name", ConfigDataLoader.superSecretSettingsShowName);
		superSecretSettingsSelectionBlur = config.getOrDefault("super_secret_settings_selection_blur", ConfigDataLoader.superSecretSettingsSelectionBlur);
		texturedNamedEntity = config.getOrDefault("textured_named_entity", ConfigDataLoader.texturedNamedEntity);
		texturedRandomEntity = config.getOrDefault("textured_random_entity", ConfigDataLoader.texturedRandomEntity);
		allowAprilFools = config.getOrDefault("allow_april_fools", ConfigDataLoader.allowAprilFools);
		forceAprilFools = config.getOrDefault("force_april_fools", ConfigDataLoader.forceAprilFools);
		allowHalloween = config.getOrDefault("allow_halloween", ConfigDataLoader.allowHalloween);
		forceHalloween = config.getOrDefault("force_halloween", ConfigDataLoader.forceHalloween);
		versionOverlay = config.getOrDefault("version_overlay", ConfigDataLoader.versionOverlay);
		positionOverlay = config.getOrDefault("position_overlay", ConfigDataLoader.positionOverlay);
		timeOverlay = config.getOrDefault("time_overlay", ConfigDataLoader.timeOverlay);
		dayOverlay = config.getOrDefault("day_overlay", ConfigDataLoader.dayOverlay);
		biomeOverlay = config.getOrDefault("biome_overlay", ConfigDataLoader.biomeOverlay);
		forcePride = config.getOrDefault("force_pride", ConfigDataLoader.forcePride);
		forcePrideType = config.getOrDefault("force_pride_type", ConfigDataLoader.forcePrideType);
		showDeathCoordinates = config.getOrDefault("show_death_coordinates", ConfigDataLoader.showDeathCoordinates);
		uiBackground = config.getOrDefault("ui_background", ConfigDataLoader.uiBackground);
		uiBackgroundTexture = config.getOrDefault("ui_background_texture", ConfigDataLoader.uiBackgroundTexture);
		hideBlockOutline = config.getOrDefault("hide_block_outline", ConfigDataLoader.hideBlockOutline);
		blockOutline = config.getOrDefault("block_outline", ConfigDataLoader.blockOutline);
		rainbowBlockOutline = config.getOrDefault("rainbow_block_outline", ConfigDataLoader.rainbowBlockOutline);
		crosshairType = config.getOrDefault("crosshair_type", ConfigDataLoader.crosshairType);
		hideArmor = config.getOrDefault("hide_armor", ConfigDataLoader.hideArmor);
		hideNametags = config.getOrDefault("hide_nametags", ConfigDataLoader.hideNametags);
		hidePlayers = config.getOrDefault("hide_players", ConfigDataLoader.hidePlayers);
		hideShowMessage = config.getOrDefault("hide_show_message", ConfigDataLoader.hideShowMessage);
		tutorials = config.getOrDefault("tutorials", ConfigDataLoader.tutorials);
		detectUpdateChannel = config.getOrDefault("detect_update_channel", ConfigDataLoader.detectUpdateChannel);
		rippleDensity = config.getOrDefault("ripple_density", ConfigDataLoader.rippleDensity);
		rippleSound = config.getOrDefault("ripple_sound", ConfigDataLoader.rippleSound);
		fallingLeavesDensity = config.getOrDefault("falling_leaves", ConfigDataLoader.fallingLeavesDensity);
		debug = config.getOrDefault("debug", ConfigHelper.defaultDebug);
		configVersion = config.getOrDefault("config_version", ConfigHelper.defaultConfigVersion);
	}

	protected static void save() {
		Data.version.sendToLog(LogType.INFO,"Writing config to file.");
		configProvider.setConfig("zoom_enabled", zoomEnabled);
		configProvider.setConfig("zoom_level", zoomLevel);
		configProvider.setConfig("zoom_increment_size", zoomIncrementSize);
		configProvider.setConfig("zoom_transition", zoomTransition);
		configProvider.setConfig("zoom_smooth_speed_in", zoomSmoothSpeedIn);
		configProvider.setConfig("zoom_smooth_speed_out", zoomSmoothSpeedOut);
		configProvider.setConfig("zoom_scale_mode", zoomScaleMode);
		configProvider.setConfig("zoom_hide_hud", zoomHideHud);
		configProvider.setConfig("zoom_show_percentage", zoomShowPercentage);
		configProvider.setConfig("zoom_type", zoomType);
		configProvider.setConfig("zoom_reset", zoomReset);
		configProvider.setConfig("zoom_cinematic", zoomCinematic);
		configProvider.setConfig("hold_perspective_back_multiplier", holdPerspectiveBackMultiplier);
		configProvider.setConfig("hold_perspective_front_multiplier", holdPerspectiveFrontMultiplier);
		configProvider.setConfig("hold_perspective_back_hide_hud", holdPerspectiveBackHideHud);
		configProvider.setConfig("hold_perspective_front_hide_hud", holdPerspectiveFrontHideHud);
		configProvider.setConfig("super_secret_settings_shader", superSecretSettingsShader);
		configProvider.setConfig("super_secret_settings_mode", superSecretSettingsMode);
		configProvider.setConfig("super_secret_settings_enabled", superSecretSettingsEnabled);
		configProvider.setConfig("super_secret_settings_sound", superSecretSettingsSound);
		configProvider.setConfig("super_secret_settings_show_name", superSecretSettingsShowName);
		configProvider.setConfig("super_secret_settings_selection_blur", superSecretSettingsSelectionBlur);
		configProvider.setConfig("textured_named_entity", texturedNamedEntity);
		configProvider.setConfig("textured_random_entity", texturedRandomEntity);
		configProvider.setConfig("allow_april_fools", allowAprilFools);
		configProvider.setConfig("force_april_fools", forceAprilFools);
		configProvider.setConfig("allow_halloween", allowHalloween);
		configProvider.setConfig("force_halloween", forceHalloween);
		configProvider.setConfig("version_overlay", versionOverlay);
		configProvider.setConfig("position_overlay", positionOverlay);
		configProvider.setConfig("time_overlay", timeOverlay);
		configProvider.setConfig("day_overlay", dayOverlay);
		configProvider.setConfig("biome_overlay", biomeOverlay);
		configProvider.setConfig("force_pride", forcePride);
		configProvider.setConfig("force_pride_type", forcePrideType);
		configProvider.setConfig("show_death_coordinates", showDeathCoordinates);
		configProvider.setConfig("ui_background", uiBackground);
		configProvider.setConfig("ui_background_texture", uiBackgroundTexture);
		configProvider.setConfig("hide_block_outline", hideBlockOutline);
		configProvider.setConfig("block_outline", blockOutline);
		configProvider.setConfig("rainbow_block_outline", rainbowBlockOutline);
		configProvider.setConfig("crosshair_type", crosshairType);
		configProvider.setConfig("hide_armor", hideArmor);
		configProvider.setConfig("hide_nametags", hideNametags);
		configProvider.setConfig("hide_players", hidePlayers);
		configProvider.setConfig("hide_show_message", hideShowMessage);
		configProvider.setConfig("tutorials", tutorials);
		configProvider.setConfig("detect_update_channel", detectUpdateChannel);
		configProvider.setConfig("ripple_density", rippleDensity);
		configProvider.setConfig("ripple_sound", rippleSound);
		configProvider.setConfig("falling_leaves", fallingLeavesDensity);
		configProvider.setConfig("debug", debug);
		configProvider.setConfig("config_version", ConfigHelper.defaultConfigVersion);
		configProvider.saveConfig(Data.version, id);
	}
	static {
		options = new Object[]{
				zoomLevel,
				zoomIncrementSize,
				zoomTransition,
				zoomSmoothSpeedIn,
				zoomSmoothSpeedOut,
				zoomScaleMode,
				zoomHideHud,
				zoomShowPercentage,
				zoomType,
				zoomReset,
				zoomCinematic,
				holdPerspectiveBackMultiplier,
				holdPerspectiveFrontMultiplier,
				holdPerspectiveBackHideHud,
				holdPerspectiveFrontHideHud,
				superSecretSettingsShader,
				superSecretSettingsMode,
				superSecretSettingsEnabled,
				superSecretSettingsSound,
				superSecretSettingsShowName,
				superSecretSettingsSelectionBlur,
				texturedNamedEntity,
				texturedRandomEntity,
				allowAprilFools,
				forceAprilFools,
				allowHalloween,
				forceHalloween,
				versionOverlay,
				positionOverlay,
				timeOverlay,
				dayOverlay,
				biomeOverlay,
				forcePride,
				forcePrideType,
				showDeathCoordinates,
				uiBackground,
				uiBackgroundTexture,
				crosshairType,
				hideBlockOutline,
				blockOutline,
				rainbowBlockOutline,
				hideArmor,
				hideNametags,
				hidePlayers,
				hideShowMessage,
				detectUpdateChannel,
				tutorials,
				rippleDensity,
				rippleSound,
				fallingLeavesDensity,
				debug,
				configVersion
		};
	}
}