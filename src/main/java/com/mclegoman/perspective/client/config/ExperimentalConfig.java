/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.common.config.ConfigProvider;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import com.mclegoman.simplefabriclibs.simple_config.SimpleConfig;

public class ExperimentalConfig {
	protected static final String ID = Data.VERSION.getID() + "-experimental";
	protected static SimpleConfig CONFIG;
	protected static ConfigProvider CONFIG_PROVIDER;

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
	}

	protected static void assign() {
	}

	protected static void save() {
		if (ConfigHelper.EXPERIMENTS_AVAILABLE) {
			Data.VERSION.sendToLog(Helper.LogType.INFO,"Writing experimental config to file.");
			CONFIG_PROVIDER.saveConfig(ID);
		}
	}
}