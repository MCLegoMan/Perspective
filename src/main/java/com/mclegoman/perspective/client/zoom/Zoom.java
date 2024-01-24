/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.zoom;

import com.mclegoman.perspective.client.config.ConfigDataLoader;
import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hud.MessageOverlay;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.List;

public class Zoom {
	private static final String[] ZoomTransitions = new String[]{"smooth", "instant"};
	private static final String[] ZoomMouseModes = new String[]{"scaled", "vanilla"};
	private static final String[] ZoomTypes = new String[]{"linear", "logarithmic"};
	public static boolean zoomInverted;
	public static double fov;
	public static double prevZoomMultiplier;
	public static double zoomMultiplier;
	private static boolean zoomUpdated;
	public static void updateZoomMultiplier() {
		float f = getZoomMultiplier();
		prevZoomMultiplier = zoomMultiplier;
		zoomMultiplier += (f - zoomMultiplier) * 0.5F;
	}
	public static float getZoomMultiplier() {
		if (isZooming()) {
			String zoomType = (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_type");
			if (zoomType.equals("linear")) {
				return 1 - ((float) getZoomLevel() / 100);
			} else if (zoomType.equals("logarithmic")) {
				return (float) (1 - Math.log(getZoomLevel() + 1) / Math.log(100.0 + 1));
			}
		}
		return 1.0F;
	}

	public static double limitFov(double fov) {
		return MathHelper.clamp(fov, 0.1, 110);
	}

	public static boolean isZooming() {
		return ClientData.CLIENT.player != null && (zoomInverted != Keybindings.HOLD_ZOOM.isPressed());
	}

	public static void tick() {
		try {
			if (Keybindings.TOGGLE_ZOOM.wasPressed()) zoomInverted = !zoomInverted;
			if (!isZooming() && zoomUpdated) {
				ConfigHelper.saveConfig(true);
				zoomUpdated = false;
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to tick zoom: {}", Data.VERSION.getLoggerPrefix(), error);
		}
	}
	public static int getZoomLevel() {
		return (int) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_level");
	}
	public static void zoom(int amount, int multiplier) {
		try {
			boolean updated = false;
			for (int i = 0; i < multiplier; i++) {
				if (!(getZoomLevel() <= 0) || !(getZoomLevel() >= 100)) {
					ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "zoom_level", getZoomLevel() + amount);
					updated = true;
					zoomUpdated = true;
				}
			}
			if (updated) setOverlay();
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to set zoom level: {}", Data.VERSION.getLoggerPrefix(), error);
		}
	}
	public static void reset() {
		try {
			if ((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_level") != ConfigDataLoader.ZOOM_LEVEL) {
				ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "zoom_level", ConfigDataLoader.ZOOM_LEVEL);
				setOverlay();
				zoomUpdated = true;
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to reset zoom level: {}", Data.VERSION.getLoggerPrefix(), error);
		}
	}
	private static void setOverlay() {
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_show_percentage"))
			MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.zoom_level", Text.literal((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_level") + "%")).formatted(Formatting.GOLD));
	}
	public static String nextTransition() {
		List<String> transitions = Arrays.stream(ZoomTransitions).toList();
		return transitions.contains((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_transition")) ? ZoomTransitions[(transitions.indexOf((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_transition")) + 1) % ZoomTransitions.length] : ZoomTransitions[0];
	}
	public static String nextMouseMode() {
		List<String> cameraModes = Arrays.stream(ZoomMouseModes).toList();
		return cameraModes.contains((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_mouse_mode")) ? ZoomMouseModes[(cameraModes.indexOf((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_mouse_mode")) + 1) % ZoomMouseModes.length] : ZoomMouseModes[0];
	}
	public static String nextZoomType() {
		List<String> zoomTypes = Arrays.stream(ZoomTypes).toList();
		return zoomTypes.contains((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_type")) ? ZoomTypes[(zoomTypes.indexOf((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_type")) + 1) % ZoomTypes.length] : ZoomTypes[0];
	}
}