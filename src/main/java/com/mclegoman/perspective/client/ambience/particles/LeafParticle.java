/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.ambience.particles;

import com.mclegoman.perspective.client.texture.TextureHelper;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;

public class LeafParticle extends CherryLeavesParticle {
	public LeafParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
		super(world, x + world.random.nextDouble(), y - 0.05, z + world.random.nextDouble(), spriteProvider);
		BlockPos blockPos = BlockPos.ofFloored(x, y, z);
		double[] color = TextureHelper.getAverageColorFromBlock(world, blockPos, world.getBlockState(blockPos));
		this.red = (float) color[0];
		this.green = (float) color[1];
		this.blue = (float) color[2];
	}
	public void tick() {
		super.tick();
		//TODO: Rewrite tick function so we can have leaves be on the floor then fade before despawning.
	}
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}
	public static class DefaultFactory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;
		public DefaultFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		public Particle createParticle(SimpleParticleType particle, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new LeafParticle(world, x, y, z, this.spriteProvider);
		}
	}
}
