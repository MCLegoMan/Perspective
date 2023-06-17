/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.mixin;

import com.mclegoman.perspective.client.config.PerspectiveConfig;
import com.mclegoman.perspective.client.util.PerspectiveZoomUtils;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class PerspectiveMouse {@Inject(method = "tick", at = @At("TAIL"))
private void tickMouse(CallbackInfo ci) {
    try {
        if (PerspectiveZoomUtils.isZooming()) {
            if (Mouse.hasWheel()) {
                int scroll = Mouse.getDWheel();
                if (scroll != 0) {
                    PerspectiveZoomUtils.OVERLAY = PerspectiveConfig.OVERLAY_DELAY;
                    if (scroll > 0) PerspectiveZoomUtils.zoom(true);
                    else if (scroll < 0) PerspectiveZoomUtils.zoom(false);
                }
            }
        }
    } catch (Exception e) {
        PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst changing zoom level.");
        PerspectiveData.LOGGER.error(e.getLocalizedMessage());
    }
}
}