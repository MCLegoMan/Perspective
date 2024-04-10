/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.mooshroom;

import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import com.mclegoman.perspective.common.util.IdentifierHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.MooshroomMushroomFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = MooshroomMushroomFeatureRenderer.class)
public class MooshroomMushroomFeatureRendererMixin {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/MooshroomEntity$Type;getMushroomState()Lnet/minecraft/block/BlockState;"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/MooshroomEntity;FFFFFF)V")
	private BlockState perspective$getMushroom(MooshroomEntity.Type mooshroomType) {
		try {
			if (entity != null) {
				JsonObject entitySpecific = TexturedEntity.getEntitySpecific(entity, "minecraft:mooshroom");
				if (entitySpecific != null) {
					if (entitySpecific.has("variants")) {
						JsonObject variants = JsonHelper.getObject(entitySpecific, "variants");
						if (variants != null) {
							if (entitySpecific.has(mooshroomType.asString().toLowerCase())) {
								JsonObject typeRegistry = JsonHelper.getObject(variants, mooshroomType.asString().toLowerCase());
								if (typeRegistry != null) {
									boolean enabled = JsonHelper.getBoolean(typeRegistry, "enabled", true);
									if (enabled) {
										if (typeRegistry.has("mushroom")) {
											JsonObject mushroom = JsonHelper.getObject(typeRegistry, "mushroom");
											Identifier blockId = IdentifierHelper.identifierFromString(JsonHelper.getString(mushroom, "identifier", IdentifierHelper.stringFromIdentifier(Registries.BLOCK.getId(mooshroomType.getMushroomState().getBlock()))));
											if (Registries.BLOCK.containsId(blockId)) return Registries.BLOCK.get(blockId).getDefaultState();
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception ignored) {}
		return mooshroomType.getMushroomState();
	}
	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/MooshroomEntity;FFFFFF)V")
	private void perspective$getEntity(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, MooshroomEntity mooshroomEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		entity = mooshroomEntity;
	}
	@Unique
	private MooshroomEntity entity;
}