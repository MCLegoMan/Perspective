/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.skeleton;

import com.mclegoman.perspective.client.entity.TexturedEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.SkeletonOverlayFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = SkeletonOverlayFeatureRenderer.class)
public class SkeletonOverlayFeatureRendererMixin {
	@Mutable @Shadow @Final private Identifier texture;
	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/mob/MobEntity;FFFFFF)V")
	private void perspective$getTexture(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, MobEntity entity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		texture = TexturedEntity.getTexture(entity, "", "_overlay", Identifier.of(Registries.ENTITY_TYPE.getId(entity.getType()).getNamespace(), "textures/entity/skeleton/" + Registries.ENTITY_TYPE.getId(entity.getType()).getPath() + "_overlay.png"));
	}
}