/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.translation;

import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.util.Couple;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;
import org.apache.commons.lang3.StringUtils;

public class Translation {
	public static MutableText getVariableTranslation(String namespace, boolean toggle, Translation.Type type) {
		return toggle ? getTranslation(namespace, "variable." + type.asString() + ".on") : getTranslation(namespace, "variable." + type.asString() + ".off");
	}
	public static MutableText getShaderTranslation(String namespace, String shaderName) {
		return getText("shader." + namespace + "." + shaderName, true);
	}
	public static MutableText getShaderModeTranslation(String namespace, String key) {
		if (key.equalsIgnoreCase("game")) return getConfigTranslation(namespace, "shaders.mode.game");
		else if (key.equalsIgnoreCase("screen")) return getConfigTranslation(namespace, "shaders.mode.screen");
		else return getErrorTranslation(namespace);
	}
	public static MutableText getZoomTransitionTranslation(String namespace, String key) {
		if (key.equalsIgnoreCase("instant")) return getConfigTranslation(namespace, "zoom.transition.instant");
		else if (key.equalsIgnoreCase("smooth")) return getConfigTranslation(namespace, "zoom.transition.smooth");
		else return getErrorTranslation(namespace);
	}
	public static MutableText getZoomScaleModeTranslation(String namespace, String key) {
		if (key.equalsIgnoreCase("spyglass")) return getConfigTranslation(namespace, "zoom.camera_mode.spyglass");
		else if (key.equalsIgnoreCase("default")) return getConfigTranslation(namespace, "zoom.camera_mode.default");
		else return getErrorTranslation(namespace);
	}
	public static MutableText getHideCrosshairModeTranslation(String namespace, String key) {
		if (key.equalsIgnoreCase("false")) return getTranslation(namespace, "variable.onff.off");
		else if (key.equalsIgnoreCase("dynamic")) return getTranslation(namespace, "variable.dynamic");
		else if (key.equalsIgnoreCase("true")) return getTranslation(namespace, "variable.onff.on");
		else return getErrorTranslation(namespace);
	}
	public static MutableText getDetectUpdateChannelTranslation(String namespace, String key) {
		if (key.equalsIgnoreCase("none")) return getConfigTranslation(namespace, "detect_update_channel.none");
		else if (key.equalsIgnoreCase("alpha")) return getConfigTranslation(namespace, "detect_update_channel.alpha");
		else if (key.equalsIgnoreCase("beta")) return getConfigTranslation(namespace, "detect_update_channel.beta");
		else if (key.equalsIgnoreCase("release")) return getConfigTranslation(namespace, "detect_update_channel.release");
		else return getErrorTranslation(namespace);
	}
	public enum Type implements StringIdentifiable {
		ENDISABLE("endisable"),
		ONFF("onff"),
		DISABLE_SCREEN_MODE("disable_screen_mode"),
		BLUR("blur");
		private final String name;
		Type(String name) {
			this.name = name;
		}
		@Override
		public String asString() {
			return this.name;
		}
	}
	public static MutableText getText(String string, boolean isTranslatable) {
		return getText(string, isTranslatable, new Object[]{});
	}
	public static MutableText getText(String string, boolean isTranslatable, Formatting[] formattings) {
		return getText(string, isTranslatable).formatted(formattings);
	}
	public static MutableText getText(String string, boolean isTranslatable, Object[] variables) {
		return isTranslatable ? Text.translatable(string, variables) : Text.literal(getString(string, "%s", variables));
	}
	public static MutableText getText(String string, boolean isTranslatable, Object[] variables, Formatting[] formattings) {
		return getText(string, isTranslatable, variables).formatted(formattings);
	}
	public static MutableText getText(Couple<String, Boolean> data) {
		return getText(data.getFirst(), data.getSecond());
	}
	public static MutableText getText(Couple<String, Boolean> data, Formatting[] formattings) {
		return getText(data.getFirst(), data.getSecond(), formattings);
	}
	public static MutableText getText(Couple<String, Boolean> data, Object[] variables) {
		return getText(data.getFirst(), data.getSecond(), variables);
	}
	public static MutableText getText(Couple<String, Boolean> data, Object[] variables, Formatting[] formattings) {
		return getText(data.getFirst(), data.getSecond(), variables, formattings);
	}
	public static MutableText getCombinedText(MutableText... texts) {
		MutableText outputText = getText("", false);
		for (Text text : texts) outputText.append(text);
		return outputText;
	}
	public static MutableText getConfigTranslation(String namespace, String name, Object[] variables, Formatting[] formattings, boolean hover) {
		return hover ? getTranslation(namespace, "config." + name + ".hover", variables, formattings) : getTranslation(namespace, "config." + name, variables, formattings);
	}
	public static MutableText getConfigTranslation(String namespace, String name, Object[] variables, Formatting[] formattings) {
		return getTranslation(namespace, "config." + name, variables, formattings);
	}
	public static MutableText getConfigTranslation(String namespace, String name, Object[] variables, boolean hover) {
		return hover ? getTranslation(namespace, "config." + name + ".hover", variables) : getTranslation(namespace, "config." + name, variables);
	}
	public static MutableText getConfigTranslation(String namespace, String name, Object[] variables) {
		return getTranslation(namespace, "config." + name, variables);
	}
	public static MutableText getConfigTranslation(String namespace, String name, Formatting[] formattings, boolean hover) {
		return hover ? getTranslation(namespace, "config." + name + ".hover", formattings) : getTranslation(namespace, "config." + name, formattings);
	}
	public static MutableText getConfigTranslation(String namespace, String name, Formatting[] formattings) {
		return getTranslation(namespace, "config." + name, formattings);
	}
	public static MutableText getConfigTranslation(String namespace, String name, boolean hover) {
		return hover ? getTranslation(namespace, "config." + name + ".hover") : getTranslation(namespace, "config." + name);
	}
	public static MutableText getConfigTranslation(String namespace, String name) {
		return getTranslation(namespace, "config." + name);
	}
	public static MutableText getTranslation(String namespace, String key, Object[] variables, Formatting[] formattings) {
		return getText("gui." + namespace + "." + key, true, variables, formattings);
	}
	public static MutableText getTranslation(String namespace, String key, Object[] variables) {
		return getText("gui." + namespace + "." + key, true, variables);
	}
	public static MutableText getTranslation(String namespace, String key, Formatting[] formattings) {
		return getText("gui." + namespace + "." + key, true, formattings);
	}
	public static MutableText getTranslation(String namespace, String key) {
		return getText("gui." + namespace + "." + key, true);
	}
	public static String getFormattedString(String value, String searchString, Object[] variables) {
		String string = value;
		for (Object variable : variables) string = StringUtils.replaceOnce(string, searchString, String.valueOf(variable));
		return string;
	}
	public static String getString(String string, Object... variables) {
		return getFormattedString(string, "{}", variables);
	}
	public static String getKeybindingTranslation(String namespace, String key, boolean category) {
		return category ? getString("gui.{}.keybindings.category.{}", namespace, key) : getString("gui.{}.keybindings.keybinding.{}", namespace, key);
	}
	public static String getKeybindingTranslation(String namespace, String key) {
		return getString("gui.{}.keybindings.keybinding.{}", namespace, key);
	}
	public static MutableText getErrorTranslation(String namespace) {
		return getConfigTranslation(namespace, "error", new Formatting[]{Formatting.RED, Formatting.BOLD});
	}
}