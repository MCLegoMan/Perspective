/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.entity;

import com.mclegoman.perspective.client.entity.model.ArmorStandOverlayEntityModel;
import com.mclegoman.perspective.client.entity.model.CowOverlayEntityModel;
import com.mclegoman.perspective.client.entity.model.LivingEntityCapeModel;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class TexturedEntityModels {
	public static double piglinCapeY = 0.0F;
	public static final EntityModelLayer entityCape = new EntityModelLayer(Identifier.of(Data.version.getID(), "entity"), "cape");
	public static final EntityModelLayer pigOverlay = new EntityModelLayer(Identifier.of(Data.version.getID(), "pig"), "outer");
	public static final EntityModelLayer beeOverlay = new EntityModelLayer(Identifier.of(Data.version.getID(), "bee"), "outer");
	public static final EntityModelLayer mooshroomOverlay = new EntityModelLayer(Identifier.of(Data.version.getID(), "mooshroom"), "outer");
	public static final EntityModelLayer skeletonOverlay = new EntityModelLayer(Identifier.of(Data.version.getID(), "skeleton"), "outer");
	public static final EntityModelLayer zombieOverlay = new EntityModelLayer(Identifier.of(Data.version.getID(), "zombie"), "outer");
	public static final EntityModelLayer armorStandOverlay = new EntityModelLayer(Identifier.of(Data.version.getID(), "armor_stand"), "outer");
	public static void init() {
		EntityModelLayerRegistry.registerModelLayer(entityCape, () -> TexturedModelData.of(LivingEntityCapeModel.getModelData(new Dilation(0.0F)), 64, 64));
		EntityModelLayerRegistry.registerModelLayer(pigOverlay, () -> PigEntityModel.getTexturedModelData(new Dilation(0.499F)));
		EntityModelLayerRegistry.registerModelLayer(beeOverlay, () -> getBeeEntityModelData(new Dilation(0.5F)));
		EntityModelLayerRegistry.registerModelLayer(mooshroomOverlay, CowOverlayEntityModel::getTexturedOverlayModelData);
		EntityModelLayerRegistry.registerModelLayer(skeletonOverlay, () -> getBipedEntityModelData(new Dilation(0.5F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(zombieOverlay, () -> getBipedEntityModelData(new Dilation(0.5F), 64, 64));
		EntityModelLayerRegistry.registerModelLayer(armorStandOverlay, () -> TexturedModelData.of(ArmorStandOverlayEntityModel.getModelData(new Dilation(0.01F), 0.0F), 64, 64));
	}
	public static void tick() {
		piglinCapeY = (piglinCapeY + 0.5F) % 80.0F;
	}
	public static double getPiglinCapeY(LivingEntity entity) {
		return (piglinCapeY + entity.hashCode()) % 80.0F;
	}
	public static TexturedModelData getBipedEntityModelData(Dilation dilation, int width, int height) {
		return TexturedModelData.of(BipedEntityModel.getModelData(dilation, 0.0F), width, height);
	}
	public static TexturedModelData getBeeEntityModelData(Dilation dilation) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData modelPartData2 = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 19.0F, 0.0F));
		ModelPartData modelPartData3 = modelPartData2.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-3.5F, -4.0F, -5.0F, 7.0F, 7.0F, 10.0F, dilation), ModelTransform.NONE);
		modelPartData3.addChild("stinger", ModelPartBuilder.create().uv(26, 7).cuboid(0.0F, -1.0F, 5.0F, 0.0F, 1.0F, 2.0F, dilation), ModelTransform.NONE);
		modelPartData3.addChild("left_antenna", ModelPartBuilder.create().uv(2, 0).cuboid(1.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, dilation), ModelTransform.pivot(0.0F, -2.0F, -5.0F));
		modelPartData3.addChild("right_antenna", ModelPartBuilder.create().uv(2, 3).cuboid(-2.5F, -2.0F, -3.0F, 1.0F, 2.0F, 3.0F, dilation), ModelTransform.pivot(0.0F, -2.0F, -5.0F));
		Dilation outerDilation = dilation.add(0.001F);
		modelPartData2.addChild("right_wing", ModelPartBuilder.create().uv(0, 18).cuboid(-9.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, outerDilation), ModelTransform.of(-1.5F, -4.0F, -3.0F, 0.0F, -0.2618F, 0.0F));
		modelPartData2.addChild("left_wing", ModelPartBuilder.create().uv(0, 18).mirrored().cuboid(0.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, outerDilation), ModelTransform.of(1.5F, -4.0F, -3.0F, 0.0F, 0.2618F, 0.0F));
		modelPartData2.addChild("front_legs", ModelPartBuilder.create().cuboid("front_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 1), ModelTransform.pivot(1.5F, 3.0F, -2.0F));
		modelPartData2.addChild("middle_legs", ModelPartBuilder.create().cuboid("middle_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 3), ModelTransform.pivot(1.5F, 3.0F, 0.0F));
		modelPartData2.addChild("back_legs", ModelPartBuilder.create().cuboid("back_legs", -5.0F, 0.0F, 0.0F, 7, 2, 0, 26, 5), ModelTransform.pivot(1.5F, 3.0F, 2.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}
}