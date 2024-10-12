/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.ambience.particles;

import com.mclegoman.perspective.client.texture.TextureHelper;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Random;

public class RippleParticle extends SpriteBillboardParticle {
	public final SpriteProvider spriteProvider;
	public RippleParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ);
		this.velocityX = 0;
		this.velocityY = 0;
		this.velocityZ = 0;
		this.spriteProvider = spriteProvider;
		int randomAge = 1 + new Random().nextInt(16);
		this.scale *= 2.0F + random.nextFloat() / 16.0F * randomAge;
		this.maxAge = 10 + new Random().nextInt(randomAge);
	}
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}
	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;
		if (this.age++ >= this.maxAge) this.markDead();
		else this.alpha = Math.max(0.0F, this.alpha - (1.0F/this.maxAge));
	}
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		this.setSpriteForAge(spriteProvider);
		Vec3d cameraPos = camera.getPos();
		float x = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - cameraPos.getX());
		float y = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - cameraPos.getY());
		float z = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - cameraPos.getZ());
		Vector3f[] positions = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
		for (int axis = 0; axis < 4; ++axis) {
			Vector3f position = positions[axis];
			position.rotate(new Quaternionf().rotateXYZ((float) Math.toRadians(90.0F), 0.0F, 0.0F));
			position.mul(this.getSize(tickDelta));
			position.add(x, y, z);
		}
		float minU = this.getMinU();
		float minV = this.getMinV();
		float maxU = this.getMaxU();
		float maxV = this.getMaxV();
		int light = this.getBrightness(tickDelta);
		vertexConsumer.vertex(positions[0].x(), positions[0].y(), positions[0].z()).texture(maxU, maxV).color(this.red, this.green, this.blue, this.alpha).light(light);
		vertexConsumer.vertex(positions[1].x(), positions[1].y(), positions[1].z()).texture(maxU, minV).color(this.red, this.green, this.blue, this.alpha).light(light);
		vertexConsumer.vertex(positions[2].x(), positions[2].y(), positions[2].z()).texture(minU, minV).color(this.red, this.green, this.blue, this.alpha).light(light);
		vertexConsumer.vertex(positions[3].x(), positions[3].y(), positions[3].z()).texture(minU, maxV).color(this.red, this.green, this.blue, this.alpha).light(light);
	}
	public static class DefaultFactory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;
		public DefaultFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		public Particle createParticle(SimpleParticleType particle, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new RippleParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
		}
	}
}
