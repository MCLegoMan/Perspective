/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.textured_entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.util.Identifier;

public class TexturedEntityModels {
	public static final EntityModelLayer PIG_OVERLAY = new EntityModelLayer(new Identifier("minecraft", "pig"), "outer");
	public static final EntityModelLayer SKELETON_OVERLAY = new EntityModelLayer(new Identifier("minecraft", "skeleton"), "outer");
	public static final EntityModelLayer WITHER_SKELETON_OVERLAY = new EntityModelLayer(new Identifier("minecraft", "wither_skeleton"), "outer");
	public static final EntityModelLayer HUSK_OVERLAY = new EntityModelLayer(new Identifier("minecraft", "husk"), "outer");
	public static final EntityModelLayer ZOMBIE_OVERLAY = new EntityModelLayer(new Identifier("minecraft", "zombie"), "outer");
	public static void init() {
		EntityModelLayerRegistry.registerModelLayer(PIG_OVERLAY, () -> PigEntityModel.getTexturedModelData(new Dilation(0.499F)));
		EntityModelLayerRegistry.registerModelLayer(SKELETON_OVERLAY, () -> getBipedEntityModelData(new Dilation(0.5F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(WITHER_SKELETON_OVERLAY, () -> getBipedEntityModelData(new Dilation(0.5F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(HUSK_OVERLAY, () -> getBipedEntityModelData(new Dilation(0.5F), 64, 64));
		EntityModelLayerRegistry.registerModelLayer(ZOMBIE_OVERLAY, () -> getBipedEntityModelData(new Dilation(0.5F), 64, 64));
	}
	public static TexturedModelData getBipedEntityModelData(Dilation dilation, int width, int height) {
		return TexturedModelData.of(BipedEntityModel.getModelData(dilation, 0.0F), width, height);
	}
}