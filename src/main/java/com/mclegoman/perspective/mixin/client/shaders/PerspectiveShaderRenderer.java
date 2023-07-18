/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.shaders;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.shaders.PerspectiveShader;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class PerspectiveShaderRenderer {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;beginWrite(Z)V"))
    private void render$game(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (!(boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_mode") && shouldRender()) PerspectiveShader.postProcessor.render(tickDelta);
    }
    @Inject(method = "render", at = @At(value = "TAIL"))
    private void render$overlay(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if ((boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_mode") && shouldRender()) PerspectiveShader.postProcessor.render(tickDelta);
    }
    @Inject(method = "onResized", at = @At(value = "TAIL"))
    private void onResized(int width, int height, CallbackInfo ci) {
        if (PerspectiveShader.postProcessor != null) {
            PerspectiveShader.postProcessor.setupDimensions(width, height);
        }
    }
    private boolean shouldRender() {
        return PerspectiveShader.postProcessor != null && (boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_enabled");
    }
}