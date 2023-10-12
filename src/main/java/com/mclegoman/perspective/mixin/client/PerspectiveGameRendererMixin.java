/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.shaders.PerspectiveShader;
import com.mclegoman.perspective.client.shaders.PerspectiveShaderDataLoader;
import com.mclegoman.perspective.client.shaders.PerspectiveShaderRegistryValue;
import com.mclegoman.perspective.client.util.PerspectiveHideHUD;
import com.mclegoman.perspective.client.zoom.PerspectiveZoom;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = GameRenderer.class)
public abstract class PerspectiveGameRendererMixin {
    @Shadow public abstract boolean isRenderingPanorama();
    @Inject(at = @At("HEAD"), method = "shouldRenderBlockOutline", cancellable = true)
    private void perspective$renderBlockOutline(CallbackInfoReturnable<Boolean> cir) {
        try {
            if ((PerspectiveHideHUD.shouldHideHUD()) || ((boolean)PerspectiveConfigHelper.getConfig("hide_block_outline")) || (PerspectiveShader.shouldRenderShader() && (boolean) Objects.requireNonNull(PerspectiveShaderDataLoader.get((int) PerspectiveConfigHelper.getConfig("super_secret_settings"), PerspectiveShaderRegistryValue.HIDE_BLOCK_OUTLINE)))) cir.setReturnValue(false);
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to GameRenderer$renderCrosshair.", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;getFov(Lnet/minecraft/client/render/Camera;FZ)D"), method = "renderHand")
    private double perspective$renderHand(double fov) {
        return PerspectiveZoom.fov;
    }
    @ModifyReturnValue(method = "getFov", at = @At("RETURN"))
    private double perspective$getFov(double fov, Camera camera, float tickDelta, boolean changingFov) {
        PerspectiveZoom.fov = fov;
        double newFOV = fov;
        if (!this.isRenderingPanorama()) {
            if (PerspectiveZoom.isZooming()) {
                if (PerspectiveConfigHelper.getConfig("zoom_mode").equals("instant")) {
                    newFOV *= PerspectiveZoom.getZoomMultiplier();
                }
            }
            if (PerspectiveConfigHelper.getConfig("zoom_mode").equals("smooth")) {
                newFOV *= MathHelper.lerp(tickDelta, PerspectiveZoom.prevZoomMultiplier, PerspectiveZoom.zoomMultiplier);
            }
        }
        return PerspectiveZoom.limitFov(newFOV);
    }
}