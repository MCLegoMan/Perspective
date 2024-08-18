/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.parrot;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.entity.TexturedEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ParrotEntityRenderer;
import net.minecraft.client.render.entity.feature.ShoulderParrotFeatureRenderer;
import net.minecraft.client.render.entity.model.ParrotEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(priority = 100, value = ShoulderParrotFeatureRenderer.class)
public class ShoulderParrotFeatureRendererMixin {
	@Shadow @Final private ParrotEntityModel model;
	@Inject(at = @At("HEAD"), method = "renderShoulderParrot", cancellable = true)
	private void perspective$renderShoulderParrot(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PlayerEntity player, float limbAngle, float limbDistance, float headYaw, float headPitch, boolean leftShoulder, CallbackInfo ci) {
		NbtCompound nbtCompound = leftShoulder ? player.getShoulderEntityLeft() : player.getShoulderEntityRight();
		EntityType.get(nbtCompound.getString("id")).filter((type) -> type == EntityType.PARROT).ifPresent((type) -> {
			Optional<Entity> shoulderEntity = EntityType.getEntityFromNbt(nbtCompound, ClientData.minecraft.world);
			if (shoulderEntity.isPresent()) {
				matrices.push();
				matrices.translate(leftShoulder ? 0.4F : -0.4F, player.isInSneakingPose() ? -1.3F : -1.5F, 0.0F);
				ParrotEntity.Variant variant = ParrotEntity.Variant.byIndex(nbtCompound.getInt("Variant"));
				Identifier texture = TexturedEntity.getTexture(shoulderEntity.get(), ParrotEntityRenderer.getTexture(variant));
				VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.model.getLayer(texture));
				this.model.poseOnShoulder(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, limbAngle, limbDistance, headYaw, headPitch, player.age);
				matrices.pop();
			}
		});
		ci.cancel();
	}
}