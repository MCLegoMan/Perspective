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
import net.minecraft.client.render.entity.AxolotlEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(AxolotlEntityRenderer.class)
public class PerspectiveAxolotlEntityRenderer {
    @Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    private void getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
        if (entity instanceof AxolotlEntity) cir.setReturnValue(PerspectiveTexturedEntityUtils.getTexture(entity, "minecraft:axolotl", "", cir.getReturnValue()));
    }
}