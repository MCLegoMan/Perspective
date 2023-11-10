/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.widget;

import com.mclegoman.perspective.client.data.ClientData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class LogoWidget extends ClickableWidget {

	public LogoWidget() {
		super(0, 0, 256, 48, Text.empty());
	}

	public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
		context.drawTexture(ClientData.getLogo(), this.getX(), this.getY(), 0.0F, 0.0F, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
	}

	@Override
	protected boolean isValidClickButton(int button) {
		return false;
	}
}