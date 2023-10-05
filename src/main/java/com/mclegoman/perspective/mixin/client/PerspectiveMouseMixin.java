/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.zoom.PerspectiveZoom;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = Mouse.class)
public abstract class PerspectiveMouseMixin {
    @Shadow private double eventDeltaVerticalWheel;

    @Inject(at = @At("HEAD"), method = "onMouseScroll", cancellable = true)
    private void perspective$onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        try {
            if (PerspectiveClientData.CLIENT.currentScreen == null) {
                if (PerspectiveZoom.isZooming()) {
                    double d = (PerspectiveClientData.CLIENT.options.getDiscreteMouseScroll().getValue() ? Math.signum(vertical) : vertical) * PerspectiveClientData.CLIENT.options.getMouseWheelSensitivity().getValue();
                    if (this.eventDeltaVerticalWheel != 0.0 && Math.signum(d) != Math.signum(this.eventDeltaVerticalWheel)) {
                        this.eventDeltaVerticalWheel = 0.0;
                    }
                    this.eventDeltaVerticalWheel += d;
                    int i = (int)this.eventDeltaVerticalWheel;
                    if (i == 0) {
                        return;
                    }
                    this.eventDeltaVerticalWheel -= i;
                    PerspectiveZoom.zoom(i > 0, (int)PerspectiveConfigHelper.getConfig("zoom_increment_size"));
                    ci.cancel();
                }
            }
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to Mouse$onMouseScroll: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    @Inject(at = @At("HEAD"), method = "onMouseButton", cancellable = true)
    private void perspective$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        try {
            if (PerspectiveZoom.isZooming()) {
                if (button == 2) {
                    PerspectiveZoom.reset(PerspectiveClientData.CLIENT);
                    ci.cancel();
                }
            }
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to Mouse$onMouseButton: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
}