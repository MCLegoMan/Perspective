/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.zoom;

import com.mclegoman.perspective.config.ConfigDataLoader;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hud.MessageOverlay;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.common.util.IdentifierHelper;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Zoom {
	public static final List<Identifier> zoomTypes = new ArrayList<>();
	public static final String[] zoomTransitions = new String[]{"smooth", "instant"};
	public static final String[] zoomScaleModes = new String[]{"scaled", "vanilla"};
	private static boolean isZooming;
	private static boolean hasUpdated;
	private static double prevMultiplier;
	private static double multiplier;
	public static double fov;
	public static double zoomFOV;
	public static void addZoomType(Identifier identifier) {
		if (!zoomTypes.contains(identifier)) zoomTypes.add(identifier);
	}
	public static void init() {
		try {
			addZoomType(Logarithmic.getIdentifier());
			addZoomType(Linear.getIdentifier());
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to init zoom: {}", error));
		}
	}
	public static void tick() {
		try {
			if (Keybindings.TOGGLE_ZOOM.wasPressed()) isZooming = !isZooming;
			if (!isZooming() && hasUpdated) {
				ConfigHelper.saveConfig();
				hasUpdated = false;
			}
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to tick zoom: {}", error));
		}
	}
	public static boolean isZooming() {
		return ClientData.CLIENT.player != null && (isZooming != Keybindings.HOLD_ZOOM.isPressed());
	}
	public static void updateMultiplier() {
		try {
			prevMultiplier = multiplier;
			if (!isZooming()) Multiplier.setMultiplier(1.0F);
			else {
				if (getZoomType().equals(Logarithmic.getIdentifier())) Logarithmic.updateMultiplier();
				if (getZoomType().equals(Linear.getIdentifier())) Linear.updateMultiplier();
			}
			multiplier = Multiplier.getMultiplier();
			updateTransition();
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to update zoom multiplier: {}", error));
		}
	}
	public static void updateTransition() {
		try {
			if (ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_transition").equals("smooth")) {
				multiplier = (prevMultiplier + multiplier) * 0.5;
			}
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to update zoom transition: {}", error));
		}
	}
	public static double getPrevMultiplier() {
		return prevMultiplier;
	}
	public static double getMultiplier() {
		return multiplier;
	}
	public static Identifier getZoomType() {
		if (!isValidZoomType(IdentifierHelper.identifierFromString((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_type")))) cycleZoomType();
		return IdentifierHelper.identifierFromString((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_type"));
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
					hasUpdated = true;
				}
			}
			if (updated) setOverlay();
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to set zoom level: {}", error));
		}
	}
	public static void reset() {
		try {
			if ((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_level") != ConfigDataLoader.ZOOM_LEVEL) {
				ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "zoom_level", ConfigDataLoader.ZOOM_LEVEL);
				setOverlay();
				hasUpdated = true;
			}
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to reset zoom level: {}", error));
		}
	}
	private static void setOverlay() {
		try {
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_show_percentage"))
				MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.zoom_level", Text.literal((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_level") + "%")).formatted(Formatting.GOLD));
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to set zoom overlay: {}", error));
		}
	}
	public static void cycleZoomType() {
		cycleZoomType(true);
	}
	public static void cycleZoomType(boolean direction) {
		try {
			int currentIndex = zoomTypes.indexOf(getZoomType());
			ConfigHelper.setConfig(ConfigHelper.ConfigType.NORMAL, "zoom_type", IdentifierHelper.stringFromIdentifier(zoomTypes.get(direction ? (currentIndex + 1) % zoomTypes.size() : (currentIndex - 1 + zoomTypes.size()) % zoomTypes.size())));
		} catch (Exception error) {
			Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to cycle zoom type: {}", error));
		}
	}
	public static boolean isValidZoomType(Identifier ZoomType) {
		return zoomTypes.contains(ZoomType);
	}
	public static String nextTransition() {
		List<String> transitions = Arrays.stream(zoomTransitions).toList();
		return transitions.contains((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_transition")) ? zoomTransitions[(transitions.indexOf((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_transition")) + 1) % zoomTransitions.length] : zoomTransitions[0];
	}
	public static String nextScaleMode() {
		List<String> scaleModes = Arrays.stream(zoomScaleModes).toList();
		return scaleModes.contains((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_scale_mode")) ? zoomScaleModes[(scaleModes.indexOf((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_scale_mode")) + 1) % zoomScaleModes.length] : zoomScaleModes[0];
	}
	public static class Logarithmic {
		public static Identifier getIdentifier() {
			return new Identifier(Data.VERSION.getID(), "logarithmic");
		}
		public static double getLimitFOV(double input) {
			return MathHelper.clamp(input, 0.01, 179.99);
		}
		public static void updateMultiplier() {
			Multiplier.setMultiplier((float) (1.0F - (Math.log(Zoom.getZoomLevel() + 1.0F) / Math.log(100.0 + 1.0F))));
		}
	}
	public static class Linear {
		public static Identifier getIdentifier() {
			return new Identifier(Data.VERSION.getID(), "linear");
		}
		public static double getLimitFOV(double input) {
			return MathHelper.clamp(input, 0.01, 179.99);
		}
		public static void updateMultiplier() {
			Multiplier.setMultiplier(1.0F - (Zoom.getZoomLevel() / 100.0F));
		}
	}
	public static class Multiplier {
		protected static float currentMultiplier = 1.0F;
		protected static float getMultiplier() {
			return currentMultiplier;
		}
		protected static void setMultiplier(float multiplier) {
			try {
				currentMultiplier = multiplier;
			} catch (Exception error) {
				Data.VERSION.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to set Zoom Multiplier: {}", error));
			}
		}
	}
}