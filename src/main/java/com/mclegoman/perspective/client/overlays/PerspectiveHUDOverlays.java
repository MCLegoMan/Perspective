/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.overlays;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.client.util.PerspectiveHideHUD;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;

public class PerspectiveHUDOverlays {
    private static Text MESSAGE;
    private static float REMAINING;
    public static void init() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            TextRenderer textRenderer = PerspectiveClientData.CLIENT.textRenderer;
            float h = REMAINING - tickDelta;
            int l = (int)(h * 255.0F / 20.0F);
            if (l > 255) l = 255;
            if (l > 10) {
                context.getMatrices().push();
                context.getMatrices().translate((float)(PerspectiveClientData.CLIENT.getWindow().getScaledWidth() / 2), 27, 0.0F);
                int k = 16777215;
                int m = l << 24 & -16777216;
                int n = textRenderer.getWidth(MESSAGE);
                context.drawTextWithShadow(textRenderer, MESSAGE, -n / 2, -4, k | m);
                context.getMatrices().pop();
            }
            if (!PerspectiveHideHUD.shouldHideHUD()) {
                if (!PerspectiveClientData.CLIENT.options.debugEnabled) {
                    if ((boolean)PerspectiveConfigHelper.getConfig("version_overlay")) context.drawTextWithShadow(textRenderer, PerspectiveTranslation.getTranslation("version_overlay", new Object[]{SharedConstants.getGameVersion().getName()}), 2, 2, 0xffffff);
                }
            }
        });
    }
    public static void tick(MinecraftClient client) {
        if (REMAINING > 0) REMAINING -= 1;
    }
    public static void setOverlay(Text text) {
        MESSAGE = text;
        REMAINING = 60;
    }
}
