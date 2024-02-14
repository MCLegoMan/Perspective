/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.piglin;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.client.render.entity.PiglinEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = PiglinEntityRenderer.class)
public class PiglinEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity instanceof PiglinEntity)
			cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:piglin", cir.getReturnValue()));
		else if (entity instanceof ZombifiedPiglinEntity)
			cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:zombified_piglin", cir.getReturnValue()));
		else if (entity instanceof PiglinBruteEntity)
			cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:piglin_brute", cir.getReturnValue()));
	}
}