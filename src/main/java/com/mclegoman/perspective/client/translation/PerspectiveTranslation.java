/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
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
}