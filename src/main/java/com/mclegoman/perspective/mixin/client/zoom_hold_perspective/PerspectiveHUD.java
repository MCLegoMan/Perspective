/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.zoom_hold_perspective;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.perspective.PerspectivePerspective;
import com.mclegoman.perspective.client.zoom.PerspectiveZoom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class PerspectiveHUD {
    @Shadow protected abstract void drawTextBackground(DrawContext context, TextRenderer textRenderer, int yOffset, int width, int color);
    @Inject(method="render", at=@At("HEAD"), cancellable = true)
    private void perspective$render(DrawContext context, float tickDelta, CallbackInfo ci) {
        int k;
        float h;
        int m;
        int n;
        int l;
        if (PerspectiveZoom.OVERLAY_MESSAGE != null && PerspectiveZoom.OVERLAY_REMAINING > 0) {
            h = (float)PerspectiveZoom.OVERLAY_REMAINING - tickDelta;
            l = (int)(h * 255.0F / 20.0F);
            if (l > 255) {
                l = 255;
            }
            if (l > 8) {
                context.getMatrices().push();
                context.getMatrices().translate((float)(PerspectiveClientData.CLIENT.getWindow().getScaledWidth() / 2), 27, 0.0F);
                k = 16777215;
                m = l << 24 & -16777216;
                n = PerspectiveClientData.CLIENT.textRenderer.getWidth(PerspectiveZoom.OVERLAY_MESSAGE);
                this.drawTextBackground(context, PerspectiveClientData.CLIENT.textRenderer, -4, n, 16777215 | m);
                context.drawTextWithShadow(PerspectiveClientData.CLIENT.textRenderer, PerspectiveZoom.OVERLAY_MESSAGE, -n / 2, -4, k | m);
                context.getMatrices().pop();
            }
        }
        if ((PerspectiveZoom.isZooming() || PerspectivePerspective.isHoldingPerspective()) && (boolean)PerspectiveConfigHelper.getConfig("hide_hud")) {
            ci.cancel();
        }
    }
}