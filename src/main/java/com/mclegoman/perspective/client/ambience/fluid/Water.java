/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.ambience.fluid;

import com.mclegoman.perspective.client.ambience.particles.Particles;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Water {
	public static void spawnRipple(World world, Vec3d pos) {
		world.addParticle(Particles.ripple, pos.getX(), pos.getY(), pos.getZ(), 0.0F, 0.0F, 0.0F);
	}
}
