/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.zoom;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 10000, value = Mouse.class)
public abstract class MouseMixin {
    @Shadow private double eventDeltaVerticalWheel;

    @Inject(at = @At("HEAD"), method = "onMouseScroll", cancellable = true)
    private void perspective$onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        try {
            if (ClientData.CLIENT.currentScreen == null) {
                if (Zoom.isZooming()) {
                    double d = (ClientData.CLIENT.options.getDiscreteMouseScroll().getValue() ? Math.signum(vertical) : vertical) * ClientData.CLIENT.options.getMouseWheelSensitivity().getValue();
                    if (this.eventDeltaVerticalWheel != 0.0 && Math.signum(d) != Math.signum(this.eventDeltaVerticalWheel)) {
                        this.eventDeltaVerticalWheel = 0.0;
                    }
                    this.eventDeltaVerticalWheel += d;
                    int i = (int)this.eventDeltaVerticalWheel;
                    if (i == 0) {
                        return;
                    }
                    this.eventDeltaVerticalWheel -= i;
                    Zoom.zoom(i > 0, (int) ConfigHelper.getConfig("zoom_increment_size"));
                    ci.cancel();
                }
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to Mouse$onMouseScroll: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    @Inject(at = @At("HEAD"), method = "onMouseButton", cancellable = true)
    private void perspective$onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        try {
            if (Zoom.isZooming()) {
                if (button == 2) {
                    Zoom.reset(ClientData.CLIENT);
                    ci.cancel();
                }
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to Mouse$onMouseButton: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
}