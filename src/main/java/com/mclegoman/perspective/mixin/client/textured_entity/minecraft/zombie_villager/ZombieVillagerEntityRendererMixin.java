/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.zombie_villager;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.client.render.entity.ZombieVillagerEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = ZombieVillagerEntityRenderer.class)
public class ZombieVillagerEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity instanceof ZombieVillagerEntity)
			cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:zombie_villager", "", cir.getReturnValue()));
	}
}