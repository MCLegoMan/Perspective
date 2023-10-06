/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.data;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.time.LocalDate;
import java.time.Month;
import java.util.Random;
import java.util.TimeZone;

public class PerspectiveClientData {
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    public static final String[] PRIDE_LOGOS = new String[]{"pride", "trans", "bi", "pan"};
    public static final int PRIDE_LOGO = new Random().nextInt(PRIDE_LOGOS.length);
    public static Identifier getLogo() {
        return PerspectiveData.PERSPECTIVE_VERSION.isDevelopmentBuild() ? getLogoType(PerspectiveData.PERSPECTIVE_VERSION.getID(), true, isPride()) : getLogoType(PerspectiveData.PERSPECTIVE_VERSION.getID(), false, isPride());
    }
    public static Identifier getLogoType(String namespace, boolean development, boolean pride) {
        return development ? new Identifier(namespace, (getLogoPath(pride) + "development.png")) : new Identifier(namespace, (getLogoPath(pride) + "release.png"));
    }
    public static String getPrideLogoType() {
        return ((boolean)PerspectiveConfigHelper.getConfig("force_pride_type")) ? PRIDE_LOGOS[(int)PerspectiveConfigHelper.getConfig("force_pride_type_index")] : PRIDE_LOGOS[PRIDE_LOGO];
    }
    public static String getLogoPath(boolean pride) {
        return pride ? "textures/gui/logo/pride/" + getPrideLogoType() + "/" : "textures/gui/logo/normal/";
    }
    public static boolean isPride() {
        if ((boolean) PerspectiveConfigHelper.getConfig("force_pride")) return true;
        else {
            LocalDate date = LocalDate.now(TimeZone.getTimeZone("GMT+12").toZoneId());
            return date.getMonth() == Month.JUNE || date.getMonth() == Month.JULY && date.getDayOfMonth() <= 2;
        }
    }
}