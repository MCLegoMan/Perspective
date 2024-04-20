/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

public class Update {
	public static JsonElement getModrinthData(String project_id, String request) {
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
}
