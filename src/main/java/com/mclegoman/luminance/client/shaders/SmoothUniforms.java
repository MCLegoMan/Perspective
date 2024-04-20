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
	public static float prevViewDistance = getViewDistance();
	public static float viewDistance = getViewDistance();

	public static Vector3f prevEyePosition = getEyePosition();
	public static Vector3f eyePosition = getEyePosition();

	public static float prevPitch = getPitch();
	public static float pitch = getPitch();

	public static float prevYaw = getYaw();
	public static float yaw = getYaw();

	public static float prevCurrentHealth = getCurrentHealth();
	public static float currentHealth = getCurrentHealth();

	public static float prevMaxHealth = getMaxHealth();
	public static float maxHealth = getMaxHealth();

	public static float prevCurrentAir = getCurrentAir();
	public static float currentAir = getCurrentAir();

	public static float prevMaxAir = getMaxAir();
	public static float maxAir = getMaxAir();

	public static float prevIsSprinting = getIsSprinting();
	public static float isSprinting = getIsSprinting();

	public static float prevIsSwimming = getIsSwimming();
	public static float isSwimming = getIsSwimming();

	public static float prevIsSneaking = getIsSneaking();
	public static float isSneaking = getIsSneaking();

	public static float prevIsCrawling = getIsCrawling();
	public static float isCrawling = getIsCrawling();

	public static float prevAlpha = getAlpha();
	public static float alpha = getAlpha();

	public static void init() {
	}
	public static void tick() {
		prevViewDistance = viewDistance;
		viewDistance = (prevViewDistance + getViewDistance()) * 0.5F;

		prevEyePosition = eyePosition;
		Vector3f currentEyePosition = getEyePosition();
		eyePosition = new Vector3f((prevEyePosition.x + currentEyePosition.x) * 0.5F, (prevEyePosition.y + currentEyePosition.y) * 0.5F, (prevEyePosition.z + currentEyePosition.z) * 0.5F);

		prevPitch = pitch;
		pitch = (prevPitch + getPitch()) * 0.5F;
		prevYaw = yaw;
		yaw = (prevYaw + getYaw()) * 0.5F;

		prevCurrentHealth = currentHealth;
		currentHealth = (prevCurrentHealth + getCurrentHealth()) * 0.5F;
		prevMaxHealth = maxHealth;
		maxHealth = (prevMaxHealth + getMaxHealth()) * 0.5F;

		prevCurrentAir = currentAir;
		currentAir = (prevCurrentAir + getCurrentAir()) * 0.5F;
		prevMaxAir = maxAir;
		maxAir = (prevMaxAir + getMaxAir()) * 0.5F;

		prevIsSprinting = isSprinting;
		isSprinting = (prevIsSprinting + getIsSprinting()) * 0.5F;
		prevIsSwimming = isSwimming;
		isSwimming = (prevIsSwimming + getIsSwimming()) * 0.5F;
		prevIsSneaking = isSneaking;
		isSneaking = (prevIsSneaking + getIsSneaking()) * 0.5F;
		prevIsCrawling = isCrawling;
		isCrawling = (prevIsCrawling + getIsCrawling()) * 0.5F;

		prevAlpha = alpha;
		alpha = (prevAlpha + getAlpha()) * 0.5F;
	}
	public static void update(JsonEffectShaderProgram program) {
		float tickDelta = ClientData.minecraft.getTickDelta();
		setUniform(program, "viewDistanceSmooth", MathHelper.lerp(tickDelta, prevViewDistance, viewDistance));

		setUniform(program, "eyePositionSmooth", new Vector3f(MathHelper.lerp(tickDelta, prevEyePosition.x, eyePosition.x), MathHelper.lerp(tickDelta, prevEyePosition.y, eyePosition.y), MathHelper.lerp(tickDelta, prevEyePosition.z, eyePosition.z)));
		setUniform(program, "pitchSmooth", MathHelper.lerp(tickDelta, prevPitch, pitch));
		setUniform(program, "yawSmooth", MathHelper.lerp(tickDelta, prevYaw, yaw));

		setUniform(program, "currentHealthSmooth", MathHelper.lerp(tickDelta, prevCurrentHealth, currentHealth));
		setUniform(program, "maxHealthSmooth", MathHelper.lerp(tickDelta, prevMaxHealth, maxHealth));

		setUniform(program, "currentAirSmooth", MathHelper.lerp(tickDelta, prevCurrentAir, currentAir));
		setUniform(program, "maxAirSmooth", MathHelper.lerp(tickDelta, prevMaxAir, maxAir));

		setUniform(program, "isSprintingSmooth", MathHelper.lerp(tickDelta, prevIsSprinting, isSprinting));
		setUniform(program, "isSwimmingSmooth", MathHelper.lerp(tickDelta, prevIsSwimming, isSwimming));
		setUniform(program, "isSneakingSmooth", MathHelper.lerp(tickDelta, prevIsSneaking, isSneaking));
		setUniform(program, "isCrawlingSmooth", MathHelper.lerp(tickDelta, prevIsCrawling, isCrawling));

		setUniform(program, "alphaSmooth", MathHelper.lerp(tickDelta, prevAlpha, alpha));
	}
}
