package com.mclegoman.perspective;

import com.mclegoman.perspective.data.PerspectiveData;
import net.fabricmc.api.DedicatedServerModInitializer;

public class PerspectiveServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Perspective is designed for clients and has no server functionality.");
    }
}