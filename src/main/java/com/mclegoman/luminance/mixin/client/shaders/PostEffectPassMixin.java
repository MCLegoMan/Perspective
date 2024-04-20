/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.mixin.client.shaders;

import com.mclegoman.luminance.client.data.ClientData;
import com.mclegoman.luminance.client.shaders.Uniforms;
import com.mclegoman.luminance.client.translation.Translation;
import com.mclegoman.luminance.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.PostEffectPass;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = PostEffectPass.class)
public abstract class PostEffectPassMixin {
	@Shadow @Final private JsonEffectShaderProgram program;
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/JsonEffectShaderProgram;enable()V"))
	private void perspective$updateUniforms(float time, CallbackInfo ci) {
		try {
			// TODO: Add variants of uniforms that lerp using tickDelta for a smoother effect.
			Uniforms.setUniform(this.program, "viewDistance", ClientData.minecraft.options.getViewDistance().getValue());

			Uniforms.setUniform(this.program, "eyePosition", ClientData.minecraft.cameraEntity != null ? ClientData.minecraft.cameraEntity.getEyePos().toVector3f() : new Vector3f(0.0F, 0.0F, 0.0F));
			Uniforms.setUniform(this.program, "pitch", ClientData.minecraft.player != null ? ClientData.minecraft.player.getPitch(ClientData.minecraft.getTickDelta()) % 360.0F : 0.0F);
			Uniforms.setUniform(this.program, "yaw", ClientData.minecraft.player != null ? ClientData.minecraft.player.getYaw(ClientData.minecraft.getTickDelta()) % 360.0F : 0.0F);

			Uniforms.setUniform(this.program, "currentHealth", ClientData.minecraft.player != null ? ClientData.minecraft.player.getHealth() : 20.0F);
			Uniforms.setUniform(this.program, "maxHealth", ClientData.minecraft.player != null ? ClientData.minecraft.player.getMaxHealth() : 20.0F);

			Uniforms.setUniform(this.program, "currentAir", ClientData.minecraft.player != null ? ClientData.minecraft.player.getAir() : 10.0F);
			Uniforms.setUniform(this.program, "maxAir", ClientData.minecraft.player != null ? ClientData.minecraft.player.getMaxAir() : 10.0F);

			Uniforms.setUniform(this.program, "isSprinting", ClientData.minecraft.player != null ? (ClientData.minecraft.player.isSprinting() ? 1.0F : 0.0F) : 0.0F);
			Uniforms.setUniform(this.program, "isSwimming", ClientData.minecraft.player != null ? (ClientData.minecraft.player.isSwimming() ? 1.0F : 0.0F) : 0.0F);
			Uniforms.setUniform(this.program, "isSneaking", ClientData.minecraft.player != null ? (ClientData.minecraft.player.isSneaking() ? 1.0F : 0.0F) : 0.0F);
			Uniforms.setUniform(this.program, "isCrawling", ClientData.minecraft.player != null ? (ClientData.minecraft.player.isCrawling() ? 1.0F : 0.0F) : 0.0F);

			Uniforms.setUniform(this.program, "alpha", Uniforms.getAlpha());
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to set shader uniforms: {}", error));
		}
	}
}