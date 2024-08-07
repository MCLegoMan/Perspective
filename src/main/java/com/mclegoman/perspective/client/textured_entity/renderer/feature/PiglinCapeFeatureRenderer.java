package com.mclegoman.perspective.client.textured_entity.renderer.feature;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import com.mclegoman.perspective.client.textured_entity.TexturedEntityModels;
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
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, MobEntity piglinEntity, float f, float g, float h, float j, float k, float l) {
		matrixStack.push();
		if (piglinEntity.isBaby()) {
			matrixStack.scale(0.5F, 0.5F, 0.5F);
			matrixStack.translate(0.0F, 1.5F, 0.0F);
		}
		matrixStack.translate(0.0F, 0.0F, 0.125F);
		double d = 0.0F;
		double e = (Math.cos(TexturedEntityModels.getPiglinCapeY(piglinEntity) * (2.0F * 3.141592653589793F / 80.0F)) + 1.0F) * 0.48F;
		double m = 0.0F;
		float n = MathHelper.lerpAngleDegrees(h, piglinEntity.prevBodyYaw, piglinEntity.bodyYaw);
		double o = MathHelper.sin(n * 0.017453292F);
		double p = -MathHelper.cos(n * 0.017453292F);
		float q = (float)e * 10.0F;
		q = MathHelper.clamp(q, -6.0F, 32.0F);
		float r = (float)(d * o + m * p) * 100.0F;
		r = MathHelper.clamp(r, 0.0F, 150.0F);
		float s = (float)(d * p - m * o) * 100.0F;
		s = MathHelper.clamp(s, -20.0F, 20.0F);
		if (r < 0.0F) {
			r = 0.0F;
		}
		q += MathHelper.sin(MathHelper.lerp(h, piglinEntity.prevHorizontalSpeed, piglinEntity.horizontalSpeed) * 6.0F) * 32.0F;// * t;
		if (piglinEntity.isInSneakingPose()) {
			q += 25.0F;
		}
		matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(6.0F + r / 2.0F + q));
		matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(s / 2.0F));
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - s / 2.0F));

		Identifier texture;
		switch (piglinEntity) {
			case PiglinEntity entity -> texture = TexturedEntity.getTexture(entity, "minecraft:piglin", "", "_cape", Identifier.of("perspective", "textures/entity/empty/empty_cape.png"));
			case ZombifiedPiglinEntity entity -> texture = TexturedEntity.getTexture(entity, "minecraft:zombified_piglin", "", "_cape", Identifier.of("perspective", "textures/entity/empty/empty_cape.png"));
			case PiglinBruteEntity entity -> texture = TexturedEntity.getTexture(entity, "minecraft:piglin_brute", "", "_cape", Identifier.of("perspective", "textures/entity/empty/empty_cape.png"));
			default -> texture = Identifier.of("perspective", "textures/entity/empty/empty_cape.png");
		}

		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(texture));
		(this.getContextModel()).renderCape(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
		matrixStack.pop();
	}
}
