package com.mclegoman.perspective.client.mixin;

import com.mclegoman.perspective.client.util.PerspectiveZoomUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class PerspectiveMouse {
    @Shadow @Final private MinecraftClient client;

    @Inject(at = @At("HEAD"), method = "onMouseScroll", cancellable = true)
    private void onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (PerspectiveZoomUtils.isZooming()) {
            double scroll = (client.options.getDiscreteMouseScroll().getValue() ? Math.signum(vertical) : vertical) * client.options.getMouseWheelSensitivity().getValue();
            if (scroll > 0) PerspectiveZoomUtils.in();
            else if (scroll < 0) PerspectiveZoomUtils.out();
            ci.cancel();
        }
    }
}