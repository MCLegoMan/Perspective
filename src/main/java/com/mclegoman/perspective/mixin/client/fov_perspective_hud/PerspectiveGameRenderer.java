/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.fov_perspective_hud;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.util.PerspectiveHideHUD;
import com.mclegoman.perspective.client.zoom.PerspectiveZoom;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = GameRenderer.class)
public abstract class PerspectiveGameRenderer {
    @Shadow private boolean renderingPanorama;
    @Inject(method = "getFov", at = @At("HEAD"), cancellable = true)
    private void perspective$getFov(CallbackInfoReturnable<Double> ci) {
        try {
            if (!this.renderingPanorama) {
                if ((boolean) PerspectiveConfigHelper.getConfig("smooth_zoom")) {
                    if (PerspectiveZoom.isZooming() && PerspectiveZoom.smoothZoom > PerspectiveZoom.zoomFOV(PerspectiveClientData.CLIENT)) PerspectiveZoom.setSmoothZoom(PerspectiveClientData.CLIENT, (int)PerspectiveConfigHelper.getConfig("smooth_zoom_scale"), true);
                    if (!PerspectiveZoom.isZooming() && PerspectiveZoom.smoothZoom < PerspectiveClientData.CLIENT.options.getFov().getValue()) PerspectiveZoom.setSmoothZoom(PerspectiveClientData.CLIENT, (int)PerspectiveConfigHelper.getConfig("smooth_zoom_scale"), false);
                    if (PerspectiveZoom.isZooming()) ci.setReturnValue(PerspectiveZoom.getZoomFOV(PerspectiveClientData.CLIENT, true, true));
                    else if (!PerspectiveZoom.isZooming() && PerspectiveZoom.smoothZoom != PerspectiveClientData.CLIENT.options.getFov().getValue()) ci.setReturnValue(PerspectiveZoom.getZoomFOV(PerspectiveClientData.CLIENT, false, true));
                } else {
                    if (PerspectiveZoom.isZooming()) ci.setReturnValue(PerspectiveZoom.getZoomFOV(PerspectiveClientData.CLIENT, true, false));
                }
            }
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to GameRenderer$getFov.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(at = @At("HEAD"), method = "shouldRenderBlockOutline", cancellable = true)
    private void perspective$renderBlockOutline(CallbackInfoReturnable<Boolean> cir) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) cir.setReturnValue(false);
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to GameRenderer$renderCrosshair.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(at = @At("HEAD"), method = "renderHand", cancellable = true)
    private void perspective$renderHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to GameRenderer$renderHand.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
}