/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.config;

import com.mclegoman.luminance.common.data.Data;
import com.mclegoman.luminance.common.util.Couple;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.darktree.simplelibs.config.SimpleConfig;

public class Config {
	protected static final String id = Data.version.getID();
	protected static SimpleConfig config;
	protected static ConfigProvider configProvider;
	protected static int alphaLevel;
	protected static int configVersion;
	protected static void init() {
		try {
			configProvider = new ConfigProvider();
			create();
			config = SimpleConfig.of(id).provider(configProvider).request();
			assign();
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.WARN, Translation.getString("Failed to initialize {} config: {}", id, error));
		}
	}
	protected static void create() {
		configProvider.add(new Couple<>("alpha_level", ConfigHelper.defaultAlphaLevel));
		configProvider.add(new Couple<>("config_version", ConfigHelper.defaultConfigVersion));
	}
	protected static void assign() {
		alphaLevel = config.getOrDefault("alpha_level", ConfigHelper.defaultAlphaLevel);
		configVersion = config.getOrDefault("config_version", ConfigHelper.defaultConfigVersion);
	}
	protected static void save() {
		Data.version.sendToLog(Helper.LogType.INFO,"Writing config to file.");
		configProvider.setConfig("alpha_level", alphaLevel);
		configProvider.setConfig("config_version", ConfigHelper.defaultConfigVersion);
		configProvider.saveConfig(Data.version, id);
	}
}