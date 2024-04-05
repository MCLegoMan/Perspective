/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.mooshroom;

import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import com.mclegoman.perspective.client.textured_entity.TexturedEntityModels;
import com.mclegoman.perspective.client.textured_entity.renderer.feature.MooshroomOverlayFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.MooshroomEntityRenderer;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(priority = 100, value = MooshroomEntityRenderer.class)
public class MooshroomEntityRendererMixin extends MobEntityRenderer<MooshroomEntity, CowEntityModel<MooshroomEntity>> {
	@Shadow @Final
	private static Map<MooshroomEntity.Type, Identifier> TEXTURES;
	public MooshroomEntityRendererMixin(EntityRendererFactory.Context context, CowEntityModel<MooshroomEntity> entityModel, float f) {
		super(context, entityModel, f);
	}
	@Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;)V", at = @At("TAIL"))
	private void perspective$init(EntityRendererFactory.Context context, CallbackInfo ci) {
		this.addFeature(new MooshroomOverlayFeatureRenderer<>(this, new CowEntityModel<>(context.getPart(TexturedEntityModels.mooshroomOverlay))));
	}
	@Override
	public Identifier getTexture(MooshroomEntity mooshroomEntity) {
		boolean isTexturedEntity = true;
		JsonObject entitySpecific = TexturedEntity.getEntitySpecific(mooshroomEntity, "minecraft:mooshroom");
		if (entitySpecific != null) {
			String type = String.valueOf(mooshroomEntity.getVariant()).toLowerCase();
			if (entitySpecific.has(type)) {
				JsonObject typeRegistry = JsonHelper.getObject(entitySpecific, String.valueOf(mooshroomEntity.getVariant()).toLowerCase());
				if (typeRegistry != null) {
					isTexturedEntity = JsonHelper.getBoolean(typeRegistry, "enabled", true);
				}
			}
		}
		if (isTexturedEntity) return TexturedEntity.getTexture(mooshroomEntity, "minecraft:mooshroom", TexturedEntity.Affix.PREFIX, mooshroomEntity.getVariant().asString().toLowerCase() + "_", TEXTURES.get(mooshroomEntity.getVariant()));
		else return TEXTURES.get(mooshroomEntity.getVariant());
	}
}