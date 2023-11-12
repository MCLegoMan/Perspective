/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.textured_entity;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.util.Identifier;

public class TexturedEntityModels {
	public static final EntityModelLayer PIG_OVERLAY = new EntityModelLayer(new Identifier("minecraft", "pig"), "outer");
	public static void init() {
		EntityModelLayerRegistry.registerModelLayer(PIG_OVERLAY, () -> PigEntityModel.getTexturedModelData(new Dilation(0.499F)));
	}
}
