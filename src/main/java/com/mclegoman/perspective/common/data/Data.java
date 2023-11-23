/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.common.data;

import com.mclegoman.perspective.common.version.Version;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.StringUtils;

public class Data {
    public static final Version PERSPECTIVE_VERSION = new Version("Perspective", "perspective", 1, 2, 0, Helper.ReleaseType.BETA, 1);
    public static boolean isModInstalled(String MOD_ID) {
        return FabricLoader.getInstance().isModLoaded(MOD_ID);
    }
    public static boolean isModInstalledVersionOrHigher(String MOD_ID, String REQUIRED_VERSION, boolean SUBSTRING) {
        try {
            if (isModInstalled(MOD_ID)) {
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
            net.fabricmc.loader.api.Version MOD_VER = net.fabricmc.loader.api.Version.parse(modVersion);
            net.fabricmc.loader.api.Version REQ_VER = net.fabricmc.loader.api.Version.parse(REQUIRED_VERSION);
            return REQ_VER.compareTo(MOD_VER) <= 0;
        } catch (Exception error) {
            PERSPECTIVE_VERSION.getLogger().error(PERSPECTIVE_VERSION.getLoggerPrefix() + "Failed to check mod version!");
        }
        return false;
    }
}