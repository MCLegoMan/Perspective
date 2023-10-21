/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client;

import com.mclegoman.perspective.client.april_fools_prank.PerspectiveAprilFoolsPrank;
import com.mclegoman.perspective.client.april_fools_prank.PerspectiveAprilFoolsPrankDataLoader;
import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.shaders.PerspectiveShader;
import com.mclegoman.perspective.client.shaders.PerspectiveShaderDataLoader;
import com.mclegoman.perspective.client.shaders.PerspectiveShaderRegistryValue;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = LivingEntityRenderer.class)
public class PerspectiveLivingEntityRendererMixin {
    @Inject(at = @At("RETURN"), method = "shouldFlipUpsideDown", cancellable = true)
    private static void perspective$shouldFlipUpsideDown(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        try {
            if (entity instanceof PlayerEntity && PerspectiveAprilFoolsPrank.isPrankEnabled() && PerspectiveAprilFoolsPrank.isAprilFools() && PerspectiveAprilFoolsPrankDataLoader.shouldFlipUpsideDown) cir.setReturnValue(true);
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} An error occurred whilst trying to set April Fools shouldFlipUpsideDown.", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    @Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void perspective$hide_nametag(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (PerspectiveClientData.CLIENT.gameRenderer.isRenderingPanorama() || (boolean) PerspectiveConfigHelper.getConfig("hide_nametags") || (PerspectiveShader.shouldRenderShader() && (Boolean) Objects.requireNonNull(PerspectiveShaderDataLoader.get((int) PerspectiveConfigHelper.getConfig("super_secret_settings"), PerspectiveShaderRegistryValue.HIDE_NAMETAGS)))) cir.setReturnValue(false);
    }
}