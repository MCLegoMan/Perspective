/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.ui;

import com.mclegoman.perspective.client.ui.UIBackground;
import com.mclegoman.perspective.client.ui.UIBackgroundData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = TitleScreen.class)
public abstract class TitleScreenMixin {
	@Inject(method = "renderPanoramaBackground", at = @At(value = "HEAD"), cancellable = true)
	private void perspective$renderTitleScreen(DrawContext context, float delta, CallbackInfo ci) {
		UIBackgroundData data = UIBackground.getCurrentUIBackground();
		if (data.getRenderTitleScreen() != null) data.getRenderTitleScreen().run(context);
		if (!data.getRenderTitleScreenPanorama()) ci.cancel();
	}
}