/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.entity_renderer;

import com.mclegoman.perspective.client.textured_entity.PerspectiveTexturedEntity;
import com.mclegoman.perspective.client.textured_entity.features.PerspectiveSaddleableEntityOverlayFeatureRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PigEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Environment(EnvType.CLIENT)
@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(priority = 10000, value = PigEntityRenderer.class)
public abstract class PerspectivePigEntityRenderer extends LivingEntityRenderer {
    public PerspectivePigEntityRenderer(EntityRendererFactory.Context ctx, EntityModel model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }
    @Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;)V", at = @At("TAIL"))
    private void perspective$init(EntityRendererFactory.Context context, CallbackInfo ci) {
        this.addFeature(new PerspectiveSaddleableEntityOverlayFeatureRenderer(this, new PigEntityModel(context.getPart(EntityModelLayers.PIG_SADDLE)), "minecraft:pig", new Identifier("textures/entity/pig/pig_overlay.png")));
    }
    @Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    private void perspective$getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
        if (entity instanceof PigEntity) cir.setReturnValue(PerspectiveTexturedEntity.getTexture(entity, "minecraft:pig", "", cir.getReturnValue()));
    }
}