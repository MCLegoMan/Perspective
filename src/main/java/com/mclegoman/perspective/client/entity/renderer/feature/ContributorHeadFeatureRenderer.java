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
import net.minecraft.entity.EquipmentSlot;
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

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T player, float f, float g, float h, float j, float k, float l) {
		for (ContributorData contributor : ContributorDataLoader.registry) {
			if (contributor.getUuid().equals(player.getGameProfile().getId().toString())) {
				if (contributor.getShouldRenderHeadItem()) {
					ItemStack itemStack = Registries.ITEM.get(contributor.getHeadItem()).getDefaultStack();
					if (!itemStack.isEmpty()) {
						Item item = itemStack.getItem();
						matrixStack.push();
						matrixStack.scale(this.scaleX, this.scaleY, this.scaleZ);
						float n;
						this.getContextModel().getHead().rotate(matrixStack);
						if (item instanceof BlockItem && ((BlockItem)item).getBlock() instanceof AbstractSkullBlock) {
							n = 1.1875F;
							matrixStack.scale(1.1875F, -1.1875F, -1.1875F);

							ProfileComponent profileComponent = itemStack.get(DataComponentTypes.PROFILE);
							matrixStack.translate(-0.5, 0.0, -0.5);
							SkullBlock.SkullType skullType = ((AbstractSkullBlock)((BlockItem)item).getBlock()).getSkullType();
							SkullBlockEntityModel skullBlockEntityModel = this.headModels.get(skullType);
							RenderLayer renderLayer = SkullBlockEntityRenderer.getRenderLayer(skullType, profileComponent);
							Entity var22 = player.getVehicle();
							LimbAnimator limbAnimator;
							if (var22 instanceof LivingEntity) {
								LivingEntity livingEntity2 = (LivingEntity)var22;
								limbAnimator = livingEntity2.limbAnimator;
							} else {
								limbAnimator = player.limbAnimator;
							}

							float o = limbAnimator.getPos(h);
							SkullBlockEntityRenderer.renderSkull(null, 180.0F, o, matrixStack, vertexConsumerProvider, i, skullBlockEntityModel, renderLayer);
						} else {
							label54: {
								if (item instanceof ArmorItem) {
									ArmorItem armorItem = (ArmorItem)item;
									if (armorItem.getSlotType() == EquipmentSlot.HEAD) {
										break label54;
									}
								}

								translate(matrixStack);
								this.heldItemRenderer.renderItem(player, itemStack, ModelTransformationMode.HEAD, false, matrixStack, vertexConsumerProvider, i);
							}
						}

						matrixStack.pop();
					}
				}
			}
		}
	}

	public static void translate(MatrixStack matrices) {
		float f = 0.625F;
		matrices.translate(0.0F, -0.25F, 0.0F);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
		matrices.scale(0.625F, -0.625F, -0.625F);

	}
}
