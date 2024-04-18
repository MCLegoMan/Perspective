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
import net.darktree.simplelibs.config.SimpleConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ConfigProvider implements SimpleConfig.DefaultConfig {
	private String CONTENTS = "";
	private List<Couple<String, ?>> CONFIG_LIST = new ArrayList<>();
	public List<Couple<String, ?>> getConfigList() {
		return CONFIG_LIST;
	}
	public void add(Couple<String, ?> keyValueSet) {
		try {
			CONFIG_LIST.add(keyValueSet);
		} catch (Exception error) {
			Data.version.getLogger().warn("{} Failed to add {} config value: {}", Data.version.getLoggerPrefix(), keyValueSet, error);
		}
	}
	@Override
	public String get(String namespace) {
		return CONTENTS;
	}
	public void setContents(String ID) {
		StringBuilder contents = new StringBuilder(Translation.getString("#{}.properties file\n", ID));
		for (Couple<String, ?> option : CONFIG_LIST) {
			contents.append(option.getFirst()).append("=").append(option.getSecond()).append("\n");
		}
		CONTENTS = contents.toString();
	}
	public void setConfig(String KEY_NAME, Object KEY_VALUE) {
		try {
			List<Couple<String, ?>> NEW_CONFIG_LIST = this.CONFIG_LIST;
			for (Couple<String, ?> key : NEW_CONFIG_LIST) {
				int KEY_INDEX = NEW_CONFIG_LIST.indexOf(key);
				if (key.getFirst().equals(KEY_NAME)) {
					NEW_CONFIG_LIST.set(KEY_INDEX, new Couple<>(KEY_NAME, KEY_VALUE));
				}
			}
			CONFIG_LIST = NEW_CONFIG_LIST;
		} catch (Exception error) {
			Data.version.getLogger().warn("{} Failed to set {} config value: {}", Data.version.getLoggerPrefix(), KEY_NAME, error);
		}
	}
	public void saveConfig(String ID) {
		try {
			setContents(ID);
			PrintWriter writer = new PrintWriter(FabricLoader.getInstance().getConfigDir().resolve(ID + ".properties").toFile(), StandardCharsets.UTF_8);
			writer.write(CONTENTS);
			writer.close();
		} catch (Exception error) {
			Data.version.getLogger().warn("{} Failed to save {} config: {}", Data.version.getLoggerPrefix(), ID, error);
		}
	}
}