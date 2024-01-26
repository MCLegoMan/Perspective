/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.common.config.ConfigProvider;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.common.util.Couple;
import com.mclegoman.releasetypeutils.common.version.Helper;
import com.mclegoman.simplefabriclibs.simple_config.SimpleConfig;

public class TutorialsConfig {
	protected static final String ID = Data.VERSION.getID() + "-tutorials";
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
			Data.VERSION.getLogger().warn("{} Failed to initialize {} config: {}", Data.VERSION.getLoggerPrefix(), ID, error);
		}
	}

	protected static void create() {
		CONFIG_PROVIDER.add(new Couple<>("super_secret_settings", false));
	}

	protected static void assign() {
		SUPER_SECRET_SETTINGS = CONFIG.getOrDefault("super_secret_settings", false);
	}

	protected static void save() {
		Data.VERSION.sendToLog(Helper.LogType.INFO,"Writing tutorial config to file.");
		CONFIG_PROVIDER.setConfig("super_secret_settings", SUPER_SECRET_SETTINGS);
		CONFIG_PROVIDER.saveConfig(ID);
	}
}