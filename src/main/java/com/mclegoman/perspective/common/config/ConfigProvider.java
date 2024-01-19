/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.common.config;

import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.simplefabriclibs.simple_config.SimpleConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ConfigProvider implements SimpleConfig.DefaultConfig {
	private String CONTENTS = "";
	private List<ConfigOption<String, ?>> CONFIG_LIST = new ArrayList<>();

	public List<ConfigOption<String, ?>> getConfigList() {
		return CONFIG_LIST;
	}

	public void add(ConfigOption<String, ?> keyValueSet) {
		try {
			CONFIG_LIST.add(keyValueSet);
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to add {} config value: {}", Data.VERSION.getLoggerPrefix(), keyValueSet, error);
		}
	}

	@Override
	public String get(String namespace) {
		return CONTENTS;
	}

	public void setContents(String ID) {
		StringBuilder contents = new StringBuilder(Translation.getString("#{}.properties file\n", ID));
		for (ConfigOption<String, ?> option : CONFIG_LIST) {
			contents.append(option.getOption()).append("=").append(option.getValue()).append("\n");
		}
		CONTENTS = contents.toString();
	}

	public void setConfig(String KEY_NAME, Object KEY_VALUE) {
		try {
			List<ConfigOption<String, ?>> NEW_CONFIG_LIST = this.CONFIG_LIST;
			for (ConfigOption<String, ?> key : NEW_CONFIG_LIST) {
				String KEY_FIRST = key.getOption();
				int KEY_INDEX = NEW_CONFIG_LIST.indexOf(key);
				if (KEY_FIRST.equals(KEY_NAME)) {
					NEW_CONFIG_LIST.set(KEY_INDEX, new ConfigOption<>(KEY_NAME, KEY_VALUE));
				}
			}
			CONFIG_LIST = NEW_CONFIG_LIST;
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to set {} config value: {}", Data.VERSION.getLoggerPrefix(), KEY_NAME, error);
		}
	}

	public void saveConfig(String ID) {
		try {
			setContents(ID);
			PrintWriter writer = new PrintWriter(FabricLoader.getInstance().getConfigDir().resolve(ID + ".properties").toFile(), StandardCharsets.UTF_8);
			writer.write(CONTENTS);
			writer.close();
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to save {} config: {}", Data.VERSION.getLoggerPrefix(), ID, error);
		}
	}
}