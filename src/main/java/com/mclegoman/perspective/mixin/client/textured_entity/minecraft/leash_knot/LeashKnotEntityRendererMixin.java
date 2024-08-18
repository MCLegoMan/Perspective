/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.leash_knot;

import com.mclegoman.perspective.client.entity.TexturedEntity;
import net.minecraft.client.render.entity.LeashKnotEntityRenderer;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = LeashKnotEntityRenderer.class)
public class LeashKnotEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/decoration/LeashKnotEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(LeashKnotEntity entity, CallbackInfoReturnable<Identifier> cir) {
		cir.setReturnValue(TexturedEntity.getTexture(entity, cir.getReturnValue()));
	}
}