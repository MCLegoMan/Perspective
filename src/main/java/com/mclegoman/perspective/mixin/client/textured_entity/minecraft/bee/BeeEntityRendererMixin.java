/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.bee;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = net.minecraft.client.render.entity.BeeEntityRenderer.class)
public class BeeEntityRendererMixin {
	@Shadow
	@Final
	private static Identifier ANGRY_NECTAR_TEXTURE;

	@Shadow
	@Final
	private static Identifier ANGRY_TEXTURE;

	@Shadow
	@Final
	private static Identifier PASSIVE_TEXTURE;

	@Shadow
	@Final
	private static Identifier NECTAR_TEXTURE;

	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity instanceof BeeEntity) {
			if (((BeeEntity) entity).hasAngerTime()) {
				if (((BeeEntity) entity).hasNectar()) {
					cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:bee", "_angry_nectar", ANGRY_NECTAR_TEXTURE));
				} else {
					cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:bee", "_angry", ANGRY_TEXTURE));
				}
			} else {
				if (((BeeEntity) entity).hasNectar()) {
					cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:bee", "_nectar", NECTAR_TEXTURE));
				} else {
					cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:bee", "", PASSIVE_TEXTURE));
				}
			}
		}
	}
}