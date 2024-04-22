/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.shaders;

import com.mclegoman.luminance.client.data.ClientData;
import com.mclegoman.luminance.client.keybindings.Keybindings;
import com.mclegoman.luminance.config.ConfigHelper;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;

public class Uniforms {
	private static int alpha = 100;
	public static void tick() {
		UniformHelper.updateTime();
		if (!updatingAlpha() && updatingAlpha) {
			updatingAlpha = false;
			if (alpha != prevAlpha) ConfigHelper.setConfig("alpha_level", alpha);
		}
		SmoothUniforms.tick();
	}
	public static void init() {
		alpha = (int)ConfigHelper.getConfig("alpha_level");
		prevAlpha = alpha;
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "viewDistance", Uniforms::getViewDistance);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "fov", Uniforms::getFov);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "fps", Uniforms::getFps);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "time", Uniforms::getTime);
		ShaderRenderEvents.ShaderUniform.registerFloats("lu", "eyePosition", Uniforms::getEyePosition);
		ShaderRenderEvents.ShaderUniform.registerFloats("lu", "position", Uniforms::getPosition);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "pitch", Uniforms::getPitch);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "yaw", Uniforms::getYaw);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "currentHealth", Uniforms::getCurrentHealth);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "maxHealth", Uniforms::getMaxHealth);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "currentAbsorption", Uniforms::getCurrentAbsorption);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "maxAbsorption", Uniforms::getMaxAbsorption);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "currentHurtTime", Uniforms::getCurrentHurtTime);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "maxHurtTime", Uniforms::getMaxHurtTime);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "currentAir", Uniforms::getCurrentAir);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "maxAir", Uniforms::getMaxAir);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "isSprinting", Uniforms::getIsSprinting);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "isSwimming", Uniforms::getIsSwimming);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "isSneaking", Uniforms::getIsSneaking);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "isCrawling", Uniforms::getIsCrawling);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "isInvisible", Uniforms::getIsInvisible);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "isWithered", () -> Uniforms.getHasEffect(StatusEffects.WITHER));
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "isPoisoned", () -> Uniforms.getHasEffect(StatusEffects.POISON));
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "isBurning", Uniforms::getIsBurning);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "isOnGround", Uniforms::getIsOnGround);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "isOnLadder", Uniforms::getIsOnLadder);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "isRiding", Uniforms::getIsRiding);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "hasPassengers", Uniforms::getHasPassengers);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "biomeTemperature", Uniforms::getBiomeTemperature);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "alpha", Uniforms::getAlpha);
		ShaderRenderEvents.ShaderUniform.registerFloat("lu", "perspective", Uniforms::getPerspective);
		SmoothUniforms.init();
		UniformHelper.init();
	}
	public static float getViewDistance() {
		return ClientData.minecraft.options != null ? ClientData.minecraft.options.getViewDistance().getValue() : 12.0F;
	}
	public static float getFov() {
		// This probably should be changed to the one in GameRenderer.
		return ClientData.minecraft.options != null ? ClientData.minecraft.options.getFov().getValue() : 70.0F;
	}
	public static float getFps() {
		return ClientData.minecraft.getCurrentFps();
	}
	public static float getTime() {
		return UniformHelper.time;
	}
	public static float[] getEyePosition() {
		return ClientData.minecraft.player != null ? new float[]{(float) ClientData.minecraft.player.getEyePos().x, (float) ClientData.minecraft.player.getEyePos().y, (float) ClientData.minecraft.player.getEyePos().z} : new float[]{0.0F, 66.0F, 0.0F};
	}
	public static float[] getPosition() {
		return ClientData.minecraft.player != null ? new float[]{(float) ClientData.minecraft.player.getPos().x, (float) ClientData.minecraft.player.getPos().y, (float) ClientData.minecraft.player.getPos().z} : new float[]{0.0F, 64.0F, 0.0F};
	}
	public static float getPitch() {
		return ClientData.minecraft.player != null ? ClientData.minecraft.player.getPitch(ClientData.minecraft.getTickDelta()) % 360.0F : 0.0F;
	}
	public static float getYaw() {
		return ClientData.minecraft.player != null ? ClientData.minecraft.player.getYaw(ClientData.minecraft.getTickDelta()) % 360.0F : 0.0F;
	}
	public static float getCurrentHealth() {
		return ClientData.minecraft.player != null ? ClientData.minecraft.player.getHealth() : 20.0F;
	}
	public static float getMaxHealth() {
		return ClientData.minecraft.player != null ? ClientData.minecraft.player.getMaxHealth() : 20.0F;
	}
	public static float getCurrentAbsorption() {
		return ClientData.minecraft.player != null ? ClientData.minecraft.player.getAbsorptionAmount() : 0.0F;
	}
	public static float getMaxAbsorption() {
		return ClientData.minecraft.player != null ? ClientData.minecraft.player.getMaxAbsorption() : 0.0F;
	}
	public static float getCurrentHurtTime() {
		return ClientData.minecraft.player != null ? ClientData.minecraft.player.hurtTime : 0.0F;
	}
	public static float getMaxHurtTime() {
		return ClientData.minecraft.player != null ? ClientData.minecraft.player.maxHurtTime : 10.0F;
	}
	public static float getCurrentAir() {
		return ClientData.minecraft.player != null ? ClientData.minecraft.player.getAir() : 10.0F;
	}
	public static float getMaxAir() {
		return ClientData.minecraft.player != null ? ClientData.minecraft.player.getMaxAir() : 10.0F;
	}
	public static float getIsSprinting() {
		return ClientData.minecraft.player != null ? (ClientData.minecraft.player.isSprinting() ? 1.0F : 0.0F) : 0.0F;
	}
	public static float getIsSwimming() {
		return ClientData.minecraft.player != null ? (ClientData.minecraft.player.isSwimming() ? 1.0F : 0.0F) : 0.0F;
	}
	public static float getIsSneaking() {
		return ClientData.minecraft.player != null ? (ClientData.minecraft.player.isSneaking() ? 1.0F : 0.0F) : 0.0F;
	}
	public static float getIsCrawling() {
		return ClientData.minecraft.player != null ? (ClientData.minecraft.player.isCrawling() ? 1.0F : 0.0F) : 0.0F;
	}
	public static float getIsInvisible() {
		return ClientData.minecraft.player != null ? (ClientData.minecraft.player.isInvisible() ? 1.0F : 0.0F) : 0.0F;
	}
	public static float getHasEffect(RegistryEntry<StatusEffect> statusEffect) {
		return ClientData.minecraft.player != null ? (ClientData.minecraft.player.hasStatusEffect(statusEffect) ? 1.0F : 0.0F) : 0.0F;
	}
	public static float getIsBurning() {
		return ClientData.minecraft.player != null ? (ClientData.minecraft.player.isOnFire() ? 1.0F : 0.0F) : 0.0F;
	}
	public static float getIsOnGround() {
		return ClientData.minecraft.player != null ? (ClientData.minecraft.player.isOnGround() ? 1.0F : 0.0F) : 1.0F;
	}
	public static float getIsOnLadder() {
		return ClientData.minecraft.player != null ? (ClientData.minecraft.player.isHoldingOntoLadder() ? 1.0F : 0.0F) : 1.0F;
	}
	public static float getIsRiding() {
		return ClientData.minecraft.player != null ? (ClientData.minecraft.player.isRiding() ? 1.0F : 0.0F) : 0.0F;
	}
	public static float getHasPassengers() {
		return ClientData.minecraft.player != null ? (ClientData.minecraft.player.hasPassengers() ? 1.0F : 0.0F) : 0.0F;
	}
	public static float getBiomeTemperature() {
		return ClientData.minecraft.world != null && ClientData.minecraft.player != null ? ClientData.minecraft.world.getBiome(ClientData.minecraft.player.getBlockPos()).value().getTemperature() : 1.0F;
	}
	public static float getAlpha() {
		return Math.max(0.0F, Math.min((alpha / 100.0F), 1.0F));
	}
	public static void setAlpha(int value) {
		alpha = Math.max(0, Math.min((value), 100));
	}
	public static void resetAlpha() {
		setAlpha(100);
	}
	public static void adjustAlpha(int amount) {
		setAlpha(alpha + amount);
	}
	public static boolean updatingAlpha = false;
	private static int prevAlpha = 100;
	public static boolean updatingAlpha() {
		boolean value = Keybindings.adjustAlpha.isPressed();
		if (value) {
			if (!updatingAlpha) {
				prevAlpha = alpha;
			}
			updatingAlpha = true;
		}
		return value;
	}
	public static float getPerspective() {
		if (ClientData.minecraft.options != null) {
			Perspective perspective = ClientData.minecraft.options.getPerspective();
			return perspective.equals(Perspective.THIRD_PERSON_FRONT) ? 3.0F : (perspective.equals(Perspective.THIRD_PERSON_BACK) ? 2.0F : (perspective.equals(Perspective.FIRST_PERSON) ? 1.0F : 0.0F));
		}
		return 1.0F;
	}
}
