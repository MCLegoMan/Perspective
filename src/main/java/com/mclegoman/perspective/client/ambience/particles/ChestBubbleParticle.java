/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.ambience.particles;

import net.minecraft.block.AbstractChestBlock;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;

public class ChestBubbleParticle extends SpriteBillboardParticle {
	ChestBubbleParticle(ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, x, y, z);
		this.gravityStrength = -0.125F;
		this.velocityMultiplier = 0.85F;
		this.setBoundingBoxSpacing(0.02F, 0.02F);
		this.scale *= this.random.nextFloat() * 0.6F + 0.2F;
		this.velocityX = velocityX * 0.20000000298023224 + (Math.random() * 2.0 - 1.0) * 0.019999999552965164;
		this.velocityY = velocityY * 0.20000000298023224 + (Math.random() * 2.0 - 1.0) * 0.019999999552965164;
		this.velocityZ = velocityZ * 0.20000000298023224 + (Math.random() * 2.0 - 1.0) * 0.019999999552965164;
		this.maxAge = (int)(40.0 / (Math.random() * 0.8 + 0.2));
	}

	public void tick() {
		super.tick();
		if (!this.dead && (!this.world.getFluidState(BlockPos.ofFloored(this.x, this.y, this.z)).isIn(FluidTags.WATER) && !(this.world.getBlockState(BlockPos.ofFloored(this.x, this.y, this.z)).getBlock() instanceof AbstractChestBlock<?>))) {
			this.markDead();
		}
	}
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}
	public static class DefaultFactory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;
		public DefaultFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			ChestBubbleParticle particle = new ChestBubbleParticle(clientWorld, d, e, f, g, h, i);
			particle.setSprite(this.spriteProvider);
			return particle;
		}
	}
}
