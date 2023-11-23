/*
    Perspective
    Contributor(s): MCLegoMan, Nettakrim
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.super_secret_settings;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.shaders.ShaderRegistryValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 10000, value = GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow protected abstract void renderHand(MatrixStack matrices, Camera camera, float tickDelta);

    @Shadow public abstract Camera getCamera();

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;beginWrite(Z)V"))
    private void perspective$render_game(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (Shader.shouldRenderShader() && String.valueOf(ConfigHelper.getConfig("super_secret_settings_mode")).equalsIgnoreCase("game") || (boolean)Shader.getShaderData(ShaderRegistryValue.DISABLE_SCREEN_MODE)) Shader.render(tickDelta);
    }
    @Inject(method = "render", at = @At(value = "TAIL"))
    private void perspective$render_overlay(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (Shader.shouldRenderShader() && String.valueOf(ConfigHelper.getConfig("super_secret_settings_mode")).equalsIgnoreCase("screen") && !(boolean)Shader.getShaderData(ShaderRegistryValue.DISABLE_SCREEN_MODE)) Shader.render(tickDelta);
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
}