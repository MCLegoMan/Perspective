/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.common.data.PerspectiveData;
import com.mclegoman.simplefabric.fabric_simplelibs.simple_config.SimpleConfig;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class PerspectiveExperimentalConfig {
    protected static final String ID = PerspectiveData.ID + "-experimental";
    protected static SimpleConfig CONFIG;
    protected static PerspectiveConfigProvider CONFIG_PROVIDER;
    protected static void init() {
        CONFIG_PROVIDER = new PerspectiveConfigProvider();
        create();
        CONFIG = SimpleConfig.of(ID).provider(CONFIG_PROVIDER).request();
        assign();
    }
    protected static void create() {
    }
    protected static void assign() {
    }
    protected static void save() {
    }
}