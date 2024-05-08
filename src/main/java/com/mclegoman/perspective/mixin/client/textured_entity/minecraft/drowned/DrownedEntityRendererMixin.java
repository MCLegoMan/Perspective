/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.drowned;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = net.minecraft.client.render.entity.DrownedEntityRenderer.class)
public class DrownedEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture", cancellable = true)
	private void perspective$getTexture(ZombieEntity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity instanceof DrownedEntity)
			cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:drowned", "", cir.getReturnValue()));
	}
}