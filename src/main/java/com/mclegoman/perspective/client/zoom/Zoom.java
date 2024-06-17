/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.zoom;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hud.MessageOverlay;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigDataLoader;
import com.mclegoman.perspective.config.ConfigHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.List;

public class Zoom {
	private static final String[] ZoomTransitions = new String[]{"smooth", "instant"};
	private static final String[] ZoomCameraModes = new String[]{"default", "spyglass"};
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
		return isZooming() ? 1 - ((float) getZoomLevel() / 100) : 1;
	}
	public static double limitFov(double fov) {
		return MathHelper.clamp(fov, 0.1, 110);
	}
	public static boolean isZooming() {
		return ClientData.minecraft.player != null && (zoomInverted != Keybindings.HOLD_ZOOM.isPressed());
	}
	public static void tick() {
		try {
			updateZoomMultiplier();
			if (Keybindings.TOGGLE_ZOOM.wasPressed()) zoomInverted = !zoomInverted;
			if (!isZooming() && zoomUpdated) {
				ConfigHelper.saveConfig(true);
				zoomUpdated = false;
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to tick zoom: {}"));
		}
	}
	public static int getZoomLevel() {
		return (int) ConfigHelper.getConfig("zoom_level");
	}
	public static void zoom(boolean in, int amount) {
		try {
			boolean updated = false;
			for (int i = 0; i < amount; i++) {
				if (in) {
					if (!(getZoomLevel() >= 100)) {
						ConfigHelper.setConfig("zoom_level", getZoomLevel() + 1);
						updated = true;
						zoomUpdated = true;
					}
				} else {
					if (!(getZoomLevel() <= -50)) {
						ConfigHelper.setConfig("zoom_level", getZoomLevel() - 1);
						updated = true;
						zoomUpdated = true;
					}
				}
			}
			if (updated) setOverlay();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to set zoom: {}"));
		}
	}
	public static void reset() {
		try {
			if ((int) ConfigHelper.getConfig("zoom_level") != ConfigDataLoader.ZOOM_LEVEL) {
				ConfigHelper.setConfig("zoom_level", ConfigDataLoader.ZOOM_LEVEL);
				setOverlay();
				zoomUpdated = true;
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to reset zoom: {}"));
		}
	}
	private static void setOverlay() {
		if ((boolean) ConfigHelper.getConfig("zoom_show_percentage"))
			MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.zoom_level", Text.literal((int) ConfigHelper.getConfig("zoom_level") + "%")).formatted(Formatting.GOLD));
	}
	public static String nextTransition() {
		List<String> transitions = Arrays.stream(ZoomTransitions).toList();
		return transitions.contains((String) ConfigHelper.getConfig("zoom_transition")) ? ZoomTransitions[(transitions.indexOf((String) ConfigHelper.getConfig("zoom_transition")) + 1) % ZoomTransitions.length] : ZoomTransitions[0];
	}
	public static String nextCameraMode() {
		List<String> cameraModes = Arrays.stream(ZoomCameraModes).toList();
		return cameraModes.contains((String) ConfigHelper.getConfig("zoom_camera_mode")) ? ZoomCameraModes[(cameraModes.indexOf((String) ConfigHelper.getConfig("zoom_camera_mode")) + 1) % ZoomCameraModes.length] : ZoomCameraModes[0];
	}
}