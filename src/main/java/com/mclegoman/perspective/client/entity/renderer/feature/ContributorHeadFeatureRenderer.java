/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.entity.renderer.feature;

import com.mclegoman.perspective.client.contributor.ContributorData;
import com.mclegoman.perspective.client.contributor.ContributorDataLoader;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LimbAnimator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.RotationAxis;
import java.util.Map;

public class ContributorHeadFeatureRenderer<T extends PlayerEntity, M extends EntityModel<T> & ModelWithHead> extends FeatureRenderer<T, M> {
	private final float scaleX;
	private final float scaleY;
	private final float scaleZ;
	private final Map<SkullBlock.SkullType, SkullBlockEntityModel> headModels;
	private final HeldItemRenderer heldItemRenderer;
	public ContributorHeadFeatureRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader, HeldItemRenderer heldItemRenderer) {
		this(context, loader, 1.0F, 1.0F, 1.0F, heldItemRenderer);
	}
	public ContributorHeadFeatureRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader, float scaleX, float scaleY, float scaleZ, HeldItemRenderer heldItemRenderer) {
		super(context);
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
		this.headModels = SkullBlockEntityRenderer.getModels(loader);
		this.heldItemRenderer = heldItemRenderer;
	}
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		for (ContributorData contributor : ContributorDataLoader.registry) {
			if (contributor.getUuid().equals(entity.getGameProfile().getId().toString())) {
				if (contributor.getShouldRenderHeadItem()) {
					ItemStack itemStack = Registries.ITEM.get(contributor.getHeadItem()).getDefaultStack();
					if (!itemStack.isEmpty()) {
						Item item = itemStack.getItem();
						matrices.push();
						matrices.scale(this.scaleX, this.scaleY, this.scaleZ);
						this.getContextModel().getHead().rotate(matrices);
						if (item instanceof BlockItem && ((BlockItem)item).getBlock() instanceof AbstractSkullBlock) {
							matrices.scale(1.1875F, -1.1875F, -1.1875F);
							ProfileComponent profileComponent = itemStack.get(DataComponentTypes.PROFILE);
							matrices.translate(-0.5, 0.0, -0.5);
							SkullBlock.SkullType skullType = ((AbstractSkullBlock)((BlockItem)item).getBlock()).getSkullType();
							SkullBlockEntityModel skullBlockEntityModel = this.headModels.get(skullType);
							RenderLayer renderLayer = SkullBlockEntityRenderer.getRenderLayer(skullType, profileComponent);
							Entity vehicle = entity.getVehicle();
							LimbAnimator limbAnimator = vehicle instanceof LivingEntity vehicleEntity ? vehicleEntity.limbAnimator : entity.limbAnimator;
							SkullBlockEntityRenderer.renderSkull(null, 180.0F, limbAnimator.getPos(tickDelta), matrices, vertexConsumers, light, skullBlockEntityModel, renderLayer);
						} else {
							matrices.translate(0.0F, -0.25F, 0.0F);
							matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
							matrices.scale(0.625F, -0.625F, -0.625F);
							this.heldItemRenderer.renderItem(entity, itemStack, ModelTransformationMode.HEAD, false, matrices, vertexConsumers, light);
						}
						matrices.pop();
					}
				}
			}
		}
	}
}
