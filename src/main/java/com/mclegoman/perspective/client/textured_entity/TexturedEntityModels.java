/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.textured_entity;

import com.mclegoman.perspective.client.textured_entity.model.CowOverlayEntityModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.util.Identifier;

public class TexturedEntityModels {
	public static final EntityModelLayer pigOverlay = new EntityModelLayer(new Identifier("minecraft", "pig"), "outer");
	public static final EntityModelLayer mooshroomOverlay = new EntityModelLayer(new Identifier("minecraft", "mooshroom"), "outer");
	public static final EntityModelLayer skeletonOverlay = new EntityModelLayer(new Identifier("minecraft", "skeleton"), "outer");
	public static final EntityModelLayer witherSkeletonOverlay = new EntityModelLayer(new Identifier("minecraft", "wither_skeleton"), "outer");
	public static final EntityModelLayer huskOverlay = new EntityModelLayer(new Identifier("minecraft", "husk"), "outer");
	public static final EntityModelLayer zombieOverlay = new EntityModelLayer(new Identifier("minecraft", "zombie"), "outer");
	public static void init() {
		EntityModelLayerRegistry.registerModelLayer(pigOverlay, () -> PigEntityModel.getTexturedModelData(new Dilation(0.499F)));
		EntityModelLayerRegistry.registerModelLayer(mooshroomOverlay, CowOverlayEntityModel::getTexturedOverlayModelData);
		EntityModelLayerRegistry.registerModelLayer(skeletonOverlay, () -> getBipedEntityModelData(new Dilation(0.5F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(witherSkeletonOverlay, () -> getBipedEntityModelData(new Dilation(0.5F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(huskOverlay, () -> getBipedEntityModelData(new Dilation(0.5F), 64, 64));
		EntityModelLayerRegistry.registerModelLayer(zombieOverlay, () -> getBipedEntityModelData(new Dilation(0.5F), 64, 64));
	}
	public static TexturedModelData getBipedEntityModelData(Dilation dilation, int width, int height) {
		return TexturedModelData.of(BipedEntityModel.getModelData(dilation, 0.0F), width, height);
	}
}