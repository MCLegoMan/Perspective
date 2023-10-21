/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.april_fools_prank;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.toasts.PerspectiveToast;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;

import java.time.LocalDate;
import java.time.Month;
import java.util.TimeZone;

public class PerspectiveAprilFoolsPrank {
    private static boolean SEEN_WARNING;
    public static void init() {
        try {
            ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveAprilFoolsPrankDataLoader());
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize april fools prank: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    public static void tick(MinecraftClient client) {
        boolean save = false;
        if ((boolean) PerspectiveConfigHelper.getWarningConfig("prank") && !(isPrankEnabled() && isAprilFools())) {
            PerspectiveConfigHelper.setWarningConfig("prank", false);
            save = true;
        } else {
            if (!SEEN_WARNING && client.world != null) {
                PerspectiveClientData.CLIENT.getToastManager().add(new PerspectiveToast(PerspectiveTranslation.getTranslation("toasts.title", new Object[]{PerspectiveTranslation.getTranslation("name"), PerspectiveTranslation.getTranslation("toasts.tutorial.prank.title")}), PerspectiveTranslation.getTranslation("toasts.tutorial.prank.description", new Object[]{KeyBindingHelper.getBoundKeyOf(PerspectiveKeybindings.OPEN_CONFIG).getLocalizedText()}), 280, PerspectiveToast.Type.TUTORIAL));
                PerspectiveConfigHelper.setWarningConfig("prank", true);
                SEEN_WARNING = true;
                save = true;
            }
        }
        if (save) PerspectiveConfigHelper.saveConfig(false);
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