/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.config;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mojang.datafixers.util.Pair;
import net.darktree.simplelibs.config.SimpleConfig;

public class WarningsConfig {
	protected static final String ID = Data.version.getID() + "-warnings";
	protected static SimpleConfig CONFIG;
	protected static ConfigProvider CONFIG_PROVIDER;
	protected static boolean PHOTOSENSITIVITY;
	protected static boolean PRANK;
	protected static void init() {
		try {
			CONFIG_PROVIDER = new ConfigProvider();
			create();
			CONFIG = SimpleConfig.of(ID).provider(CONFIG_PROVIDER).request();
			assign();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize {} config: {}", ID, error));
		}
	}
	protected static void create() {
		CONFIG_PROVIDER.add(new Pair<>("photosensitivity", false));
		CONFIG_PROVIDER.add(new Pair<>("prank", false));
	}
	protected static void assign() {
		PHOTOSENSITIVITY = CONFIG.getOrDefault("photosensitivity", false);
		PRANK = CONFIG.getOrDefault("prank", false);
	}
	protected static void save() {
		Data.version.sendToLog(LogType.INFO, Translation.getString("Writing {} config to file.", ID));
		CONFIG_PROVIDER.setConfig("photosensitivity", PHOTOSENSITIVITY);
		CONFIG_PROVIDER.setConfig("prank", PRANK);
		CONFIG_PROVIDER.saveConfig(ID);
	}
}