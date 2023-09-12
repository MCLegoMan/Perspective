package com.mclegoman.perspective.client.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonHelper;

public class PerspectiveJsonHelper extends JsonHelper {
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
    private static String asShaderMode(String STRING) {
        if (STRING.equalsIgnoreCase("screen")) return "screen";
        else if (STRING.equalsIgnoreCase("game")) return "game";
        return "game";
    }
    private static String asShaderMode(boolean BOOLEAN) {
        // This is to keep backwards compatibility with older versions of Perspective.
        if (BOOLEAN) return "screen";
        return "game";
    }
}