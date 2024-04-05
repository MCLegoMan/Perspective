/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.fox;

import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import com.mclegoman.perspective.common.util.IdentifierHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = net.minecraft.client.render.entity.FoxEntityRenderer.class)
public class FoxEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/passive/FoxEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(FoxEntity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity != null) {
			boolean isTexturedEntity = true;
			JsonObject entitySpecific = TexturedEntity.getEntitySpecific(entity, "minecraft:fox");
			if (entitySpecific != null) {
				String type = entity.getVariant().asString().toLowerCase();
				if (entitySpecific.has(type)) {
					JsonObject typeRegistry = JsonHelper.getObject(entitySpecific, entity.getVariant().asString().toLowerCase());
					if (typeRegistry != null) isTexturedEntity = JsonHelper.getBoolean(typeRegistry, "enabled", true);
				}
			}
			if (isTexturedEntity) {
				String variant = entity.getVariant().asString();
				cir.setReturnValue(entity.isSleeping() ? TexturedEntity.getTexture(entity, "minecraft:fox", TexturedEntity.Affix.SUFFIX, "_" + variant + "_sleep", cir.getReturnValue()) : TexturedEntity.getTexture(entity, "minecraft:fox", TexturedEntity.Affix.SUFFIX, "_" + variant, cir.getReturnValue()));
			}
		}
	}
}