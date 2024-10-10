/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.entity.renderer.feature;

import com.mclegoman.perspective.client.contributor.ContributorData;
import com.mclegoman.perspective.client.contributor.ContributorDataLoader;
import com.mclegoman.perspective.client.events.AprilFoolsPrank;
import com.mclegoman.perspective.client.events.AprilFoolsPrankDataLoader;
import com.mclegoman.perspective.client.texture.TextureHelper;
import com.mclegoman.perspective.config.ConfigHelper;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ContributorOverlayFeatureRenderer<P extends PlayerEntity, M extends PlayerEntityModel<P>> extends FeatureRenderer<P, M> {
	private final M model;
	public ContributorOverlayFeatureRenderer(FeatureRendererContext<P, M> context, M model) {
		super(context);
		this.model = model;
	}
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, P entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		String uuid = ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "allow_april_fools") && AprilFoolsPrank.isAprilFools() && !AprilFoolsPrankDataLoader.registry.isEmpty()) ? AprilFoolsPrankDataLoader.contributor : String.valueOf(entity.getUuid());
		if (shouldOverlayTexture(uuid)) {
			Identifier texture = getOverlayTexture(uuid);
			if (texture != null) {
				PlayerEntityModel<P> playerModel = this.getContextModel();
				this.model.sneaking = playerModel.sneaking;
				this.model.head.copyTransform(playerModel.head);
				this.model.hat.copyTransform(playerModel.hat);
				this.model.body.copyTransform(playerModel.body);
				this.model.rightArm.copyTransform(playerModel.rightArm);
				this.model.leftArm.copyTransform(playerModel.leftArm);
				this.model.rightLeg.copyTransform(playerModel.rightLeg);
				this.model.leftLeg.copyTransform(playerModel.leftLeg);
				this.model.leftSleeve.copyTransform(playerModel.leftSleeve);
				this.model.rightSleeve.copyTransform(playerModel.rightSleeve);
				this.model.leftPants.copyTransform(playerModel.leftPants);
				this.model.rightPants.copyTransform(playerModel.rightPants);
				this.model.jacket.copyTransform(playerModel.jacket);
				this.model.head.visible = playerModel.head.visible;
				this.model.hat.visible = playerModel.hat.visible;
				this.model.body.visible = playerModel.body.visible;
				this.model.rightArm.visible = playerModel.rightArm.visible;
				this.model.leftArm.visible = playerModel.leftArm.visible;
				this.model.rightLeg.visible = playerModel.rightLeg.visible;
				this.model.leftLeg.visible = playerModel.leftLeg.visible;
				this.model.leftSleeve.visible = playerModel.leftSleeve.visible;
				this.model.rightSleeve.visible = playerModel.rightSleeve.visible;
				this.model.leftPants.visible = playerModel.leftPants.visible;
				this.model.rightPants.visible = playerModel.rightPants.visible;
				this.model.jacket.visible = playerModel.jacket.visible;
				playerModel.copyStateTo(this.model);
				this.model.animateModel(entity, limbAngle, limbDistance, tickDelta);
				this.model.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
				this.model.render(matrices, vertexConsumers.getBuffer(isEmissive(uuid) ? RenderLayer.getEntityTranslucentEmissive(texture) : RenderLayer.getEntityTranslucent(texture)), light, LivingEntityRenderer.getOverlay(entity, 0.0F));
			}
		}
	}
	private ContributorData getContributorData(String uuid) {
		if (!ContributorDataLoader.registry.isEmpty()) {
			for (ContributorData contributorData : ContributorDataLoader.registry) {
				if (contributorData.getUuid().equals(uuid)) return contributorData;
			}
		}
		return null;
	}
	private boolean shouldOverlayTexture(String uuid) {
		ContributorData contributorData = getContributorData(uuid);
		return contributorData != null && contributorData.getShouldRenderOverlay();
	}
	private boolean isEmissive(String uuid) {
		ContributorData contributorData = getContributorData(uuid);
		return contributorData != null && contributorData.getIsOverlayEmissive();
	}
	private Identifier getOverlayTexture(String uuid) {
		ContributorData contributorData = getContributorData(uuid);
		if (contributorData != null) return TextureHelper.getTexture(contributorData.getOverlayTexture(), Identifier.of("perspective", "textures/contributors/overlay/none.png"));
		return null;
	}
}