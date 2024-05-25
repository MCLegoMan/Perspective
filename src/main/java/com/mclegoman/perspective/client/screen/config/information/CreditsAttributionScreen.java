/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.information;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.logo.PerspectiveLogo;
import com.mclegoman.perspective.client.ui.UIBackground;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class CreditsAttributionScreen extends com.mclegoman.luminance.client.screen.config.information.CreditsAttributionScreen {
	private float backgroundFade;
	private final boolean overrideLogo;
	public CreditsAttributionScreen(Screen parentScreen, Identifier creditsJsonId) {
		super(parentScreen, PerspectiveLogo.isPride(), creditsJsonId);
		this.overrideLogo = true;
	}
	public CreditsAttributionScreen(Screen parentScreen) {
		super(parentScreen, PerspectiveLogo.isPride());
		this.overrideLogo = false;
	}
	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float tickDelta) {
		if (UIBackground.getUIBackgroundType().equalsIgnoreCase("legacy")) {
			if (this.time > (this.creditsHeight + ClientData.minecraft.getWindow().getScaledHeight() + 48)) {
				float fadeStart = (this.creditsHeight + ClientData.minecraft.getWindow().getScaledHeight() + 48);
				float fadeEnd = (this.creditsHeight + ClientData.minecraft.getWindow().getScaledHeight() + 92);
				this.backgroundFade = MathHelper.lerp(((this.time - fadeStart) / (fadeEnd - fadeStart)) * 0.5F, this.backgroundFade, 0.0F);
			} else {
				this.backgroundFade = MathHelper.lerp((this.time / 48) * 0.25F, this.backgroundFade, 0.25F);
			}
			context.setShaderColor(this.backgroundFade, this.backgroundFade, this.backgroundFade, 1.0F);
			context.drawTexture(new Identifier("minecraft", "textures/block/dirt.png"), 0, 0, 0, 0.0F, this.time * 0.5F, ClientData.minecraft.getWindow().getScaledWidth(), ClientData.minecraft.getWindow().getScaledHeight(), 16, 16);
			context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			this.renderDarkening(context);
		} else super.renderBackground(context, mouseX, mouseY, tickDelta);
	}
	@Override
	protected void renderLogo(DrawContext context) {
		if (this.overrideLogo) PerspectiveLogo.renderLogo(context, ClientData.minecraft.getWindow().getScaledWidth() / 2 - 128, ClientData.minecraft.getWindow().getScaledHeight() + 2, 256, 64, false);
		else super.renderLogo(context);
	}
}
