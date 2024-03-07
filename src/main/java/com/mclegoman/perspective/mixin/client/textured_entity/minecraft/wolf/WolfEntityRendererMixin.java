/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.wolf;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.client.render.entity.WolfEntityRenderer;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = WolfEntityRenderer.class)
public class WolfEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/passive/WolfEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(WolfEntity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity != null) cir.setReturnValue(entity.isTamed() ? (TexturedEntity.getTexture(entity, "minecraft:wolf", TexturedEntity.Affix.SUFFIX, "_tame", new Identifier("textures/entity/wolf/wolf_tame.png"))) : (entity.hasAngerTime() ? TexturedEntity.getTexture(entity, "minecraft:wolf", TexturedEntity.Affix.SUFFIX, "_angry", new Identifier("textures/entity/wolf/wolf_angry.png")) : TexturedEntity.getTexture(entity, "minecraft:wolf", new Identifier("textures/entity/wolf/wolf.png"))));
	}
}