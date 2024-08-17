/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.hud;

import com.mclegoman.luminance.common.util.IdentifierHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigHelper;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class Overlays {
	public static Text getEntityPositionTextTitle() {
		return Translation.getTranslation(Data.version.getID(), "position.title");
	}
	public static Text getEntityPositionTextDescription(Vec3d pos) {
		return Translation.getTranslation(Data.version.getID(), "position.description", new Object[]{
				(int) pos.getX(),
				(int) pos.getY(),
				(int) pos.getZ(),
		});
	}
	public static void renderOverlays(DrawContext context) {
		if (!ClientData.minecraft.getDebugHud().shouldShowDebugHud() && !ClientData.minecraft.options.hudHidden && !HUDHelper.shouldHideHUD()) {
			if (DebugOverlay.debugType.equals(DebugOverlay.Type.none)) {
				// Version Overlay
				if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "version_overlay"))
					context.drawTextWithShadow(ClientData.minecraft.textRenderer, Translation.getTranslation(Data.version.getID(), "version_overlay", new Object[]{SharedConstants.getGameVersion().getName()}), 2, 2, 0xffffff);
				// Position and Day Overlays
				int y = 40;
				List<Text> overlayTexts = new ArrayList<>();
				if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "position_overlay")) {
					if (ClientData.minecraft.player != null) {
						overlayTexts.add(Translation.getTranslation(Data.version.getID(), "position_overlay", new Object[]{
								getEntityPositionTextTitle(),
								getEntityPositionTextDescription(ClientData.minecraft.player.getPos())
						}));
					}
				}
				if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "day_overlay")) {
					if (ClientData.minecraft.world != null) {
						overlayTexts.add(Translation.getTranslation(Data.version.getID(), "day_overlay", new Object[]{
								ClientData.minecraft.world.getTimeOfDay() / 24000L
						}));
					}
				}
				if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "biome_overlay")) {
					if (ClientData.minecraft.player != null && ClientData.minecraft.world != null) {
						String biome = ClientData.minecraft.world.getBiome(ClientData.minecraft.player.getBlockPos()).getKeyOrValue().map((biomeKey) -> biomeKey.getValue().toString(), (biome_) -> "[unregistered " + biome_ + "]");
						overlayTexts.add(Translation.getTranslation(Data.version.getID(), "biome_overlay", new Object[]{
								Translation.getText("biome." + IdentifierHelper.getStringPart(IdentifierHelper.Type.NAMESPACE, biome) + "." + IdentifierHelper.getStringPart(IdentifierHelper.Type.KEY, biome), true)
						}));
					}
				}
				renderOverlays(context, overlayTexts, 0, y, false);
			} else DebugOverlay.renderDebugHUD(context);
		}
	}
	public static void renderOverlay(DrawContext context, int x, int y, Text text) {
		renderOverlay(context, x, y, text, -1873784752, 0xffffff, false);
	}
	public static void renderOverlay(DrawContext context, int x, int y, Text text, int backgroundColor, int textColor, boolean shadow) {
		context.fill(x, y, x + ClientData.minecraft.textRenderer.getWidth(text) + 4, y + 12, backgroundColor);
		context.drawText(ClientData.minecraft.textRenderer, text, x + 2, y + 2, textColor, shadow);
	}
	public static void renderOverlays(DrawContext context, List<Text> overlays, int x, int y, boolean wrap) {
		renderOverlays(context, overlays, x, y, wrap, 0);
	}
	public static void renderOverlays(DrawContext context, List<Text> overlays, int x, int y, boolean wrap, int wrapY) {
		int wrapX = 0;
		for (Text overlay : overlays) {
			if (!overlay.equals(Text.empty())) {
				wrapX = Math.max(wrapX, ClientData.minecraft.textRenderer.getWidth(overlay));
				if (wrap && (y > ClientData.minecraft.getWindow().getScaledHeight() - 2 - 9)) {
					y = wrapY;
					x += (wrapX + 4);
				}
				Overlays.renderOverlay(context, x, y, overlay);
			}
			y = HUDHelper.addY(y);
		}
	}
}