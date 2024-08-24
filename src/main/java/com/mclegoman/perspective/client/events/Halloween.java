/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.events;

import com.mclegoman.luminance.common.util.DateHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.keybindings.Keybindings;
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigHelper;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import java.time.LocalDate;
import java.time.Month;

public class Halloween {
	private static boolean seenWarning;
	public static void tick() {
		boolean shouldSave = false;
		if (!seenWarning && ClientData.minecraft.world != null) {
			if (isHalloween()) {
				if (!(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.warning, "halloween")) {
					ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.tutorial.halloween.title")}), Translation.getTranslation(Data.version.getID(), "toasts.tutorial.halloween.description", new Object[]{KeyBindingHelper.getBoundKeyOf(Keybindings.openConfig).getLocalizedText()}), 280, Toast.Type.TUTORIAL));
					ConfigHelper.setConfig(ConfigHelper.ConfigType.warning, "halloween", true);
					shouldSave = true;
					seenWarning = true;
				}
			} else {
				if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.warning, "halloween")) {
					ConfigHelper.setConfig(ConfigHelper.ConfigType.warning, "halloween", false);
					shouldSave = true;
				}
			}
		}
		if (shouldSave) ConfigHelper.saveConfig();
	}
	public static boolean isHalloween() {
		if (!(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "allow_halloween")) return false;
		else {
			LocalDate date = DateHelper.getDate();
			return ((date.getMonth() == Month.OCTOBER && date.getDayOfMonth() == 31) || (date.getMonth() == Month.NOVEMBER && date.getDayOfMonth() == 1)) || isForceHalloween();
		}
	}
	public static boolean isForceHalloween() {
		return (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "force_halloween");
	}
}