/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.hide;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hide.HidePlayerDataLoader;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(priority = 10000, value = PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void perspective$hide_players(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (ClientData.CLIENT.player != null) {
            if ((boolean) ConfigHelper.getExperimentalConfig("allow_hide_players")) {
                UUID uuid = abstractClientPlayerEntity.getGameProfile().getId();
                if (!uuid.equals(ClientData.CLIENT.player.getGameProfile().getId()))
                    if ((boolean) ConfigHelper.getExperimentalConfig("hide_players") || HidePlayerDataLoader.REGISTRY.contains(String.valueOf(abstractClientPlayerEntity.getGameProfile().getId()))) ci.cancel();
            }
        }
    }
}