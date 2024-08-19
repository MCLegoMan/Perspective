/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.bogged;

import com.mclegoman.perspective.client.entity.TexturedEntity;
import com.mclegoman.perspective.client.entity.TexturedEntityModels;
import com.mclegoman.perspective.client.entity.model.LivingEntityCapeModel;
import com.mclegoman.perspective.client.entity.renderer.feature.EntityCapeFeatureRenderer;
import net.minecraft.client.render.entity.BoggedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SkeletonEntityRenderer;
import net.minecraft.entity.mob.BoggedEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = BoggedEntityRenderer.class)
public abstract class BoggedEntityRendererMixin extends SkeletonEntityRenderer<BoggedEntity> {
	public BoggedEntityRendererMixin(EntityRendererFactory.Context context) {
		super(context);
	}
	@Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;)V", at = @At("TAIL"))
	private void perspective$init(EntityRendererFactory.Context context, CallbackInfo ci) {
		this.addFeature(new EntityCapeFeatureRenderer.Builder(this, new LivingEntityCapeModel(context.getPart(TexturedEntityModels.entityCape)), Identifier.of("perspective", "textures/entity/skeleton/bogged_cape.png")).build());
	}
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/mob/BoggedEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(BoggedEntity entity, CallbackInfoReturnable<Identifier> cir) {
		cir.setReturnValue(TexturedEntity.getTexture(entity, cir.getReturnValue()));
	}
}