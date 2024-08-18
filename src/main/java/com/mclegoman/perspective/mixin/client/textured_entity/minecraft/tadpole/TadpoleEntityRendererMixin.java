/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.tadpole;

import com.mclegoman.perspective.client.entity.TexturedEntity;
import net.minecraft.client.render.entity.TadpoleEntityRenderer;
import net.minecraft.entity.passive.TadpoleEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = TadpoleEntityRenderer.class)
public class TadpoleEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/passive/TadpoleEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(TadpoleEntity entity, CallbackInfoReturnable<Identifier> cir) {
		cir.setReturnValue(TexturedEntity.getTexture(entity, cir.getReturnValue()));
	}
}