/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.textured_entity;

import com.mclegoman.perspective.client.textured_entity.models.SkeletonOverlayModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.entity.feature.SkeletonOverlayFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.util.Identifier;

public class TexturedEntityModels {
	public static final EntityModelLayer PIG_OVERLAY = new EntityModelLayer(new Identifier("minecraft", "pig"), "outer");
	public static final EntityModelLayer SKELETON_OVERLAY = new EntityModelLayer(new Identifier("minecraft", "skeleton"), "outer");
	public static final EntityModelLayer WITHER_SKELETON_OVERLAY = new EntityModelLayer(new Identifier("minecraft", "wither_skeleton"), "outer");

	public static void init() {
		EntityModelLayerRegistry.registerModelLayer(PIG_OVERLAY, () -> PigEntityModel.getTexturedModelData(new Dilation(0.499F)));
		EntityModelLayerRegistry.registerModelLayer(SKELETON_OVERLAY, () -> SkeletonOverlayModel.getTexturedModelData(new Dilation(0.5F)));
		EntityModelLayerRegistry.registerModelLayer(WITHER_SKELETON_OVERLAY, () -> SkeletonOverlayModel.getTexturedModelData(new Dilation(0.5F)));
	}
}