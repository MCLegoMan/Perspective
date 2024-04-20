/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.config;

import com.mclegoman.luminance.config.ConfigProvider;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.luminance.common.util.Couple;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.darktree.simplelibs.config.SimpleConfig;

public class TutorialsConfig {
	protected static final String id = Data.version.getID() + "-tutorials";
	protected static SimpleConfig config;
	protected static ConfigProvider configProvider;
	protected static boolean superSecretSettings;

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
		configProvider.add(new Couple<>("super_secret_settings", false));
	}

	protected static void assign() {
		superSecretSettings = config.getOrDefault("super_secret_settings", false);
	}

	protected static void save() {
		Data.version.sendToLog(Helper.LogType.INFO,"Writing tutorial config to file.");
		configProvider.setConfig("super_secret_settings", superSecretSettings);
		configProvider.saveConfig(Data.version, id);
	}
}