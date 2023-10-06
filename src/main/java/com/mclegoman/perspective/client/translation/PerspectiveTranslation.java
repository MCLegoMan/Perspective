/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.translation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

@Environment(EnvType.CLIENT)
public class PerspectiveTranslation {
    public static Text getVariableTranslation(boolean toggle, PerspectiveTranslationType type) {
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
    public static Text getPlural(int amount, String key, Object[] variables, Formatting[] formattings) {
        return getTranslation(setPluralKey(amount, key), variables, formattings);
    }
    public static Text getPlural(int amount, String key, Object[] variables) {
        return getTranslation(setPluralKey(amount, key), variables);
    }
    public static Text getPlural(int amount, String key, Formatting[] formattings) {
        return getTranslation(setPluralKey(amount, key), formattings);
    }
    public static Text getPlural(int amount, String key) {
        return getTranslation(setPluralKey(amount, key));
    }
    private static String setPluralKey(int amount, String key) {
        return amount == 1 ? key + ".single" : key + ".plural";
    }
    public static String getKeybindingTranslation(String key, boolean category) {
        return category ? PerspectiveTranslation.getString("gui.perspective.keybindings.category.{}", key) : PerspectiveTranslation.getString("gui.perspective.keybindings.keybinding.{}", key);
    }
    public static String getKeybindingTranslation(String key) {
        return PerspectiveTranslation.getString("gui.perspective.keybindings.keybinding.{}", key);
    }
    public static Text getShaderModeTranslation(String key) {
        if (key.equalsIgnoreCase("game")) return getConfigTranslation("shaders.mode.game");
        else if (key.equalsIgnoreCase("screen")) return getConfigTranslation("shaders.mode.screen");
        else return getErrorTranslation();
    }
    public static Text getZoomModeTranslation(String key) {
        if (key.equalsIgnoreCase("instant")) return getConfigTranslation("zoom.mode.instant");
        else if (key.equalsIgnoreCase("smooth")) return getConfigTranslation("zoom.mode.smooth");
        else return getErrorTranslation();
    }

    public static Text getDetectUpdateChannelTranslation(String key) {
        if (key.equalsIgnoreCase("none")) return getConfigTranslation("more_options.detect_update_channel.none");
        else if (key.equalsIgnoreCase("alpha")) return getConfigTranslation("more_options.detect_update_channel.alpha");
        else if (key.equalsIgnoreCase("beta")) return getConfigTranslation("more_options.detect_update_channel.beta");
        else if (key.equalsIgnoreCase("release")) return getConfigTranslation("more_options.detect_update_channel.release");
        else return getErrorTranslation();
    }

    public static Text getErrorTranslation() {
        return getConfigTranslation("error", new Formatting[]{Formatting.RED, Formatting.BOLD});
    }
}