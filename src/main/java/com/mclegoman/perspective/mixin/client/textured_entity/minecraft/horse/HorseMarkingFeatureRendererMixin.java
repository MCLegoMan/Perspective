/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.horse;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HorseMarkingFeatureRenderer;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.HorseMarking;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(priority = 10000, value = HorseMarkingFeatureRenderer.class)
public class HorseMarkingFeatureRendererMixin extends FeatureRenderer<HorseEntity, HorseEntityModel<HorseEntity>> {
	@Shadow @Final private static Map<HorseMarking, Identifier> TEXTURES;
	public HorseMarkingFeatureRendererMixin(FeatureRendererContext<HorseEntity, HorseEntityModel<HorseEntity>> context) {
		super(context);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, HorseEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (perspective$getHorseMarking(entity.getMarking()) != null) {
			Identifier identifier = TexturedEntity.getTexture(entity, "minecraft:horse", perspective$getHorseMarking(entity.getMarking()), TEXTURES.get(entity.getMarking()));
			if (identifier != null && !entity.isInvisible()) {
				VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(identifier));
				this.getContextModel().render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}
	@Unique
	private String perspective$getHorseMarking(HorseMarking marking) {
		if (marking.equals(HorseMarking.WHITE)) return "_markings_white";
		else if (marking.equals(HorseMarking.WHITE_FIELD)) return "_markings_whitefield";
		else if (marking.equals(HorseMarking.WHITE_DOTS)) return "_markings_whitedots";
		else if (marking.equals(HorseMarking.BLACK_DOTS)) return "_markings_blackdots";
		else return null;
	}
}