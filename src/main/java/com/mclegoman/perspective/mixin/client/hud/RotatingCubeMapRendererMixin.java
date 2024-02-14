/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.hud;

import com.mclegoman.perspective.client.config.ConfigHelper;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = RotatingCubeMapRenderer.class)
public abstract class RotatingCubeMapRendererMixin {
	@Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
	private void perspective$render(float delta, float alpha, CallbackInfo ci) {
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "dirt_title_screen")) {
			ci.cancel();
		}
	}
}