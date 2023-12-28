/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.zoom;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.hud.HUDHelper;
import com.mclegoman.perspective.client.zoom.Zoom;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 10000, value = GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow
	public abstract boolean isRenderingPanorama();
	@ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;getFov(Lnet/minecraft/client/render/Camera;FZ)D"), method = "renderHand")
	private double perspective$renderHand(double fov) {
		return Zoom.fov;
	}
	@Inject(method = "tick", at = @At("TAIL"))
	private void perspective$tick(CallbackInfo ci) {
		Zoom.updateZoomMultiplier();
	}
	@ModifyReturnValue(method = "getFov", at = @At("RETURN"))
	private double perspective$getFov(double fov, Camera camera, float tickDelta, boolean changingFov) {
		Zoom.fov = fov;
		double newFOV = fov;
		if (!this.isRenderingPanorama()) {
			if (Zoom.isZooming()) {
				if (ConfigHelper.getConfig("zoom_transition").equals("instant")) {
					newFOV *= Zoom.getZoomMultiplier();
				}
			}
			if (ConfigHelper.getConfig("zoom_transition").equals("smooth")) {
				newFOV *= MathHelper.lerp(tickDelta, Zoom.prevZoomMultiplier, Zoom.zoomMultiplier);
			}
		}
		return Zoom.limitFov(newFOV);
	}
	@Inject(at = @At("HEAD"), method = "renderHand", cancellable = true)
	private void perspective$renderHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
		if (HUDHelper.shouldHideHUD()) ci.cancel();
	}
}