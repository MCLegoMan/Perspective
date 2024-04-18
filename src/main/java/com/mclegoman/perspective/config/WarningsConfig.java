/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.config;

import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.common.util.Couple;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.darktree.simplelibs.config.SimpleConfig;

public class WarningsConfig {
	protected static final String id = Data.version.getID() + "-warnings";
	protected static SimpleConfig config;
	protected static ConfigProvider configProvider;
	protected static boolean photosensitivity;
	protected static boolean prank;

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
		configProvider.add(new Couple<>("photosensitivity", false));
		configProvider.add(new Couple<>("prank", false));
	}

	protected static void assign() {
		photosensitivity = config.getOrDefault("photosensitivity", false);
		prank = config.getOrDefault("prank", false);
	}

	protected static void save() {
		Data.version.sendToLog(Helper.LogType.INFO,"Writing warning config to file.");
		configProvider.setConfig("photosensitivity", photosensitivity);
		configProvider.setConfig("prank", prank);
		configProvider.saveConfig(id);
	}
}