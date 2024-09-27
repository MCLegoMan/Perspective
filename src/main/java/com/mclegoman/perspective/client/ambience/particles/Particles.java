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
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Particles {
	public static SimpleParticleType ripple;
	public static void init() {
		ParticleFactoryRegistry.getInstance().register(ripple, RippleParticle.DefaultFactory::new);
	}
	static {
		ripple = Registry.register(Registries.PARTICLE_TYPE, Identifier.of(Data.version.getID(), "ripple"), FabricParticleTypes.simple(true));
	}
}
