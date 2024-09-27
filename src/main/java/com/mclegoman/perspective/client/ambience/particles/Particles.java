/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.ambience.particles;

import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Particles {
	public static SimpleParticleType ripple;
	public static SimpleParticleType leaf;
	public static void init() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new IgnoreLeavesDataLoader());
		ParticleFactoryRegistry.getInstance().register(ripple, RippleParticle.DefaultFactory::new);
		ParticleFactoryRegistry.getInstance().register(leaf, LeafParticle.DefaultFactory::new);
	}
	public static void addRipple(World world, Vec3d pos) {
		world.addParticle(Particles.ripple, pos.getX(), pos.getY(), pos.getZ(), 0.0F, 0.0F, 0.0F);
	}
	public static void addLeaf(World world, BlockPos pos) {
		world.addParticle(Particles.leaf, pos.getX(), pos.getY(), pos.getZ(), 0.0, 0.0, 0.0);
	}
	static {
		ripple = Registry.register(Registries.PARTICLE_TYPE, Identifier.of(Data.version.getID(), "ripple"), FabricParticleTypes.simple(true));
		leaf = Registry.register(Registries.PARTICLE_TYPE, Identifier.of(Data.version.getID(), "leaf"), FabricParticleTypes.simple(true));
	}
}
