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
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.*;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(MinecartEntityRenderer.class)
public class PerspectiveMinecartEntityRenderer {
    @Inject(at = @At("HEAD"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    private void perspective$getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
        if (entity instanceof ChestMinecartEntity) cir.setReturnValue(PerspectiveTexturedEntity.getTexture(entity, "minecraft:chest_minecart", "", cir.getReturnValue()));
        else if (entity instanceof CommandBlockMinecartEntity) cir.setReturnValue(PerspectiveTexturedEntity.getTexture(entity, "minecraft:command_block_minecart", "", cir.getReturnValue()));
        else if (entity instanceof FurnaceMinecartEntity) cir.setReturnValue(PerspectiveTexturedEntity.getTexture(entity, "minecraft:furnace_minecart", "", cir.getReturnValue()));
        else if (entity instanceof HopperMinecartEntity) cir.setReturnValue(PerspectiveTexturedEntity.getTexture(entity, "minecraft:hopper_minecart", "", cir.getReturnValue()));
        else if (entity instanceof SpawnerMinecartEntity) cir.setReturnValue(PerspectiveTexturedEntity.getTexture(entity, "minecraft:spawner_minecart", "", cir.getReturnValue()));
        else if (entity instanceof TntMinecartEntity) cir.setReturnValue(PerspectiveTexturedEntity.getTexture(entity, "minecraft:tnt_minecart", "", cir.getReturnValue()));
        else if (entity instanceof MinecartEntity) cir.setReturnValue(PerspectiveTexturedEntity.getTexture(entity, "minecraft:minecart", "", cir.getReturnValue()));
    }
}