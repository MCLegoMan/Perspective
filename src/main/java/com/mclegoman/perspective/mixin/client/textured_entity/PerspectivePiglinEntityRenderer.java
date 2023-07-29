/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.textured_entity;

import com.mclegoman.perspective.client.textured_entity.PerspectiveTexturedEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.PiglinEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.PiglinBruteEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(PiglinEntityRenderer.class)
public class PerspectivePiglinEntityRenderer {
    @Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    private void perspective$getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
        if (entity instanceof PiglinEntity) cir.setReturnValue(PerspectiveTexturedEntity.getTexture(entity, "minecraft:piglin", "", cir.getReturnValue()));
        else if (entity instanceof ZombifiedPiglinEntity) cir.setReturnValue(PerspectiveTexturedEntity.getTexture(entity, "minecraft:zombified_piglin", "", cir.getReturnValue()));
        else if (entity instanceof PiglinBruteEntity) cir.setReturnValue(PerspectiveTexturedEntity.getTexture(entity, "minecraft:piglin_brute", "", cir.getReturnValue()));
    }
}