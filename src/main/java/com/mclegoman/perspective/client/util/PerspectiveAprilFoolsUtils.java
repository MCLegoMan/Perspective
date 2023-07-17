/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.dataloader.PerspectiveAprilFoolsDataLoader;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

import java.time.LocalDate;
import java.time.Month;
import java.util.TimeZone;

public class PerspectiveAprilFoolsUtils {
    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveAprilFoolsDataLoader());
    }
    public static boolean isAprilFools() {
        if ((boolean)PerspectiveConfigHelper.getConfig("force_april_fools")) return true;
        else {
            LocalDate date = LocalDate.now(TimeZone.getTimeZone("GMT+12").toZoneId());
            return date.getMonth() == Month.APRIL && date.getDayOfMonth() <= 2;
        }
    }
    public static boolean isPrankEnabled() {
        return (boolean)PerspectiveConfigHelper.getConfig("allow_april_fools");
    }
}