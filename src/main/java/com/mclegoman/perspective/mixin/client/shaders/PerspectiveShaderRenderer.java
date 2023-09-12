/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.shaders;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.shaders.PerspectiveShader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 10000, value = GameRenderer.class)
public class PerspectiveShaderRenderer {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;beginWrite(Z)V"))
    private void perspective$render_game(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (PerspectiveShader.shouldRenderShader() && String.valueOf(PerspectiveConfigHelper.getConfig("super_secret_settings_mode")).equalsIgnoreCase("game") || PerspectiveShader.shouldDisableScreenMode()) PerspectiveShader.render(tickDelta);
    }
    @Inject(method = "render", at = @At(value = "TAIL"))
    private void perspective$render_overlay(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (PerspectiveShader.shouldRenderShader() && String.valueOf(PerspectiveConfigHelper.getConfig("super_secret_settings_mode")).equalsIgnoreCase("screen") && !PerspectiveShader.shouldDisableScreenMode()) PerspectiveShader.render(tickDelta);
    }
    @Inject(method = "onResized", at = @At(value = "TAIL"))
    private void perspective$onResized(int width, int height, CallbackInfo ci) {
        if (PerspectiveShader.postProcessor != null) {
            PerspectiveShader.postProcessor.setupDimensions(width, height);
        }
        /*
            Perspective
            Author: MCLegoMan
            Github: https://github.com/MCLegoMan/Perspective

            The following code was forked from Souper Secret Settings by Nettakrim.
            Licensed under GNU Lesser General Public License v3.0
            https://github.com/Nettakrim/Souper-Secret-Settings
        */
        if (PerspectiveShader.DEPTH_FRAME_BUFFER == null) {
            PerspectiveShader.DEPTH_FRAME_BUFFER = new SimpleFramebuffer(width, height, true, MinecraftClient.IS_SYSTEM_MAC);
        } else {
            PerspectiveShader.DEPTH_FRAME_BUFFER.resize(width, height, false);
        }
    }
}