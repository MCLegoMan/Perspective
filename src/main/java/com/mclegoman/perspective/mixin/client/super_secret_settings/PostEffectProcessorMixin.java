/*
    Perspective
    Contributor(s): Nettakrim
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.super_secret_settings;

import com.google.gson.JsonElement;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.shaders.Shader;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 10000, value = PostEffectProcessor.class)
public class PostEffectProcessorMixin {
	@Inject(at = @At(value = "INVOKE", target = "Ljava/lang/String;substring(II)Ljava/lang/String;"), method = "parsePass")
	public void perspective$detectDepth(TextureManager textureManager, JsonElement jsonPass, CallbackInfo ci) {
		if (Shader.DEPTH_FIX) {
			Shader.USE_DEPTH = true;
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "render")
	public void perspective$fixDepth(float tickDelta, CallbackInfo ci) {
		if (Shader.USE_DEPTH) {
			ClientData.CLIENT.getFramebuffer().copyDepthFrom(Shader.DEPTH_FRAME_BUFFER);
		}
	}
}