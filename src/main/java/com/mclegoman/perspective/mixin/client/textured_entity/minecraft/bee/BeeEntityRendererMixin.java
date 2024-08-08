/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.bee;

import com.mclegoman.perspective.client.entity.TexturedEntity;
import com.mclegoman.perspective.client.entity.TexturedEntityModels;
import com.mclegoman.perspective.client.entity.renderer.feature.OverlayFeatureRenderer;
import net.minecraft.client.render.entity.BeeEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BeeEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = BeeEntityRenderer.class)
public abstract class BeeEntityRendererMixin extends MobEntityRenderer<BeeEntity, BeeEntityModel<BeeEntity>> {
	@Shadow
	@Final
	private static Identifier ANGRY_NECTAR_TEXTURE;

	@Shadow
	@Final
	private static Identifier ANGRY_TEXTURE;

	@Shadow
	@Final
	private static Identifier PASSIVE_TEXTURE;

	@Shadow
	@Final
	private static Identifier NECTAR_TEXTURE;

	public BeeEntityRendererMixin(EntityRendererFactory.Context context, BeeEntityModel<BeeEntity> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;)V", at = @At("TAIL"))
	private void perspective$init(EntityRendererFactory.Context context, CallbackInfo ci) {
		this.addFeature(new OverlayFeatureRenderer<>(this, new BeeEntityModel<>(context.getPart(TexturedEntityModels.beeOverlay)), "minecraft:bee", Identifier.of("textures/entity/bee/bee_overlay.png")));
	}

	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity instanceof BeeEntity) {
			if (((BeeEntity) entity).hasAngerTime()) {
				if (((BeeEntity) entity).hasNectar()) {
					cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:bee", TexturedEntity.Affix.SUFFIX, "_angry_nectar", ANGRY_NECTAR_TEXTURE));
				} else {
					cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:bee", TexturedEntity.Affix.SUFFIX, "_angry", ANGRY_TEXTURE));
				}
			} else {
				if (((BeeEntity) entity).hasNectar()) {
					cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:bee", TexturedEntity.Affix.SUFFIX, "_nectar", NECTAR_TEXTURE));
				} else {
					cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:bee", PASSIVE_TEXTURE));
				}
			}
		}
	}
}