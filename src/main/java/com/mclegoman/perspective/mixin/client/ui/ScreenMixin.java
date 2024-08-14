/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.ui;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.ui.UIBackground;
import com.mclegoman.perspective.client.ui.UIBackgroundData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = Screen.class)
public abstract class ScreenMixin {
	@Inject(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;applyBlur(F)V"))
	private void perspective$renderBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		UIBackgroundData data = UIBackground.getCurrentUIBackground();
		if (ClientData.minecraft.world != null && data.getRenderWorld() != null) data.getRenderWorld().run(context);
	}
	@Inject(method = "renderPanoramaBackground", at = @At(value = "HEAD"), cancellable = true)
	private void perspective$renderPanoramaBackground(DrawContext context, float delta, CallbackInfo ci) {
		UIBackgroundData data = UIBackground.getCurrentUIBackground();
		if (data.getRenderMenu() != null) data.getRenderMenu().run(context);
		if (!data.getRenderPanorama()) ci.cancel();
	}
}