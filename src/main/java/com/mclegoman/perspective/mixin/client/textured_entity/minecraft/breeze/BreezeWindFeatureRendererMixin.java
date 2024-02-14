/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.breeze;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.BreezeWindFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.BreezeEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = BreezeWindFeatureRenderer.class)
public class BreezeWindFeatureRendererMixin {
	@Mutable
	@Shadow
	@Final
	private static Identifier texture;

	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/mob/BreezeEntity;FFFFFF)V")
	private void perspective$getTexture(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, BreezeEntity breezeEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		texture = TexturedEntity.getTexture(breezeEntity, "minecraft:breeze", TexturedEntity.Affix.SUFFIX, "_wind", new Identifier("textures/entity/breeze/breeze_wind.png"));
	}
}