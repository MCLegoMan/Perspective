/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.dragon_fireball;

import com.mclegoman.perspective.client.entity.TexturedEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = net.minecraft.client.render.entity.DragonFireballEntityRenderer.class)
public class DragonFireballEntityRendererMixin {
	@Shadow
	@Final
	private static Identifier TEXTURE;
	@Mutable
	@Shadow
	@Final
	private static RenderLayer LAYER;
	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/entity/projectile/DragonFireballEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
	private void perspective$render(DragonFireballEntity dragonFireballEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		LAYER = RenderLayer.getEntityCutoutNoCull(TexturedEntity.getTexture(dragonFireballEntity, "minecraft:dragon_fireball", TEXTURE));
	}
}