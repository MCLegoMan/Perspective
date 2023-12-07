/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.hud;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hud.DebugHUD;
import com.mclegoman.perspective.client.hud.HUD;
import com.mclegoman.perspective.client.overlays.HUDOverlays;
import com.mclegoman.perspective.client.translation.Translation;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 10000, value = InGameHud.class)
public abstract class InGameHudMixin {
	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void perspective$render(DrawContext context, float tickDelta, CallbackInfo ci) {
		if (HUD.shouldHideHUD()) ci.cancel();
		if (!ClientData.CLIENT.gameRenderer.isRenderingPanorama()) {
			float h = HUDOverlays.REMAINING - tickDelta;
			int l = (int) (h * 255.0F / 20.0F);
			if (l > 255) l = 255;
			if (l > 10) {
				context.getMatrices().push();
				context.getMatrices().translate((float) (ClientData.CLIENT.getWindow().getScaledWidth() / 2), 27, 0.0F);
				int k = 16777215;
				int m = l << 24 & -16777216;
				int n = ClientData.CLIENT.textRenderer.getWidth(HUDOverlays.MESSAGE);
				context.drawTextWithShadow(ClientData.CLIENT.textRenderer, HUDOverlays.MESSAGE, -n / 2, -4, k | m);
				context.getMatrices().pop();
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "render")
	private void perspective$renderOverlays(DrawContext context, float tickDelta, CallbackInfo ci) {
		if (!HUD.shouldHideHUD()) {
			if (!ClientData.CLIENT.getDebugHud().shouldShowDebugHud()) {
				if (!DebugHUD.debugType.equals(DebugHUD.Type.NONE)) {
					DebugHUD.renderDebugHUD(context);
				}
				if ((boolean) ConfigHelper.getConfig("version_overlay"))
					context.drawTextWithShadow(ClientData.CLIENT.textRenderer, Translation.getTranslation("version_overlay", new Object[]{SharedConstants.getGameVersion().getName()}), 2, 2, 0xffffff);
			}
		}
	}
}