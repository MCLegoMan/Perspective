/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.common.data.PerspectiveData;
import com.mclegoman.simplefabriclibs.simple_config.SimpleConfig;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PerspectiveWarningsConfig {
    protected static final String ID = PerspectiveData.PERSPECTIVE_VERSION.getID() + "-warnings";
    protected static SimpleConfig CONFIG;
    protected static PerspectiveConfigProvider CONFIG_PROVIDER;
    protected static boolean PHOTOSENSITIVITY;
    protected static void init() {
        try {
            CONFIG_PROVIDER = new PerspectiveConfigProvider();
            create();
            CONFIG = SimpleConfig.of(ID).provider(CONFIG_PROVIDER).request();
            assign();
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize {} config: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), ID, error);
        }
    }
    protected static void create() {
        CONFIG_PROVIDER.add(new Pair<>("photosensitivity", false));
    }
    protected static void assign() {
        PHOTOSENSITIVITY = CONFIG.getOrDefault("photosensitivity", false);
    }
    protected static void save() {
        PerspectiveData.PERSPECTIVE_VERSION.getLogger().info("{} Writing warning config to file.", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix());
        CONFIG_PROVIDER.setConfig("photosensitivity", PHOTOSENSITIVITY);
        CONFIG_PROVIDER.saveConfig(ID);
    }
}