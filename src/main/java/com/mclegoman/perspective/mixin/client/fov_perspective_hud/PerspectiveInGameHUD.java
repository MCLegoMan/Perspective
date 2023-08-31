/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.fov_perspective_hud;

import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.overlays.PerspectiveHUDOverlays;
import com.mclegoman.perspective.client.util.PerspectiveHideHUD;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = InGameHud.class)
public abstract class PerspectiveInGameHUD {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void perspective$render(DrawContext context, float tickDelta, CallbackInfo ci) {
        try {
            float h = PerspectiveHUDOverlays.REMAINING - tickDelta;
            int l = (int)(h * 255.0F / 20.0F);
            if (l > 255) l = 255;
            if (l > 10) {
                context.getMatrices().push();
                context.getMatrices().translate((float)(PerspectiveClientData.CLIENT.getWindow().getScaledWidth() / 2), 27, 0.0F);
                int k = 16777215;
                int m = l << 24 & -16777216;
                int n = PerspectiveClientData.CLIENT.textRenderer.getWidth(PerspectiveHUDOverlays.MESSAGE);
                context.drawTextWithShadow(PerspectiveClientData.CLIENT.textRenderer, PerspectiveHUDOverlays.MESSAGE, -n / 2, -4, k | m);
                context.getMatrices().pop();
            }
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to InGameHUD$render.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
}