/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.snow_golem;

import com.mclegoman.perspective.client.entity.TexturedEntity;
import net.minecraft.client.render.entity.SnowGolemEntityRenderer;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = SnowGolemEntityRenderer.class)
public class SnowGolemEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/passive/SnowGolemEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(SnowGolemEntity entity, CallbackInfoReturnable<Identifier> cir) {
		cir.setReturnValue(TexturedEntity.getTexture(entity, cir.getReturnValue()));
	}
}