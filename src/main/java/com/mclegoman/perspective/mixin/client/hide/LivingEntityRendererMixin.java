/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.hide;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hide.HideNameTagsDataLoader;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.shaders.ShaderDataLoader;
import com.mclegoman.perspective.client.shaders.ShaderRegistryValue;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(priority = 10000, value = LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
    @Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void perspective$hide_nametag(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (ClientData.CLIENT.gameRenderer.isRenderingPanorama() || ((boolean) ConfigHelper.getConfig("hide_nametags") || (Shader.shouldRenderShader() && (Boolean) Objects.requireNonNull(ShaderDataLoader.get((int) ConfigHelper.getConfig("super_secret_settings"), ShaderRegistryValue.HIDE_NAMETAGS)))) || (livingEntity instanceof PlayerEntity && HideNameTagsDataLoader.REGISTRY.contains(String.valueOf((((PlayerEntity) livingEntity).getGameProfile().getId()))))) cir.setReturnValue(false);
    }
}