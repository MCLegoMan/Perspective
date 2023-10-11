/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.common.data;

import com.mclegoman.perspective.common.version.PerspectiveVersion;
import com.mclegoman.releasetypeutils.common.releasetype.RTUReleaseTypes;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import org.apache.commons.lang3.StringUtils;

public class PerspectiveData {
    public static final PerspectiveVersion PERSPECTIVE_VERSION = new PerspectiveVersion("Perspective", "perspective", 1, 2, 0, RTUReleaseTypes.ALPHA, 5);
    public static boolean isModInstalled(String MOD_ID) {
        return FabricLoader.getInstance().isModLoaded(MOD_ID);
    }
    public static boolean isModInstalledVersionOrHigher(String MOD_ID, String REQUIRED_VERSION, boolean SUBSTRING) {
        try {
            if (FabricLoader.getInstance().isModLoaded(MOD_ID)) {
                return checkModVersion(FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString(), REQUIRED_VERSION, SUBSTRING);
            }
        } catch (Exception error) {
            PERSPECTIVE_VERSION.getLogger().error(PERSPECTIVE_VERSION.getLoggerPrefix() + "Failed to check mod version for " + MOD_ID + ": {}", (Object)error);
        }
        return false;
    }

    public static boolean checkModVersion(String CURRENT_VERSION, String REQUIRED_VERSION, boolean SUBSTRING) {
        try {
            String modVersion = SUBSTRING ? StringUtils.substringBefore(CURRENT_VERSION, "-") : CURRENT_VERSION;
            Version MOD_VER = Version.parse(modVersion);
            Version REQ_VER = Version.parse(REQUIRED_VERSION);
            return REQ_VER.compareTo(MOD_VER) <= 0;
        } catch (Exception error) {
            PERSPECTIVE_VERSION.getLogger().error(PERSPECTIVE_VERSION.getLoggerPrefix() + "Failed to check mod version!");
        }
        return false;
    }
}