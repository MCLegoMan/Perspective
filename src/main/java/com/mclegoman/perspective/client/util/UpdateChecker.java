/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import com.mclegoman.releasetypeutils.common.version.Version;

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
					//ClientData.minecraft.getToastManager().add(new Toast(Translation.getTranslation(Data.version.getID(), "toasts.title", new Object[]{Translation.getTranslation(Data.version.getID(), "name"), Translation.getTranslation(Data.version.getID(), "toasts.update.title")}), Translation.getTranslation(Data.version.getID(), "toasts.update.description", new Object[]{UpdateChecker.LATEST_VERSION_FOUND}), 280, Toast.Type.INFO));
					SEEN_UPDATE_TOAST = true;
				}
			}
			UPDATE_CHECKER_COMPLETE = true;
		}
	}
	public static void checkForUpdates() {
		UPDATE_CHECKER_COMPLETE = true;
		NEWER_VERSION_FOUND = false;
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
			Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to get Modrinth data: {}", error));
		}
		return null;
	}
	public static String nextUpdateChannel() {
		List<String> updateChannels = Arrays.stream(detectUpdateChannels).toList();
		return updateChannels.contains((String) ConfigHelper.getConfig("detect_update_channel")) ? detectUpdateChannels[(updateChannels.indexOf((String) ConfigHelper.getConfig("detect_update_channel")) + 1) % detectUpdateChannels.length] : detectUpdateChannels[0];
	}
}