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
import com.google.gson.JsonParser;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.UpdateCheckerScreen;
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.common.data.Version;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.minecraft.SharedConstants;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UpdateChecker {
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
				if (!ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "detect_update_channel").equals("none")) {
					Data.version.sendToLog(Helper.LogType.INFO, "Checking for new updates...");
					Data.version.sendToLog(Helper.LogType.INFO, Translation.getString("Current Version: {}", Data.version.getFriendlyString()));
					Data.version.sendToLog(Helper.LogType.INFO, Translation.getString("Update Channel: {}", ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "detect_update_channel")));
					Data.version.sendToLog(Helper.LogType.INFO, Translation.getString("Minecraft Version: {}", SharedConstants.getGameVersion().getName()));
					JsonArray apiDataVersion = (JsonArray) getModrinthData(Data.version.getModrinthID(), "version");
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
								Helper.ReleaseType type = Helper.stringToType(version_number.substring(6, version_number.lastIndexOf(".")));
								int build = Integer.parseInt(version_number.substring((version_number.lastIndexOf(".") + 1)));
								apiVersion = new Version(currentVersion.getName(), currentVersion.getID(), major, minor, patch, type, build, Data.version.getModrinthID());
								if (apiVersion.compareTo(currentVersion) > 0) {
									if (ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "detect_update_channel").equals("alpha")) {
										if (apiVersion.getType().equals(Helper.ReleaseType.ALPHA) || apiVersion.getType().equals(Helper.ReleaseType.BETA) || apiVersion.getType().equals(Helper.ReleaseType.RELEASE_CANDIDATE) || apiVersion.getType().equals(Helper.ReleaseType.RELEASE)) {
											newerVersionFound = true;
											String version_id = JsonHelper.getString(version_obj, "version_number");
											if (!version_id.contains("-"))
												version_id = version_id.replace("+", "-release.1+");
											latestVersionFound = version_id;
											downloadLink = "https://modrinth.com/mod/mclegoman-perspective/version/" + JsonHelper.getString(version_obj, "version_number");
										}
									} else if (ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "detect_update_channel").equals("beta")) {
										if (apiVersion.getType().equals(Helper.ReleaseType.BETA) || apiVersion.getType().equals(Helper.ReleaseType.RELEASE_CANDIDATE) || apiVersion.getType().equals(Helper.ReleaseType.RELEASE)) {
											newerVersionFound = true;
											String version_id = JsonHelper.getString(version_obj, "version_number");
											if (!version_id.contains("-"))
												version_id = version_id.replace("+", "-release.1+");
											latestVersionFound = version_id;
											downloadLink = "https://modrinth.com/mod/mclegoman-perspective/version/" + JsonHelper.getString(version_obj, "version_number");
										}
									} else {
										if (apiVersion.getType().equals(Helper.ReleaseType.RELEASE)) {
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
									Data.version.sendToLog(Helper.LogType.INFO, Translation.getString("A newer version of {} was found using Modrinth API: {}", currentVersion.getName(), apiVersion.getFriendlyString()));
									break;
								}
							}
						}
						if (!compatible_version) {
							Data.version.sendToLog(Helper.LogType.INFO, Translation.getString("Could not find a compatible version of {} using Modrinth API.", currentVersion.getName()));
						} else {
							if (!newerVersionFound) Data.version.sendToLog(Helper.LogType.INFO, Translation.getString("You are already running the latest version of {}: {}", currentVersion.getName(), currentVersion.getFriendlyString()));
						}
					}
				}
			} catch (Exception error) {
				Data.version.sendToLog(Helper.LogType.INFO, Translation.getString("Failed to check for updates using Modrinth API: {}", error));
			}
			updateCheckerComplete = true;
		});
		if (newerVersionFound) {
			if (!seenUpdateToast) {
				ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.update.title")}), Translation.getTranslation(Data.version.getID(), "toasts.update.description", new Object[]{UpdateChecker.latestVersionFound}), 280, Toast.Type.INFO));
				seenUpdateToast = true;
			}
		}
	}
	private static JsonElement getModrinthData(String project_id, String request) {
		try {
			Data.version.sendToLog(Helper.LogType.INFO, "Getting data from Modrinth API...");
			URL url = Objects.equals(request, "") ? new URI("https://api.modrinth.com/v2/project/" + project_id).toURL() : new URI("https://api.modrinth.com/v2/project/" + project_id + "/" + request).toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder jsonContent = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					jsonContent.append(line);
				}
				return JsonParser.parseString(jsonContent.toString());
			} catch (Exception error) {
				Data.version.sendToLog(Helper.LogType.INFO, Translation.getString("Failed to read data from Modrinth API: {}", error));
			}
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.INFO, Translation.getString("Failed to get data from Modrinth API: {}", error));
		}
		return null;
	}
	public static String nextUpdateChannel() {
		List<String> updateChannels = Arrays.stream(detectUpdateChannels).toList();
		return updateChannels.contains((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "detect_update_channel")) ? detectUpdateChannels[(updateChannels.indexOf((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "detect_update_channel")) + 1) % detectUpdateChannels.length] : detectUpdateChannels[0];
	}
}