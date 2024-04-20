/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.shaders;

import com.mclegoman.luminance.client.shaders.Uniforms;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.Uniform;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = PostEffectPass.class)
public abstract class PostEffectPassMixin {
	@Shadow @Final private JsonEffectShaderProgram program;
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/JsonEffectShaderProgram;enable()V"))
	private void perspective$updateUniforms(float time, CallbackInfo ci) {
		try {
			// Uniforms with the "lu_" prefix will be moved to a separate mod in a future update to allow other shader mods to use them too without having perspective installed.
			// TODO: Add variants of uniforms that lerp using tickDelta for a smoother effect.
			setUniform("viewDistance", ClientData.minecraft.options.getViewDistance().getValue());

			setUniform("eyePosition", ClientData.minecraft.cameraEntity != null ? ClientData.minecraft.cameraEntity.getEyePos().toVector3f() : new Vector3f(0.0F, 0.0F, 0.0F));
			setUniform("pitch", ClientData.minecraft.player != null ? ClientData.minecraft.player.getPitch(ClientData.minecraft.getTickDelta()) % 360.0F : 0.0F);
			setUniform("yaw", ClientData.minecraft.player != null ? ClientData.minecraft.player.getYaw(ClientData.minecraft.getTickDelta()) % 360.0F : 0.0F);

			setUniform("currentHealth", ClientData.minecraft.player != null ? ClientData.minecraft.player.getHealth() : 20.0F);
			setUniform("maxHealth", ClientData.minecraft.player != null ? ClientData.minecraft.player.getMaxHealth() : 20.0F);

			setUniform("currentAir", ClientData.minecraft.player != null ? ClientData.minecraft.player.getAir() : 10.0F);
			setUniform("maxAir", ClientData.minecraft.player != null ? ClientData.minecraft.player.getMaxAir() : 10.0F);

			setUniform("isSprinting", ClientData.minecraft.player != null ? (ClientData.minecraft.player.isSprinting() ? 1.0F : 0.0F) : 0.0F);
			setUniform("isSwimming", ClientData.minecraft.player != null ? (ClientData.minecraft.player.isSwimming() ? 1.0F : 0.0F) : 0.0F);
			setUniform("isSneaking", ClientData.minecraft.player != null ? (ClientData.minecraft.player.isSneaking() ? 1.0F : 0.0F) : 0.0F);
			setUniform("isCrawling", ClientData.minecraft.player != null ? (ClientData.minecraft.player.isCrawling() ? 1.0F : 0.0F) : 0.0F);

			// This will be a config option in Luminance.
			setUniform("alpha", Uniforms.getAlpha());

			// Perspective Uniforms
			setUniform(getUniformName(Data.version.getID(), "zoomMultiplier"), (float) Zoom.getMultiplier());
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to set shader uniforms: {}", error));
		}
	}
	@Unique
	private Uniform getUniform(String uniformName) {
		// Prefixed with "lu_" to stop
		return getUniform("lu", uniformName);
	}
	@Unique
	private Uniform getUniform(String prefix, String uniformName) {
		return this.program.getUniformByNameOrDummy(getUniformName(prefix, uniformName));
	}
	@Unique
	private String getUniformName(String prefix, String uniformName) {
		return prefix + "_" + uniformName;
	}
	@Unique
	private void setUniform(String uniformName, float... value) {
		getUniform(uniformName).set(value);
	}
	@Unique
	private void setUniform(String uniformName, Vector3f value) {
		getUniform(uniformName).set(value);
	}
}