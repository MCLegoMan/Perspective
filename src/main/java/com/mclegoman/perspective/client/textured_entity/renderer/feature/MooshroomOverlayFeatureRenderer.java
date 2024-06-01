/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.textured_entity.renderer.feature;

import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class MooshroomOverlayFeatureRenderer<T extends CowEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
	private final M model;

	public MooshroomOverlayFeatureRenderer(FeatureRendererContext<T, M> context, M model) {
		super(context);
		this.model = model;
	}
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		this.getContextModel().copyStateTo(this.model);
		this.model.animateModel(entity, limbAngle, limbDistance, tickDelta);
		this.model.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(getFinalTexture(entity)));
		this.model.render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0.0F));
	}
	public Identifier getFinalTexture(Entity entity) {
		boolean isTexturedEntity = true;
		JsonObject entitySpecific = TexturedEntity.getEntitySpecific(entity, "minecraft", "mooshroom");
		if (entitySpecific != null) {
				String type = String.valueOf(((MooshroomEntity)entity).getVariant()).toLowerCase();
				if (entitySpecific.has(type)) {
					JsonObject typeRegistry = JsonHelper.getObject(entitySpecific, String.valueOf(((MooshroomEntity)entity).getVariant()).toLowerCase());
					if (typeRegistry != null) {
						isTexturedEntity = JsonHelper.getBoolean(typeRegistry, "enabled", true);
					}
				}
		}
		Identifier defaultId = Identifier.of("minecraft", "textures/entity/mooshroom/" + ((MooshroomEntity)entity).getVariant().asString().toLowerCase() + "_mooshroom_overlay.png");
		return isTexturedEntity ? TexturedEntity.getTexture(entity, "minecraft:mooshroom", ((MooshroomEntity)entity).getVariant().asString().toLowerCase() + "_", "_overlay", defaultId) : defaultId;
	}
}