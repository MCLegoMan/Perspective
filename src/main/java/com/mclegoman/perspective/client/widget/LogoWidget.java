/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.widget;

import com.mclegoman.perspective.client.logo.PerspectiveLogo;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class LogoWidget extends ClickableWidget {
	private final boolean experimental;
	public LogoWidget(boolean experimental) {
		super(0, 0, 256, 48, Text.empty());
		this.experimental = experimental;
	}
	public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		PerspectiveLogo.renderPerspectiveLogo(context, this.getX(), this.getY(), 256, 64, this.experimental);
	}
	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
	}
	@Override
	protected boolean isValidClickButton(int button) {
		return false;
	}
}