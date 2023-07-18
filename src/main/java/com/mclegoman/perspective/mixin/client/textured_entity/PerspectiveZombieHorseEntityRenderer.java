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
import net.minecraft.client.render.entity.ZombieHorseEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ZombieHorseEntityRenderer.class)
public class PerspectiveZombieHorseEntityRenderer {
    @Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    private void getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
        if (entity instanceof SkeletonHorseEntity) cir.setReturnValue(PerspectiveTexturedEntityUtils.getTexture(entity, "minecraft:skeleton_horse", "", cir.getReturnValue()));
        else if (entity instanceof ZombieHorseEntity) cir.setReturnValue(PerspectiveTexturedEntityUtils.getTexture(entity, "minecraft:zombie_horse", "", cir.getReturnValue()));
    }
}