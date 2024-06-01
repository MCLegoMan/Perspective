/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.husk;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import com.mclegoman.perspective.client.textured_entity.TexturedEntityModels;
import com.mclegoman.perspective.client.textured_entity.renderer.feature.OverlayFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.HuskEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = HuskEntityRenderer.class)
public abstract class HuskEntityRendererMixin extends MobEntityRenderer<ZombieEntity, ZombieEntityModel<ZombieEntity>> {
	public HuskEntityRendererMixin(EntityRendererFactory.Context context, ZombieEntityModel<ZombieEntity> entityModel, float f) {
		super(context, entityModel, f);
	}
	@Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;)V", at = @At("TAIL"))
	private void perspective$init(EntityRendererFactory.Context context, CallbackInfo ci) {
		this.addFeature(new OverlayFeatureRenderer<>(this, new ZombieEntityModel<>(context.getPart(TexturedEntityModels.zombieOverlay)), "minecraft:zombie", Identifier.of("textures/entity/zombie/husk_overlay.png")));
	}
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity instanceof HuskEntity)
			cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:husk", cir.getReturnValue()));
	}
}