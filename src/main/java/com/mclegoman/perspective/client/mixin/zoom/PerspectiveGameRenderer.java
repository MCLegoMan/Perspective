/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.mixin.zoom;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.util.PerspectiveZoomUtils;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class PerspectiveGameRenderer {
    @Shadow @Final
    MinecraftClient client;
    @Shadow private boolean renderingPanorama;
    @Shadow private boolean renderHand;
    @Inject(method = "getFov", at = @At("HEAD"), cancellable = true)
    private void keyZoom(CallbackInfoReturnable<Double> callbackInfo) {
        try {
            if (!this.renderingPanorama) {
                if (PerspectiveZoomUtils.isZooming()) {
                    this.renderHand = false;
                    callbackInfo.setReturnValue((double) Math.max(Math.max(1, PerspectiveConfig.ZOOM_LEVEL * client.options.getFov().getValue() / 100), Math.min(client.options.getFov().getValue(), PerspectiveConfig.ZOOM_LEVEL * client.options.getFov().getValue() / 100)));
                } else this.renderHand = true;
            }
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst zooming.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
}