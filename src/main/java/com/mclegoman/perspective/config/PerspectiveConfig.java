package com.mclegoman.perspective.config;

import com.mclegoman.simplefabric.fabric_simplelibs.simple_config.SimpleConfig;
import com.mojang.datafixers.util.Pair;

public class PerspectiveConfig {
    public static SimpleConfig CONFIG;
    private static PerspectiveConfigProvider CONFIG_PROVIDER;
    public static int ZOOM_LEVEL;
    public static void init() {
        CONFIG_PROVIDER = new PerspectiveConfigProvider();
        create();
        CONFIG = SimpleConfig.of("zoom").provider(CONFIG_PROVIDER).request();
        assign();
    }
    private static void create() {
        CONFIG_PROVIDER.add("zoom properties file");
        CONFIG_PROVIDER.add(new Pair<>("zoom_level", 20));
    }
    private static void assign() {
        ZOOM_LEVEL = CONFIG.getOrDefault("zoom_level", 20);
    }
}