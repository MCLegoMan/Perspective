/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.boat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mclegoman.perspective.client.entity.TexturedEntity;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(priority = 100, value = net.minecraft.client.render.entity.BoatEntityRenderer.class)
public abstract class BoatEntityRendererMixin {
	@Shadow @Final private Map<BoatEntity.Type, Pair<Identifier, CompositeEntityModel<BoatEntity>>> texturesAndModels;
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
	@ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/CompositeEntityModel;getLayer(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"), method = "render(Lnet/minecraft/entity/vehicle/BoatEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
	private RenderLayer perspective$getTexture(RenderLayer renderLayer) {
		Pair<Identifier, CompositeEntityModel<BoatEntity>> pair = this.texturesAndModels.get(entity.getVariant());
		return pair.getSecond().getLayer(TexturedEntity.getTexture(entity, "", "/" + entity.getVariant().getName().toLowerCase(), pair.getFirst()));
	}
	// TODO: Update to Entity Specific.
}