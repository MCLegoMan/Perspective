/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.shaders;

import com.mclegoman.luminance.client.data.ClientData;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

public class SmoothUniforms extends Uniforms {
	public static float viewDistance = ClientData.minecraft.options.getViewDistance().getValue();
	public static Vector3f eyePosition = new Vector3f(0.0F, 0.0F, 0.0F);
	public static float pitch = 0.0F;
	public static float yaw = 0.0F;
	public static float currentHealth = 20.0F;
	public static float maxHealth = 20.0F;
	public static float currentAir = 10.0F;
	public static float maxAir = 10.0F;
	public static float isSprinting = 0.0F;
	public static float isSwimming = 0.0F;
	public static float isSneaking = 0.0F;
	public static float isCrawling = 0.0F;
	public static float alpha = getAlpha();
	public static void init() {
	}
	public static void tick() {
		float tickDelta = ClientData.minecraft.getTickDelta();
		viewDistance = MathHelper.lerp(tickDelta, viewDistance, getViewDistance());

		Vector3f eyePosition2 = getEyePosition();
		eyePosition = new Vector3f(MathHelper.lerp(tickDelta, eyePosition.x, eyePosition2.x), MathHelper.lerp(tickDelta, eyePosition.y, eyePosition2.y), MathHelper.lerp(tickDelta, eyePosition.z, eyePosition2.z));
		pitch = MathHelper.lerp(tickDelta, pitch, getPitch());
		yaw = MathHelper.lerp(tickDelta, yaw, getYaw());

		currentHealth = MathHelper.lerp(tickDelta, currentHealth, getCurrentHealth());
		maxHealth = MathHelper.lerp(tickDelta, maxHealth, getMaxHealth());

		currentAir = MathHelper.lerp(tickDelta, currentAir, getCurrentAir());
		maxAir = MathHelper.lerp(tickDelta, maxAir, getMaxAir());

		isSprinting = MathHelper.lerp(tickDelta, isSprinting, getIsSprinting());
		isSwimming = MathHelper.lerp(tickDelta, isSwimming, getIsSwimming());
		isSneaking = MathHelper.lerp(tickDelta, isSneaking, getIsSneaking());
		isCrawling = MathHelper.lerp(tickDelta, isCrawling, getIsCrawling());

		alpha = MathHelper.lerp(tickDelta, alpha, getAlpha());
	}
	public static void update(JsonEffectShaderProgram program) {
		setUniform(program, "viewDistanceSmooth", viewDistance);

		setUniform(program, "eyePositionSmooth", eyePosition);
		setUniform(program, "pitchSmooth", pitch);
		setUniform(program, "yawSmooth", yaw);

		setUniform(program, "currentHealthSmooth", currentHealth);
		setUniform(program, "maxHealthSmooth", maxHealth);

		setUniform(program, "currentAirSmooth", currentAir);
		setUniform(program, "maxAirSmooth", maxAir);

		setUniform(program, "isSprintingSmooth", isSprinting);
		setUniform(program, "isSwimmingSmooth", isSwimming);
		setUniform(program, "isSneakingSmooth", isSneaking);
		setUniform(program, "isCrawlingSmooth", isCrawling);

		setUniform(program, "alphaSmooth", alpha);
	}
}
