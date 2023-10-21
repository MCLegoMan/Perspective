/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mclegoman.perspective.client.april_fools_prank.PerspectiveAprilFoolsPrank;
import com.mclegoman.perspective.client.april_fools_prank.PerspectiveAprilFoolsPrankDataLoader;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = PlayerEntityRenderer.class)
public abstract class PerspectivePlayerEntityRenderer {
    @Shadow public abstract Identifier getTexture(AbstractClientPlayerEntity abstractClientPlayerEntity);

    @Inject(at = @At("HEAD"), method = "getTexture(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    private void perspective$getTexture(AbstractClientPlayerEntity player, CallbackInfoReturnable<Identifier> cir) {
        if (player != null && PerspectiveAprilFoolsPrank.isPrankEnabled() && PerspectiveAprilFoolsPrank.isAprilFools() && PerspectiveAprilFoolsPrankDataLoader.REGISTRY.size() > 0) cir.setReturnValue(new Identifier(PerspectiveData.PERSPECTIVE_VERSION.getID(), "textures/prank/" + player.getSkinTextures().model().getName().toLowerCase() + "/" + PerspectiveAprilFoolsPrankDataLoader.REGISTRY.get(Math.floorMod(player.getUuid().getLeastSignificantBits(), PerspectiveAprilFoolsPrankDataLoader.REGISTRY.size())).toLowerCase() + ".png"));
    }
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getEntitySolid(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;", ordinal = 0), method = "renderArm")
    private RenderLayer perspective$renderArm_renderSolid(RenderLayer renderLayer) {
        return RenderLayer.getEntitySolid(this.getTexture(PerspectiveClientData.CLIENT.player));
    }
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getEntityTranslucent(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;", ordinal = 0), method = "renderArm")
    private RenderLayer perspective$renderArm_renderTranslucent(RenderLayer renderLayer) {
        return RenderLayer.getEntityTranslucent(this.getTexture(PerspectiveClientData.CLIENT.player));
    }
}