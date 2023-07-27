/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.data;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.time.LocalDate;
import java.time.Month;
import java.util.TimeZone;

public class PerspectiveClientData {
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    public static Identifier getLogo() {
        return PerspectiveData.IS_DEVELOPMENT ? getLogoType(true, isPride()) : getLogoType(false, isPride());
    }
    private static Identifier getLogoType(boolean development, boolean pride) {
        return development ? new Identifier(PerspectiveData.ID, (getLogoPath(pride) + "development.png")) : new Identifier(PerspectiveData.ID, (getLogoPath(pride) + "release.png"));
    }
    private static String getLogoPath(boolean pride) {
        return pride ? "textures/logo/pride/" : "textures/logo/normal/";
    }
    private static boolean isPride() {
        if ((boolean) PerspectiveConfigHelper.getConfig("force_pride")) return true;
        else {
            LocalDate date = LocalDate.now(TimeZone.getTimeZone("GMT+12").toZoneId());
            return date.getMonth() == Month.JUNE || date.getMonth() == Month.JULY && date.getDayOfMonth() <= 2;
        }
    }
}