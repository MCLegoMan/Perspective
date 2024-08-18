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
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ArmorStandOverlayEntityModel<T extends ArmorStandEntity> extends AnimalModel<T> implements ModelWithArms, ModelWithHead {
	public final ModelPart head;
	public final ModelPart body;
	public final ModelPart rightArm;
	public final ModelPart leftArm;
	public final ModelPart rightLeg;
	public final ModelPart leftLeg;
	public ArmorStandOverlayEntityModel(ModelPart root) {
		this(root, RenderLayer::getEntityTranslucent);
	}
	public ArmorStandOverlayEntityModel(ModelPart root, Function<Identifier, RenderLayer> renderLayerFactory) {
		super(renderLayerFactory, true, 16.0F, 0.0F, 2.0F, 2.0F, 24.0F);
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.rightArm = root.getChild("right_arm");
		this.leftArm = root.getChild("left_arm");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");
	}
	public static ModelData getModelData(Dilation dilation, float pivotOffsetY) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -7.99F, -4.0F, 8.0F, 8.0F, 8.0F, dilation), ModelTransform.pivot(0.0F, 0.0F + pivotOffsetY, 0.0F));
		modelPartData.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(0.0F, 0.0F + pivotOffsetY, 0.0F));
		modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(-5.0F, 2.0F + pivotOffsetY, 0.0F));
		modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(5.0F, 2.0F + pivotOffsetY, 0.0F));
		modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(-1.9F, 12.0F + pivotOffsetY, 0.0F));
		modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(1.9F, 12.0F + pivotOffsetY, 0.0F));
		return modelData;
	}
	public void setAngles(T armorStandEntity, float a, float b, float c, float d, float e) {
		float multiplier = 0.017453292F;
		this.head.pitch = multiplier * armorStandEntity.getHeadRotation().getPitch();
		this.head.yaw = multiplier * armorStandEntity.getHeadRotation().getYaw();
		this.head.roll = multiplier * armorStandEntity.getHeadRotation().getRoll();

		this.body.pitch = multiplier * armorStandEntity.getBodyRotation().getPitch();
		this.body.yaw = multiplier * armorStandEntity.getBodyRotation().getYaw();
		this.body.roll = multiplier * armorStandEntity.getBodyRotation().getRoll();

		this.leftArm.pitch = multiplier * armorStandEntity.getLeftArmRotation().getPitch();
		this.leftArm.yaw = multiplier * armorStandEntity.getLeftArmRotation().getYaw();
		this.leftArm.roll = multiplier * armorStandEntity.getLeftArmRotation().getRoll();

		this.rightArm.pitch = multiplier * armorStandEntity.getRightArmRotation().getPitch();
		this.rightArm.yaw = multiplier * armorStandEntity.getRightArmRotation().getYaw();
		this.rightArm.roll = multiplier * armorStandEntity.getRightArmRotation().getRoll();

		this.leftLeg.pitch = multiplier * armorStandEntity.getLeftLegRotation().getPitch();
		this.leftLeg.yaw = multiplier * armorStandEntity.getLeftLegRotation().getYaw();
		this.leftLeg.roll = multiplier * armorStandEntity.getLeftLegRotation().getRoll();

		this.rightLeg.pitch = multiplier * armorStandEntity.getRightLegRotation().getPitch();
		this.rightLeg.yaw = multiplier * armorStandEntity.getRightLegRotation().getYaw();
		this.rightLeg.roll = multiplier * armorStandEntity.getRightLegRotation().getRoll();
	}
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of(this.head);
	}

	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg);
	}
	public ModelPart getHead() {
		return this.head;
	}
	public void setArmAngle(Arm arm, MatrixStack matrices) {
		this.getArm(arm).rotate(matrices);
	}
	protected ModelPart getArm(Arm arm) {
		return arm == Arm.LEFT ? this.leftArm : this.rightArm;
	}
}