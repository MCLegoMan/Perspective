/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.hud;

import com.mclegoman.perspective.client.hud.HUDHelper;
import com.mclegoman.perspective.client.hud.Overlays;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = InGameHud.class)
public abstract class InGameHudMixin {
	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void perspective$render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		if (HUDHelper.shouldHideHUD()) ci.cancel();
	}
	@Inject(at = @At("RETURN"), method = "render")
	private void perspective$renderOverlays(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		Overlays.renderOverlays(context);
	}
}