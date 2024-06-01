/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;
import java.util.Optional;

public class ConfigDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	public static final String ID = "config";
	public static int zoomLevel;
	public static int zoomIncrementSize;
	public static String zoomTransition;
	public static String zoomScaleMode;
	public static boolean zoomHideHud;
	public static boolean zoomShowPercentage;
	public static String zoomType;
	public static boolean holdPerspectiveHideHud;
	public static String superSecretSettingsShader;
	public static String superSecretSettingsMode;
	public static boolean superSecretSettingsEnabled;
	public static boolean superSecretSettingsSound;
	public static boolean superSecretSettingsShowName;
	public static boolean superSecretSettingsSelectionBlur;
	public static boolean texturedNamedEntity;
	public static boolean texturedRandomEntity;
	public static boolean allowAprilFools;
	public static boolean forceAprilFools;
	public static boolean versionOverlay;
	public static boolean positionOverlay;
	public static boolean dayOverlay;
	public static boolean forcePride;
	public static String forcePrideType;
	public static boolean showDeathCoordinates;
	public static String titleScreen;
	public static String uiBackground;
	public static String uiBackgroundTexture;
	public static boolean hideBlockOutline;
	public static String hideCrosshair;
	public static boolean hideArmor;
	public static boolean hideNametags;
	public static boolean hidePlayers;
	public static boolean hideShowMessage;
	public static boolean tutorials;
	public static String detectUpdateChannel;
	public static boolean debug;
	public static boolean testResourcePack;

	public ConfigDataLoader() {
		super(new Gson(), ID);
	}

	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			Optional<Resource> resource = manager.getResource(Identifier.of(Data.version.getID(), ID + ".json"));
			if (resource.isPresent()) {
				zoomLevel = JsonHelper.getInt(JsonHelper.deserialize(resource.get().getReader()), "zoom_level", 40);
				zoomIncrementSize = JsonHelper.getInt(JsonHelper.deserialize(resource.get().getReader()), "zoom_increment_size", 1);
				zoomTransition = JsonHelper.getString(JsonHelper.deserialize(resource.get().getReader()), "zoom_transition", "smooth");
				zoomScaleMode = JsonHelper.getString(JsonHelper.deserialize(resource.get().getReader()), "zoom_scale_mode", "scaled");
				zoomHideHud = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "zoom_hide_hud", false);
				zoomType = JsonHelper.getString(JsonHelper.deserialize(resource.get().getReader()), "zoom_type", "perspective:logarithmic");
				zoomShowPercentage = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "zoom_show_percentage", false);
				holdPerspectiveHideHud = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "hold_perspective_hide_hud", true);
				superSecretSettingsShader = JsonHelper.getString(JsonHelper.deserialize(resource.get().getReader()), "super_secret_settings_shader", "minecraft:blur");
				superSecretSettingsMode = JsonHelper.getString(JsonHelper.deserialize(resource.get().getReader()), "super_secret_settings_mode", "game");
				superSecretSettingsEnabled = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "super_secret_settings_enabled", false);
				superSecretSettingsSound = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "super_secret_settings_sound", true);
				superSecretSettingsShowName = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "super_secret_settings_show_name", true);
				superSecretSettingsSelectionBlur = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "super_secret_settings_selection_blur", false);
				texturedNamedEntity = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "textured_named_entity", true);
				texturedRandomEntity = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "textured_random_entity", false);
				allowAprilFools = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "allow_april_fools", true);
				forceAprilFools = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "force_april_fools", false);
				versionOverlay = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "version_overlay", false);
				positionOverlay = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "position_overlay", false);
				dayOverlay = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "day_overlay", false);
				forcePride = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "force_pride", false);
				forcePrideType = JsonHelper.getString(JsonHelper.deserialize(resource.get().getReader()), "force_pride_type", "random");
				showDeathCoordinates = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "show_death_coordinates", false);
				titleScreen = JsonHelper.getString(JsonHelper.deserialize(resource.get().getReader()), "title_screen", "default");
				uiBackground = JsonHelper.getString(JsonHelper.deserialize(resource.get().getReader()), "ui_background", "default");
				uiBackgroundTexture = JsonHelper.getString(JsonHelper.deserialize(resource.get().getReader()), "ui_background_texture", "minecraft:block/dirt");
				hideBlockOutline = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "hide_block_outline", false);
				hideCrosshair = JsonHelper.getString(JsonHelper.deserialize(resource.get().getReader()), "hide_crosshair", "false");
				hideArmor = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "hide_armor", false);
				hideNametags = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "hide_nametags", false);
				hidePlayers = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "hide_players", false);
				hideShowMessage = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "hide_show_message", true);
				tutorials = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "tutorials", true);
				detectUpdateChannel = JsonHelper.getString(JsonHelper.deserialize(resource.get().getReader()), "detect_update_channel", "release");
				debug = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "debug", false);
				testResourcePack = JsonHelper.getBoolean(JsonHelper.deserialize(resource.get().getReader()), "test_resource_pack", false);
			}
			ConfigHelper.loadConfig();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to load default config values: {}", error));
		}
	}

	@Override
	public Identifier getFabricId() {
		return Identifier.of(Data.version.getID(), ID);
	}
}