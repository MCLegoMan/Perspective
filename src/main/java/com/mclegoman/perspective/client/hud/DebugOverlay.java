/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.hud;

import com.mclegoman.perspective.client.events.AprilFoolsPrank;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.update.Update;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class DebugOverlay {
	public static Type debugType = Type.none;
	public static Formatting shaderColor;

	public static void renderDebugHUD(DrawContext context) {
		List<Text> debugTexts = new ArrayList<>();
		debugTexts.add(Text.literal(Data.version.getName() + " " + Data.version.getFriendlyString(false)));
		if (debugType.equals(Type.misc)) {
			debugTexts.add(Text.empty());
			debugTexts.add(Text.literal("debug: " + ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "debug")));
			debugTexts.add(Text.literal("isAprilFools(): " + AprilFoolsPrank.isAprilFools()));
			debugTexts.add(Text.literal("isSaving(): " + ConfigHelper.isSaving()));
			debugTexts.add(Text.literal("isZooming(): " + Zoom.isZooming()));
			debugTexts.add(Text.literal("getZoomLevel(): " + Zoom.getZoomLevel()));
			debugTexts.add(Translation.getCombinedText(Text.literal("getZoomType(): "), Translation.getZoomTypeTranslation(Zoom.getZoomType().getNamespace(), Zoom.getZoomType().getPath())));
			debugTexts.add(Text.literal("isNewerVersionFound(): " + Update.isNewerVersionFound()));
		}
		if (debugType.equals(Type.config) || debugType.equals(Type.experimentalConfig) || debugType.equals(Type.tutorialsConfig) || debugType.equals(Type.warningsConfig)) {
			debugTexts.add(Text.empty());
			debugTexts.add(Translation.getTranslation(Data.version.getID(), "debug.config", new Formatting[]{Formatting.BOLD}));
			if (debugType.equals(Type.config)) debugTexts.addAll(ConfigHelper.getDebugConfigText(ConfigHelper.ConfigType.normal));
			if (debugType.equals(Type.experimentalConfig)) debugTexts.addAll(ConfigHelper.getDebugConfigText(ConfigHelper.ConfigType.experimental));
			if (debugType.equals(Type.tutorialsConfig)) debugTexts.addAll(ConfigHelper.getDebugConfigText(ConfigHelper.ConfigType.tutorial));
			if (debugType.equals(Type.warningsConfig)) debugTexts.addAll(ConfigHelper.getDebugConfigText(ConfigHelper.ConfigType.warning));
		}
		Overlays.renderOverlays(context, debugTexts, 0, 0, true);
	}

	public enum Type {
		none,
		misc,
		config,
		experimentalConfig,
		tutorialsConfig,
		warningsConfig;
		private static final Type[] values = values();
		public Type prev() {
			return values[getIndex(false)];
		}
		public Type next() {
			return values[getIndex(true)];
		}
		private int getIndex(boolean forwards) {
			if (!ConfigHelper.experimentsAvailable && values[nextIndex(true)].equals(Type.experimentalConfig)) return values[nextIndex(forwards)].nextIndex(forwards);
			return nextIndex(forwards);
		}
		private int nextIndex(boolean forwards) {
			return forwards ? (this.ordinal() + 1) % values.length : (this.ordinal() - 1) < 0 ? values.length - 1 : this.ordinal() - 1;
		}
	}
}