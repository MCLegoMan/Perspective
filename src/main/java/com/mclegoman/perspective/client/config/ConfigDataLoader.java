/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mclegoman.perspective.client.util.JsonHelper;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;
import java.util.Optional;

public class ConfigDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	public static final String ID = "config";
	public static int ZOOM_LEVEL;
	public static int ZOOM_INCREMENT_SIZE;
	public static String ZOOM_TRANSITION;
	public static String ZOOM_SCALE_MODE;
	public static boolean ZOOM_HIDE_HUD;
	public static boolean ZOOM_SHOW_PERCENTAGE;
	public static String ZOOM_TYPE;
	public static boolean HOLD_PERSPECTIVE_HIDE_HUD;
	public static String SUPER_SECRET_SETTINGS_SHADER;
	public static String SUPER_SECRET_SETTINGS_MODE;
	public static boolean SUPER_SECRET_SETTINGS_ENABLED;
	public static boolean SUPER_SECRET_SETTINGS_SOUND;
	public static boolean SUPER_SECRET_SETTINGS_OPTIONS_SCREEN;
	public static boolean SUPER_SECRET_SETTINGS_SHOW_NAME;
	public static boolean SUPER_SECRET_SETTINGS_SELECTION_BLUR;
	public static boolean TEXTURED_NAMED_ENTITY;
	public static boolean TEXTURED_RANDOM_ENTITY;
	public static boolean ALLOW_APRIL_FOOLS;
	public static boolean FORCE_APRIL_FOOLS;
	public static boolean POSITION_OVERLAY;
	public static boolean VERSION_OVERLAY;
	public static boolean FORCE_PRIDE;
	public static boolean FORCE_PRIDE_TYPE;
	public static int FORCE_PRIDE_TYPE_INDEX;
	public static boolean SHOW_DEATH_COORDINATES;
	public static String TITLE_SCREEN;
	public static String UI_BACKGROUND;
	public static boolean HIDE_BLOCK_OUTLINE;
	public static String HIDE_CROSSHAIR;
	public static boolean HIDE_ARMOR;
	public static boolean HIDE_NAMETAGS;
	public static boolean HIDE_PLAYERS;
	public static boolean HIDE_SHOW_MESSAGE;
	public static boolean TUTORIALS;
	public static String DETECT_UPDATE_CHANNEL;
	public static boolean DEBUG;
	public static boolean TEST_RESOURCE_PACK;

	public ConfigDataLoader() {
		super(new Gson(), ID);
	}

	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			Optional<Resource> resource = manager.getResource(new Identifier(Data.VERSION.getID(), ID + ".json"));
			if (resource.isPresent()) {
				ZOOM_LEVEL = net.minecraft.util.JsonHelper.getInt(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "zoom_level", 40);
				ZOOM_INCREMENT_SIZE = net.minecraft.util.JsonHelper.getInt(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "zoom_increment_size", 1);
				ZOOM_TRANSITION = net.minecraft.util.JsonHelper.getString(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "zoom_transition", "smooth");
				ZOOM_SCALE_MODE = JsonHelper.getString(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "zoom_scale_mode", "scaled");
				ZOOM_HIDE_HUD = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "zoom_hide_hud", false);
				ZOOM_TYPE = JsonHelper.getZoomType(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "zoom_type", "perspective:logarithmic");
				ZOOM_SHOW_PERCENTAGE = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "zoom_show_percentage", false);
				HOLD_PERSPECTIVE_HIDE_HUD = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "hold_perspective_hide_hud", true);
				SUPER_SECRET_SETTINGS_SHADER = net.minecraft.util.JsonHelper.getString(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "super_secret_settings_shader", "minecraft:none");
				SUPER_SECRET_SETTINGS_MODE = JsonHelper.getShaderMode(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "super_secret_settings_mode", "game");
				SUPER_SECRET_SETTINGS_ENABLED = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "super_secret_settings_enabled", false);
				SUPER_SECRET_SETTINGS_SOUND = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "super_secret_settings_sound", true);
				SUPER_SECRET_SETTINGS_OPTIONS_SCREEN = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "super_secret_settings_options_screen", false);
				SUPER_SECRET_SETTINGS_SHOW_NAME = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "super_secret_settings_show_name", true);
				SUPER_SECRET_SETTINGS_SELECTION_BLUR = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "super_secret_settings_selection_blur", true);
				TEXTURED_NAMED_ENTITY = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "textured_named_entity", true);
				TEXTURED_RANDOM_ENTITY = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "textured_random_entity", false);
				ALLOW_APRIL_FOOLS = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "allow_april_fools", true);
				FORCE_APRIL_FOOLS = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "force_april_fools", false);
				POSITION_OVERLAY = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "position_overlay", false);
				VERSION_OVERLAY = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "version_overlay", false);
				FORCE_PRIDE = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "force_pride", false);
				FORCE_PRIDE_TYPE = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "force_pride_type", false);
				FORCE_PRIDE_TYPE_INDEX = net.minecraft.util.JsonHelper.getInt(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "force_pride_type_index", 0);
				SHOW_DEATH_COORDINATES = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "show_death_coordinates", false);
				TITLE_SCREEN = JsonHelper.getTitleScreenBackground(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "title_screen", "default");
				UI_BACKGROUND = JsonHelper.getUIBackground(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "ui_background", "default");
				HIDE_BLOCK_OUTLINE = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "hide_block_outline", false);
				HIDE_CROSSHAIR = net.minecraft.util.JsonHelper.getString(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "hide_crosshair", "false");
				HIDE_ARMOR = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "hide_armor", false);
				HIDE_NAMETAGS = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "hide_nametags", false);
				HIDE_PLAYERS = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "hide_players", false);
				HIDE_SHOW_MESSAGE = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "hide_show_message", true);
				TUTORIALS = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "tutorials", true);
				DETECT_UPDATE_CHANNEL = JsonHelper.getDetectUpdateChannel(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "detect_update_channel", "release");
				DEBUG = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "debug", false);
				TEST_RESOURCE_PACK = net.minecraft.util.JsonHelper.getBoolean(net.minecraft.util.JsonHelper.deserialize(resource.get().getReader()), "test_resource_pack", false);
			}
			ConfigHelper.loadConfig();
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to load default config values: {}", Data.VERSION.getLoggerPrefix(), error);
		}
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier(Data.VERSION.getID(), ID);
	}
}