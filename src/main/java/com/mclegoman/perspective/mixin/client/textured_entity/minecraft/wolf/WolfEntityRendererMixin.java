/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.wolf;

import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import com.mclegoman.luminance.common.util.IdentifierHelper;
import net.minecraft.client.render.entity.WolfEntityRenderer;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = WolfEntityRenderer.class)
public class WolfEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/passive/WolfEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(WolfEntity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity != null) {
			boolean isTexturedEntity = true;
			JsonObject entitySpecific = TexturedEntity.getEntitySpecific(entity, "minecraft", "wolf");
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
				cir.setReturnValue(entity.isTamed() ? TexturedEntity.getTexture(entity, variantNamespace, "minecraft:wolf", TexturedEntity.Affix.SUFFIX, variantKey + "_tame", cir.getReturnValue()) : (entity.hasAngerTime() ? TexturedEntity.getTexture(entity, variantNamespace, "minecraft:wolf", TexturedEntity.Affix.SUFFIX, variantKey + "_angry", cir.getReturnValue()) : TexturedEntity.getTexture(entity, variantNamespace, "minecraft:wolf", TexturedEntity.Affix.SUFFIX, variantKey, cir.getReturnValue())));
			}
		}
	}
}