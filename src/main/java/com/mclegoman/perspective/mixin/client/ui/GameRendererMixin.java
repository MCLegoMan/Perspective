/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.ui;

import com.mclegoman.perspective.client.ui.UIBackground;
import com.mclegoman.perspective.client.ui.UIBackgroundData;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = GameRenderer.class)
public abstract class GameRendererMixin {
	@Inject(method = "renderBlur", at = @At("HEAD"), cancellable = true)
	private void perspective$applyBlur(float tickDelta, CallbackInfo ci) {
		UIBackgroundData data = UIBackground.getCurrentUIBackground();
		if (data.getRenderShader()) {
			if (data.getShaderId() != null) {
				ci.cancel();
				UIBackground.renderShader(tickDelta, data);
			}
		} else {
			ci.cancel();
		}
	}
	@Inject(method = "onResized", at = @At(value = "TAIL"))
	private void perspective$onResized(int width, int height, CallbackInfo ci) {
		if (UIBackground.postProcessor != null) {
			UIBackground.postProcessor.setupDimensions(width, height);
		}
	}
}