/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.translation;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

public class Translation {
    public static Text getVariableTranslation(boolean toggle, TranslationType type) {
        return toggle ? getTranslation("variable." + type.asString() + ".on") : getTranslation("variable." + type.asString() + ".off");
    }
    public static Text getConfigTranslation(String name, Object[] variables, Formatting[] formattings, boolean hover) {
        return hover ? getTranslation("config." + name + ".hover", variables, formattings) : getTranslation("config." + name, variables, formattings);
    }
    public static Text getConfigTranslation(String name, Object[] variables, Formatting[] formattings) {
        return getTranslation("config." + name, variables, formattings);
    }
    public static Text getConfigTranslation(String name, Object[] variables, boolean hover) {
        return hover ? getTranslation("config." + name + ".hover", variables) : getTranslation("config." + name, variables);
    }
    public static Text getConfigTranslation(String name, Object[] variables) {
        return getTranslation("config." + name, variables);
    }
    public static Text getConfigTranslation(String name, Formatting[] formattings, boolean hover) {
        return hover ? getTranslation("config." + name + ".hover", formattings) : getTranslation("config." + name, formattings);
    }
    public static Text getConfigTranslation(String name, Formatting[] formattings) {
        return getTranslation("config." + name, formattings);
    }
    public static Text getConfigTranslation(String name, boolean hover) {
        return hover ? getTranslation("config." + name + ".hover") : getTranslation("config." + name);
    }
    public static Text getConfigTranslation(String name) {
        return getTranslation("config." + name);
    }
    public static Text getTranslation(String key, Object[] variables, Formatting[] formattings) {
        return Text.translatable("gui.perspective." + key, variables).formatted(formattings);
    }
    public static Text getTranslation(String key, Object[] variables) {
        return Text.translatable("gui.perspective." + key, variables);
    }
    public static Text getTranslation(String key, Formatting[] formattings) {
        return Text.translatable("gui.perspective." + key).formatted(formattings);
    }
    public static Text getTranslation(String key) {
        return Text.translatable("gui.perspective." + key);
    }
    public static String getString(String string, Object... variables) {
        String RETURN = string;
        for (Object variable : variables) RETURN = StringUtils.replaceOnce(RETURN, "{}", (String)variable);
        return RETURN;
    }
    public static String getKeybindingTranslation(String key, boolean category) {
        return category ? Translation.getString("gui.perspective.keybindings.category.{}", key) : Translation.getString("gui.perspective.keybindings.keybinding.{}", key);
    }
    public static String getKeybindingTranslation(String key) {
        return Translation.getString("gui.perspective.keybindings.keybinding.{}", key);
    }
    public static Text getShaderModeTranslation(String key) {
        if (key.equalsIgnoreCase("game")) return getConfigTranslation("shaders.mode.game");
        else if (key.equalsIgnoreCase("screen")) return getConfigTranslation("shaders.mode.screen");
        else return getErrorTranslation();
    }
    public static Text getZoomModeTranslation(String key) {
        if (key.equalsIgnoreCase("instant")) return getConfigTranslation("zoom.transition.instant");
        else if (key.equalsIgnoreCase("smooth")) return getConfigTranslation("zoom.transition.smooth");
        else return getErrorTranslation();
    }
    public static Text getZoomCameraModeTranslation(String key) {
        if (key.equalsIgnoreCase("default")) return getConfigTranslation("zoom.camera_mode.default");
        else if (key.equalsIgnoreCase("spyglass")) return getConfigTranslation("zoom.camera_mode.spyglass");
        else return getErrorTranslation();
    }
    public static Text getDetectUpdateChannelTranslation(String key) {
        if (key.equalsIgnoreCase("none")) return getConfigTranslation("detect_update_channel.none");
        else if (key.equalsIgnoreCase("alpha")) return getConfigTranslation("detect_update_channel.alpha");
        else if (key.equalsIgnoreCase("beta")) return getConfigTranslation("detect_update_channel.beta");
        else if (key.equalsIgnoreCase("release")) return getConfigTranslation("detect_update_channel.release");
        else return getErrorTranslation();
    }

    public static Text getErrorTranslation() {
        return getConfigTranslation("error", new Formatting[]{Formatting.RED, Formatting.BOLD});
    }
}