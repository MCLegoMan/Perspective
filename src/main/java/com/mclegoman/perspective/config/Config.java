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
	protected static String zoomScaleMode;
	protected static boolean zoomHideHud;
	protected static boolean zoomShowPercentage;
	protected static String zoomType;
	protected static boolean zoomReset;
	protected static boolean zoomCinematic;
	protected static boolean holdPerspectiveHideHud;
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
	protected static boolean versionOverlay;
	protected static boolean positionOverlay;
	protected static boolean dayOverlay;
	protected static boolean forcePride;
	protected static String forcePrideType;
	protected static boolean showDeathCoordinates;
	protected static String titleScreen;
	protected static String uiBackground;
	protected static String uiBackgroundTexture;
	protected static String hideCrosshair;
	protected static boolean hideBlockOutline;
	protected static boolean hideArmor;
	protected static boolean hideNametags;
	protected static boolean hidePlayers;
	protected static boolean hideShowMessage;
	protected static String detectUpdateChannel;
	protected static boolean tutorials;
	protected static boolean debug;
	protected static boolean testResourcePack;
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
		configProvider.add(new Couple<>("zoom_scale_mode", ConfigDataLoader.zoomScaleMode));
		configProvider.add(new Couple<>("zoom_hide_hud", ConfigDataLoader.zoomHideHud));
		configProvider.add(new Couple<>("zoom_show_percentage", ConfigDataLoader.zoomShowPercentage));
		configProvider.add(new Couple<>("zoom_type", ConfigDataLoader.zoomType));
		configProvider.add(new Couple<>("zoom_reset", ConfigDataLoader.zoomReset));
		configProvider.add(new Couple<>("zoom_cinematic", ConfigDataLoader.zoomCinematic));
		configProvider.add(new Couple<>("hold_perspective_hide_hud", ConfigDataLoader.holdPerspectiveHideHud));
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
		configProvider.add(new Couple<>("version_overlay", ConfigDataLoader.versionOverlay));
		configProvider.add(new Couple<>("position_overlay", ConfigDataLoader.positionOverlay));
		configProvider.add(new Couple<>("day_overlay", ConfigDataLoader.dayOverlay));
		configProvider.add(new Couple<>("force_pride", ConfigDataLoader.forcePride));
		configProvider.add(new Couple<>("force_pride_type", ConfigDataLoader.forcePrideType));
		configProvider.add(new Couple<>("show_death_coordinates", ConfigDataLoader.showDeathCoordinates));
		configProvider.add(new Couple<>("title_screen", ConfigDataLoader.titleScreen));
		configProvider.add(new Couple<>("ui_background", ConfigDataLoader.uiBackground));
		configProvider.add(new Couple<>("ui_background_texture", ConfigDataLoader.uiBackgroundTexture));
		configProvider.add(new Couple<>("hide_block_outline", ConfigDataLoader.hideBlockOutline));
		configProvider.add(new Couple<>("hide_crosshair", ConfigDataLoader.hideCrosshair));
		configProvider.add(new Couple<>("hide_armor", ConfigDataLoader.hideArmor));
		configProvider.add(new Couple<>("hide_nametags", ConfigDataLoader.hideNametags));
		configProvider.add(new Couple<>("hide_players", ConfigDataLoader.hidePlayers));
		configProvider.add(new Couple<>("hide_show_message", ConfigDataLoader.hideShowMessage));
		configProvider.add(new Couple<>("tutorials", ConfigDataLoader.tutorials));
		configProvider.add(new Couple<>("detect_update_channel", ConfigDataLoader.detectUpdateChannel));
		configProvider.add(new Couple<>("debug", ConfigDataLoader.debug));
		configProvider.add(new Couple<>("test_resource_pack", ConfigDataLoader.testResourcePack));
		configProvider.add(new Couple<>("config_version", ConfigHelper.defaultConfigVersion));
	}

	protected static void assign() {
		zoomEnabled = config.getOrDefault("zoom_enabled", ConfigDataLoader.zoomEnabled);
		zoomLevel = config.getOrDefault("zoom_level", ConfigDataLoader.zoomLevel);
		zoomIncrementSize = config.getOrDefault("zoom_increment_size", ConfigDataLoader.zoomIncrementSize);
		zoomTransition = config.getOrDefault("zoom_transition", ConfigDataLoader.zoomTransition);
		zoomScaleMode = config.getOrDefault("zoom_scale_mode", ConfigDataLoader.zoomScaleMode);
		zoomHideHud = config.getOrDefault("zoom_hide_hud", ConfigDataLoader.zoomHideHud);
		zoomShowPercentage = config.getOrDefault("zoom_show_percentage", ConfigDataLoader.zoomShowPercentage);
		zoomType = config.getOrDefault("zoom_type", ConfigDataLoader.zoomType);
		zoomReset = config.getOrDefault("zoom_reset", ConfigDataLoader.zoomReset);
		zoomCinematic = config.getOrDefault("zoom_cinematic", ConfigDataLoader.zoomCinematic);
		holdPerspectiveHideHud = config.getOrDefault("hold_perspective_hide_hud", ConfigDataLoader.holdPerspectiveHideHud);
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
		versionOverlay = config.getOrDefault("version_overlay", ConfigDataLoader.versionOverlay);
		positionOverlay = config.getOrDefault("position_overlay", ConfigDataLoader.positionOverlay);
		dayOverlay = config.getOrDefault("day_overlay", ConfigDataLoader.dayOverlay);
		forcePride = config.getOrDefault("force_pride", ConfigDataLoader.forcePride);
		forcePrideType = config.getOrDefault("force_pride_type", ConfigDataLoader.forcePrideType);
		showDeathCoordinates = config.getOrDefault("show_death_coordinates", ConfigDataLoader.showDeathCoordinates);
		titleScreen = config.getOrDefault("title_screen", ConfigDataLoader.titleScreen);
		uiBackground = config.getOrDefault("ui_background", ConfigDataLoader.uiBackground);
		uiBackgroundTexture = config.getOrDefault("ui_background_texture", ConfigDataLoader.uiBackgroundTexture);
		hideBlockOutline = config.getOrDefault("hide_block_outline", ConfigDataLoader.hideBlockOutline);
		hideCrosshair = config.getOrDefault("hide_crosshair", ConfigDataLoader.hideCrosshair);
		hideArmor = config.getOrDefault("hide_armor", ConfigDataLoader.hideArmor);
		hideNametags = config.getOrDefault("hide_nametags", ConfigDataLoader.hideNametags);
		hidePlayers = config.getOrDefault("hide_nametags", ConfigDataLoader.hidePlayers);
		hideShowMessage = config.getOrDefault("hide_show_message", ConfigDataLoader.hideShowMessage);
		tutorials = config.getOrDefault("tutorials", ConfigDataLoader.tutorials);
		detectUpdateChannel = config.getOrDefault("detect_update_channel", ConfigDataLoader.detectUpdateChannel);
		debug = config.getOrDefault("debug", ConfigDataLoader.debug);
		testResourcePack = config.getOrDefault("test_resource_pack", ConfigDataLoader.testResourcePack);
		configVersion = config.getOrDefault("config_version", ConfigHelper.defaultConfigVersion);
	}

	protected static void save() {
		Data.version.sendToLog(LogType.INFO,"Writing config to file.");
		configProvider.setConfig("zoom_enabled", zoomEnabled);
		configProvider.setConfig("zoom_level", zoomLevel);
		configProvider.setConfig("zoom_increment_size", zoomIncrementSize);
		configProvider.setConfig("zoom_transition", zoomTransition);
		configProvider.setConfig("zoom_scale_mode", zoomScaleMode);
		configProvider.setConfig("zoom_hide_hud", zoomHideHud);
		configProvider.setConfig("zoom_show_percentage", zoomShowPercentage);
		configProvider.setConfig("zoom_type", zoomType);
		configProvider.setConfig("zoom_reset", zoomReset);
		configProvider.setConfig("zoom_cinematic", zoomCinematic);
		configProvider.setConfig("hold_perspective_hide_hud", holdPerspectiveHideHud);
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
		configProvider.setConfig("version_overlay", versionOverlay);
		configProvider.setConfig("position_overlay", positionOverlay);
		configProvider.setConfig("day_overlay", dayOverlay);
		configProvider.setConfig("force_pride", forcePride);
		configProvider.setConfig("force_pride_type", forcePrideType);
		configProvider.setConfig("show_death_coordinates", showDeathCoordinates);
		configProvider.setConfig("title_screen", titleScreen);
		configProvider.setConfig("ui_background", uiBackground);
		configProvider.setConfig("ui_background_texture", uiBackgroundTexture);
		configProvider.setConfig("hide_block_outline", hideBlockOutline);
		configProvider.setConfig("hide_crosshair", hideCrosshair);
		configProvider.setConfig("hide_armor", hideArmor);
		configProvider.setConfig("hide_nametags", hideNametags);
		configProvider.setConfig("hide_players", hidePlayers);
		configProvider.setConfig("hide_show_message", hideShowMessage);
		configProvider.setConfig("tutorials", tutorials);
		configProvider.setConfig("detect_update_channel", detectUpdateChannel);
		configProvider.setConfig("debug", debug);
		configProvider.setConfig("test_resource_pack", testResourcePack);
		configProvider.setConfig("config_version", ConfigHelper.defaultConfigVersion);
		configProvider.saveConfig(Data.version, id);
	}
	static {
		options = new Object[]{
				zoomLevel,
				zoomIncrementSize,
				zoomTransition,
				zoomScaleMode,
				zoomHideHud,
				zoomShowPercentage,
				zoomType,
				zoomReset,
				zoomCinematic,
				holdPerspectiveHideHud,
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
				versionOverlay,
				positionOverlay,
				dayOverlay,
				forcePride,
				forcePrideType,
				showDeathCoordinates,
				titleScreen,
				uiBackground,
				uiBackgroundTexture,
				hideCrosshair,
				hideBlockOutline,
				hideArmor,
				hideNametags,
				hidePlayers,
				hideShowMessage,
				detectUpdateChannel,
				tutorials,
				debug,
				testResourcePack,
				configVersion
		};
	}
}