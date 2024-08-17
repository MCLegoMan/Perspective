/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.panda;

import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.entity.TexturedEntity;
import com.mclegoman.perspective.client.entity.TexturedEntityData;
import net.minecraft.client.render.entity.PandaEntityRenderer;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = PandaEntityRenderer.class)
public class PandaEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/passive/PandaEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(PandaEntity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity != null) {
			boolean isTexturedEntity = true;
			TexturedEntityData entityData = TexturedEntity.getEntity(entity, "minecraft", "panda");
			if (entityData != null) {
				JsonObject entitySpecific = entityData.getEntitySpecific();
				if (entitySpecific != null) {
					if (entitySpecific.has("variants")) {
						JsonObject variants = JsonHelper.getObject(entitySpecific, "variants", new JsonObject());
						if (variants.has(entity.getProductGene().asString().toLowerCase())) {
							JsonObject typeRegistry = JsonHelper.getObject(variants, entity.getProductGene().asString().toLowerCase(), new JsonObject());
							isTexturedEntity = JsonHelper.getBoolean(typeRegistry, "enabled", true);
						}
					}
				}
			}
			if (isTexturedEntity) {
				String variant = entity.getProductGene().asString() != null ? "_" + entity.getProductGene().asString().toLowerCase() : "";
				cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:panda", TexturedEntity.Affix.SUFFIX, variant, cir.getReturnValue()));
			}
		}
	}
}