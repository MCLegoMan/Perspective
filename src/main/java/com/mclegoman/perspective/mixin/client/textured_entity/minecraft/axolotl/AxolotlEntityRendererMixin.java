/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.axolotl;

import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.entity.TexturedEntity;
import com.mclegoman.perspective.client.entity.TexturedEntityData;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = net.minecraft.client.render.entity.AxolotlEntityRenderer.class)
public class AxolotlEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/passive/AxolotlEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(AxolotlEntity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity != null) {
			boolean isTexturedEntity = true;
			TexturedEntityData entityData = TexturedEntity.getEntity(entity);
			if (entityData != null) {
				JsonObject entitySpecific = entityData.getEntitySpecific();
				if (entitySpecific != null) {
					if (entitySpecific.has("variants")) {
						JsonObject variants = JsonHelper.getObject(entitySpecific, "variants", new JsonObject());
						if (variants.has(entity.getVariant().getName().toLowerCase())) {
							JsonObject typeRegistry = JsonHelper.getObject(variants, entity.getVariant().getName().toLowerCase(), new JsonObject());
							isTexturedEntity = JsonHelper.getBoolean(typeRegistry, "enabled", true);
						}
					}
				}
			}
			if (isTexturedEntity) {
				String variant = entity.getVariant().getName() != null ? "_" + entity.getVariant().getName().toLowerCase() : "";
				cir.setReturnValue(TexturedEntity.getTexture(entity, "", variant, cir.getReturnValue()));
			}
		}
	}
}