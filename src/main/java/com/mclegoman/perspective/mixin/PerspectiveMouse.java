package com.mclegoman.perspective.mixin;

import com.mclegoman.perspective.data.PerspectiveData;
import com.mclegoman.perspective.registry.PerspectiveKeybindRegistry;
import com.mclegoman.perspective.util.PerspectiveUtils;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class PerspectiveMouse {
    @Shadow @Final private MinecraftClient client;

    @Inject(at = @At("HEAD"), method = "onMouseScroll", cancellable = true)
    private void onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (PerspectiveKeybindRegistry.IS_ZOOMING) {
            double scroll = (client.options.getDiscreteMouseScroll().getValue() ? Math.signum(vertical) : vertical) * client.options.getMouseWheelSensitivity().getValue();
            PerspectiveData.LOGGER.info(String.valueOf(scroll));
            if (scroll > 0) {
                PerspectiveUtils.zoomIn();
                PerspectiveUtils.SHOW_ZOOM_LEVEL = 180;
            }
            else if (scroll < 0) {
                PerspectiveUtils.zoomOut();
                PerspectiveUtils.SHOW_ZOOM_LEVEL = 180;
            }
            ci.cancel();
        }
    }
}