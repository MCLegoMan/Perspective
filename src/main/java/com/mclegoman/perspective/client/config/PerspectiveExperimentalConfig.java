/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.common.data.PerspectiveData;
import com.mclegoman.simplefabric.fabric_simplelibs.simple_config.SimpleConfig;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PerspectiveExperimentalConfig {
    protected static final String ID = PerspectiveData.ID + "-experimental";
    protected static SimpleConfig CONFIG;
    protected static PerspectiveConfigProvider CONFIG_PROVIDER;
    public static int SUPER_SECRET_SETTINGS;
    public static boolean TEXTURED_NAMED_ENTITY;
    public static boolean TEXTURED_RANDOM_ENTITY;
    protected static void init() {
        CONFIG_PROVIDER = new PerspectiveConfigProvider();
        create();
        CONFIG = SimpleConfig.of(ID).provider(CONFIG_PROVIDER).request();
        assign();
    }
    protected static void create() {
        CONFIG_PROVIDER.add(ID, new Pair<>("textured_named_entity", PerspectiveDefaultConfigDataLoader.TEXTURED_NAMED_ENTITY));
        CONFIG_PROVIDER.add(ID, new Pair<>("textured_random_entity", PerspectiveDefaultConfigDataLoader.TEXTURED_RANDOM_ENTITY));
    }
    protected static void assign() {
        SUPER_SECRET_SETTINGS = CONFIG.getOrDefault("super_secret_settings", PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS);
        TEXTURED_NAMED_ENTITY = CONFIG.getOrDefault("textured_named_entity", PerspectiveDefaultConfigDataLoader.TEXTURED_NAMED_ENTITY);
        TEXTURED_RANDOM_ENTITY = CONFIG.getOrDefault("textured_random_entity", PerspectiveDefaultConfigDataLoader.TEXTURED_RANDOM_ENTITY);
    }
    protected static void reset() {
        SUPER_SECRET_SETTINGS = PerspectiveDefaultConfigDataLoader.SUPER_SECRET_SETTINGS;
        TEXTURED_NAMED_ENTITY = PerspectiveDefaultConfigDataLoader.TEXTURED_NAMED_ENTITY;
        TEXTURED_RANDOM_ENTITY = PerspectiveDefaultConfigDataLoader.TEXTURED_RANDOM_ENTITY;
        write_to_file();
    }
    protected static void write_to_file() {
        CONFIG_PROVIDER.write_to_file(ID, "super_secret_settings", SUPER_SECRET_SETTINGS);
        CONFIG_PROVIDER.write_to_file(ID, "textured_named_entity", TEXTURED_NAMED_ENTITY);
        CONFIG_PROVIDER.write_to_file(ID, "textured_random_entity", TEXTURED_RANDOM_ENTITY);
    }
}