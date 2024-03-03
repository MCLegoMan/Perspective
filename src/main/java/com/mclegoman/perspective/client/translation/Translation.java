/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.translation;

import com.mclegoman.perspective.client.ui.UIBackground;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

public class Translation {
	public static Text getCombinedText(Text... texts) {
		MutableText outputText = Text.literal("");
		for (Text text : texts) outputText.append(text);
		return outputText;
	}
	public static Text getVariableTranslation(String namespace, boolean toggle, TranslationType type) {
		return toggle ? getTranslation(namespace, "variable." + type.asString() + ".on") : getTranslation(namespace, "variable." + type.asString() + ".off");
	}
	public static Text getConfigTranslation(String namespace, String name, Object[] variables, Formatting[] formattings, boolean hover) {
		return hover ? getTranslation(namespace, "config." + name + ".hover", variables, formattings) : getTranslation(namespace, "config." + name, variables, formattings);
	}
	public static Text getConfigTranslation(String namespace, String name, Object[] variables, Formatting[] formattings) {
		return getTranslation(namespace, "config." + name, variables, formattings);
	}
	public static Text getConfigTranslation(String namespace, String name, Object[] variables, boolean hover) {
		return hover ? getTranslation(namespace, "config." + name + ".hover", variables) : getTranslation(namespace, "config." + name, variables);
	}
	public static Text getConfigTranslation(String namespace, String name, Object[] variables) {
		return getTranslation(namespace, "config." + name, variables);
	}
	public static Text getConfigTranslation(String namespace, String name, Formatting[] formattings, boolean hover) {
		return hover ? getTranslation(namespace, "config." + name + ".hover", formattings) : getTranslation(namespace, "config." + name, formattings);
	}
	public static Text getConfigTranslation(String namespace, String name, Formatting[] formattings) {
		return getTranslation(namespace, "config." + name, formattings);
	}
	public static Text getConfigTranslation(String namespace, String name, boolean hover) {
		return hover ? getTranslation(namespace, "config." + name + ".hover") : getTranslation(namespace, "config." + name);
	}
	public static Text getConfigTranslation(String namespace, String name) {
		return getTranslation(namespace, "config." + name);
	}
	public static Text getTranslation(String namespace, String key, Object[] variables, Formatting[] formattings) {
		return Text.translatable("gui." + namespace + "." + key, variables).formatted(formattings);
	}
	public static Text getTranslation(String namespace, String key, Object[] variables) {
		return Text.translatable("gui." + namespace + "." + key, variables);
	}
	public static Text getTranslation(String namespace, String key, Formatting[] formattings) {
		return Text.translatable("gui." + namespace + "." + key).formatted(formattings);
	}
	public static Text getTranslation(String namespace, String key) {
		return Text.translatable("gui." + namespace + "." + key);
	}
	public static Text getShaderTranslation(String namespace, String shaderName) {
		return Text.translatable("shader." + namespace + "." + shaderName);
	}
	public static String getString(String string, Object... variables) {
		String RETURN = string;
		for (Object variable : variables) RETURN = StringUtils.replaceOnce(RETURN, "{}", String.valueOf(variable));
		return RETURN;
	}
	public static String getKeybindingTranslation(String key, boolean category) {
		return category ? Translation.getString("gui.perspective.keybindings.category.{}", key) : Translation.getString("gui.perspective.keybindings.keybinding.{}", key);
	}
	public static String getKeybindingTranslation(String key) {
		return Translation.getString("gui.perspective.keybindings.keybinding.{}", key);
	}
	public static Text getShaderModeTranslation(String namespace, String key) {
		if (key.equalsIgnoreCase("game")) return getConfigTranslation(namespace, "shaders.mode.game");
		else if (key.equalsIgnoreCase("screen")) return getConfigTranslation(namespace, "shaders.mode.screen");
		else return getErrorTranslation();
	}
	public static Text getZoomTransitionTranslation(String namespace, String key) {
		if (key.equalsIgnoreCase("instant")) return getConfigTranslation(namespace, "zoom.transition.instant");
		else if (key.equalsIgnoreCase("smooth")) return getConfigTranslation(namespace, "zoom.transition.smooth");
		else return getErrorTranslation();
	}
	public static Text getZoomScaleModeTranslation(String namespace, String key) {
		if (key.equalsIgnoreCase("scaled")) return getConfigTranslation(namespace, "zoom.scale_mode.scaled");
		else if (key.equalsIgnoreCase("vanilla")) return getConfigTranslation(namespace, "zoom.scale_mode.vanilla");
		else return getErrorTranslation();
	}
	public static Text getZoomTypeTranslation() {
		if (Zoom.isValidZoomType(Zoom.getZoomType())) {
			return getConfigTranslation(Zoom.getZoomType().getNamespace(), "zoom.type." + Zoom.getZoomType().getPath());
		}
		return getErrorTranslation();
	}
	public static Text getHideCrosshairModeTranslation(String namespace, String key) {
		if (key.equalsIgnoreCase("false")) return getTranslation(namespace, "variable.onff.off");
		else if (key.equalsIgnoreCase("dynamic")) return getTranslation(namespace, "variable.dynamic");
		else if (key.equalsIgnoreCase("true")) return getTranslation(namespace, "variable.onff.on");
		else return getErrorTranslation();
	}
	public static Text getUIBackgroundTranslation(String namespace, String key) {
		if (UIBackground.isValidUIBackgroundType(UIBackground.getUIBackgroundType())) return getConfigTranslation(namespace, "ui_background.type." + UIBackground.getUIBackgroundType());
		return getErrorTranslation();
	}
	public static Text getTitleScreenBackgroundTranslation(String namespace, String key) {
		if (UIBackground.isValidTitleScreenBackgroundType(UIBackground.getTitleScreenBackgroundType())) return getConfigTranslation(namespace, "title_screen.type." + UIBackground.getTitleScreenBackgroundType());
		return getErrorTranslation();
	}
	public static Text getDetectUpdateChannelTranslation(String namespace, String key) {
		if (key.equalsIgnoreCase("none")) return getConfigTranslation(namespace, "detect_update_channel.none");
		else if (key.equalsIgnoreCase("alpha")) return getConfigTranslation(namespace, "detect_update_channel.alpha");
		else if (key.equalsIgnoreCase("beta")) return getConfigTranslation(namespace, "detect_update_channel.beta");
		else if (key.equalsIgnoreCase("release")) return getConfigTranslation(namespace, "detect_update_channel.release");
		else return getErrorTranslation();
	}
	public static Text getErrorTranslation() {
		return getConfigTranslation(Data.VERSION.getID(), "error", new Formatting[]{Formatting.RED, Formatting.BOLD});
	}
}