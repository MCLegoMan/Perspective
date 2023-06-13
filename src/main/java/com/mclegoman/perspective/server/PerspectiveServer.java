package com.mclegoman.perspective.server;

import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.DedicatedServerModInitializer;

public class PerspectiveServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Perspective does not have any server functionality.");
    }
}