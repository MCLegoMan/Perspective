/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.textured_entity;

import com.mclegoman.perspective.client.texturedentity.PerspectiveTexturedEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.DrownedEntityRenderer;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(DrownedEntityRenderer.class)
public class PerspectiveDrownedEntityRenderer {
    @Inject(at = @At("RETURN"), method = "getTexture", cancellable = true)
    private void getTexture(ZombieEntity entity, CallbackInfoReturnable<Identifier> cir) {
        if (entity instanceof DrownedEntity) cir.setReturnValue(PerspectiveTexturedEntity.getTexture(entity, "minecraft:drowned", "", cir.getReturnValue()));
    }
}