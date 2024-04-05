package com.mclegoman.perspective.client.textured_entity.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.entity.passive.CowEntity;

public class CowOverlayEntityModel extends CowEntityModel<CowEntity> {
	public CowOverlayEntityModel(ModelPart root) {
		super(root);
	}
	public static TexturedModelData getTexturedModelData() {
		return getTexturedModelData(new Dilation(0.0F));
	}
	public static TexturedModelData getTexturedOverlayModelData() {
		return getTexturedModelData(new Dilation(0.5F));
	}
	public static TexturedModelData getTexturedModelData(Dilation dilation) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F, dilation).uv(22, 0).cuboid("right_horn", -5.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F, dilation).uv(22, 0).cuboid("left_horn", 4.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F, dilation), ModelTransform.pivot(0.0F, 4.0F, -8.0F));
		modelPartData.addChild("body", ModelPartBuilder.create().uv(18, 4).cuboid(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F, dilation).uv(52, 0).cuboid(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F, dilation), ModelTransform.of(0.0F, 5.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
		ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation);
		modelPartData.addChild("right_hind_leg", modelPartBuilder, ModelTransform.pivot(-4.0F, 12.0F, 7.0F));
		modelPartData.addChild("left_hind_leg", modelPartBuilder, ModelTransform.pivot(4.0F, 12.0F, 7.0F));
		modelPartData.addChild("right_front_leg", modelPartBuilder, ModelTransform.pivot(-4.0F, 12.0F, -6.0F));
		modelPartData.addChild("left_front_leg", modelPartBuilder, ModelTransform.pivot(4.0F, 12.0F, -6.0F));
		return TexturedModelData.of(modelData, 64, 32);
	}
}
