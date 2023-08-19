/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.fov_perspective_hud;

import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.zoom.PerspectiveZoom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = Mouse.class)
public abstract class PerspectiveAdjustZoom {
    @Inject(at = @At("HEAD"), method = "onMouseScroll", cancellable = true)
    private void perspective$onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (PerspectiveZoom.isZooming()) {
            double scroll = (PerspectiveClientData.CLIENT.options.getDiscreteMouseScroll().getValue() ? Math.signum(vertical) : vertical) * PerspectiveClientData.CLIENT.options.getMouseWheelSensitivity().getValue();
            if (scroll > 0) PerspectiveZoom.zoom(true);
            else if (scroll < 0) PerspectiveZoom.zoom(false);
            ci.cancel();
        }
    }
    @Inject(at = @At("HEAD"), method = "onMouseButton", cancellable = true)
    private void perspective$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if (PerspectiveZoom.isZooming()) {
            if (button == 2) {
                PerspectiveZoom.reset(PerspectiveClientData.CLIENT);
                ci.cancel();
            }
        }
    }
}