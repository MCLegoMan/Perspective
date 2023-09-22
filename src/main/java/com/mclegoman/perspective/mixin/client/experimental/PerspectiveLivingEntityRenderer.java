/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.experimental;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.shaders.PerspectiveShader;
import com.mclegoman.perspective.client.shaders.PerspectiveShaderDataLoader;
import com.mclegoman.perspective.client.shaders.PerspectiveShaderRegistryValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = LivingEntityRenderer.class)
public class PerspectiveLivingEntityRenderer<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {
    @Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void perspective$hide_nametag(T livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (PerspectiveClientData.CLIENT.gameRenderer.isRenderingPanorama() || (boolean)PerspectiveConfigHelper.getConfig("hide_nametags") || (PerspectiveShader.shouldRenderShader() && (Boolean) Objects.requireNonNull(PerspectiveShaderDataLoader.get((int) PerspectiveConfigHelper.getConfig("super_secret_settings"), PerspectiveShaderRegistryValue.HIDE_NAMETAGS)))) cir.setReturnValue(false);
    }
}