/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.goat;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = net.minecraft.client.render.entity.GoatEntityRenderer.class)
public class GoatEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity instanceof GoatEntity) {
			if (((GoatEntity)entity).isScreaming()) {
				cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:goat", TexturedEntity.Affix.PREFIX, "screaming_", cir.getReturnValue()));
			} else {
				cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:goat", cir.getReturnValue()));
			}
		}
	}
}