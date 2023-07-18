package com.mclegoman.perspective.mixin.client.supersecretsettings;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.super_secret_settings.PerspectiveSuperSecretSettingsUtil;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class PerspectiveGameRenderer {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;beginWrite(Z)V"))
    private void render$game(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if (!(boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_mode") && shouldRender()) PerspectiveSuperSecretSettingsUtil.postProcessor.render(tickDelta);
    }
    @Inject(method = "render", at = @At(value = "TAIL"))
    private void render$overlay(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if ((boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_mode") && shouldRender()) PerspectiveSuperSecretSettingsUtil.postProcessor.render(tickDelta);
    }
    @Inject(method = "onResized", at = @At(value = "TAIL"))
    private void onResized(int width, int height, CallbackInfo ci) {
        if (PerspectiveSuperSecretSettingsUtil.postProcessor != null) {
            PerspectiveSuperSecretSettingsUtil.postProcessor.setupDimensions(width, height);
        }
    }
    private boolean shouldRender() {
        return PerspectiveSuperSecretSettingsUtil.postProcessor != null && (boolean)PerspectiveConfigHelper.getConfig("super_secret_settings_enabled");
    }
}