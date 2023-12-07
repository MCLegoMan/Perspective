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

public class TutorialsConfig {
	protected static final String ID = Data.PERSPECTIVE_VERSION.getID() + "-tutorials";
	protected static SimpleConfig CONFIG;
	protected static ConfigProvider CONFIG_PROVIDER;
	protected static boolean SUPER_SECRET_SETTINGS;
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
		CONFIG_PROVIDER.add(new Pair<>("super_secret_settings", false));
	}
	protected static void assign() {
		SUPER_SECRET_SETTINGS = CONFIG.getOrDefault("super_secret_settings", false);
	}
	protected static void save() {
		Data.PERSPECTIVE_VERSION.getLogger().info("{} Writing tutorial config to file.", Data.PERSPECTIVE_VERSION.getLoggerPrefix());
		CONFIG_PROVIDER.setConfig("super_secret_settings", SUPER_SECRET_SETTINGS);
		CONFIG_PROVIDER.saveConfig(ID);
	}
}