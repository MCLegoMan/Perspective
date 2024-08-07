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
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

public class TexturedEntityModels {
	public static double piglinCapeY = 0.0F;
	public static final EntityModelLayer pigOverlay = new EntityModelLayer(Identifier.of("minecraft", "pig"), "outer");
	public static final EntityModelLayer mooshroomOverlay = new EntityModelLayer(Identifier.of("minecraft", "mooshroom"), "outer");
	public static final EntityModelLayer skeletonOverlay = new EntityModelLayer(Identifier.of("minecraft", "skeleton"), "outer");
	public static final EntityModelLayer witherSkeletonOverlay = new EntityModelLayer(Identifier.of("minecraft", "wither_skeleton"), "outer");
	public static final EntityModelLayer huskOverlay = new EntityModelLayer(Identifier.of("minecraft", "husk"), "outer");
	public static final EntityModelLayer zombieOverlay = new EntityModelLayer(Identifier.of("minecraft", "zombie"), "outer");
	public static void init() {
		EntityModelLayerRegistry.registerModelLayer(pigOverlay, () -> PigEntityModel.getTexturedModelData(new Dilation(0.499F)));
		EntityModelLayerRegistry.registerModelLayer(mooshroomOverlay, CowOverlayEntityModel::getTexturedOverlayModelData);
		EntityModelLayerRegistry.registerModelLayer(skeletonOverlay, () -> getBipedEntityModelData(new Dilation(0.5F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(witherSkeletonOverlay, () -> getBipedEntityModelData(new Dilation(0.5F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(huskOverlay, () -> getBipedEntityModelData(new Dilation(0.5F), 64, 64));
		EntityModelLayerRegistry.registerModelLayer(zombieOverlay, () -> getBipedEntityModelData(new Dilation(0.5F), 64, 64));
	}
	public static void tick() {
		piglinCapeY = (piglinCapeY + 0.5F) % 80.0F;
	}
	public static double getPiglinCapeY(MobEntity entity) {
		return (piglinCapeY + entity.hashCode()) % 80.0F;
	}
	public static TexturedModelData getBipedEntityModelData(Dilation dilation, int width, int height) {
		return TexturedModelData.of(BipedEntityModel.getModelData(dilation, 0.0F), width, height);
	}
}