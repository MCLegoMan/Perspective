/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.mixin.client.shaders;

import com.mclegoman.luminance.client.shaders.ShaderRenderEvents;
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
	@Shadow @Final private JsonEffectShaderProgram program;
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/JsonEffectShaderProgram;enable()V"))
	private void perspective$updateUniforms(float time, CallbackInfo ci) {
		ShaderRenderEvents.BeforeRender.registry.forEach((runnable -> runnable.run(program)));
	}
}