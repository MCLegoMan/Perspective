/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.hud;

import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hud.DebugOverlay;
import com.mclegoman.perspective.client.hud.PositionOverlay;
import com.mclegoman.perspective.client.hud.HUDHelper;
import com.mclegoman.perspective.client.hud.MessageOverlay;
import com.mclegoman.perspective.client.translation.Translation;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = InGameHud.class)
public abstract class InGameHudMixin {
	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void perspective$render(DrawContext context, float tickDelta, CallbackInfo ci) {
		if (HUDHelper.shouldHideHUD()) ci.cancel();
		if (!ClientData.CLIENT.gameRenderer.isRenderingPanorama()) {
			float h = MessageOverlay.REMAINING - tickDelta;
			int l = (int) (h * 255.0F / 20.0F);
			if (l > 255) l = 255;
			if (l > 10) {
				context.getMatrices().push();
				context.getMatrices().translate((float) (ClientData.CLIENT.getWindow().getScaledWidth() / 2), 27, 0.0F);
				int k = 16777215;
				int m = l << 24 & -16777216;
				int n = ClientData.CLIENT.textRenderer.getWidth(MessageOverlay.MESSAGE);
				context.drawTextWithShadow(ClientData.CLIENT.textRenderer, MessageOverlay.MESSAGE, -n / 2, -4, k | m);
				context.getMatrices().pop();
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "render")
	private void perspective$renderOverlays(DrawContext context, float tickDelta, CallbackInfo ci) {
		if (!HUDHelper.shouldHideHUD()) {
			if (!ClientData.CLIENT.getDebugHud().shouldShowDebugHud()) {
				if ((boolean) ConfigHelper.getConfig("version_overlay"))
					context.drawTextWithShadow(ClientData.CLIENT.textRenderer, Translation.getTranslation(Data.version.getID(), "version_overlay", new Object[]{SharedConstants.getGameVersion().getName()}), 2, 2, 0xffffff);
				if ((boolean) ConfigHelper.getConfig("position_overlay")) PositionOverlay.render(context);
				if (!DebugOverlay.debugType.equals(DebugOverlay.Type.NONE)) {
					DebugOverlay.renderDebugHUD(context);
				}
			}
		}
	}
}