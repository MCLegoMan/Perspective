/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.luminance.common.util.Helper;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.luminance.common.util.ReleaseType;
import com.mclegoman.luminance.common.util.Version;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.UpdateCheckerScreen;
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.SharedConstants;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Util;

import java.util.Arrays;
import java.util.List;

public class Update extends com.mclegoman.luminance.client.util.Update {
	public static final String[] detectUpdateChannels = new String[]{"release", "beta", "alpha", "none"};
	public static Version apiVersion;
	public static boolean seenUpdateToast;
	public static boolean updateCheckerComplete;
	public static boolean newerVersionFound;
	public static String latestVersionFound = Data.version.getFriendlyString();
	public static String downloadLink;

	public static void checkForUpdates(Version currentVersion, boolean showScreen) {
		if (showScreen) ClientData.minecraft.setScreen(new UpdateCheckerScreen(ClientData.minecraft.currentScreen));
		checkForUpdates(currentVersion);
	}
	public static void checkForUpdates(Version currentVersion) {
		Util.getMainWorkerExecutor().execute(() -> {
			updateCheckerComplete = false;
			newerVersionFound = false;
			try {
				if (!ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "detect_update_channel").equals("none") && currentVersion.hasModrinthID()) {
					currentVersion.sendToLog(LogType.INFO, "Checking for new updates...");
					currentVersion.sendToLog(LogType.INFO, Translation.getString("Current Version: {}", currentVersion.getFriendlyString()));
					currentVersion.sendToLog(LogType.INFO, Translation.getString("Update Channel: {}", ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "detect_update_channel")));
					currentVersion.sendToLog(LogType.INFO, Translation.getString("Minecraft Version: {}", SharedConstants.getGameVersion().getName()));
					JsonArray apiDataVersion = (JsonArray) getModrinthData(currentVersion.getModrinthID(), "version");
					if (apiDataVersion != null) {
						boolean compatible_version = false;
						for (JsonElement version : apiDataVersion) {
							JsonObject version_obj = (JsonObject) version;
							JsonArray game_versions = JsonHelper.getArray(version_obj, "game_versions");
							for (JsonElement game_version : game_versions) {
								if (game_version.getAsString().equalsIgnoreCase(SharedConstants.getGameVersion().getName())) {
									compatible_version = true;
									break;
								}
							}
							if (compatible_version) {
								String version_number = JsonHelper.getString(version_obj, "version_number");
								int indexOfPlus = version_number.indexOf("+");
								if (indexOfPlus != -1) version_number = version_number.substring(0, indexOfPlus);
								if (!version_number.contains("-")) version_number = version_number + "-release.1";
								int major = Integer.parseInt(version_number.substring(0, 1));
								int minor = Integer.parseInt(version_number.substring(2, 3));
								int patch = Integer.parseInt(version_number.substring(4, 5));
								ReleaseType type = Helper.stringToType(version_number.substring(6, version_number.lastIndexOf(".")));
								int build = Integer.parseInt(version_number.substring((version_number.lastIndexOf(".") + 1)));
								apiVersion = Version.create(currentVersion.getName(), currentVersion.getID(), major, minor, patch, type, build, currentVersion.getModrinthID());
								if (apiVersion.compareTo(currentVersion) > 0) {
									if (ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "detect_update_channel").equals("alpha")) {
										if (apiVersion.getType().equals(ReleaseType.ALPHA) || apiVersion.getType().equals(ReleaseType.BETA) || apiVersion.getType().equals(ReleaseType.RELEASE_CANDIDATE) || apiVersion.getType().equals(ReleaseType.RELEASE)) {
											newerVersionFound = true;
											String version_id = JsonHelper.getString(version_obj, "version_number");
											if (!version_id.contains("-"))
												version_id = version_id.replace("+", "-release.1+");
											latestVersionFound = version_id;
											downloadLink = "https://modrinth.com/mod/mclegoman-perspective/version/" + JsonHelper.getString(version_obj, "version_number");
										}
									} else if (ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "detect_update_channel").equals("beta")) {
										if (apiVersion.getType().equals(ReleaseType.BETA) || apiVersion.getType().equals(ReleaseType.RELEASE_CANDIDATE) || apiVersion.getType().equals(ReleaseType.RELEASE)) {
											newerVersionFound = true;
											String version_id = JsonHelper.getString(version_obj, "version_number");
											if (!version_id.contains("-"))
												version_id = version_id.replace("+", "-release.1+");
											latestVersionFound = version_id;
											downloadLink = "https://modrinth.com/mod/mclegoman-perspective/version/" + JsonHelper.getString(version_obj, "version_number");
										}
									} else {
										if (apiVersion.getType().equals(ReleaseType.RELEASE)) {
											newerVersionFound = true;
											String version_id = JsonHelper.getString(version_obj, "version_number");
											if (!version_id.contains("-"))
												version_id = version_id.replace("+", "-release.1+");
											latestVersionFound = version_id;
											downloadLink = "https://modrinth.com/mod/mclegoman-perspective/version/" + JsonHelper.getString(version_obj, "version_number");
										}
									}
								}
								if (newerVersionFound) {
									currentVersion.sendToLog(LogType.INFO, Translation.getString("A newer version of {} was found using Modrinth API: {}", currentVersion.getName(), apiVersion.getFriendlyString()));
									break;
								}
							}
						}
						if (!compatible_version) {
							currentVersion.sendToLog(LogType.INFO, Translation.getString("Could not find a compatible version of {} using Modrinth API.", currentVersion.getName()));
						} else {
							if (!newerVersionFound) currentVersion.sendToLog(LogType.INFO, Translation.getString("You are already running the latest version of {}: {}", currentVersion.getName(), currentVersion.getFriendlyString()));
						}
					}
				}
			} catch (Exception error) {
				currentVersion.sendToLog(LogType.INFO, Translation.getString("Failed to check for updates using Modrinth API: {}", error));
			}
			updateCheckerComplete = true;
		});
		if (newerVersionFound) {
			if (!seenUpdateToast) {
				ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(currentVersion.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.update.title")}), Translation.getTranslation(Data.version.getID(), "toasts.update.description", new Object[]{Update.latestVersionFound}), 280, Toast.Type.INFO));
				seenUpdateToast = true;
			}
		}
	}
	public static String nextUpdateChannel() {
		List<String> updateChannels = Arrays.stream(detectUpdateChannels).toList();
		return updateChannels.contains((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "detect_update_channel")) ? detectUpdateChannels[(updateChannels.indexOf((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "detect_update_channel")) + 1) % detectUpdateChannels.length] : detectUpdateChannels[0];
	}
}