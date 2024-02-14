/*
    Perspective
    Contributor(s): MCLegoMan, Nettakrim
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.super_secret_settings;

import com.mclegoman.perspective.client.shaders.ShaderUniforms;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.PostEffectPass;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = PostEffectPass.class)
public abstract class PostEffectPassMixin {
	// TO BE MOVED TO SEPARATE LIBRARY MOD.
	@Shadow @Final private JsonEffectShaderProgram program;
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/JsonEffectShaderProgram;enable()V"))
	private void perspective$updateUniforms(float time, CallbackInfo ci) {
		try {
			this.program.getUniformByNameOrDummy("perspectiveFOV").set(ShaderUniforms.getFOV());
			this.program.getUniformByNameOrDummy("perspectiveRenderDistance").set(ShaderUniforms.getRenderDistance());
			this.program.getUniformByNameOrDummy("perspectiveLight").set(ShaderUniforms.getLight());
			this.program.getUniformByNameOrDummy("perspectiveSmoothLight").set(ShaderUniforms.getSmoothLight());
			this.program.getUniformByNameOrDummy("perspectiveSmoothFogColor").set(ShaderUniforms.getSmoothFogColor().toVector3f());
			this.program.getUniformByNameOrDummy("perspectiveZoomMultiplier").set(ShaderUniforms.getZoomMultiplier());
		} catch (Exception ignored) {}
	}
}