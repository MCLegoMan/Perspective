/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.tropical_fish;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.TropicalFishColorFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = TropicalFishColorFeatureRenderer.class)
public class TropicalFishColorFeatureRendererMixin {
	@Mutable
	@Shadow
	@Final
	private static Identifier KOB_TEXTURE;
	@Mutable
	@Shadow
	@Final
	private static Identifier SUNSTREAK_TEXTURE;
	@Mutable
	@Shadow
	@Final
	private static Identifier SNOOPER_TEXTURE;
	@Mutable
	@Shadow
	@Final
	private static Identifier DASHER_TEXTURE;
	@Mutable
	@Shadow
	@Final
	private static Identifier BRINELY_TEXTURE;
	@Mutable
	@Shadow
	@Final
	private static Identifier SPOTTY_TEXTURE;
	@Mutable
	@Shadow
	@Final
	private static Identifier FLOPPER_TEXTURE;
	@Mutable
	@Shadow
	@Final
	private static Identifier STRIPEY_TEXTURE;
	@Mutable
	@Shadow
	@Final
	private static Identifier GLITTER_TEXTURE;
	@Mutable
	@Shadow
	@Final
	private static Identifier BLOCKFISH_TEXTURE;
	@Mutable
	@Shadow
	@Final
	private static Identifier BETTY_TEXTURE;
	@Mutable
	@Shadow
	@Final
	private static Identifier CLAYFISH_TEXTURE;

	@Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/Entity;FFFFFF)V", at = @At("RETURN"))
	private void perspective$render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
		if (entity instanceof TropicalFishEntity) {
			if (((TropicalFishEntity) entity).getVariant().equals(TropicalFishEntity.Variety.KOB))
				KOB_TEXTURE = TexturedEntity.getTexture(entity, "minecraft:tropical_fish", "_kob", new Identifier("textures/entity/fish/tropical_a_pattern_1.png"));
			else if (((TropicalFishEntity) entity).getVariant().equals(TropicalFishEntity.Variety.SUNSTREAK))
				SUNSTREAK_TEXTURE = TexturedEntity.getTexture(entity, "minecraft:tropical_fish", "_sunstreak", new Identifier("textures/entity/fish/tropical_a_pattern_2.png"));
			else if (((TropicalFishEntity) entity).getVariant().equals(TropicalFishEntity.Variety.SNOOPER))
				SNOOPER_TEXTURE = TexturedEntity.getTexture(entity, "minecraft:tropical_fish", "_snooper", new Identifier("textures/entity/fish/tropical_a_pattern_3.png"));
			else if (((TropicalFishEntity) entity).getVariant().equals(TropicalFishEntity.Variety.DASHER))
				DASHER_TEXTURE = TexturedEntity.getTexture(entity, "minecraft:tropical_fish", "_dasher", new Identifier("textures/entity/fish/tropical_a_pattern_4.png"));
			else if (((TropicalFishEntity) entity).getVariant().equals(TropicalFishEntity.Variety.BRINELY))
				BRINELY_TEXTURE = TexturedEntity.getTexture(entity, "minecraft:tropical_fish", "_brinely", new Identifier("textures/entity/fish/tropical_a_pattern_5.png"));
			else if (((TropicalFishEntity) entity).getVariant().equals(TropicalFishEntity.Variety.SPOTTY))
				SPOTTY_TEXTURE = TexturedEntity.getTexture(entity, "minecraft:tropical_fish", "_spotty", new Identifier("textures/entity/fish/tropical_a_pattern_6.png"));
			else if (((TropicalFishEntity) entity).getVariant().equals(TropicalFishEntity.Variety.FLOPPER))
				FLOPPER_TEXTURE = TexturedEntity.getTexture(entity, "minecraft:tropical_fish", "_flopper", new Identifier("textures/entity/fish/tropical_b_pattern_1.png"));
			else if (((TropicalFishEntity) entity).getVariant().equals(TropicalFishEntity.Variety.STRIPEY))
				STRIPEY_TEXTURE = TexturedEntity.getTexture(entity, "minecraft:tropical_fish", "_stripey", new Identifier("textures/entity/fish/tropical_b_pattern_2.png"));
			else if (((TropicalFishEntity) entity).getVariant().equals(TropicalFishEntity.Variety.GLITTER))
				GLITTER_TEXTURE = TexturedEntity.getTexture(entity, "minecraft:tropical_fish", "_glitter", new Identifier("textures/entity/fish/tropical_b_pattern_3.png"));
			else if (((TropicalFishEntity) entity).getVariant().equals(TropicalFishEntity.Variety.BLOCKFISH))
				BLOCKFISH_TEXTURE = TexturedEntity.getTexture(entity, "minecraft:tropical_fish", "_blockfish", new Identifier("textures/entity/fish/tropical_b_pattern_4.png"));
			else if (((TropicalFishEntity) entity).getVariant().equals(TropicalFishEntity.Variety.BETTY))
				BETTY_TEXTURE = TexturedEntity.getTexture(entity, "minecraft:tropical_fish", "_betty", new Identifier("textures/entity/fish/tropical_b_pattern_5.png"));
			else if (((TropicalFishEntity) entity).getVariant().equals(TropicalFishEntity.Variety.CLAYFISH))
				CLAYFISH_TEXTURE = TexturedEntity.getTexture(entity, "minecraft:tropical_fish", "_clayfish", new Identifier("textures/entity/fish/tropical_b_pattern_6.png"));
		}
	}
}