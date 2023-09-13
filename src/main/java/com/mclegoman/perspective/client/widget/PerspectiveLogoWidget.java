package com.mclegoman.perspective.client.widget;

import com.mclegoman.perspective.client.data.PerspectiveClientData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class PerspectiveLogoWidget extends ClickableWidget {

	public PerspectiveLogoWidget() {
		super(0, 0, 256, 48, Text.empty());
	}

	public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
		context.drawTexture(PerspectiveClientData.getLogo(), this.getX(), this.getY(), 0.0F, 0.0F, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
	}

	@Override
	protected boolean isValidClickButton(int button) {
		return false;
	}
}