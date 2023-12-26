/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.boat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 10000, value = net.minecraft.client.render.entity.BoatEntityRenderer.class)
public abstract class BoatEntityRendererMixin {
	@Unique
	private boolean isChest;
	@Unique
	private BoatEntity entity;
	@Inject(at = @At("RETURN"), method = "<init>")
	private void perspective$init(EntityRendererFactory.Context ctx, boolean chest, CallbackInfo ci) {
		isChest = chest;
	}
	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/entity/vehicle/BoatEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
	private void perspective$render(BoatEntity boatEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		entity = boatEntity;
	}
	@ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lcom/mojang/datafixers/util/Pair;getFirst()Ljava/lang/Object;"), method = "render(Lnet/minecraft/entity/vehicle/BoatEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
	private Object perspective$getTexture(Object texture) {
		return TexturedEntity.getTexture(entity, isChest ? "minecraft:chest_boat" : "minecraft:boat", "_" + entity.getVariant().getName().toLowerCase(), (Identifier)texture);
	}
}