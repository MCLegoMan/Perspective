/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.zoom;

import com.mclegoman.perspective.client.util.PerspectiveZoomUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Mouse.class)
public abstract class PerspectiveMouse {
    @Shadow @Final private MinecraftClient client;

    @Inject(at = @At("HEAD"), method = "onMouseScroll", cancellable = true)
    private void onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (PerspectiveZoomUtils.isZooming()) {
            double scroll = (client.options.getDiscreteMouseScroll().getValue() ? Math.signum(vertical) : vertical) * client.options.getMouseWheelSensitivity().getValue();
            if (scroll > 0) PerspectiveZoomUtils.zoom(true);
            else if (scroll < 0) PerspectiveZoomUtils.zoom(false);
            ci.cancel();
        }
    }
    @Inject(at = @At("HEAD"), method = "onMouseButton", cancellable = true)
    private void onClick(long window, int button, int action, int mods, CallbackInfo ci) {
        if (PerspectiveZoomUtils.isZooming()) {
            if (button == 2) {
                PerspectiveZoomUtils.reset();
                ci.cancel();
            }
        }
    }
}