/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.goat;

import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = net.minecraft.client.render.entity.GoatEntityRenderer.class)
public class GoatEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/passive/GoatEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(GoatEntity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity != null) {
			boolean isTexturedEntity = true;
			boolean isScreaming = true;
			JsonObject entitySpecific = TexturedEntity.getEntitySpecific(entity, "minecraft:goat");
			if (entitySpecific != null) {
				if (entitySpecific.has("variants")) {
					JsonObject variants = JsonHelper.getObject(entitySpecific, "variants");
					if (variants != null) {
						if (variants.has("normal")) {
							JsonObject normal = JsonHelper.getObject(variants, "normal");
							if (normal != null) isTexturedEntity = JsonHelper.getBoolean(normal, "enabled", true);
						}
						if (variants.has("screaming")) {
							JsonObject screaming = JsonHelper.getObject(variants, "screaming");
							if (screaming != null) isTexturedEntity = JsonHelper.getBoolean(screaming, "enabled", true);
						}
					}
				}
			}
			if (isTexturedEntity) {
				String variant = isScreaming && entity.isScreaming() ? "_screaming" : "";
				cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:goat", TexturedEntity.Affix.SUFFIX, variant, cir.getReturnValue()));
			}
		}
	}
}