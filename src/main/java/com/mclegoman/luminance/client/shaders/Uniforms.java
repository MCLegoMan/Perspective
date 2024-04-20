/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.shaders;

import com.mclegoman.luminance.client.data.ClientData;
import com.mclegoman.luminance.config.ConfigHelper;
import com.mclegoman.luminance.client.keybindings.Keybindings;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.Uniform;
import org.joml.Vector3f;

public class Uniforms {
	private static int alpha = 100;
	public static void init() {
		alpha = (int)ConfigHelper.getConfig("alpha_level");
		prevAlpha = alpha;
		SmoothUniforms.init();
	}
	public static void tick() {
		if (!updatingAlpha() && updatingAlpha) {
			updatingAlpha = false;
			if (alpha != prevAlpha) ConfigHelper.setConfig("alpha_level", alpha);
		}
		SmoothUniforms.tick();
	}
	public static void update(JsonEffectShaderProgram program) {
		setUniform(program, "viewDistance", getViewDistance());

		setUniform(program, "eyePosition", getEyePosition());
		setUniform(program, "pitch", getPitch());
		setUniform(program, "yaw", getYaw());

		setUniform(program, "currentHealth", getCurrentHealth());
		setUniform(program, "maxHealth", getMaxHealth());

		setUniform(program, "currentAir", getCurrentAir());
		setUniform(program, "maxAir", getMaxAir());

		setUniform(program, "isSprinting", getIsSprinting());
		setUniform(program, "isSwimming", getIsSwimming());
		setUniform(program, "isSneaking", getIsSneaking());
		setUniform(program, "isCrawling", getIsCrawling());

		setUniform(program, "alpha", getAlpha());

		SmoothUniforms.update(program);
	}
	public static float getViewDistance() {
		return ClientData.minecraft.options != null ? ClientData.minecraft.options.getViewDistance().getValue() : 12;
	}
	public static Vector3f getEyePosition() {
		return ClientData.minecraft.cameraEntity != null ? ClientData.minecraft.cameraEntity.getEyePos().toVector3f() : new Vector3f(0.0F, 0.0F, 0.0F);
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
	public static Uniform getUniform(JsonEffectShaderProgram program, String uniformName) {
		return getUniform(program, "lu", uniformName);
	}
	public static Uniform getUniform(JsonEffectShaderProgram program, String prefix, String uniformName) {
		return program.getUniformByNameOrDummy(getUniformName(prefix, uniformName));
	}
	public static String getUniformName(String prefix, String uniformName) {
		return prefix + "_" + uniformName;
	}
	public static void setUniform(JsonEffectShaderProgram program, String uniformName, float... value) {
		getUniform(program, uniformName).set(value);
	}
	public static void setUniform(JsonEffectShaderProgram program, String uniformName, Vector3f value) {
		getUniform(program, uniformName).set(value);
	}
}
