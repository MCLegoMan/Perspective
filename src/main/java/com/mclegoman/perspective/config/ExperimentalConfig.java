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

public class ExperimentalConfig {
	protected static final String id = Data.version.getID() + "-experimental";
	protected static SimpleConfig config;
	protected static ConfigProvider configProvider;
	protected static boolean improvedShaderRenderer;

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
		configProvider.add(new Couple<>("improved_shader_renderer", false));
	}

	protected static void assign() {
		improvedShaderRenderer = config.getOrDefault("improved_shader_renderer", false);
	}

	protected static void save() {
		if (ConfigHelper.experimentsAvailable) {
			Data.version.sendToLog(Helper.LogType.INFO,"Writing experimental config to file.");
			configProvider.setConfig("improved_shader_renderer", improvedShaderRenderer);
			configProvider.saveConfig(id);
		}
	}
}