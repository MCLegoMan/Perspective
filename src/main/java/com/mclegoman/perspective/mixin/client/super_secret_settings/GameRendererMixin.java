/*
    Perspective
    Contributor(s): MCLegoMan, Nettakrim
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.super_secret_settings;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.shaders.Shader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 10000, value = GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawEntityOutlinesFramebuffer()V", shift = At.Shift.AFTER))
    private void perspective$render_game(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (Shader.shouldRenderShader() && (String.valueOf(ConfigHelper.getConfig("super_secret_settings_mode")).equalsIgnoreCase("game") || Shader.shouldDisableScreenMode())) {
            Shader.render(tickDelta, Shader.USE_DEPTH ? "game:depth" : "game");
        }
    }
    @Inject(method = "render", at = @At("TAIL"))
    private void perspective$render_screen(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (Shader.shouldRenderShader() && String.valueOf(ConfigHelper.getConfig("super_secret_settings_mode")).equalsIgnoreCase("screen") && !Shader.shouldDisableScreenMode())
            Shader.render(tickDelta, "screen");
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