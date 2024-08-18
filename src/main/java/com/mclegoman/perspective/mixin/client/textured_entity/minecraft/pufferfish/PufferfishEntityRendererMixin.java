/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.pufferfish;

import com.mclegoman.perspective.client.entity.TexturedEntity;
import net.minecraft.client.render.entity.PufferfishEntityRenderer;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = PufferfishEntityRenderer.class)
public class PufferfishEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/passive/PufferfishEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(PufferfishEntity entity, CallbackInfoReturnable<Identifier> cir) {
		cir.setReturnValue(TexturedEntity.getTexture(entity, cir.getReturnValue()));
	}
}