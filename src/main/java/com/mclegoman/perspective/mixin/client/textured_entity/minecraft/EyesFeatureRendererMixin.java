/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(priority = 10000, value = EyesFeatureRenderer.class)
public class EyesFeatureRendererMixin<T extends Entity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
	public EyesFeatureRendererMixin(FeatureRendererContext<T, M> context) {
		super(context);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (entity instanceof EndermanEntity) {
			RenderLayer eyesTexture = RenderLayer.getEyes(TexturedEntity.getTexture(entity, "minecraft:enderman", TexturedEntity.Affix.SUFFIX, "_eyes", new Identifier("textures/entity/enderman/enderman_eyes.png")));
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(eyesTexture);
			this.getContextModel().render(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		} else if (entity instanceof SpiderEntity) {
			RenderLayer eyesTexture = RenderLayer.getEyes(TexturedEntity.getTexture(entity, "minecraft:spider", TexturedEntity.Affix.SUFFIX, "_eyes", new Identifier("textures/entity/spider_eyes.png")));
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(eyesTexture);
			this.getContextModel().render(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		} else if (entity instanceof PhantomEntity) {
			RenderLayer eyesTexture = RenderLayer.getEyes(TexturedEntity.getTexture(entity, "minecraft:phantom", TexturedEntity.Affix.SUFFIX, "_eyes", new Identifier("textures/entity/phantom_eyes.png")));
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(eyesTexture);
			this.getContextModel().render(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
}