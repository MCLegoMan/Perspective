/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.widget;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LogoWidget extends ClickableWidget {
	private final boolean experimental;
	public LogoWidget(boolean experimental) {
		super(0, 0, 256, 48, Text.empty());
		this.experimental = experimental;
	}
	public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
		renderPerspectiveLogo(context, mouseX, mouseY, delta);
	}
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		renderPerspectiveLogo(context, mouseX, mouseY, delta);
	}
	private void renderPerspectiveLogo(DrawContext context, int mouseX, int mouseY, float delta) {
		Identifier logoIdentifier = this.experimental ? new Identifier(Data.PERSPECTIVE_VERSION.getID(), "textures/gui/logo/normal/experimental.png") : ClientData.getLogo();
		context.drawTexture(logoIdentifier, this.getX(), this.getY(), 0.0F, 0.0F, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
	}
	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
	}
	@Override
	protected boolean isValidClickButton(int button) {
		return false;
	}
}