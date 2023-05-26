package com.mclegoman.perspective;

import com.mclegoman.perspective.data.PerspectiveData;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerspectiveServer implements ModInitializer {
    @Override
    public void onInitialize() {
        PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Perspective is designed for clients and has no server functionality.");
    }
}