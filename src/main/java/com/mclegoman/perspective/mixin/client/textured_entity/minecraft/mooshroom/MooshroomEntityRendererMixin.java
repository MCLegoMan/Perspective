/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.mooshroom;

import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.client.render.entity.MooshroomEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = MooshroomEntityRenderer.class)
public class MooshroomEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/passive/MooshroomEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(MooshroomEntity mooshroomEntity, CallbackInfoReturnable<Identifier> cir) {
		boolean isTexturedEntity = true;
		JsonObject entitySpecific = TexturedEntity.getEntitySpecific(mooshroomEntity, "minecraft:mooshroom");
		if (entitySpecific != null) {
			String type = String.valueOf(mooshroomEntity.getVariant()).toLowerCase();
			if (entitySpecific.has(type)) {
				JsonObject typeRegistry = JsonHelper.getObject(entitySpecific, String.valueOf(mooshroomEntity.getVariant()).toLowerCase());
				if (typeRegistry != null) {
					isTexturedEntity = JsonHelper.getBoolean(typeRegistry, "enabled", true);
				}
			}
		}
		if (isTexturedEntity) cir.setReturnValue(TexturedEntity.getTexture(mooshroomEntity, "minecraft:mooshroom", TexturedEntity.Affix.PREFIX, mooshroomEntity.getVariant().asString().toLowerCase() + "_", cir.getReturnValue()));
	}
}