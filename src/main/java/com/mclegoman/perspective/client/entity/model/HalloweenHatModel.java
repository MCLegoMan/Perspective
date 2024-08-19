/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mclegoman.perspective.client.events.Halloween;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class HalloweenHatModel<T extends PlayerEntity> extends AnimalModel<T> {
	private final ModelPart halloween;
	public HalloweenHatModel(ModelPart root) {
		this(root, RenderLayer::getEntityTranslucent);
	}
	public HalloweenHatModel(ModelPart root, Function<Identifier, RenderLayer> renderLayerFactory) {
		super(renderLayerFactory, true, 16.0F, 0.0F, 2.0F, 2.0F, 24.0F);
		this.halloween = root.getChild("halloween");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData halloween = modelPartData.addChild("halloween", ModelPartBuilder.create().uv(0, 64).cuboid(-5.0F, -7.95F, -5.0F, 10.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(-9.0F, 22.95F, -9.0F));
		ModelPartData halloween2 = halloween.addChild("halloween2", ModelPartBuilder.create().uv(0, 76).cuboid(-5.5F, -7.45F, -5.5F, 7.0F, 4.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(1.75F, -4.0F, 2.0F, -0.0524F, 0.0F, 0.0262F));
		ModelPartData halloween3 = halloween2.addChild("halloween3", ModelPartBuilder.create().uv(0, 87).cuboid(-6.0F, -5.95F, -6.5F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(1.75F, -4.0F, 2.0F, -0.1047F, 0.0F, 0.0524F));
		halloween3.addChild("halloween4", ModelPartBuilder.create().uv(0, 95).cuboid(-6.5F, -3.95F, -7.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.25F)), ModelTransform.of(1.75F, -2.0F, 2.0F, -0.2094F, 0.0F, 0.1047F));
		return TexturedModelData.of(modelData, 64, 128);
	}
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of();
	}
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of();
	}
	public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
	}
	public void renderHalloween(MatrixStack matrixStack, VertexConsumer vertices, int light, int overlay, PlayerEntityModel<?> playerEntityModel) {
		if (Halloween.isHalloween()) {
			this.halloween.copyTransform(playerEntityModel.head);
			matrixStack.scale(1.25F, 1.25F, 1.25F);
			this.halloween.render(matrixStack, vertices, light, overlay);
		}
	}
}
