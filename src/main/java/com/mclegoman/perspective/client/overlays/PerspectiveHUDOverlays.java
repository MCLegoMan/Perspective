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
import net.minecraft.text.Text;

public class PerspectiveHUDOverlays {
    public static Text MESSAGE;
    public static float REMAINING;
    public static void init() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            if (!PerspectiveHideHUD.shouldHideHUD()) {
                if (!PerspectiveClientData.CLIENT.options.debugEnabled) {
                    if ((boolean)PerspectiveConfigHelper.getConfig("version_overlay")) context.drawTextWithShadow(PerspectiveClientData.CLIENT.textRenderer, PerspectiveTranslation.getTranslation("version_overlay", new Object[]{SharedConstants.getGameVersion().getName()}), 2, 2, 0xffffff);
                }
            }
        });
    }
    public static void tick(MinecraftClient client) {
        if (REMAINING > 0) REMAINING -= 1;
    }
    public static void setOverlay(Text text) {
        MESSAGE = text;
        REMAINING = 40;
    }
}
