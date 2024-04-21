/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.shaders;

import com.mclegoman.luminance.client.data.ClientData;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

public class SmoothUniforms extends Uniforms {
	public static float prevViewDistance = getViewDistance();
	public static float viewDistance = getViewDistance();
	public static float prevFov = getFov();
	public static float fov = getFov();
	public static float prevFps = getFps();
	public static float fps = getFps();
	public static float prevTime = getTime();
	public static float time = getTime();
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
	public static void init() {
	}
	public static void tick() {
		prevViewDistance = viewDistance;
		viewDistance = (prevViewDistance + getViewDistance()) * 0.5F;
		prevFov = fov;
		fov = (prevFov + getFov()) * 0.5F;
		prevFps = fps;
		fps = (prevFps + getFps()) * 0.5F;
		prevTime = time;
		time = (prevTime + getTime()) * 0.5F;
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
	public static void update(JsonEffectShaderProgram program) {
		setUniform(program, "viewDistanceSmooth", getSmooth(prevViewDistance, viewDistance));
		setUniform(program, "fovSmooth", getSmooth(prevFov, fov));
		setUniform(program, "fpsSmooth", getSmooth(prevFps, fps));
		setUniform(program, "timeSmooth", getSmooth(prevTime, time));
		setUniform(program, "eyePositionSmooth", getSmooth(prevEyePosition, eyePosition));
		setUniform(program, "pitchSmooth", getSmooth(prevPitch, pitch));
		setUniform(program, "yawSmooth", getSmooth(prevYaw, yaw));
		setUniform(program, "currentHealthSmooth", getSmooth(prevCurrentHealth, currentHealth));
		setUniform(program, "maxHealthSmooth", getSmooth(prevMaxHealth, maxHealth));
		setUniform(program, "currentAbsorptionSmooth", getSmooth(prevCurrentAbsorption, currentAbsorption));
		setUniform(program, "maxAbsorptionSmooth", getSmooth(prevMaxAbsorption, maxAbsorption));
		setUniform(program, "currentHurtTimeSmooth", getSmooth(prevCurrentHurtTime, currentHurtTime));
		setUniform(program, "maxHurtTimeSmooth", getSmooth(prevMaxHurtTime, maxHurtTime));
		setUniform(program, "currentAirSmooth", getSmooth(prevCurrentAir, currentAir));
		setUniform(program, "maxAirSmooth", getSmooth(prevMaxAir, maxAir));
		setUniform(program, "isSprintingSmooth", getSmooth(prevIsSprinting, isSprinting));
		setUniform(program, "isSwimmingSmooth", getSmooth(prevIsSwimming, isSwimming));
		setUniform(program, "isSneakingSmooth", getSmooth(prevIsSneaking, isSneaking));
		setUniform(program, "isCrawlingSmooth", getSmooth(prevIsCrawling, isCrawling));
		setUniform(program, "isInvisibleSmooth", getSmooth(prevIsInvisible, isInvisible));
		setUniform(program, "isWitheredSmooth", getSmooth(prevIsWithered, isWithered));
		setUniform(program, "isPoisonedSmooth", getSmooth(prevIsPoisoned, isPoisoned));
		setUniform(program, "isBurningSmooth", getSmooth(prevIsBurning, isBurning));
		setUniform(program, "isOnGroundSmooth", getSmooth(prevIsOnGround, isOnGround));
		setUniform(program, "isOnLadderSmooth", getSmooth(prevIsOnLadder, isOnLadder));
		setUniform(program, "isRidingSmooth", getSmooth(prevIsRiding, isRiding));
		setUniform(program, "hasPassengersSmooth", getSmooth(prevHasPassengers, hasPassengers));
		setUniform(program, "biomeTemperatureSmooth", getSmooth(prevBiomeTemperature, biomeTemperature));
		setUniform(program, "alphaSmooth", getSmooth(prevAlpha, alpha));
	}
	public static float getSmooth(float prev, float current) {
		float tickDelta = ClientData.minecraft.getTickDelta();
		return MathHelper.lerp(tickDelta, prev, current);
	}
	public static Vector3f getSmooth(Vector3f prev, Vector3f current) {
		return new Vector3f(getSmooth(prev.x, current.x), getSmooth(prev.y, current.y), getSmooth(prev.z, current.z));
	}
}