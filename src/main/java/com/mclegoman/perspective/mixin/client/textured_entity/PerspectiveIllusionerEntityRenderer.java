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
import net.minecraft.client.render.entity.IllusionerEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.IllusionerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(IllusionerEntityRenderer.class)
public class PerspectiveIllusionerEntityRenderer {
    @Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    private void getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
        if (entity instanceof IllusionerEntity) cir.setReturnValue(PerspectiveTexturedEntityUtils.getTexture(entity, "minecraft:illusioner", "", cir.getReturnValue()));
    }
}