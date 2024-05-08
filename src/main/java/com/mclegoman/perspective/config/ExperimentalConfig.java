/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.config;

import com.mclegoman.perspective.common.data.Data;
import com.mojang.datafixers.util.Pair;
import net.darktree.simplelibs.config.SimpleConfig;

public class ExperimentalConfig {
	protected static final String ID = Data.version.getID() + "-experimental";
	protected static SimpleConfig CONFIG;
	protected static ConfigProvider CONFIG_PROVIDER;
	protected static boolean SUPER_SECRET_SETTINGS_LIST;
	protected static boolean SUPER_SECRET_SETTINGS_NOTICE;
	protected static void init() {
		try {
			CONFIG_PROVIDER = new ConfigProvider();
			create();
			CONFIG = SimpleConfig.of(ID).provider(CONFIG_PROVIDER).request();
			assign();
		} catch (Exception error) {
			Data.version.getLogger().warn("{} Failed to initialize {} config: {}", Data.version.getLoggerPrefix(), ID, error);
		}
	}
	protected static void create() {
		CONFIG_PROVIDER.add(new Pair<>("super_secret_settings_list", false));
		CONFIG_PROVIDER.add(new Pair<>("super_secret_settings_notice", false));
	}
	protected static void assign() {
		SUPER_SECRET_SETTINGS_LIST = CONFIG.getOrDefault("super_secret_settings_list", false);
		SUPER_SECRET_SETTINGS_NOTICE = CONFIG.getOrDefault("super_secret_settings_notice", false);
	}
	protected static void save() {
		if (ConfigHelper.EXPERIMENTS_AVAILABLE) {
			Data.version.getLogger().info("{} Writing experimental config to file.", Data.version.getLoggerPrefix());
			CONFIG_PROVIDER.setConfig("super_secret_settings_list", SUPER_SECRET_SETTINGS_LIST);
			CONFIG_PROVIDER.setConfig("super_secret_settings_notice", SUPER_SECRET_SETTINGS_NOTICE);
			CONFIG_PROVIDER.saveConfig(ID);
		}
	}
}