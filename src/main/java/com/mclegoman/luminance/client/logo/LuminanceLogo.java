/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.logo;

import com.mclegoman.luminance.common.data.Data;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LuminanceLogo {
	public static Logo getLogo() {
		return new Logo(new Identifier(Data.version.getID(), Data.version.getID()));
	}
	public static void renderLogo(DrawContext context, int x, int y, int width, int height) {
		context.drawTexture(getLogo().getTexture(), x, y, 0.0F, 0.0F, width, (int) (height * 0.6875), width, height);
		LogoHelper.renderDevelopmentOverlay(context, x, y, width, height, Data.version.isDevelopmentBuild());
	}
	public record Logo(Identifier id) {
		public String getNamespace() {
			return this.id.getNamespace();
		}
		public String getName() {
			return this.id.getPath();
		}
		public Identifier getTexture() {
			return new Identifier(getNamespace(), "textures/gui/logo/" + getName() + ".png");
		}
	}
	public static class Widget extends ClickableWidget {
		public Widget() {
			super(0, 0, 256, 64, Text.empty());
		}
		public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
			renderLogo(context, this.getX(), this.getY() + 7, this.getWidth(), this.getHeight());
		}
		@Override
		protected void appendClickableNarrations(NarrationMessageBuilder builder) {
		}
		@Override
		protected boolean isValidClickButton(int button) {
			return false;
		}
	}
}
