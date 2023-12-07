/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.boat;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 10000, value = net.minecraft.client.render.entity.BoatEntityRenderer.class)
public class BoatEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity instanceof ChestBoatEntity)
			cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:chest_boat", "_" + ((BoatEntity) entity).getVariant().getName().toLowerCase(), cir.getReturnValue()));
		else if (entity instanceof BoatEntity)
			cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:boat", "_" + ((BoatEntity) entity).getVariant().getName().toLowerCase(), cir.getReturnValue()));
	}
}