/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.april_fools_prank;

import com.mclegoman.luminance.common.util.DateHelper;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

import java.time.LocalDate;
import java.time.Month;

public class AprilFoolsPrank {
	private static boolean seenWarning;
	public static void init() {
		try {
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new AprilFoolsPrankDataLoader());
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize april fools prank: {}", error));
		}
	}
	public static void tick() {
		boolean shouldSave = false;
		if (!seenWarning && ClientData.minecraft.world != null) {
			if ((boolean) ConfigHelper.getConfig("allow_april_fools") && isAprilFools()) {
				if (!(boolean) ConfigHelper.getWarningConfig("prank")) {
					ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.tutorial.prank.title")}), Translation.getTranslation(Data.version.getID(), "toasts.tutorial.prank.description", new Object[]{KeyBindingHelper.getBoundKeyOf(Keybindings.OPEN_CONFIG).getLocalizedText()}), 280, Toast.Type.TUTORIAL));
					ConfigHelper.setWarningConfig("prank", true);
					shouldSave = true;
					seenWarning = true;
				}
			} else {
				if ((boolean) ConfigHelper.getWarningConfig("prank")) {
					ConfigHelper.setWarningConfig("prank", false);
					shouldSave = true;
				}
			}
		}
		if (shouldSave) ConfigHelper.saveConfig(false);
	}
	public static boolean isAprilFools() {
		LocalDate date = DateHelper.getDate();
		return isForceAprilFools() || (date.getMonth() == Month.APRIL && date.getDayOfMonth() <= 2);
	}
	public static boolean isForceAprilFools() {
		return (boolean)ConfigHelper.getConfig("force_april_fools");
	}
	public static int getAprilFoolsIndex(long getLeastSignificantBits, int registrySize) {
		// We add the current year to the player's uuid, so they get a different skin each year.
		return Math.floorMod(getLeastSignificantBits + DateHelper.getDate().getYear(), registrySize);
	}
}