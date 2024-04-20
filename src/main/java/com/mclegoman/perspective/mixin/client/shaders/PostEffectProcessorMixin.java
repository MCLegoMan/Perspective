/*
    Perspective
    Contributor(s): Nettakrim, MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.shaders;

import com.google.gson.JsonElement;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.shaders.Shader;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = PostEffectProcessor.class)
public abstract class PostEffectProcessorMixin {
	@Inject(at = @At(value = "INVOKE", target = "Ljava/lang/String;substring(II)Ljava/lang/String;"), method = "parsePass")
	public void perspective$detectDepth(TextureManager textureManager, JsonElement jsonPass, CallbackInfo ci) {
		if (Shader.depthFix) {
			Shader.useDepth = true;
		}
		if (Shader.entityDepthFix) {
			Shader.entityUseDepth = true;
		}
	}
	@Inject(at = @At(value = "HEAD"), method = "render")
	public void perspective$fixDepth(float tickDelta, CallbackInfo ci) {
		if (Shader.useDepth || Shader.entityUseDepth) {
			ClientData.minecraft.getFramebuffer().copyDepthFrom(Shader.depthFramebuffer);
		}
	}
}