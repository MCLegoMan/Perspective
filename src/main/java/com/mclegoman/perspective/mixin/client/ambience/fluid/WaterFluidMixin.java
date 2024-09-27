/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.ambience.fluid;

import com.mclegoman.perspective.client.ambience.fluid.Water;
import com.mclegoman.perspective.config.ConfigHelper;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WaterFluid.class)
public class WaterFluidMixin {
	@Inject(method = "randomDisplayTick", at = @At("HEAD"))
	protected void perspective$randomDisplayTick(World world, BlockPos pos, FluidState state, Random random, CallbackInfo ci) {
		if ((boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.experimental, "ambience") && (int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "ripple_density") > 0) {
			if (world.getFluidState(pos).isStill() && world.isRaining() && world.getBlockState(pos.add(0, 1, 0)).isAir()) {
				if (random.nextInt(16) <= (int)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "ripple_density")) {
					if (world.getBiome(pos).value().getPrecipitation(pos) == Biome.Precipitation.RAIN && world.isSkyVisibleAllowingSea(pos)) {
						Water.spawnRipple(world, Vec3d.ofCenter(pos).add(random.nextFloat() - random.nextFloat(), 0.40F, random.nextFloat() - random.nextFloat()));
					}
				}
			}
		}
	}
}
