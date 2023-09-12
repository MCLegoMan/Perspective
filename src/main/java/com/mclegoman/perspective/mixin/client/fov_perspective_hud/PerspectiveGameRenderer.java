/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.fov_perspective_hud;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.util.PerspectiveHideHUD;
import com.mclegoman.perspective.client.zoom.PerspectiveZoom;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = GameRenderer.class)
public abstract class PerspectiveGameRenderer {
    @Shadow private boolean renderingPanorama;
    @Shadow private float lastFovMultiplier;
    @Shadow private float fovMultiplier;

    @Shadow public abstract boolean isRenderingPanorama();

    @Inject(at = @At("HEAD"), method = "shouldRenderBlockOutline", cancellable = true)
    private void perspective$renderBlockOutline(CallbackInfoReturnable<Boolean> cir) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) cir.setReturnValue(false);
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to GameRenderer$renderCrosshair.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;getFov(Lnet/minecraft/client/render/Camera;FZ)D"), method = "renderHand")
    private double perspective$renderHand(double fov) {
        return perspective$getFovWithoutZoom(PerspectiveClientData.CLIENT.gameRenderer.getCamera(), PerspectiveClientData.CLIENT.getTickDelta(), false);
    }
    @ModifyReturnValue(method = "getFov", at = @At("RETURN"))
    private double perspective$getFov(double fov, Camera camera, float tickDelta, boolean changingFov) {
        double newFOV = fov;
        if (!this.isRenderingPanorama()) {
            if (PerspectiveZoom.isZooming() && PerspectiveConfigHelper.getConfig("zoom_mode").equals("instant")) {
                newFOV = PerspectiveZoom.limitedZoomFov(fov);
            }
        }
        return newFOV;
    }
    private double perspective$getFovWithoutZoom(Camera camera, float tickDelta, boolean changingFov) {
        if (this.renderingPanorama) {
            return 90.0;
        } else {
            double d = 70.0;
            if (changingFov) {
                d = (double) PerspectiveClientData.CLIENT.options.getFov().getValue();
                d *= MathHelper.lerp(tickDelta, this.lastFovMultiplier, this.fovMultiplier);
            }
            if (camera.getFocusedEntity() instanceof LivingEntity && ((LivingEntity)camera.getFocusedEntity()).isDead()) {
                float f = Math.min((float)((LivingEntity)camera.getFocusedEntity()).deathTime + tickDelta, 20.0F);
                d /= (1.0F - 500.0F / (f + 500.0F)) * 2.0F + 1.0F;
            }
            CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
            if (cameraSubmersionType == CameraSubmersionType.LAVA || cameraSubmersionType == CameraSubmersionType.WATER) {
                d *= MathHelper.lerp(PerspectiveClientData.CLIENT.options.getFovEffectScale().getValue(), 1.0, 0.8571428656578064);
            }
            return d;
        }
    }
}