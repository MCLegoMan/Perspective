/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.breeze;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.client.render.entity.feature.BreezeWindFeatureRenderer;
import net.minecraft.entity.mob.BreezeEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 10000, value = BreezeWindFeatureRenderer.class)
public class BreezeWindFeatureRendererMixin {
    @Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/mob/BreezeEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    private void perspective$getTexture(BreezeEntity entity, CallbackInfoReturnable<Identifier> cir) {
        cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:breeze", "_wind", cir.getReturnValue()));
    }
}