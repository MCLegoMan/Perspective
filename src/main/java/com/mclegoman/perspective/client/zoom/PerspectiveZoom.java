/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.zoom;

import com.mclegoman.perspective.client.config.PerspectiveConfigDataLoader;
import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.overlays.PerspectiveHUDOverlays;
import com.mclegoman.perspective.client.util.PerspectiveKeybindings;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class PerspectiveZoom {
	public static double zoomFov(double fov) {
		return (100 - getZoomLevel()) * fov / 100;
	}
	public static double limitedZoomFov(double fov) {
		return Math.max(Math.max(0.1, zoomFov(fov)), Math.min(fov, zoomFov(fov)));
	}

	public static boolean SET_ZOOM;
	public static boolean isZooming() {
		try {
			if (PerspectiveClientData.CLIENT.player != null) return PerspectiveKeybindings.HOLD_ZOOM.isPressed() || SET_ZOOM;
		} catch (Exception ignored) {
		}
		return false;
	}
	public static void tick(MinecraftClient client) {
		try {
			if (PerspectiveKeybindings.TOGGLE_ZOOM.wasPressed()) SET_ZOOM = !SET_ZOOM;
		} catch (Exception error) {
			PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to tick zoom: {}", (Object)error);
		}
	}
	public static int getZoomLevel() {
		int zoomLevel = (int)PerspectiveConfigHelper.getConfig("zoom_level");
		if (PerspectiveConfigHelper.getConfig("zoom_mode").equals("smooth")) {
			if (zoomLevel > 90) PerspectiveConfigHelper.setConfig("zoom_level", 90);
			return Math.min(zoomLevel, 90);
		} else return zoomLevel;
	}
	public static void zoom(boolean in, int amount) {
		try {
			for (int i = 0; i < amount; i++){
				if (PerspectiveConfigHelper.getConfig("zoom_mode").equals("smooth")) {
					if (in) {
						if (getZoomLevel() >= 90) PerspectiveConfigHelper.setConfig("zoom_level", 90);
						else PerspectiveConfigHelper.setConfig("zoom_level", getZoomLevel() + 1);
					}
					else {
						if (getZoomLevel() <= 0) PerspectiveConfigHelper.setConfig("zoom_level", 0);
						else PerspectiveConfigHelper.setConfig("zoom_level", getZoomLevel() - 1);
					}
				} else {
					if (in) {
						if (getZoomLevel() >= 100) PerspectiveConfigHelper.setConfig("zoom_level", 100);
						else PerspectiveConfigHelper.setConfig("zoom_level", getZoomLevel() + 1);
					}
					else {
						if (getZoomLevel() <= 0) PerspectiveConfigHelper.setConfig("zoom_level", 0);
						else PerspectiveConfigHelper.setConfig("zoom_level", getZoomLevel() - 1);
					}
				}
			}
			setOverlay();
			PerspectiveConfigHelper.saveConfig(true);
		} catch (Exception error) {
			PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to set zoom level: {}", (Object)error);
		}
	}
	public static void reset(MinecraftClient client) {
		try {
			if ((int)PerspectiveConfigHelper.getConfig("zoom_level") != PerspectiveConfigDataLoader.ZOOM_LEVEL) {
				PerspectiveConfigHelper.setConfig("zoom_level", PerspectiveConfigDataLoader.ZOOM_LEVEL);
				setOverlay();
				PerspectiveConfigHelper.saveConfig(true);
			}
		} catch (Exception error) {
			PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to reset zoom level: {}", (Object)error);
		}
	}
	private static void setOverlay(){
		if ((boolean)PerspectiveConfigHelper.getConfig("zoom_overlay_message")) PerspectiveHUDOverlays.setOverlay(Text.translatable("gui.perspective.message.zoom_level", Text.literal((int)PerspectiveConfigHelper.getConfig("zoom_level") + "%")).formatted(Formatting.GOLD));
	}
	public static void cycleZoomModes() {
		if (PerspectiveConfigHelper.getConfig("zoom_mode").equals("smooth")) PerspectiveConfigHelper.setConfig("zoom_mode", "instant");
		else if (PerspectiveConfigHelper.getConfig("zoom_mode").equals("instant")) PerspectiveConfigHelper.setConfig("zoom_mode", "smooth");
		else PerspectiveConfigHelper.setConfig("zoom_mode", "smooth");
	}
}