/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.wind_charge;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.class_9236;
import net.minecraft.class_9238;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.WindChargeEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = WindChargeEntityRenderer.class)
public class WindChargeEntityRendererMixin {
	@Mutable
	@Shadow
	@Final
	private static Identifier TEXTURE;
	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/class_9236;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
	private void perspective$render(class_9236 windChargeEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if (windChargeEntity instanceof class_9238) {
			TEXTURE = TexturedEntity.getTexture(windChargeEntity, "minecraft:breeze_wind_charge", new Identifier("textures/entity/projectiles/wind_charge.png"));
		}
		else if (windChargeEntity instanceof WindChargeEntity) {
			TEXTURE = TexturedEntity.getTexture(windChargeEntity, "minecraft:wind_charge", new Identifier("textures/entity/projectiles/wind_charge.png"));
		}
	}
}