/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.textured_entity;

import com.mclegoman.perspective.client.util.PerspectiveTexturedEntityUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.PhantomEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(PhantomEntityRenderer.class)
public class PerspectivePhantomEntityRenderer {
    @Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    private void getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
        if (entity instanceof PhantomEntity) cir.setReturnValue(PerspectiveTexturedEntityUtils.getTexture(entity, "minecraft:phantom", "", cir.getReturnValue()));
    }
}