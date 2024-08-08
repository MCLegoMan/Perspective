/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.minecart;

import com.mclegoman.perspective.client.entity.TexturedEntity;
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.*;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = MinecartEntityRenderer.class)
public class MinecartEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
		if (entity instanceof ChestMinecartEntity)
			cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:chest_minecart", cir.getReturnValue()));
		else if (entity instanceof CommandBlockMinecartEntity)
			cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:command_block_minecart", cir.getReturnValue()));
		else if (entity instanceof FurnaceMinecartEntity)
			cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:furnace_minecart", cir.getReturnValue()));
		else if (entity instanceof HopperMinecartEntity)
			cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:hopper_minecart", cir.getReturnValue()));
		else if (entity instanceof SpawnerMinecartEntity)
			cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:spawner_minecart", cir.getReturnValue()));
		else if (entity instanceof TntMinecartEntity)
			cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:tnt_minecart", cir.getReturnValue()));
		else if (entity instanceof MinecartEntity)
			cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:minecart", cir.getReturnValue()));
	}
}