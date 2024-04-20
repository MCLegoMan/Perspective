/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.hud;

import com.mclegoman.perspective.client.april_fools_prank.AprilFoolsPrank;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.shaders.ShaderDataLoader;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.Update;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class DebugOverlay {
	public static Type debugType = Type.none;
	public static Formatting shaderColor;

	public static void renderDebugHUD(DrawContext context) {
		int y = 2;
		int x = 2;
		List<Object> debugText = new ArrayList<>();
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "version_overlay")) {
			debugText.add("\n");
		}
		debugText.add(Text.literal(Data.version.getName() + " " + Data.version.getFriendlyString(false)));
		if (debugType.equals(Type.misc)) {
			debugText.add("\n");
			debugText.add(Text.literal("debug: " + ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "debug")));
			debugText.add(Text.literal("isAprilFools(): " + AprilFoolsPrank.isAprilFools()));
			debugText.add(Text.literal("isSaving(): " + ConfigHelper.isSaving()));
			debugText.add(Text.literal("isZooming(): " + Zoom.isZooming()));
			debugText.add(Text.literal("getZoomLevel(): " + Zoom.getZoomLevel()));
			debugText.add(Translation.getCombinedText(Text.literal("getZoomType(): "), Translation.getZoomTypeTranslation(Zoom.getZoomType().getNamespace(), Zoom.getZoomType().getPath())));
			debugText.add(Text.literal("Newer Version Found: " + Update.newerVersionFound));
			debugText.add("\n");
			debugText.add(Text.literal("Super Secret Settings").formatted(Formatting.BOLD, shaderColor));
			debugText.add(Text.literal("shader: " + ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_shader")));
			debugText.add(Text.literal("disable_screen_mode: " + Shader.get(ShaderDataLoader.RegistryValue.disableScreenMode)));
			debugText.add(Text.literal("render_type: " + Shader.renderType));
			debugText.add(Text.literal("use_depth: " + Shader.useDepth));
			debugText.add(Text.literal("shouldDisableScreenMode(): " + Shader.shouldDisableScreenMode()));
			debugText.add(Text.literal("shouldRenderShader(): " + Shader.shouldRenderShader()));
			debugText.add(Text.literal("shouldDisableImprovedDepthRenderer(): " + Shader.shouldDisableImprovedDepthRenderer()));
		}
		if (debugType.equals(Type.config) || debugType.equals(Type.experimentalConfig) || debugType.equals(Type.tutorialsConfig) || debugType.equals(Type.warningsConfig)) {
			debugText.add("\n");
			debugText.add(Text.literal("Latest Saved Config Values").formatted(Formatting.BOLD));
			if (debugType.equals(Type.config)) debugText.addAll(ConfigHelper.getDebugConfigText(ConfigHelper.ConfigType.NORMAL));
			if (debugType.equals(Type.experimentalConfig)) debugText.addAll(ConfigHelper.getDebugConfigText(ConfigHelper.ConfigType.EXPERIMENTAL));
			if (debugType.equals(Type.tutorialsConfig)) debugText.addAll(ConfigHelper.getDebugConfigText(ConfigHelper.ConfigType.TUTORIAL));
			if (debugType.equals(Type.warningsConfig)) debugText.addAll(ConfigHelper.getDebugConfigText(ConfigHelper.ConfigType.WARNING));
		}
		for (Object item : debugText) {
			if (item instanceof Text text) {
				TextRenderer textRenderer = ClientData.minecraft.textRenderer;
				int width = textRenderer.getWidth(text);
				if (y > ClientData.minecraft.getWindow().getScaledHeight() - 2 - 9) {
					y = 2;
					x += 256;
				}
				context.fill(x - 1, y - 1, x + width + 1, y + 9, -1873784752);
				context.drawText(ClientData.minecraft.textRenderer, text, x, y, 0xffffff, false);
				y = HUDHelper.addY(y);
			} else {
				if (item.equals("\n")) {
					y = HUDHelper.addY(y);
				}
			}
		}
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
			return values[(this.ordinal() - 1) < 0 ? values.length - 1 : this.ordinal() - 1];
		}
		public Type next() {
			return values[(this.ordinal() + 1) % values.length];
		}
	}
}