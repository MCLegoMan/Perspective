/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.fov_perspective_hud;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.util.PerspectiveHideHUD;
import com.mclegoman.perspective.client.zoom.PerspectiveZoom;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = GameRenderer.class)
public abstract class PerspectiveGameRenderer {
    @Shadow private boolean renderingPanorama;

    @Shadow public abstract void loadProjectionMatrix(Matrix4f projectionMatrix);

    @Shadow public abstract Matrix4f getBasicProjectionMatrix(double fov);

    @Shadow protected abstract void tiltViewWhenHurt(MatrixStack matrices, float tickDelta);

    @Shadow protected abstract void bobView(MatrixStack matrices, float tickDelta);

    @Shadow @Final private LightmapTextureManager lightmapTextureManager;

    @Shadow @Final public HeldItemRenderer firstPersonRenderer;

    @Shadow @Final private BufferBuilderStorage buffers;

    @Shadow private float lastFovMultiplier;

    @Shadow private float fovMultiplier;

    @Inject(at = @At("HEAD"), method = "shouldRenderBlockOutline", cancellable = true)
    private void perspective$renderBlockOutline(CallbackInfoReturnable<Boolean> cir) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) cir.setReturnValue(false);
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to GameRenderer$renderCrosshair.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(at = @At("HEAD"), method = "renderHand", cancellable = true)
    private void perspective$renderHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        try {
            if (!PerspectiveHideHUD.shouldHideHUD()) {
                    this.loadProjectionMatrix(this.getBasicProjectionMatrix(perspective$getFovWithoutZoom(camera, tickDelta, false)));
                    matrices.loadIdentity();
                    matrices.push();
                    this.tiltViewWhenHurt(matrices, tickDelta);
                    if (PerspectiveClientData.CLIENT.options.getBobView().getValue()) {
                        this.bobView(matrices, tickDelta);
                    }

                    boolean bl = PerspectiveClientData.CLIENT.getCameraEntity() instanceof LivingEntity && ((LivingEntity)PerspectiveClientData.CLIENT.getCameraEntity()).isSleeping();
                    if (PerspectiveClientData.CLIENT.interactionManager != null) {
                        if (PerspectiveClientData.CLIENT.options.getPerspective().isFirstPerson() && !bl && !PerspectiveClientData.CLIENT.options.hudHidden && PerspectiveClientData.CLIENT.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR) {
                            this.lightmapTextureManager.enable();
                            this.firstPersonRenderer.renderItem(tickDelta, matrices, this.buffers.getEntityVertexConsumers(), PerspectiveClientData.CLIENT.player, PerspectiveClientData.CLIENT.getEntityRenderDispatcher().getLight(PerspectiveClientData.CLIENT.player, tickDelta));
                            this.lightmapTextureManager.disable();
                        }
                    }

                    matrices.pop();
                    if (PerspectiveClientData.CLIENT.options.getPerspective().isFirstPerson() && !bl) {
                        InGameOverlayRenderer.renderOverlays(PerspectiveClientData.CLIENT, matrices);
                        this.tiltViewWhenHurt(matrices, tickDelta);
                    }

                    if (PerspectiveClientData.CLIENT.options.getBobView().getValue()) {
                        this.bobView(matrices, tickDelta);
                    }
            }
            ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to GameRenderer$renderHand.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
    @Inject(method = "getFov", at = @At("HEAD"), cancellable = true)
    private void perspective$getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        try {
            if (this.renderingPanorama) {
                cir.setReturnValue(90.0);
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

                if (PerspectiveClientData.CLIENT.player != null) {
                    if (!PerspectiveClientData.CLIENT.player.isUsingSpyglass()) {
                        if (PerspectiveZoom.isZooming()) {
                            d = PerspectiveZoom.getZoom(true, (boolean)PerspectiveConfigHelper.getConfig("smooth_zoom"), d);
                        }
                        if (!PerspectiveZoom.isZooming() && PerspectiveZoom.CURRENT_FOV != d) {
                            d = PerspectiveZoom.getZoom(false, (boolean)PerspectiveConfigHelper.getConfig("smooth_zoom"), d);
                        }
                    } else {
                        PerspectiveZoom.stopZoom(d);
                    }
                }
                cir.setReturnValue(d);
            }
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to GameRenderer$getFov.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
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