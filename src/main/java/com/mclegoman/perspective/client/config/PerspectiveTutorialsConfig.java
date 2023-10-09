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
public class PerspectiveTutorialsConfig {
    protected static final String ID = PerspectiveData.PERSPECTIVE_VERSION.getID() + "-tutorials";
    protected static SimpleConfig CONFIG;
    protected static PerspectiveConfigProvider CONFIG_PROVIDER;
    protected static boolean SUPER_SECRET_SETTINGS;
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
        CONFIG_PROVIDER.add(new Pair<>("super_secret_settings", false));
    }
    protected static void assign() {
        SUPER_SECRET_SETTINGS = CONFIG.getOrDefault("super_secret_settings", false);
    }
    protected static void save() {
        PerspectiveData.PERSPECTIVE_VERSION.getLogger().info("{} Writing tutorial config to file.", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix());
        CONFIG_PROVIDER.setConfig("super_secret_settings", SUPER_SECRET_SETTINGS);
    }
}