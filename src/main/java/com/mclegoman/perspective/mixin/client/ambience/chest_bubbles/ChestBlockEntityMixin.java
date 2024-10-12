/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.ambience.chest_bubbles;

import com.mclegoman.perspective.client.ambience.particles.ParticleTypes;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.config.ConfigHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntity.class)
public abstract class ChestBlockEntityMixin {
	@Inject(method = "clientTick", at = @At("TAIL"))
	private static void perspective$clientTick(World world, BlockPos pos, BlockState state, ChestBlockEntity blockEntity, CallbackInfo ci) {
		try {
			if (((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "chest_bubbles_density") > 0)) {
				if (state.get(Properties.WATERLOGGED)) {
					if (world.random.nextInt(32) <= (int)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "chest_bubbles_density")) {
						if (world.getBlockEntity(pos) instanceof ChestBlockEntity chestBlockEntity) {
							if (chestBlockEntity.getAnimationProgress(ClientData.minecraft.getRenderTickCounter().getTickDelta(false)) > 0) ParticleTypes.addChestBubbles(world, pos);
						} else if (world.getBlockEntity(pos) instanceof EnderChestBlockEntity enderChestBlockEntity) {
							if (enderChestBlockEntity.getAnimationProgress(ClientData.minecraft.getRenderTickCounter().getTickDelta(false)) > 0) ParticleTypes.addChestBubbles(world, pos);
						}
					}
				}
			}
		} catch (Exception ignored) {}
	}
}
