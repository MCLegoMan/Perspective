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
import com.mclegoman.luminance.common.util.Helper;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.luminance.common.util.ReleaseType;
import com.mclegoman.luminance.common.util.Version;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.toasts.Toast;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.SharedConstants;
import net.minecraft.util.JsonHelper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UpdateChecker {
	private static final String[] detectUpdateChannels = new String[]{"release", "beta", "alpha", "none"};
	public static Version API_VERSION;
	public static boolean SEEN_UPDATE_TOAST;
	public static boolean UPDATE_CHECKER_COMPLETE;
	public static boolean NEWER_VERSION_FOUND;
	public static String LATEST_VERSION_FOUND = Data.version.getFriendlyString();
	public static String DOWNLOAD_LINK;
	public static void tick() {
		if (!UPDATE_CHECKER_COMPLETE) {
			checkForUpdates();
			if (NEWER_VERSION_FOUND) {
				if (!SEEN_UPDATE_TOAST) {
					ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.update.title")}), Translation.getTranslation(Data.version.getID(), "toasts.update.description", new Object[]{UpdateChecker.LATEST_VERSION_FOUND}), 280, Toast.Type.INFO));
					SEEN_UPDATE_TOAST = true;
				}
			}
			UPDATE_CHECKER_COMPLETE = true;
		}
	}
	public static void checkForUpdates() {
		UPDATE_CHECKER_COMPLETE = false;
		NEWER_VERSION_FOUND = false;
		try {
			if (!ConfigHelper.getConfig("detect_update_channel").equals("none")) {
				Data.version.sendToLog(LogType.INFO, Translation.getString("Checking for updates..."));
				Data.version.sendToLog(LogType.INFO, Translation.getString("Current Version: " + Data.version.getFriendlyString()));
				Data.version.sendToLog(LogType.INFO, Translation.getString("Update Channel: " + ConfigHelper.getConfig("detect_update_channel")));
				Data.version.sendToLog(LogType.INFO, Translation.getString("Minecraft Version: " + SharedConstants.getGameVersion().getName()));
				JsonArray apiDataVersion = (JsonArray) getModrinthData("6CTGnrNg", "version");
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
							API_VERSION = Version.create("Perspective", "perspective", major, minor, patch, type, build);
							if (API_VERSION.compareTo(Data.version) > 0) {
								if (ConfigHelper.getConfig("detect_update_channel").equals("alpha")) {
									if (API_VERSION.getType().equals(ReleaseType.ALPHA) || API_VERSION.getType().equals(ReleaseType.BETA) || API_VERSION.getType().equals(ReleaseType.RELEASE_CANDIDATE) || API_VERSION.getType().equals(ReleaseType.RELEASE)) {
										NEWER_VERSION_FOUND = true;
										String version_id = JsonHelper.getString(version_obj, "version_number");
										if (!version_id.contains("-"))
											version_id = version_id.replace("+", "-release.1+");
										LATEST_VERSION_FOUND = version_id;
										DOWNLOAD_LINK = "https://modrinth.com/mod/mclegoman-perspective/version/" + JsonHelper.getString(version_obj, "version_number");
										break;
									}
								} else if (ConfigHelper.getConfig("detect_update_channel").equals("beta")) {
									if (API_VERSION.getType().equals(ReleaseType.BETA) || API_VERSION.getType().equals(ReleaseType.RELEASE_CANDIDATE) || API_VERSION.getType().equals(ReleaseType.RELEASE)) {
										NEWER_VERSION_FOUND = true;
										String version_id = JsonHelper.getString(version_obj, "version_number");
										if (!version_id.contains("-"))
											version_id = version_id.replace("+", "-release.1+");
										LATEST_VERSION_FOUND = version_id;
										DOWNLOAD_LINK = "https://modrinth.com/mod/mclegoman-perspective/version/" + JsonHelper.getString(version_obj, "version_number");
										break;
									}
								} else {
									if (API_VERSION.getType().equals(ReleaseType.RELEASE)) {
										NEWER_VERSION_FOUND = true;
										String version_id = JsonHelper.getString(version_obj, "version_number");
										if (!version_id.contains("-"))
											version_id = version_id.replace("+", "-release.1+");
										LATEST_VERSION_FOUND = version_id;
										DOWNLOAD_LINK = "https://modrinth.com/mod/mclegoman-perspective/version/" + JsonHelper.getString(version_obj, "version_number");
										break;
									}
								}
							}
						}
					}
					if (!compatible_version) {
						Data.version.sendToLog(LogType.INFO, Translation.getString("No compatible version found."));
					} else {
						if (NEWER_VERSION_FOUND) {
							Data.version.sendToLog(LogType.INFO, Translation.getString("Newer version found: " + API_VERSION.getFriendlyString()));
						} else
							Data.version.sendToLog(LogType.INFO, Translation.getString("You are already running the latest version of {}", Data.version.getName()));
					}
				}
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("{}", error));
		}
	}
	private static JsonElement getModrinthData(String project_id, String request) {
		try {
			URL url = Objects.equals(request, "") ? new URI("https://api.modrinth.com/v2/project/" + project_id).toURL() : new URI("https://api.modrinth.com/v2/project/" + project_id + "/" + request).toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				StringBuilder jsonContent = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					jsonContent.append(line);
				}
				return JsonParser.parseString(jsonContent.toString());
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to get Modrinth data: {}", error));
		}
		return null;
	}
	public static String nextUpdateChannel() {
		List<String> updateChannels = Arrays.stream(detectUpdateChannels).toList();
		return updateChannels.contains((String) ConfigHelper.getConfig("detect_update_channel")) ? detectUpdateChannels[(updateChannels.indexOf((String) ConfigHelper.getConfig("detect_update_channel")) + 1) % detectUpdateChannels.length] : detectUpdateChannels[0];
	}
}