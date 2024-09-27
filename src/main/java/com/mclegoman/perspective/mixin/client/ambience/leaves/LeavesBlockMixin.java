/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.ambience.leaves;

import com.mclegoman.perspective.client.ambience.particles.IgnoreLeavesDataLoader;
import com.mclegoman.perspective.client.ambience.particles.Particles;
import com.mclegoman.perspective.config.ConfigHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {
	@Inject(method = "randomDisplayTick", at = @At("HEAD"))
	protected void perspective$randomDisplayTick(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {
		if ((boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.experimental, "ambience") && (int)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "falling_leaves_density") > 0) {
			if (random.nextInt(64) <= (int)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "falling_leaves_density")) {
				BlockPos blockPos = pos.down();
				if (!Block.isFaceFullSquare(world.getBlockState(blockPos).getCollisionShape(world, blockPos), Direction.UP)) {
					if (!IgnoreLeavesDataLoader.registry.contains(Registries.BLOCK.getId(world.getBlockState(pos).getBlock()))) Particles.addLeaf(world, pos);
				}
			}
		}
	}
}
