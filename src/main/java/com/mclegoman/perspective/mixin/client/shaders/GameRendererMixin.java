/*
    Perspective
    Contributor(s): MCLegoMan, Nettakrim
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.shaders;

import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.ui.UIBackground;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = GameRenderer.class)
public abstract class GameRendererMixin {
	@Inject(method = "onResized", at = @At(value = "TAIL"))
	private void perspective$onResized(int width, int height, CallbackInfo ci) {
		if (Shader.entityPostProcessor != null) {
			for (PostEffectProcessor postProcessor : Shader.entityPostProcessor) {
				postProcessor.setupDimensions(width, height);
			}
		}
	}
}