/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.zoom;

import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.config.ConfigDataLoader;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class Zoom {
	private static boolean isZooming;
	private static boolean hasUpdated;
	private static double prevMultiplier = 1.0D;
	private static double multiplier = 1.0D;
	public static double fov;
	public static double zoomFOV;
	public static double timeDelta = Double.MIN_VALUE;
	public static void init() {
	}
	public static void tick() {
		if (Keybindings.TOGGLE_ZOOM.wasPressed()) isZooming = !isZooming;
		if (!isZooming()) {
			if (hasUpdated) {
				ConfigHelper.saveConfig(true);
				hasUpdated = false;
			}
		}
	}
	public static double getMouseSensitivity() {
		return Math.pow(ClientData.minecraft.options.getMouseSensitivity().getValue() * 0.6000000238418579F + 0.20000000298023224F, 3.0F) * 8.0F;
	}
	public static boolean isZooming() {
		return ClientData.minecraft.player != null && (isZooming != Keybindings.HOLD_ZOOM.isPressed());
	}
	public static void updateMultiplier() {
		prevMultiplier = multiplier;
		if (!isZooming()) Multiplier.setMultiplier(1.0F);
		else Logarithmic.updateMultiplier();
		multiplier = Multiplier.getMultiplier();
		updateTransition();
	}
	public static void updateTransition() {
		double speedMultiplier = ((prevMultiplier + multiplier) * 0.5);
		multiplier = MathHelper.lerp(1.0F, prevMultiplier, speedMultiplier);
	}
	public static double getPrevMultiplier() {
		return prevMultiplier;
	}
	public static double getMultiplier() {
		return multiplier;
	}
	public static float getZoomLevel() {
		return MathHelper.clamp(getRawZoomLevel(), 0.0F, 99.9F);
	}
	public static int getRawZoomLevel() {
		return (int) ConfigHelper.getConfig("zoom_level");
	}
	public static void zoom(int amount, int multiplier) {
		for (int i = 0; i < multiplier; i++) {
			if (!(getRawZoomLevel() <= 0) || !(getRawZoomLevel() >= 100)) {
				ConfigHelper.setConfig("zoom_level", getRawZoomLevel() + amount);
				hasUpdated = true;
			}
		}
	}
	public static void reset() {
		if ((int) ConfigHelper.getConfig("zoom_level") != ConfigDataLoader.ZOOM_LEVEL) {
			ConfigHelper.setConfig("zoom_level", ConfigDataLoader.ZOOM_LEVEL);
			hasUpdated = true;
		}
	}
	public static class Logarithmic {
		public static Identifier getIdentifier() {
			return Identifier.of(Data.version.getID(), "logarithmic");
		}
		public static double getLimitFOV(double input) {
			return MathHelper.clamp(input, 0.01, 179.99);
		}
		public static void updateMultiplier() {
			Multiplier.setMultiplier((float) (1.0F - (Math.log(Zoom.getZoomLevel() + 1.0F) / Math.log(100.0 + 1.0F))));
		}
	}
	public static class Multiplier {
		protected static float currentMultiplier = 1.0F;
		protected static float getMultiplier() {
			return currentMultiplier;
		}
		protected static void setMultiplier(float multiplier) {
			currentMultiplier = multiplier;
		}
	}
}