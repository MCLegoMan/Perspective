/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.frog;

import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import com.mclegoman.luminance.common.util.IdentifierHelper;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = net.minecraft.client.render.entity.FrogEntityRenderer.class)
public class FrogEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/passive/FrogEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(FrogEntity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity != null) {
			boolean isTexturedEntity = true;
			JsonObject entitySpecific = TexturedEntity.getEntitySpecific(entity, "minecraft", "frog");
			if (entitySpecific != null) {
				if (entitySpecific.has("variants")) {
					JsonObject variants = JsonHelper.getObject(entitySpecific, "variants");
					if (variants != null) {
						if (entitySpecific.has(entity.getVariant().getIdAsString().toLowerCase())) {
							JsonObject typeRegistry = JsonHelper.getObject(variants, entity.getVariant().getIdAsString().toLowerCase());
							if (typeRegistry != null) {
								isTexturedEntity = JsonHelper.getBoolean(typeRegistry, "enabled", true);
							}
						}
					}
				}
			}
			if (isTexturedEntity) {
				String variant = entity.getVariant().getIdAsString();
				String variantNamespace = entity.getVariant() != null ? IdentifierHelper.getStringPart(IdentifierHelper.Type.NAMESPACE, variant) : "minecraft";
				String variantKey = entity.getVariant() != null ? "_" + IdentifierHelper.getStringPart(IdentifierHelper.Type.KEY, variant) : "";
				cir.setReturnValue(TexturedEntity.getTexture(entity, variantNamespace, "minecraft:frog", TexturedEntity.Affix.SUFFIX, variantKey, cir.getReturnValue()));
			}
		}
	}
}