/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.giant;

import com.mclegoman.perspective.client.entity.TexturedEntity;
import com.mclegoman.perspective.client.entity.TexturedEntityModels;
import com.mclegoman.perspective.client.entity.renderer.feature.OverlayFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.GiantEntityModel;
import net.minecraft.entity.mob.GiantEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = net.minecraft.client.render.entity.GiantEntityRenderer.class)
public abstract class GiantEntityRendererMixin extends MobEntityRenderer<GiantEntity, BipedEntityModel<GiantEntity>> {
	public GiantEntityRendererMixin(EntityRendererFactory.Context context, BipedEntityModel<GiantEntity> entityModel, float f) {
		super(context, entityModel, f);
	}
	@Inject(method = "<init>", at = @At("TAIL"))
	private void perspective$init(EntityRendererFactory.Context context, float scale, CallbackInfo ci) {
		this.addFeature(new OverlayFeatureRenderer<>(this, new GiantEntityModel(context.getPart(TexturedEntityModels.zombieOverlay)), Identifier.of("textures/entity/zombie/zombie_overlay.png")));
	}
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/mob/GiantEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(GiantEntity entity, CallbackInfoReturnable<Identifier> cir) {
		cir.setReturnValue(TexturedEntity.getTexture(entity, cir.getReturnValue()));
	}
}