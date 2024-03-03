/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mclegoman.perspective.client.ui.UIBackground;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.common.util.IdentifierHelper;
import net.minecraft.util.Identifier;

public class JsonHelper extends net.minecraft.util.JsonHelper {
	public static String getShaderMode(JsonObject object, String element, String defaultObj) {
		return object.has(element) ? asShaderMode(object.get(element).getAsString()) : asShaderMode(defaultObj);
	}
	public static String getShaderMode(JsonObject object, String element, boolean defaultObj) {
		return object.has(element) ? asShaderMode(object.get(element).getAsBoolean()) : asShaderMode(defaultObj);
	}
	public static String getShaderMode(JsonObject object, String element) {
		if (object.has(element)) {
			if (isString(object.get(element))) asShaderMode(object.get(element).getAsString());
			else if (isBoolean(object.get(element))) return asShaderMode(object.get(element).getAsBoolean());
			return "game";
		} else {
			throw new JsonSyntaxException("Missing " + element + ", expected to find a string");
		}
	}
	public static String asShaderMode(String STRING) {
		if (STRING.equalsIgnoreCase("screen")) return "screen";
		else if (STRING.equalsIgnoreCase("game")) return "game";
		return "game";
	}
	public static String asShaderMode(boolean BOOLEAN) {
		// This is to keep backwards compatibility with older versions of Perspective.
		if (BOOLEAN) return "screen";
		return "game";
	}
	public static String getDetectUpdateChannel(JsonObject object, String element, String defaultObj) {
		return object.has(element) ? asDetectUpdateChannel(object.get(element).getAsString()) : asDetectUpdateChannel(defaultObj);
	}
	public static String asDetectUpdateChannel(String STRING) {
		if (STRING.equalsIgnoreCase("none")) return "none";
		else if (STRING.equalsIgnoreCase("alpha")) return "alpha";
		else if (STRING.equalsIgnoreCase("beta")) return "beta";
		return "release";
	}
	public static String getUIBackground(JsonObject object, String element, String defaultObj) {
		return object.has(element) ? asUIBackground(object.get(element).getAsString()) : asUIBackground(defaultObj);
	}
	public static String asUIBackground(String STRING) {
		return UIBackground.isValidUIBackgroundType(STRING) ? STRING : "default";
	}
	public static String getTitleScreenBackground(JsonObject object, String element, String defaultObj) {
		return object.has(element) ? asTitleScreenBackground(object.get(element).getAsString()) : asTitleScreenBackground(defaultObj);
	}
	public static String asTitleScreenBackground(String STRING) {
		return UIBackground.isValidTitleScreenBackgroundType(STRING) ? STRING : "default";
	}
	public static String getZoomType(JsonObject object, String element, String defaultObj) {
		return object.has(element) ? asZoomType(object.get(element).getAsString()) : asZoomType(defaultObj);
	}
	public static String asZoomType(String STRING) {
		return Zoom.isValidZoomType(IdentifierHelper.identifierFromString(STRING)) ? STRING : IdentifierHelper.stringFromIdentifier(new Identifier(Data.VERSION.getID(), "logarithmic"));
	}
}