/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.piglin;

import com.mclegoman.perspective.client.entity.TexturedEntity;
import com.mclegoman.perspective.client.entity.TexturedEntityModels;
import com.mclegoman.perspective.client.entity.model.LivingEntityCapeModel;
import com.mclegoman.perspective.client.entity.renderer.feature.EntityCapeFeatureRenderer;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PiglinEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PiglinEntityModel;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = PiglinEntityRenderer.class)
public abstract class PiglinEntityRendererMixin extends BipedEntityRenderer<MobEntity, PiglinEntityModel<MobEntity>> {
	public PiglinEntityRendererMixin(EntityRendererFactory.Context ctx, PiglinEntityModel<MobEntity> model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}
	@Inject(method = "<init>", at = @At("TAIL"))
	private void perspective$init(EntityRendererFactory.Context context, EntityModelLayer mainLayer, EntityModelLayer innerArmorLayer, EntityModelLayer outerArmorLayer, boolean zombie, CallbackInfo ci) {
		this.addFeature(new EntityCapeFeatureRenderer(this, new LivingEntityCapeModel(context.getPart(TexturedEntityModels.entityCape)), Identifier.of("perspective", "textures/entity/piglin/piglin_cape.png")));
	}
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/mob/MobEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(MobEntity entity, CallbackInfoReturnable<Identifier> cir) {
		cir.setReturnValue(TexturedEntity.getTexture(entity, cir.getReturnValue()));
	}
}