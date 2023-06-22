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
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.vehicle.*;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(MinecartEntityRenderer.class)
public class PerspectiveMinecartEntityRenderer {
    @Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    private void getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
        if (entity instanceof ChestMinecartEntity) cir.setReturnValue(PerspectiveNamedEntityUtils.getTexture(entity, "minecraft:chest_minecart", "", cir.getReturnValue()));
        else if (entity instanceof CommandBlockMinecartEntity) cir.setReturnValue(PerspectiveNamedEntityUtils.getTexture(entity, "minecraft:command_block_minecart", "", cir.getReturnValue()));
        else if (entity instanceof FurnaceMinecartEntity) cir.setReturnValue(PerspectiveNamedEntityUtils.getTexture(entity, "minecraft:furnace_minecart", "", cir.getReturnValue()));
        else if (entity instanceof HopperMinecartEntity) cir.setReturnValue(PerspectiveNamedEntityUtils.getTexture(entity, "minecraft:hopper_minecart", "", cir.getReturnValue()));
        else if (entity instanceof SpawnerMinecartEntity) cir.setReturnValue(PerspectiveNamedEntityUtils.getTexture(entity, "minecraft:spawner_minecart", "", cir.getReturnValue()));
        else if (entity instanceof TntMinecartEntity) cir.setReturnValue(PerspectiveNamedEntityUtils.getTexture(entity, "minecraft:tnt_minecart", "", cir.getReturnValue()));
        else if (entity instanceof MinecartEntity) cir.setReturnValue(PerspectiveNamedEntityUtils.getTexture(entity, "minecraft:minecart", "", cir.getReturnValue()));
    }
}