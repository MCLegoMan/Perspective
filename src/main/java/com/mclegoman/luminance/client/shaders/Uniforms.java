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
	}
	public static void tick() {
		if (!updatingAlpha() && updatingAlpha) {
			updatingAlpha = false;
			if (alpha != prevAlpha) ConfigHelper.setConfig("alpha_level", alpha);
		}
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
	public static void update(JsonEffectShaderProgram program) {
		// TODO: Add variants of uniforms that lerp using tickDelta for a smoother effect.
		Uniforms.setUniform(program, "viewDistance", ClientData.minecraft.options.getViewDistance().getValue());

		Uniforms.setUniform(program, "eyePosition", ClientData.minecraft.cameraEntity != null ? ClientData.minecraft.cameraEntity.getEyePos().toVector3f() : new Vector3f(0.0F, 0.0F, 0.0F));
		Uniforms.setUniform(program, "pitch", ClientData.minecraft.player != null ? ClientData.minecraft.player.getPitch(ClientData.minecraft.getTickDelta()) % 360.0F : 0.0F);
		Uniforms.setUniform(program, "yaw", ClientData.minecraft.player != null ? ClientData.minecraft.player.getYaw(ClientData.minecraft.getTickDelta()) % 360.0F : 0.0F);

		Uniforms.setUniform(program, "currentHealth", ClientData.minecraft.player != null ? ClientData.minecraft.player.getHealth() : 20.0F);
		Uniforms.setUniform(program, "maxHealth", ClientData.minecraft.player != null ? ClientData.minecraft.player.getMaxHealth() : 20.0F);

		Uniforms.setUniform(program, "currentAir", ClientData.minecraft.player != null ? ClientData.minecraft.player.getAir() : 10.0F);
		Uniforms.setUniform(program, "maxAir", ClientData.minecraft.player != null ? ClientData.minecraft.player.getMaxAir() : 10.0F);

		Uniforms.setUniform(program, "isSprinting", ClientData.minecraft.player != null ? (ClientData.minecraft.player.isSprinting() ? 1.0F : 0.0F) : 0.0F);
		Uniforms.setUniform(program, "isSwimming", ClientData.minecraft.player != null ? (ClientData.minecraft.player.isSwimming() ? 1.0F : 0.0F) : 0.0F);
		Uniforms.setUniform(program, "isSneaking", ClientData.minecraft.player != null ? (ClientData.minecraft.player.isSneaking() ? 1.0F : 0.0F) : 0.0F);
		Uniforms.setUniform(program, "isCrawling", ClientData.minecraft.player != null ? (ClientData.minecraft.player.isCrawling() ? 1.0F : 0.0F) : 0.0F);

		Uniforms.setUniform(program, "alpha", Uniforms.getAlpha());
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
