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
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.zoom.Zoom;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 10000, value = GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow
	public abstract boolean isRenderingPanorama();

	@Shadow protected abstract void bobView(MatrixStack matrices, float tickDelta);

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
				if (ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_transition").equals("instant")) {
					newFOV *= Zoom.zoomMultiplier;
				}
			}
			if (ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_transition").equals("smooth")) {
				newFOV *= MathHelper.lerp(tickDelta, Zoom.prevZoomMultiplier, Zoom.zoomMultiplier);
			}
		}
		return Zoom.limitFov(newFOV);
	}
	@ModifyExpressionValue(method = "tiltViewWhenHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/SimpleOption;getValue()Ljava/lang/Object;"))
	private Object perspective$getDamageTiltStrength(Object value) {
		if (value instanceof Double) return (Zoom.isZooming() && ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_scale_mode").equals("scaled")) ? ((Double)value) * Math.max(Zoom.zoomMultiplier, 0.001) : value;
		return value;
	}
	@ModifyExpressionValue(method = "tiltViewWhenHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getDamageTiltYaw()F"))
	private float perspective$getDamageTiltYaw(float value) {
		return (Zoom.isZooming() && ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_scale_mode").equals("scaled")) ? (float) (value * Math.max(Zoom.zoomMultiplier, 0.001)) : value;
	}
	@Redirect(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
	private void perspective$bobViewStrideDistance(GameRenderer instance, MatrixStack matrices, float tickDelta) {
		if (Zoom.isZooming() && ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "zoom_scale_mode").equals("scaled")) {
			if (ClientData.CLIENT.player != null) {
				float f = ClientData.CLIENT.player.horizontalSpeed - ClientData.CLIENT.player.prevHorizontalSpeed;
				float g = -(ClientData.CLIENT.player.horizontalSpeed + f * tickDelta);
				float h = (float) (MathHelper.lerp(tickDelta, ClientData.CLIENT.player.prevStrideDistance, ClientData.CLIENT.player.strideDistance) * Math.max(Zoom.zoomMultiplier, 0.001));
				matrices.translate(MathHelper.sin(g * 3.1415927F) * h * 0.5F, -Math.abs(MathHelper.cos(g * 3.1415927F) * h), 0.0F);
				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(g * 3.1415927F) * h * 3.0F));
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.abs(MathHelper.cos(g * 3.1415927F - 0.2F) * h) * 5.0F));
			}
		} else this.bobView(matrices, tickDelta);
	}
}