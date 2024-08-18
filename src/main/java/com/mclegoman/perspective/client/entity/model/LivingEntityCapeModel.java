/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class LivingEntityCapeModel<T extends LivingEntity> extends AnimalModel<T> {
	private final ModelPart cloak;
	public LivingEntityCapeModel(ModelPart root) {
		this(root, RenderLayer::getEntityTranslucent);
	}
	public LivingEntityCapeModel(ModelPart root, Function<Identifier, RenderLayer> renderLayerFactory) {
		super(renderLayerFactory, true, 16.0F, 0.0F, 2.0F, 2.0F, 24.0F);
		this.cloak = root.getChild("cloak");
	}
	public static ModelData getModelData(Dilation dilation) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("cloak", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, dilation, 1.0F, 0.5F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return modelData;
	}
	public void setAngles(T armorStandEntity, float a, float b, float c, float d, float e) {
		if (armorStandEntity.getEquippedStack(EquipmentSlot.CHEST).isEmpty()) {
			if (armorStandEntity.isInSneakingPose()) {
				this.cloak.pivotZ = 1.4F;
				this.cloak.pivotY = 1.85F;
			} else {
				this.cloak.pivotZ = 0.0F;
				this.cloak.pivotY = 0.0F;
			}
		} else if (armorStandEntity.isInSneakingPose()) {
			this.cloak.pivotZ = 0.3F;
			this.cloak.pivotY = 0.8F;
		} else {
			this.cloak.pivotZ = -1.1F;
			this.cloak.pivotY = -0.85F;
		}
	}
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of();
	}
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of();
	}
	public void renderCape(MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {
		this.cloak.render(matrices, vertices, light, overlay);
	}
}