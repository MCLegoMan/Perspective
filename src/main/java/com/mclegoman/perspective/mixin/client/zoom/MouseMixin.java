/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.zoom;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.client.util.SmoothUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 10000, value = Mouse.class)
public abstract class MouseMixin {
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
    @ModifyExpressionValue(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/Perspective;isFirstPerson()Z"))
    private boolean perspective$isFirstPerson(boolean isFirstPerson) {
        return ConfigHelper.getConfig("zoom_camera_mode").equals("spyglass") ? (isFirstPerson || Zoom.isZooming()) : isFirstPerson;
    }
    @ModifyExpressionValue(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingSpyglass()Z"))
    private boolean perspective$isUsingSpyglass(boolean isUsingSpyglass) {
        return ConfigHelper.getConfig("zoom_camera_mode").equals("spyglass") ? (isUsingSpyglass || Zoom.isZooming()) : isUsingSpyglass;
    }
}