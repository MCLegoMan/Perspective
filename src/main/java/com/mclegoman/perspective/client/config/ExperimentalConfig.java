/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.simplefabriclibs.simple_config.SimpleConfig;
import com.mojang.datafixers.util.Pair;

public class ExperimentalConfig {
    protected static final String ID = Data.PERSPECTIVE_VERSION.getID() + "-experimental";
    protected static SimpleConfig CONFIG;
    protected static ConfigProvider CONFIG_PROVIDER;
    protected static boolean ALLOW_HIDE_PLAYERS;
    protected static boolean HIDE_PLAYERS;
    protected static void init() {
        try {
            CONFIG_PROVIDER = new ConfigProvider();
            create();
            CONFIG = SimpleConfig.of(ID).provider(CONFIG_PROVIDER).request();
            assign();
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize {} config: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), ID, error);
        }
    }
    protected static void create() {
        CONFIG_PROVIDER.add(new Pair<>("allow_hide_players", false));
        CONFIG_PROVIDER.add(new Pair<>("hide_players", false));
    }
    protected static void assign() {
        ALLOW_HIDE_PLAYERS = CONFIG.getOrDefault("allow_hide_players", false);
        HIDE_PLAYERS = CONFIG.getOrDefault("hide_players", false);
    }
    protected static void save() {
        Data.PERSPECTIVE_VERSION.getLogger().info("{} Writing experimental config to file.", Data.PERSPECTIVE_VERSION.getLoggerPrefix());
        CONFIG_PROVIDER.setConfig("allow_hide_players", ALLOW_HIDE_PLAYERS);
        CONFIG_PROVIDER.setConfig("hide_players", HIDE_PLAYERS);
        CONFIG_PROVIDER.saveConfig(ID);
    }
}