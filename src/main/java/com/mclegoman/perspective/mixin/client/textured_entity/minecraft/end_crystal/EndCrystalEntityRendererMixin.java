/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.end_crystal;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = net.minecraft.client.render.entity.EndCrystalEntityRenderer.class)
public class EndCrystalEntityRendererMixin {
	@Mutable
	@Shadow
	@Final
	private static Identifier TEXTURE;
	@Mutable
	@Shadow
	@Final
	private static RenderLayer END_CRYSTAL;

	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
	private void perspective$render(EndCrystalEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		TEXTURE = TexturedEntity.getTexture(entity, "minecraft:end_crystal", new Identifier("textures/entity/end_crystal/end_crystal.png"));
		END_CRYSTAL = RenderLayer.getEntityCutoutNoCull(TEXTURE);
	}
}