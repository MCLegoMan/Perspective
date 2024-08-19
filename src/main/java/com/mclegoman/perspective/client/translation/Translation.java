/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.translation;

import com.mclegoman.perspective.client.hide.Hide;
import com.mclegoman.perspective.client.zoom.Zoom;
import net.minecraft.text.MutableText;
import net.minecraft.util.StringIdentifiable;

import java.util.Arrays;

public class Translation extends com.mclegoman.luminance.client.translation.Translation {
	public static MutableText getVariableTranslation(String namespace, boolean toggle, Type type) {
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
		if (key.equalsIgnoreCase("scaled")) return getConfigTranslation(namespace, "zoom.scale_mode.scaled");
		else if (key.equalsIgnoreCase("vanilla")) return getConfigTranslation(namespace, "zoom.scale_mode.vanilla");
		else return getErrorTranslation(namespace);
	}
	public static MutableText getZoomTypeTranslation(String namespace, String zoomType, boolean hover) {
		if (Zoom.isValidZoomType(Zoom.getZoomType())) {
			return getConfigTranslation(namespace, "zoom.type." + zoomType + (hover ? ".hover" : ""));
		}
		return getErrorTranslation(namespace);
	}
	public static MutableText getZoomTypeTranslation(String namespace, String zoomType) {
		return getZoomTypeTranslation(namespace, zoomType, false);
	}
	public static MutableText getUIBackgroundTranslation(String namespace, String key) {
		return getConfigTranslation(namespace, "ui_background.type." + key);
	}
	public static MutableText getTimeOverlayTranslation(String namespace, String key) {
		return getConfigTranslation(namespace, "time_overlay.type." + key);
	}
	public static MutableText getCrosshairTranslation(String namespace, String key) {
		if (Arrays.stream(Hide.hideCrosshairModes).toList().contains(key)) return getConfigTranslation(namespace, "crosshair.type." + key);
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
}