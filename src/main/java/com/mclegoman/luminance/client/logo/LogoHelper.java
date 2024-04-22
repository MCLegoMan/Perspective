/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.logo;

import com.mclegoman.luminance.common.data.Data;
import com.mclegoman.luminance.common.util.Couple;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.ui.SplashesDataloader;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class LogoHelper {
	public static void renderDevelopmentOverlay(DrawContext context, int x, int y, int width, int height, boolean shouldRender) {
		if (shouldRender) context.drawTexture(new Identifier(Data.version.getID(), "textures/gui/logo/development.png"), (x + (width / 2)) - ((int) (width * 0.625) / 2), (int) (y + ((height * 0.6875) - 14)), 0.0F, 0.0F, (int) (width * 0.625), height / 4, (int) (width * 0.625), height / 4);
	}
	public static void createSplashText(DrawContext context, int width, int x, int y, TextRenderer textRenderer, Couple<String, Boolean> splashText, float rotation) {
		if (SplashesDataloader.getSplashText() != null && !ClientData.minecraft.options.getHideSplashTexts().getValue()) {
			context.getMatrices().push();
			context.getMatrices().translate(x + width, y, 0.0F);
			context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
			float scale = (1.8F - MathHelper.abs(MathHelper.sin((float)(Util.getMeasuringTimeMs() % 1000L) / 1000.0F * ((float)Math.PI * 2)) * 0.1F)) * 100.0F / (float)(textRenderer.getWidth(Translation.getText(splashText)) + 32);
			context.getMatrices().scale(scale, scale, scale);
			context.drawCenteredTextWithShadow(textRenderer, Translation.getText(splashText), 0, -8, 0xFFFF00);
			context.getMatrices().pop();
		}
	}
}