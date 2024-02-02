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
import com.mclegoman.perspective.common.util.IdentifierHelper;
import com.mclegoman.perspective.common.util.Triple;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.*;

public class Zoom {
	public static final String[] zoomTransitions = new String[]{"smooth", "instant"};
	public static final String[] zoomScaleModes = new String[]{"scaled", "vanilla"};
	public static final List<Triple<Identifier, Boolean, Runnable>> zoomTypes = new ArrayList<>();
	public static boolean zoomInverted;
	public static double fov;
	public static double zoomFov;
	public static double prevZoomMultiplier;
	public static double zoomMultiplier;
	private static boolean zoomUpdated;
	public static void addZoomType(Identifier identifier, boolean shouldLimitFOV, Runnable setZoomTypeMultiplierRunnable) {
		Triple<Identifier, Boolean, Runnable> zoomType = new Triple<>(identifier, shouldLimitFOV, setZoomTypeMultiplierRunnable);
		if (!zoomTypes.contains(zoomType)) zoomTypes.add(zoomType);
	}
	public static Triple<Identifier, Boolean, Runnable> getZoomType() {
		for (Triple<Identifier, Boolean, Runnable> zoomType : zoomTypes) {
			if (zoomType.getFirst().toString().equals(ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_type"))) {
				return zoomType;
			}
		}
		return null;
	}
	public static void init() {
		addZoomType(new Identifier(Data.VERSION.getID(), "logarithmic"), true, () -> ZoomTypeMultiplier.setMultiplier((float) (1.0F - (Math.log(Zoom.getZoomLevel() + 1.0F) / Math.log(100.0 + 1.0F)))));
		addZoomType(new Identifier(Data.VERSION.getID(), "linear"), true, () -> ZoomTypeMultiplier.setMultiplier(1.0F - (Zoom.getZoomLevel() / 100.0F)));
	}
	public static void tick() {
		try {
			if (Keybindings.TOGGLE_ZOOM.wasPressed()) zoomInverted = !zoomInverted;
			if (!isZooming() && zoomUpdated) {
				ConfigHelper.saveConfig();
				zoomUpdated = false;
			}
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to tick zoom: {}", Data.VERSION.getLoggerPrefix(), error);
		}
	}
	public static void updateZoomMultiplier() {
		prevZoomMultiplier = zoomMultiplier;
		zoomMultiplier = getZoomTypeMultiplier();
		if (ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_transition").equals("smooth")) {
			zoomMultiplier = (prevZoomMultiplier + zoomMultiplier) * 0.5;
		}
	}
	public static float getZoomTypeMultiplier() {
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
		if (getZoomType() != null) {
			List<Identifier> list = new ArrayList<>();
			zoomTypes.forEach((zoomType) -> {
				list.add(zoomType.getFirst());
			});
			return IdentifierHelper.stringFromIdentifier(list.contains(getZoomType().getFirst()) ? list.get((list.indexOf(getZoomType().getFirst()) + 1) % list.size()) : list.get(0));
		}
		return IdentifierHelper.stringFromIdentifier(zoomTypes.get(0).getFirst());
	}
}