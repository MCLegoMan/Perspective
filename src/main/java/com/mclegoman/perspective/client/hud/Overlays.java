/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.hud;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class Overlays {
	public static void renderPositionOverlay(DrawContext context) {
		if (ClientData.minecraft.player != null) {
			boolean debugEnabled = !DebugOverlay.debugType.equals(DebugOverlay.Type.none);
			Text pos = Translation.getTranslation(Data.version.getID(), "position_overlay", new Object[]{
					getEntityPositionTextTitle(),
					getEntityPositionTextDescription(ClientData.minecraft.player.getPos())
			});
			int x = debugEnabled ? ClientData.minecraft.getWindow().getScaledWidth() - 2 : 2;
			context.fill(debugEnabled ? x - ClientData.minecraft.textRenderer.getWidth(pos) - 1 : 0, 34, debugEnabled ? x + 2 : x + ClientData.minecraft.textRenderer.getWidth(pos) + 1, 45, -1873784752);
			context.drawText(ClientData.minecraft.textRenderer, pos, debugEnabled ? x - ClientData.minecraft.textRenderer.getWidth(pos) + 1 : x, 36, 0xffffff, false);
		}
	}
	public static void renderDayOverlay(DrawContext context) {
		if (ClientData.minecraft.player != null) {
			boolean debugEnabled = !DebugOverlay.debugType.equals(DebugOverlay.Type.none);
			Text pos = Translation.getTranslation(Data.version.getID(), "day_overlay", new Object[]{
					ClientData.minecraft.world != null ? ClientData.minecraft.world.getTimeOfDay() / 24000L : 0
			});
			int x = debugEnabled ? ClientData.minecraft.getWindow().getScaledWidth() - 2 : 2;
			int y = 47;
			context.fill(debugEnabled ? x - ClientData.minecraft.textRenderer.getWidth(pos) - 1 : x - 2, y - 2, debugEnabled ? x + 2 : x + ClientData.minecraft.textRenderer.getWidth(pos) + 1, y + 9, -1873784752);
			context.drawText(ClientData.minecraft.textRenderer, pos, debugEnabled ? x - ClientData.minecraft.textRenderer.getWidth(pos) + 1 : x, y, 0xffffff, false);
		}
	}
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
}