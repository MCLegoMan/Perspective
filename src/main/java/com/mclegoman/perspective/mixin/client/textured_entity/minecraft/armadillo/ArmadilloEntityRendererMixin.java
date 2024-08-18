/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.armadillo;

import com.mclegoman.perspective.client.entity.TexturedEntity;
import net.minecraft.client.render.entity.ArmadilloEntityRenderer;
import net.minecraft.entity.passive.ArmadilloEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = ArmadilloEntityRenderer.class)
public class ArmadilloEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/passive/ArmadilloEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(ArmadilloEntity entity, CallbackInfoReturnable<Identifier> cir) {
		cir.setReturnValue(TexturedEntity.getTexture(entity, cir.getReturnValue()));
	}
}