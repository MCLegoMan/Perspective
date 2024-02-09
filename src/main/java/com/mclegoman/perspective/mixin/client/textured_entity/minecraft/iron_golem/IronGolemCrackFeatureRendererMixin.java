/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.iron_golem;

import com.google.common.collect.ImmutableMap;
import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.IronGolemCrackFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(priority = 10000, value = IronGolemCrackFeatureRenderer.class)
public class IronGolemCrackFeatureRendererMixin {
	@Mutable
	@Shadow @Final private static Map<IronGolemEntity.Crack, Identifier> DAMAGE_TO_TEXTURE;
	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/IronGolemEntity;FFFFFF)V")
	private void perspective$render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, IronGolemEntity entity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if (entity != null) {
			DAMAGE_TO_TEXTURE = ImmutableMap.of(
					IronGolemEntity.Crack.LOW, TexturedEntity.getTexture(entity, "minecraft:iron_golem", TexturedEntity.Affix.SUFFIX, "_crackiness_low", new Identifier("textures/entity/iron_golem/iron_golem_crackiness_low.png")),
					IronGolemEntity.Crack.MEDIUM, TexturedEntity.getTexture(entity, "minecraft:iron_golem", TexturedEntity.Affix.SUFFIX, "_crackiness_medium", new Identifier("textures/entity/iron_golem/iron_golem_crackiness_medium.png")),
					IronGolemEntity.Crack.HIGH, TexturedEntity.getTexture(entity, "minecraft:iron_golem", TexturedEntity.Affix.SUFFIX, "_crackiness_high", new Identifier("textures/entity/iron_golem/iron_golem_crackiness_high.png"))
			);
		}
	}
}