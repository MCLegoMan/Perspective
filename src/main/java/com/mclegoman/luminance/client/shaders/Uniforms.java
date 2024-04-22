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
		SmoothUniforms.init();
		ShaderRenderEvents.BeforeRender.register("lu", "viewDistance", Uniforms::getViewDistance);
		ShaderRenderEvents.BeforeRender.register("lu", "fov", Uniforms::getFov);
		ShaderRenderEvents.BeforeRender.register("lu", "fps", Uniforms::getFps);
		ShaderRenderEvents.BeforeRender.register("lu", "time", Uniforms::getTime);
		ShaderRenderEvents.BeforeRender.register("lu", "eyePosition", Uniforms::getEyePosition);
		ShaderRenderEvents.BeforeRender.register("lu", "position", Uniforms::getPosition);
		ShaderRenderEvents.BeforeRender.register("lu", "pitch", Uniforms::getPitch);
		ShaderRenderEvents.BeforeRender.register("lu", "yaw", Uniforms::getYaw);
		ShaderRenderEvents.BeforeRender.register("lu", "currentHealth", Uniforms::getCurrentHealth);
		ShaderRenderEvents.BeforeRender.register("lu", "maxHealth", Uniforms::getMaxHealth);
		ShaderRenderEvents.BeforeRender.register("lu", "currentAbsorption", Uniforms::getCurrentAbsorption);
		ShaderRenderEvents.BeforeRender.register("lu", "maxAbsorption", Uniforms::getMaxAbsorption);
		ShaderRenderEvents.BeforeRender.register("lu", "currentHurtTime", Uniforms::getCurrentHurtTime);
		ShaderRenderEvents.BeforeRender.register("lu", "maxHurtTime", Uniforms::getMaxHurtTime);
		ShaderRenderEvents.BeforeRender.register("lu", "currentAir", Uniforms::getCurrentAir);
		ShaderRenderEvents.BeforeRender.register("lu", "maxAir", Uniforms::getMaxAir);
		ShaderRenderEvents.BeforeRender.register("lu", "isSprinting", Uniforms::getIsSprinting);
		ShaderRenderEvents.BeforeRender.register("lu", "isSwimming", Uniforms::getIsSwimming);
		ShaderRenderEvents.BeforeRender.register("lu", "isSneaking", Uniforms::getIsSneaking);
		ShaderRenderEvents.BeforeRender.register("lu", "isCrawling", Uniforms::getIsCrawling);
		ShaderRenderEvents.BeforeRender.register("lu", "isInvisible", Uniforms::getIsInvisible);
		ShaderRenderEvents.BeforeRender.register("lu", "isWithered", () -> Uniforms.getHasEffect(StatusEffects.WITHER));
		ShaderRenderEvents.BeforeRender.register("lu", "isPoisoned", () -> Uniforms.getHasEffect(StatusEffects.POISON));
		ShaderRenderEvents.BeforeRender.register("lu", "isBurning", Uniforms::getIsBurning);
		ShaderRenderEvents.BeforeRender.register("lu", "isOnGround", Uniforms::getIsOnGround);
		ShaderRenderEvents.BeforeRender.register("lu", "isOnLadder", Uniforms::getIsOnLadder);
		ShaderRenderEvents.BeforeRender.register("lu", "isRiding", Uniforms::getIsRiding);
		ShaderRenderEvents.BeforeRender.register("lu", "hasPassengers", Uniforms::getHasPassengers);
		ShaderRenderEvents.BeforeRender.register("lu", "biomeTemperature", Uniforms::getBiomeTemperature);
		ShaderRenderEvents.BeforeRender.register("lu", "alpha", Uniforms::getAlpha);
	}
	public static float getViewDistance() {
		return ClientData.minecraft.options != null ? ClientData.minecraft.options.getViewDistance().getValue() : 12.0F;
	}
	public static float getFov() {
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
}
