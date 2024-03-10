/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.shaders;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
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
		// Uniforms with the "lu_" prefix will be moved to a separate mod in a future update to allow other shader mods to use them too without having perspective installed.
		getUniform("viewDistance").set(ClientData.CLIENT.options.getViewDistance().getValue());

		getUniform("eyePosition").set(ClientData.CLIENT.cameraEntity != null ? ClientData.CLIENT.cameraEntity.getEyePos().toVector3f() : new Vector3f(0, 0, 0));
		getUniform("pitch").set(ClientData.CLIENT.player != null ? ClientData.CLIENT.player.getPitch(ClientData.CLIENT.getTickDelta()) % 360 : 0);
		getUniform("yaw").set(ClientData.CLIENT.player != null ? ClientData.CLIENT.player.getYaw(ClientData.CLIENT.getTickDelta()) % 360 : 0);

		getUniform("currentHealth").set(ClientData.CLIENT.player != null ? ClientData.CLIENT.player.getHealth() : 20);
		getUniform("maxHealth").set(ClientData.CLIENT.player != null ? ClientData.CLIENT.player.getMaxHealth() : 20);

		getUniform("currentAir").set(ClientData.CLIENT.player != null ? ClientData.CLIENT.player.getAir() : 10);
		getUniform("maxAir").set(ClientData.CLIENT.player != null ? ClientData.CLIENT.player.getMaxAir() : 10);

		// Perspective Uniforms
		getUniform(Data.VERSION.getID(), "zoomMultiplier").set((float) Zoom.getMultiplier());
	}
	@Unique
	private Uniform getUniform(String uniformName) {
		// Prefixed with "lu_" to stop
		return getUniform("lu", uniformName);
	}
	@Unique
	private Uniform getUniform(String prefix, String uniformName) {
		return this.program.getUniformByNameOrDummy(prefix + "_" + uniformName);
	}
}