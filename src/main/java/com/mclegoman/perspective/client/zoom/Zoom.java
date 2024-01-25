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
import com.mclegoman.perspective.common.util.Triplet;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.*;

public class Zoom {
	private static final String[] zoomTransitions = new String[]{"smooth", "instant"};
	private static final String[] zoomScaleModes = new String[]{"scaled", "vanilla"};
	private static final List<Triplet<String, Boolean, Runnable>> zoomTypes = new ArrayList<>();
	public static boolean zoomInverted;
	public static double fov;
	public static double prevZoomMultiplier;
	public static double zoomMultiplier;
	private static boolean zoomUpdated;
	public static void addZoomType(String name, boolean shouldLimitFOV, Runnable setZoomTypeMultiplierRunnable) {
		Triplet<String, Boolean, Runnable> zoomType = new Triplet<>(name.toLowerCase(), shouldLimitFOV, setZoomTypeMultiplierRunnable);
		if (!zoomTypes.contains(zoomType)) zoomTypes.add(zoomType);
	}
	public static Triplet<String, Boolean, Runnable> getZoomType() {
		for (Triplet<String, Boolean, Runnable> zoomType : zoomTypes) {
			if (zoomType.getFirst().equalsIgnoreCase((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_type"))) {
				return zoomType;
			}
		}
		return null;
	}
	public static void init() {
		addZoomType("linear", true, () -> ZoomTypeMultiplier.setMultiplier(1 - ((float) Zoom.getZoomLevel() / 100)));
		addZoomType("logarithmic", true, () -> ZoomTypeMultiplier.setMultiplier((float) (1 - Math.log(Zoom.getZoomLevel() + 1) / Math.log(100.0 + 1))));
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
	public static void updateZoomMultiplier() {
		float f = getZoomMultiplier();
		prevZoomMultiplier = zoomMultiplier;
		zoomMultiplier += (f - zoomMultiplier) * 0.5F;
	}
	public static float getZoomMultiplier() {
		if (isZooming()) {
			if (getZoomType() != null) getZoomType().getThird().run();
			return ZoomTypeMultiplier.getMultiplier();
		}
		return 1.0F;
	}
	public static double limitFov(double fov) {
		return getZoomType() != null && getZoomType().getSecond() ? MathHelper.clamp(fov, 0.01, 179.99): fov;
	}
	public static boolean isZooming() {
		return ClientData.CLIENT.player != null && (zoomInverted != Keybindings.HOLD_ZOOM.isPressed());
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
		List<String> transitions = Arrays.stream(zoomTransitions).toList();
		return transitions.contains((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_transition")) ? zoomTransitions[(transitions.indexOf((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_transition")) + 1) % zoomTransitions.length] : zoomTransitions[0];
	}
	public static String nextScaleMode() {
		List<String> scaleModes = Arrays.stream(zoomScaleModes).toList();
		return scaleModes.contains((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_scale_mode")) ? zoomScaleModes[(scaleModes.indexOf((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_scale_mode")) + 1) % zoomScaleModes.length] : zoomScaleModes[0];
	}
	public static String nextZoomType() {
		List<String> list = new ArrayList<>();
		zoomTypes.forEach((zoomType) -> {
			list.add(zoomType.getFirst());
		});
		return list.contains((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_type")) ? list.get((list.indexOf((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_type")) + 1) % list.size()) : list.get(0);
	}
}