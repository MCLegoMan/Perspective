/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.textured_entity;

import com.mclegoman.perspective.client.textured_entity.PerspectiveTexturedEntityUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.IronGolemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(IronGolemEntityRenderer.class)
public class PerspectiveIronGolemEntityRenderer {
    @Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    private void getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
        if (entity instanceof IronGolemEntity) cir.setReturnValue(PerspectiveTexturedEntityUtils.getTexture(entity, "minecraft:iron_golem", "", cir.getReturnValue()));
    }
}