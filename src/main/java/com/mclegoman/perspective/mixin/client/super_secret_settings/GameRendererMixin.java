/*
    Perspective
    Contributor(s): MCLegoMan, Nettakrim
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.super_secret_settings;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.zoom.Zoom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.GameMode;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 10000, value = GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow private boolean renderHand;
    @Shadow public abstract void loadProjectionMatrix(Matrix4f projectionMatrix);
    @Shadow public abstract Matrix4f getBasicProjectionMatrix(double fov);
    @Shadow protected abstract void tiltViewWhenHurt(MatrixStack matrices, float tickDelta);
    @Shadow protected abstract void bobView(MatrixStack matrices, float tickDelta);
    @Shadow @Final private LightmapTextureManager lightmapTextureManager;
    @Shadow @Final public HeldItemRenderer firstPersonRenderer;
    @Shadow @Final private BufferBuilderStorage buffers;
    @Shadow protected abstract double getFov(Camera camera, float tickDelta, boolean changingFov);
    @Shadow public abstract void tick();
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;beginWrite(Z)V", ordinal = 0))
    private void perspective$render_game(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (Shader.shouldRenderShader() && (String.valueOf(ConfigHelper.getConfig("super_secret_settings_mode")).equalsIgnoreCase("game") || Shader.shouldDisableScreenMode())) {
            Shader.render(tickDelta, Shader.USE_DEPTH ? "game:depth" : "game");
            if (Shader.USE_DEPTH && renderHand) {
                this.perspective$renderHand(ClientData.CLIENT.gameRenderer.getCamera(), tickDelta);
            }
        }
    }

    @Inject(method = "render", at = @At(value = "TAIL"))
    private void perspective$render_overlay(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (Shader.shouldRenderShader() && String.valueOf(ConfigHelper.getConfig("super_secret_settings_mode")).equalsIgnoreCase("screen") && !Shader.shouldDisableScreenMode())
            Shader.render(tickDelta, "screen");
    }

    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void perspective$renderHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        if (Shader.shouldRenderShader() && Shader.USE_DEPTH && renderHand) ci.cancel();
    }

    @Inject(method = "onResized", at = @At(value = "TAIL"))
    private void perspective$onResized(int width, int height, CallbackInfo ci) {
        if (Shader.postProcessor != null) {
            Shader.postProcessor.setupDimensions(width, height);
        }
        if (Shader.DEPTH_FRAME_BUFFER == null) {
            Shader.DEPTH_FRAME_BUFFER = new SimpleFramebuffer(width, height, true, MinecraftClient.IS_SYSTEM_MAC);
        } else {
            Shader.DEPTH_FRAME_BUFFER.resize(width, height, false);
        }
    }

    @Unique
    private void perspective$renderHand(Camera camera, float tickDelta) {
        MatrixStack matrices = new MatrixStack();
        if (!ClientData.CLIENT.gameRenderer.isRenderingPanorama()) {
            this.loadProjectionMatrix(this.getBasicProjectionMatrix(getFov(camera, tickDelta, false)));
            matrices.loadIdentity();
            matrices.push();
            this.tiltViewWhenHurt(matrices, tickDelta);
            if (ClientData.CLIENT.options.getBobView().getValue()) {
                this.bobView(matrices, tickDelta);
            }

            boolean isSleeping = ClientData.CLIENT.getCameraEntity() instanceof LivingEntity && ((LivingEntity)ClientData.CLIENT.getCameraEntity()).isSleeping();
            if (ClientData.CLIENT.interactionManager != null) {
                if (ClientData.CLIENT.options.getPerspective().isFirstPerson() && !isSleeping && !ClientData.CLIENT.options.hudHidden && ClientData.CLIENT.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR) {
                    this.lightmapTextureManager.enable();
                    this.firstPersonRenderer.renderItem(tickDelta, matrices, this.buffers.getEntityVertexConsumers(), ClientData.CLIENT.player, ClientData.CLIENT.getEntityRenderDispatcher().getLight(ClientData.CLIENT.player, tickDelta));
                    this.lightmapTextureManager.disable();
                }
            }

            matrices.pop();
            if (ClientData.CLIENT.options.getPerspective().isFirstPerson() && !isSleeping) {
                InGameOverlayRenderer.renderOverlays(ClientData.CLIENT, matrices);
                this.tiltViewWhenHurt(matrices, tickDelta);
            }

            if (ClientData.CLIENT.options.getBobView().getValue()) {
                this.bobView(matrices, tickDelta);
            }

        }
    }
}