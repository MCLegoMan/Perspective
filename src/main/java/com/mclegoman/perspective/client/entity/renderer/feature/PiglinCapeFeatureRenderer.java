/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.entity.renderer.feature;

import com.mclegoman.perspective.client.entity.TexturedEntity;
import com.mclegoman.perspective.client.entity.TexturedEntityModels;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PiglinEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class PiglinCapeFeatureRenderer extends FeatureRenderer<MobEntity, PiglinEntityModel<MobEntity>> {
	public PiglinCapeFeatureRenderer(FeatureRendererContext<MobEntity, PiglinEntityModel<MobEntity>> featureRendererContext) {
		super(featureRendererContext);
	}
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, MobEntity piglinEntity, float a, float b, float delta, float c, float d, float e) {
		matrixStack.push();
		if (piglinEntity.isBaby()) {
			matrixStack.scale(0.5F, 0.5F, 0.5F);
			matrixStack.translate(0.0F, 1.5F, 0.0F);
		}
		matrixStack.translate(0.0F, 0.0F, 0.125F);
		double f = 0.0F;
		double g = (Math.cos(TexturedEntityModels.getPiglinCapeY(piglinEntity) * (2.0F * 3.141592653589793F / 80.0F)) + 1.0F) * 0.48F;
		double h = 0.0F;
		float i = MathHelper.lerpAngleDegrees(delta, piglinEntity.prevBodyYaw, piglinEntity.bodyYaw);
		double j = MathHelper.sin(i * 0.017453292F);
		double k = -MathHelper.cos(i * 0.017453292F);
		float l = (float)g * 10.0F;
		l = MathHelper.clamp(l, -6.0F, 32.0F);
		float m = (float)(f * j + h * k) * 100.0F;
		m = MathHelper.clamp(m, 0.0F, 150.0F);
		float n = (float)(f * k - h * j) * 100.0F;
		n = MathHelper.clamp(n, -20.0F, 20.0F);
		if (m < 0.0F) {
			m = 0.0F;
		}
		l += MathHelper.sin(MathHelper.lerp(delta, piglinEntity.prevHorizontalSpeed, piglinEntity.horizontalSpeed) * 6.0F) * 32.0F;
		if (piglinEntity.isInSneakingPose()) {
			l += 25.0F;
		}
		matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(6.0F + m / 2.0F + l));
		matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(n / 2.0F));
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - n / 2.0F));

		Identifier texture;
		switch (piglinEntity) {
			case PiglinEntity entity -> texture = TexturedEntity.getTexture(entity, "minecraft:piglin", "", "_cape", Identifier.of("perspective", "textures/entity/piglin/piglin_cape.png"));
			case ZombifiedPiglinEntity entity -> texture = TexturedEntity.getTexture(entity, "minecraft:zombified_piglin", "", "_cape", Identifier.of("perspective", "textures/entity/piglin/piglin_cape.png"));
			case PiglinBruteEntity entity -> texture = TexturedEntity.getTexture(entity, "minecraft:piglin_brute", "", "_cape", Identifier.of("perspective", "textures/entity/piglin/piglin_cape.png"));
			default -> texture = Identifier.of("perspective", "textures/entity/piglin/piglin_cape.png");
		}

		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(texture));
		(this.getContextModel()).renderCape(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
		matrixStack.pop();
	}
}
