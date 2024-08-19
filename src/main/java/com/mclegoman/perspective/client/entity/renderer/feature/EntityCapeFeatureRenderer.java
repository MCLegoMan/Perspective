/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.entity.renderer.feature;

import com.mclegoman.perspective.client.entity.TexturedEntity;
import com.mclegoman.perspective.client.entity.EntityModels;
import com.mclegoman.perspective.client.entity.model.LivingEntityCapeModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

public class EntityCapeFeatureRenderer<T extends LivingEntity> extends FeatureRenderer<T, LivingEntityCapeModel<T>> {
	private final LivingEntityCapeModel<T> model;
	private final Identifier capeTexture;
	private final float scale;
	private final float offsetX;
	private final float offsetY;
	private final float offsetZ;
	private final Quaternionf rotation;
	private EntityCapeFeatureRenderer(FeatureRendererContext<T, LivingEntityCapeModel<T>> featureRendererContext, LivingEntityCapeModel<T> model, Identifier capeTexture, float offsetX, float offsetY, float offsetZ, Quaternionf rotation, float scale) {
		super(featureRendererContext);
		this.model = model;
		this.capeTexture = capeTexture;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.rotation = rotation;
		this.scale = scale;
	}
	public static class Builder<T extends LivingEntity> {
		FeatureRendererContext<T, LivingEntityCapeModel<T>> featureRendererContext;
		LivingEntityCapeModel<T> model;
		Identifier capeTexture;
		float scale = 1.0F;
		float offsetX = 0.0F;
		float offsetY = 0.0F;
		float offsetZ = 0.125F;
		Quaternionf rotation = new Quaternionf();
		public Builder(FeatureRendererContext<T, LivingEntityCapeModel<T>> featureRendererContext, LivingEntityCapeModel<T> model, Identifier capeTexture) {
			this.featureRendererContext = featureRendererContext;
			this.model = model;
			this.capeTexture = capeTexture;
		}
		public Builder scale(float scale) {
			this.scale = scale;
			return this;
		}
		public Builder offsetX(float x) {
			this.offsetX = x;
			return this;
		}
		public Builder offsetY(float y) {
			this.offsetY = y;
			return this;
		}
		public Builder offsetZ(float z) {
			this.offsetZ = z;
			return this;
		}
		public Builder rotation(Quaternionf rotation) {
			this.rotation = rotation;
			return this;
		}
		public EntityCapeFeatureRenderer<T> build() {
			return new EntityCapeFeatureRenderer<>(this.featureRendererContext, this.model, this.capeTexture, offsetX, offsetY, offsetZ, rotation, scale);
		}
	}
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, T livingEntity, float a, float b, float delta, float c, float d, float e) {
		matrixStack.push();
		if (livingEntity.isBaby()) {
			matrixStack.scale(0.5F, 0.5F, 0.5F);
			matrixStack.translate(0.0F, 1.5F, 0.0F);
		}
		matrixStack.scale(this.scale, this.scale, this.scale);
		matrixStack.translate(this.offsetX, this.offsetY, this.offsetZ);
		matrixStack.multiply(rotation);
		double f = 0.0F;
		double g = (Math.cos(EntityModels.getPiglinCapeY(livingEntity) * (2.0F * 3.141592653589793F / 80.0F)) + 1.0F) * 0.48F;
		double h = 0.0F;
		float i = MathHelper.lerpAngleDegrees(delta, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
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
		l += MathHelper.sin(MathHelper.lerp(delta, livingEntity.prevHorizontalSpeed, livingEntity.horizontalSpeed) * 6.0F) * 32.0F;
		if (livingEntity.isInSneakingPose()) {
			l += 25.0F;
		}
		matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(6.0F + m / 2.0F + l));
		matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(n / 2.0F));
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - n / 2.0F));

		this.model.renderCape(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(TexturedEntity.getTexture(livingEntity, "", "_cape", capeTexture))), livingEntity, light, OverlayTexture.DEFAULT_UV);
		matrixStack.pop();
	}
}
