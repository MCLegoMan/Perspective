/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.common.data;

import com.mclegoman.releasetypeutils.common.releasetype.RTUReleaseTranslationTypes;
import com.mclegoman.releasetypeutils.common.releasetype.RTUReleaseType;
import com.mclegoman.releasetypeutils.common.releasetype.RTUReleaseTypes;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerspectiveData {
    public static final String NAME = "Perspective";
    public static final String ID = "perspective";
    public static final Integer MAJOR_VERSION = 1;
    public static final Integer MINOR_VERSION = 2;
    public static final Integer PATCH_VERSION = 0;
    public static final RTUReleaseTypes RELEASE_TYPE = RTUReleaseTypes.ALPHA;
    public static final Integer BUILD_VERSION = 1;
    public static final Boolean IS_DEVELOPMENT = RTUReleaseType.isDevelopmentBuild(RELEASE_TYPE);
    public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + PATCH_VERSION + "-" + RTUReleaseType.releaseTypeString(RELEASE_TYPE, RTUReleaseTranslationTypes.CODE) + "." + BUILD_VERSION;
    public static final String PREFIX = "[" + NAME + " " + VERSION + "] ";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer(ID).get();
    public static boolean isModInstalled(String MOD_ID) {
        return FabricLoader.getInstance().isModLoaded(MOD_ID);
    }
    public static boolean isModInstalledVersionOrHigher(String MOD_ID, String REQUIRED_VERSION, boolean SUBSTRING) {
        try {
            if (FabricLoader.getInstance().isModLoaded(MOD_ID)) {
                String MOD_VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString();
                Version MOD_VER = Version.parse(SUBSTRING ? MOD_VERSION.substring(0, MOD_VERSION.indexOf("-")) : MOD_VERSION);
                Version REQ_VER = Version.parse(REQUIRED_VERSION);
                return MOD_VER.compareTo(REQ_VER) >= 0;
            }
        } catch (Exception error) {
            LOGGER.error(PerspectiveData.PREFIX + "Failed to check mod version for " + MOD_ID + ": {}", (Object)error);
        }
        return false;
    }
}