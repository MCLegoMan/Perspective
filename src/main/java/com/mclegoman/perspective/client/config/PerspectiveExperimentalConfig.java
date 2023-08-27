/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.common.data.PerspectiveData;
import com.mclegoman.simplefabriclibs.simple_config.SimpleConfig;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PerspectiveExperimentalConfig {
    protected static final String ID = PerspectiveData.ID + "-experimental";
    protected static SimpleConfig CONFIG;
    protected static PerspectiveConfigProvider CONFIG_PROVIDER;
    protected static boolean HIDE_ARMOR;
    protected static boolean HIDE_NAMETAGS;
    protected static boolean SMOOTH_ZOOM;
    protected static int SMOOTH_ZOOM_SCALE;
    protected static void init() {
        try {
            CONFIG_PROVIDER = new PerspectiveConfigProvider();
            create();
            CONFIG = SimpleConfig.of(ID).provider(CONFIG_PROVIDER).request();
            assign();
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to initialize {} config: {}", ID, error);
        }
    }
    protected static void create() {
        CONFIG_PROVIDER.add(new Pair<>("hide_armor", PerspectiveConfigDataLoader.HIDE_ARMOR));
        CONFIG_PROVIDER.add(new Pair<>("hide_nametags", PerspectiveConfigDataLoader.HIDE_NAMETAGS));
        CONFIG_PROVIDER.add(new Pair<>("smooth_zoom", PerspectiveConfigDataLoader.SMOOTH_ZOOM));
        CONFIG_PROVIDER.add(new Pair<>("smooth_zoom_scale", PerspectiveConfigDataLoader.SMOOTH_ZOOM_SCALE));
    }
    protected static void assign() {
        HIDE_ARMOR = CONFIG.getOrDefault("hide_armor", PerspectiveConfigDataLoader.HIDE_ARMOR);
        HIDE_NAMETAGS = CONFIG.getOrDefault("hide_nametags", PerspectiveConfigDataLoader.HIDE_NAMETAGS);
        SMOOTH_ZOOM = CONFIG.getOrDefault("smooth_zoom", PerspectiveConfigDataLoader.SMOOTH_ZOOM);
        SMOOTH_ZOOM_SCALE = CONFIG.getOrDefault("smooth_zoom_scale", PerspectiveConfigDataLoader.SMOOTH_ZOOM_SCALE);
    }
    protected static void save() {
        CONFIG_PROVIDER.setConfig("hide_armor", HIDE_ARMOR);
        CONFIG_PROVIDER.setConfig("hide_nametags", HIDE_NAMETAGS);
        CONFIG_PROVIDER.setConfig("smooth_zoom", SMOOTH_ZOOM);
        CONFIG_PROVIDER.setConfig("smooth_zoom_scale", SMOOTH_ZOOM_SCALE);
    }
}