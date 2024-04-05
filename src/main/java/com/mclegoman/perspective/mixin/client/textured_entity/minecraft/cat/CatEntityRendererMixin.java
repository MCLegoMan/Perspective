/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.cat;

import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import com.mclegoman.perspective.common.util.IdentifierHelper;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = net.minecraft.client.render.entity.CatEntityRenderer.class)
public class CatEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/passive/CatEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(CatEntity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity != null) {
			boolean isTexturedEntity = true;
			JsonObject entitySpecific = TexturedEntity.getEntitySpecific(entity, "minecraft:cat");
			if (entitySpecific != null) {
				String type = entity.getVariant().getIdAsString().toLowerCase();
				if (entitySpecific.has(type)) {
					JsonObject typeRegistry = JsonHelper.getObject(entitySpecific, entity.getVariant().getIdAsString().toLowerCase());
					if (typeRegistry != null) isTexturedEntity = JsonHelper.getBoolean(typeRegistry, "enabled", true);
				}
			}
			if (isTexturedEntity) {
				String variant = entity.getVariant().getIdAsString();
				String variantNamespace = IdentifierHelper.getStringPart(IdentifierHelper.Type.NAMESPACE, variant);
				String variantKey = IdentifierHelper.getStringPart(IdentifierHelper.Type.KEY, variant);
				cir.setReturnValue(TexturedEntity.getTexture(entity, variantNamespace, "minecraft:cat", TexturedEntity.Affix.SUFFIX, "_" + variantKey, cir.getReturnValue()));
			}
		}
	}
}