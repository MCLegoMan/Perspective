/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.textured_entity.renderer.feature;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class OverlayFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
	private final M model;
	private final String entityType;
	private final Identifier texture;

	public OverlayFeatureRenderer(FeatureRendererContext<T, M> context, M model, String entityType, Identifier texture) {
		super(context);
		this.model = model;
		this.entityType = entityType;
		this.texture = texture;
	}
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		this.getContextModel().copyStateTo(this.model);
		this.model.animateModel(entity, limbAngle, limbDistance, tickDelta);
		this.model.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TexturedEntity.getTexture(entity, this.entityType, TexturedEntity.Affix.SUFFIX, "_overlay", this.texture)));
		this.model.render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0.0F));
	}
}