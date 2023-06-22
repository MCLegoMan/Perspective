/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.mixin.named_entity.textures;

import com.mclegoman.perspective.client.util.PerspectiveNamedEntityUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.DrownedEntityRenderer;
import net.minecraft.client.render.entity.PigEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
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
        if (entity instanceof DrownedEntity) cir.setReturnValue(PerspectiveNamedEntityUtils.getTexture(entity, "minecraft:drowned", "", cir.getReturnValue()));
    }
}