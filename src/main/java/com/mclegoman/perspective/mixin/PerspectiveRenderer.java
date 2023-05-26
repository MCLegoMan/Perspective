package com.mclegoman.perspective.mixin;

import com.mclegoman.perspective.config.PerspectiveConfig;
import com.mclegoman.perspective.data.PerspectiveData;
import com.mclegoman.perspective.registry.PerspectiveKeybindRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class PerspectiveRenderer {
    @Shadow @Final
    MinecraftClient client;
    @Shadow private boolean renderingPanorama;
    @Shadow private boolean renderHand;
    @Inject(method = "getFov", at = @At("HEAD"), cancellable = true)
    private void keyZoom(CallbackInfoReturnable<Double> callbackInfo) {
        try {
            if (!this.renderingPanorama) {
                if (PerspectiveKeybindRegistry.IS_ZOOMING) {
                    this.renderHand = false;
                    int ZOOM_AMOUNT = PerspectiveConfig.ZOOM_LEVEL * client.options.getFov().getValue() / 100;
                    if (ZOOM_AMOUNT <= 0) ZOOM_AMOUNT = 1;
                    if (ZOOM_AMOUNT >= client.options.getFov().getValue()) ZOOM_AMOUNT = client.options.getFov().getValue();
                    callbackInfo.setReturnValue((double) ZOOM_AMOUNT);
                } else {
                    this.renderHand = true;
                }
            }
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
}