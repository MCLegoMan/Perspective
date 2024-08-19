/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.entity.renderer.feature;

import com.mclegoman.perspective.client.entity.model.HalloweenHatModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class HalloweenHatFeatureRenderer<T extends PlayerEntity> extends FeatureRenderer<T, PlayerEntityModel<T>> {
	private final HalloweenHatModel<T> model;
	private final Identifier hatTexture;
	public HalloweenHatFeatureRenderer(FeatureRendererContext<T, PlayerEntityModel<T>> context, HalloweenHatModel<T> model, Identifier hatTexture) {
		super(context);
		this.model = model;
		this.hatTexture = hatTexture;
	}
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, T player, float a, float b, float delta, float c, float d, float e) {
		matrixStack.push();
		matrixStack.scale(0.9375F, 0.9375F, 0.9375F);
		this.model.renderHalloween(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(hatTexture)), light, OverlayTexture.DEFAULT_UV, this.getContextModel());
		matrixStack.pop();
	}
}
