/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.toasts.PerspectiveWarningToast;
import com.mclegoman.perspective.common.data.PerspectiveData;
import com.mclegoman.perspective.common.version.PerspectiveVersion;
import com.mclegoman.perspective.common.version.PerspectiveVersionHelper;
import com.mclegoman.releasetypeutils.common.releasetype.RTUReleaseTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.JsonHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class PerspectiveUpdateChecker {
	public static boolean UPDATE_CHECKER_COMPLETE;
	public static boolean NEWER_VERSION_FOUND;
	public static String LATEST_VERSION_FOUND = PerspectiveData.PERSPECTIVE_VERSION.getFriendlyString();
	public static String DOWNLOAD_LINK;
	public static void tick(MinecraftClient client) {
		if (!UPDATE_CHECKER_COMPLETE) {
			checkForUpdates();
			if (NEWER_VERSION_FOUND) {
				PerspectiveClientData.CLIENT.getToastManager().add(new PerspectiveWarningToast(Text.translatable("gui.perspective.toasts.title", Text.translatable("gui.perspective.name"), Text.translatable("gui.perspective.toasts.update.title")), Text.translatable("gui.perspective.toasts.update.description", PerspectiveUpdateChecker.LATEST_VERSION_FOUND), 280));
			}
			UPDATE_CHECKER_COMPLETE = true;
		}
	}
	public static void checkForUpdates() {
		NEWER_VERSION_FOUND = false;
		try {
			if (!PerspectiveConfigHelper.getConfig("detect_update_channel").equals("none")) {
				PerspectiveData.PERSPECTIVE_VERSION.getLogger().info("Checking for updates...");
				PerspectiveData.PERSPECTIVE_VERSION.getLogger().info("Current Version: " + PerspectiveData.PERSPECTIVE_VERSION.getFriendlyString());
				JsonArray apiDataVersion = (JsonArray) getModrinthData("6CTGnrNg", "version");
				if (apiDataVersion != null) {
					for (JsonElement version : apiDataVersion) {
						JsonObject version_obj = (JsonObject) version;
						JsonArray game_versions = JsonHelper.getArray(version_obj, "game_versions");
						boolean compatible_version = false;
						for (JsonElement game_version : game_versions) {
							if (game_version.getAsString().equalsIgnoreCase(PerspectiveClientData.CLIENT.getGameVersion())) {
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
							RTUReleaseTypes type = PerspectiveVersionHelper.stringToType(version_number.substring(6, version_number.lastIndexOf(".")));
							int build = Integer.parseInt(version_number.substring((version_number.lastIndexOf(".") + 1)));
							PerspectiveVersion API_VERSION = new PerspectiveVersion("Perspective", "perspective", major, minor, patch, type, build);
							if (API_VERSION.compareTo(PerspectiveData.PERSPECTIVE_VERSION) > 0) {
								if (!PerspectiveConfigHelper.getConfig("detect_update_channel").equals("alpha")) {
									if (API_VERSION.getType().equals(RTUReleaseTypes.ALPHA) || API_VERSION.getType().equals(RTUReleaseTypes.BETA) || API_VERSION.getType().equals(RTUReleaseTypes.RELEASE_CANDIDATE) || API_VERSION.getType().equals(RTUReleaseTypes.RELEASE)) {
										PerspectiveData.PERSPECTIVE_VERSION.getLogger().info("Newer version found: " + API_VERSION.getFriendlyString());
										NEWER_VERSION_FOUND = true;
										String version_id = JsonHelper.getString(version_obj, "version_number");
										if (!version_id.contains("-")) version_id = version_id.replace("+", "-release.1+");
										LATEST_VERSION_FOUND = version_id;
										DOWNLOAD_LINK = "https://modrinth.com/mod/mclegoman-perspective/version/" + JsonHelper.getString(version_obj, "version_number");
										break;
									}
								} else if (!PerspectiveConfigHelper.getConfig("detect_update_channel").equals("beta")) {
									if (API_VERSION.getType().equals(RTUReleaseTypes.BETA) || API_VERSION.getType().equals(RTUReleaseTypes.RELEASE_CANDIDATE) || API_VERSION.getType().equals(RTUReleaseTypes.RELEASE)) {
										PerspectiveData.PERSPECTIVE_VERSION.getLogger().info("Newer version found: " + API_VERSION.getFriendlyString());
										NEWER_VERSION_FOUND = true;
										String version_id = JsonHelper.getString(version_obj, "version_number");
										if (!version_id.contains("-")) version_id = version_id.replace("+", "-release.1+");
										LATEST_VERSION_FOUND = version_id;
										DOWNLOAD_LINK = "https://modrinth.com/mod/mclegoman-perspective/version/" + JsonHelper.getString(version_obj, "version_number");
										break;
									}
								} else {
									if (API_VERSION.getType().equals(RTUReleaseTypes.RELEASE)) {
										PerspectiveData.PERSPECTIVE_VERSION.getLogger().info("Newer version found: " + API_VERSION.getFriendlyString());
										NEWER_VERSION_FOUND = true;
										String version_id = JsonHelper.getString(version_obj, "version_number");
										if (!version_id.contains("-")) version_id = version_id.replace("+", "-release.1+");
										LATEST_VERSION_FOUND = version_id;
										DOWNLOAD_LINK = "https://modrinth.com/mod/mclegoman-perspective/version/" + JsonHelper.getString(version_obj, "version_number");
										break;
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception error) {
			PerspectiveData.PERSPECTIVE_VERSION.getLogger().error("{} {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
		}
	}

	private static JsonElement getModrinthData(String project_id, String request) {
		try {
			URL url = Objects.equals(request, "") ? new URL("https://api.modrinth.com/v2/project/" + project_id) : new URL("https://api.modrinth.com/v2/project/" + project_id + "/" + request);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void cycleDetectUpdateChannels() {
		if (PerspectiveConfigHelper.getConfig("detect_update_channel").toString().equalsIgnoreCase("none")) PerspectiveConfigHelper.setConfig("detect_update_channel", "alpha");
		else if (PerspectiveConfigHelper.getConfig("detect_update_channel").toString().equalsIgnoreCase("alpha")) PerspectiveConfigHelper.setConfig("detect_update_channel", "beta");
		else if (PerspectiveConfigHelper.getConfig("detect_update_channel").toString().equalsIgnoreCase("beta")) PerspectiveConfigHelper.setConfig("detect_update_channel", "release");
		else PerspectiveConfigHelper.setConfig("detect_update_channel", "none");
	}
}