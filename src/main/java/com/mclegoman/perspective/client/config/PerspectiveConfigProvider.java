/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.common.data.PerspectiveData;
import com.mclegoman.simplefabric.fabric_simplelibs.simple_config.SimpleConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Pair;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class PerspectiveConfigProvider implements SimpleConfig.DefaultConfig {
    private String CONTENTS = "";
    public List<Pair> getConfigList() {
        return CONFIG_LIST;
    }
    private List<Pair> CONFIG_LIST = new ArrayList<>();
    public void add(Pair<String, ?> keyValuePair) {
        CONFIG_LIST.add(keyValuePair);
        getContents();
    }
    @Override
    public String get(String namespace) {
        return CONTENTS;
    }
    public void getContents() {
        String contents = ("#" + PerspectiveData.ID + " properties file\n");
        for (Pair<String, ?> option : CONFIG_LIST) {
            contents += option.getLeft() + "=" + option.getRight() + "\n";
        }
        CONTENTS = contents;
    }
    public void write_to_file(String KEY_NAME, Object KEY_VALUE) {
        try {
            List<Pair> NEW_CONFIG_LIST = this.CONFIG_LIST;
            for (Pair<String, ?> key : NEW_CONFIG_LIST) {
                String KEY_FIRST = key.getLeft();
                int KEY_INDEX = NEW_CONFIG_LIST.indexOf(key);
                if (KEY_FIRST.equals(KEY_NAME)) {
                    NEW_CONFIG_LIST.set(KEY_INDEX, new Pair(KEY_NAME, KEY_VALUE));
                }
            }
            CONFIG_LIST = NEW_CONFIG_LIST;
            getContents();
            PrintWriter writer = new PrintWriter(FabricLoader.getInstance().getConfigDir().resolve(PerspectiveData.ID + ".properties").toFile());
            writer.write(CONTENTS);
            writer.close();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + e.getLocalizedMessage());
        }
    }
}