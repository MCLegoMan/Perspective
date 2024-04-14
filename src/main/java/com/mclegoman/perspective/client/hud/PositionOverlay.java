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

public class PositionOverlay {
	public static void render(DrawContext context) {
		if (ClientData.CLIENT.player != null) {
			boolean debugEnabled = !DebugOverlay.debugType.equals(DebugOverlay.Type.none);
			Text pos = Translation.getTranslation(Data.VERSION.getID(), "position_overlay", new Object[]{
					(int) ClientData.CLIENT.player.getPos().getX(),
					(int) ClientData.CLIENT.player.getPos().getY(),
					(int) ClientData.CLIENT.player.getPos().getZ(),
			});
			int x = debugEnabled ? ClientData.CLIENT.getWindow().getScaledWidth() - 2 : 2;
			int y = 36;
			context.fill(debugEnabled ? x - ClientData.CLIENT.textRenderer.getWidth(pos) - 1 : x - 2, y - 2, debugEnabled ? x + 2 : x + ClientData.CLIENT.textRenderer.getWidth(pos) + 1, y + 9, -1873784752);
			context.drawText(ClientData.CLIENT.textRenderer, pos, debugEnabled ? x - ClientData.CLIENT.textRenderer.getWidth(pos) + 1 : x, y, 0xffffff, false);
		}
	}
}