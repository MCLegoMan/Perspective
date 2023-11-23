/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.developer;

import com.mclegoman.perspective.client.developer.DeveloperDataloader;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(priority = 10000, value = LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "shouldFlipUpsideDown", cancellable = true)
	private static void perspective$shouldFlipUpsideDown(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (entity instanceof PlayerEntity)
			for (List<Object> DEVELOPER : DeveloperDataloader.REGISTRY) {
				if (DEVELOPER.get(0).equals(((PlayerEntity) entity).getGameProfile().getId().toString())) {
					if ((boolean) DEVELOPER.get(1)) {
						cir.setReturnValue(true);
					}
				}
			}
	}
}