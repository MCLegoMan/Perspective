/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.shaders;

import net.minecraft.entity.effect.StatusEffects;

public class SmoothUniforms extends Uniforms {
	public static float prevViewDistance = getViewDistance();
	public static float viewDistance = getViewDistance();
	public static float prevFov = getFov();
	public static float fov = getFov();
	public static float prevTime = getTime();
	public static float time = getTime();
	public static float[] prevEyePosition = getEyePosition();
	public static float[] eyePosition = getEyePosition();
	public static float[] prevPosition = getPosition();
	public static float[] position = getPosition();
	public static float prevPitch = getPitch();
	public static float pitch = getPitch();
	public static float prevYaw = getYaw();
	public static float yaw = getYaw();
	public static float prevCurrentHealth = getCurrentHealth();
	public static float currentHealth = getCurrentHealth();
	public static float prevMaxHealth = getMaxHealth();
	public static float maxHealth = getMaxHealth();
	public static float prevCurrentAbsorption = getCurrentAbsorption();
	public static float currentAbsorption = getCurrentAbsorption();
	public static float prevMaxAbsorption = getMaxAbsorption();
	public static float maxAbsorption = getMaxAbsorption();
	public static float prevCurrentHurtTime = getCurrentHurtTime();
	public static float currentHurtTime = getCurrentHurtTime();
	public static float prevMaxHurtTime = getMaxHurtTime();
	public static float maxHurtTime = getMaxHurtTime();
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
	public static float prevIsInvisible = getIsInvisible();
	public static float isInvisible = getIsInvisible();
	public static float prevIsWithered = getHasEffect(StatusEffects.WITHER);
	public static float isWithered = getHasEffect(StatusEffects.WITHER);
	public static float prevIsPoisoned = getHasEffect(StatusEffects.POISON);
	public static float isPoisoned = getHasEffect(StatusEffects.POISON);
	public static float prevIsBurning = getIsBurning();
	public static float isBurning = getIsBurning();
	public static float prevIsOnGround = getIsOnGround();
	public static float isOnGround = getIsOnGround();
	public static float prevIsOnLadder = getIsOnLadder();
	public static float isOnLadder = getIsOnLadder();
	public static float prevIsRiding = getIsRiding();
	public static float isRiding = getIsRiding();
	public static float prevHasPassengers = getHasPassengers();
	public static float hasPassengers = getHasPassengers();
	public static float prevBiomeTemperature = getBiomeTemperature();
	public static float biomeTemperature = getBiomeTemperature();
	public static float prevAlpha = getAlpha();
	public static float alpha = getAlpha();
	public static void tick() {
		prevViewDistance = viewDistance;
		viewDistance = (prevViewDistance + getViewDistance()) * 0.5F;
		prevFov = fov;
		fov = (prevFov + getFov()) * 0.5F;
		if (getTime() < 0.01F) {
			prevTime = getTime();
			time = getTime();
		} else {
			prevTime = time;
			time = (prevTime + getTime()) * 0.5F;
		}
		prevEyePosition = eyePosition;
		float[] currentEyePosition = getEyePosition();
		eyePosition = new float[]{((prevEyePosition[0] + currentEyePosition[0]) * 0.5F), ((prevEyePosition[1] + currentEyePosition[1]) * 0.5F), ((prevEyePosition[2] + currentEyePosition[2]) * 0.5F)};
		prevPosition = position;
		float[] currentPosition = getPosition();
		position = new float[]{((prevPosition[0] + currentPosition[0]) * 0.5F), ((prevPosition[1] + currentPosition[1]) * 0.5F), ((prevPosition[2] + currentPosition[2]) * 0.5F)};
		prevPitch = pitch;
		pitch = (prevPitch + getPitch()) * 0.5F;
		prevYaw = yaw;
		yaw = (prevYaw + getYaw()) * 0.5F;
		prevCurrentHealth = currentHealth;
		currentHealth = (prevCurrentHealth + getCurrentHealth()) * 0.5F;
		prevMaxHealth = maxHealth;
		maxHealth = (prevMaxHealth + getMaxHealth()) * 0.5F;
		prevCurrentAbsorption = currentAbsorption;
		currentAbsorption = (prevCurrentAbsorption + getCurrentAbsorption()) * 0.5F;
		prevMaxAbsorption = maxAbsorption;
		maxAbsorption = (prevMaxAbsorption + getMaxAbsorption()) * 0.5F;
		prevCurrentHurtTime = currentHurtTime;
		currentHurtTime = (prevCurrentHurtTime + getCurrentHurtTime()) * 0.5F;
		prevMaxHurtTime = maxHurtTime;
		maxHurtTime = (prevMaxHurtTime + getMaxHurtTime()) * 0.5F;
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
		prevIsInvisible = isInvisible;
		isInvisible = (prevIsInvisible + getIsInvisible()) * 0.5F;
		prevIsWithered = isWithered;
		isWithered = (prevIsWithered + getHasEffect(StatusEffects.WITHER)) * 0.5F;
		prevIsPoisoned = isPoisoned;
		isPoisoned = (prevIsPoisoned + getHasEffect(StatusEffects.POISON)) * 0.5F;
		prevIsBurning = isBurning;
		isBurning = (prevIsBurning + getIsBurning()) * 0.5F;
		prevIsOnGround = isOnGround;
		isOnGround = (prevIsOnGround + getIsOnGround()) * 0.5F;
		prevIsOnLadder = isOnLadder;
		isOnLadder = (prevIsOnLadder + getIsOnLadder()) * 0.5F;
		prevIsRiding = isRiding;
		isRiding = (prevIsRiding + getIsRiding()) * 0.5F;
		prevHasPassengers = hasPassengers;
		hasPassengers = (prevHasPassengers + getHasPassengers()) * 0.5F;
		prevBiomeTemperature = biomeTemperature;
		biomeTemperature = (prevBiomeTemperature + getBiomeTemperature()) * 0.5F;
		prevAlpha = alpha;
		alpha = (prevAlpha + getAlpha()) * 0.5F;
	}
	public static void init() {
		ShaderRenderEvents.BeforeRender.register("lu", "viewDistanceSmooth", () -> UniformHelper.getSmooth(prevViewDistance, viewDistance));
		ShaderRenderEvents.BeforeRender.register("lu", "fovSmooth", () -> UniformHelper.getSmooth(prevFov, fov));
		ShaderRenderEvents.BeforeRender.register("lu", "timeSmooth", SmoothUniforms::getSmoothTime);
		ShaderRenderEvents.BeforeRender.register("lu", "eyePositionSmooth", () -> UniformHelper.getSmooth(prevEyePosition, eyePosition));
		ShaderRenderEvents.BeforeRender.register("lu", "positionSmooth", () -> UniformHelper.getSmooth(prevPosition, position));
		ShaderRenderEvents.BeforeRender.register("lu", "pitchSmooth", () -> UniformHelper.getSmooth(prevPitch, pitch));
		ShaderRenderEvents.BeforeRender.register("lu", "yawSmooth", () -> UniformHelper.getSmooth(prevYaw, yaw));
		ShaderRenderEvents.BeforeRender.register("lu", "currentHealthSmooth", () -> UniformHelper.getSmooth(prevCurrentHealth, currentHealth));
		ShaderRenderEvents.BeforeRender.register("lu", "maxHealthSmooth", () -> UniformHelper.getSmooth(prevMaxHealth, maxHealth));
		ShaderRenderEvents.BeforeRender.register("lu", "currentAbsorptionSmooth", () -> UniformHelper.getSmooth(prevCurrentAbsorption, currentAbsorption));
		ShaderRenderEvents.BeforeRender.register("lu", "maxAbsorptionSmooth", () -> UniformHelper.getSmooth(prevMaxAbsorption, maxAbsorption));
		ShaderRenderEvents.BeforeRender.register("lu", "currentHurtTimeSmooth", () -> UniformHelper.getSmooth(prevCurrentHurtTime, currentHurtTime));
		ShaderRenderEvents.BeforeRender.register("lu", "maxHurtTimeSmooth", () -> UniformHelper.getSmooth(prevMaxHurtTime, maxHurtTime));
		ShaderRenderEvents.BeforeRender.register("lu", "currentAirSmooth", () -> UniformHelper.getSmooth(prevCurrentAir, currentAir));
		ShaderRenderEvents.BeforeRender.register("lu", "maxAirSmooth", () -> UniformHelper.getSmooth(prevMaxAir, maxAir));
		ShaderRenderEvents.BeforeRender.register("lu", "isSprintingSmooth", () -> UniformHelper.getSmooth(prevIsSprinting, isSprinting));
		ShaderRenderEvents.BeforeRender.register("lu", "isSwimmingSmooth", () -> UniformHelper.getSmooth(prevIsSwimming, isSwimming));
		ShaderRenderEvents.BeforeRender.register("lu", "isSneakingSmooth", () -> UniformHelper.getSmooth(prevIsSneaking, isSneaking));
		ShaderRenderEvents.BeforeRender.register("lu", "isCrawlingSmooth", () -> UniformHelper.getSmooth(prevIsCrawling, isCrawling));
		ShaderRenderEvents.BeforeRender.register("lu", "isInvisibleSmooth", () -> UniformHelper.getSmooth(prevIsInvisible, isInvisible));
		ShaderRenderEvents.BeforeRender.register("lu", "isWitheredSmooth", () -> UniformHelper.getSmooth(prevIsWithered, isWithered));
		ShaderRenderEvents.BeforeRender.register("lu", "isPoisonedSmooth", () -> UniformHelper.getSmooth(prevIsPoisoned, isPoisoned));
		ShaderRenderEvents.BeforeRender.register("lu", "isBurningSmooth", () -> UniformHelper.getSmooth(prevIsBurning, isBurning));
		ShaderRenderEvents.BeforeRender.register("lu", "isOnGroundSmooth", () -> UniformHelper.getSmooth(prevIsOnGround, isOnGround));
		ShaderRenderEvents.BeforeRender.register("lu", "isOnLadderSmooth", () -> UniformHelper.getSmooth(prevIsOnLadder, isOnLadder));
		ShaderRenderEvents.BeforeRender.register("lu", "isRidingSmooth", () -> UniformHelper.getSmooth(prevIsRiding, isRiding));
		ShaderRenderEvents.BeforeRender.register("lu", "hasPassengersSmooth", () -> UniformHelper.getSmooth(prevHasPassengers, hasPassengers));
		ShaderRenderEvents.BeforeRender.register("lu", "biomeTemperatureSmooth", () -> UniformHelper.getSmooth(prevBiomeTemperature, biomeTemperature));
		ShaderRenderEvents.BeforeRender.register("lu", "alphaSmooth", () -> UniformHelper.getSmooth(prevAlpha, alpha));
	}
	public static float getSmoothTime() {
		if (getTime() <= 1.0F) return getTime();
		return UniformHelper.getSmooth(prevTime, time);
	}
}