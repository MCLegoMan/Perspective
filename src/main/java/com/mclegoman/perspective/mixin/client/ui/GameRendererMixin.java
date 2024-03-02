/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.ui;

import com.mclegoman.perspective.client.ui.UIBackground;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = GameRenderer.class)
public abstract class GameRendererMixin {
	@Inject(method = "renderBlur", at = @At("HEAD"), cancellable = true)
	private void perspective$applyBlur(float tickDelta, CallbackInfo ci) {
		if (!UIBackground.getUIBackgroundType().equalsIgnoreCase("default")) ci.cancel();
		if (UIBackground.getUIBackgroundType().equalsIgnoreCase("gaussian")) UIBackground.Gaussian.render(tickDelta);
	}
}