package com.mclegoman.perspective.config;

import com.mclegoman.perspective.data.PerspectiveData;
import com.mclegoman.simplefabric.fabric_simplelibs.simple_config.SimpleConfig;
import com.mojang.datafixers.util.Pair;

public class PerspectiveConfig {
    public static SimpleConfig CONFIG;
    public static PerspectiveConfigProvider getConfigProvider() {return CONFIG_PROVIDER;}
    private static PerspectiveConfigProvider CONFIG_PROVIDER;
    public static int ZOOM_LEVEL;
    public static void init() {
        CONFIG_PROVIDER = new PerspectiveConfigProvider();
        create();
        CONFIG = SimpleConfig.of("perspective").provider(CONFIG_PROVIDER).request();
        assign();
    }
    private static void create() {
        CONFIG_PROVIDER.add(new Pair<>("zoom_level", true));
    }
    private static void assign() {
        ZOOM_LEVEL = CONFIG.getOrDefault("zoom_level", 20);
    }
    public static void write_to_file() {
        PerspectiveConfig.getConfigProvider().write_to_file("zoom_level", PerspectiveConfig.ZOOM_LEVEL);
    }
}