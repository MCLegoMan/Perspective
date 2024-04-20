/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.ui;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.util.DateHelper;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.luminance.common.util.Couple;
import com.mclegoman.perspective.config.ConfigHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.time.LocalDate;
import java.time.Month;
import java.util.Random;

public class PerspectiveLogo {
	// Pride Logos will be re-worked in the near future.
	public static final String[] prideTypes = new String[]{"rainbow", "bi"};
	// Whilst currently there are only two types, there will be more in the near future as they are re-worked.
	private static final int prideIndex = new Random().nextInt(prideTypes.length);
	public static void init() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SplashesDataloader());
	}
	public static boolean isPride() {
		LocalDate date = DateHelper.getDate();
		return isForcePride() || date.getMonth() == Month.JUNE || date.getMonth() == Month.JULY && date.getDayOfMonth() <= 2;
	}
	public static boolean isForcePride() {
		return (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "force_pride");
	}
	private static String getPrideType() {
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "force_pride_type")) {
			return prideTypes[(int)ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "force_pride_type_index")];
		} else {
			return prideTypes[prideIndex];
		}
	}
	public static Logo getLogo(Logo.Type type) {
		return new Logo(new Identifier(Data.version.getID(), (type.equals(Logo.Type.PRIDE) ? getPrideType() : "perspective")), type);
	}
	public static void renderPerspectiveLogo(DrawContext context, int x, int y, int width, int height, boolean experimental) {
		Identifier logoIdentifier = getLogo((experimental ? Logo.Type.EXPERIMENTAL : (isPride() ? Logo.Type.PRIDE : Logo.Type.DEFAULT))).getTexture();
		context.drawTexture(logoIdentifier, x, y, 0.0F, 0.0F, width, (int) (height * 0.6875), width, height);
		if (Data.version.isDevelopmentBuild()) context.drawTexture(new Identifier(Data.version.getID(), "textures/gui/logo/development.png"), (x + (width / 2)) - ((int) (width * 0.625) / 2), (int) (y + ((height * 0.6875) - 14)), 0.0F, 0.0F, (int) (width * 0.625), height / 4, (int) (width * 0.625), height / 4);
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
	public record Logo(Identifier id, Type type) {
		public String getNamespace() {
			return this.id.getNamespace();
		}
		public String getName() {
			return this.id.getPath();
		}
		public Identifier getTexture() {
			return new Identifier(getNamespace(), "textures/gui/logo/" + type().toString() + "/" + getName() + ".png");
		}
		public enum Type implements StringIdentifiable {
			DEFAULT("default"),
			PRIDE("pride"),
			EXPERIMENTAL("experimental");
			private final String name;
			Type(String name) {
				this.name = name;
			}
			public String toString() {
					return this.name;
				}
			public String asString() {
				return this.name;
			}
		}
	}
	public static class Widget extends ClickableWidget {
		private final boolean experimental;
		public Widget(boolean experimental) {
			super(0, 0, 256, 64, Text.empty());
			this.experimental = experimental;
		}
		public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
			renderPerspectiveLogo(context, this.getX(), this.getY() + 7, this.getWidth(), this.getHeight(), experimental);
			createSplashText(context, this.getWidth(), this.getX(), this.getY() + 41, ClientData.minecraft.textRenderer, SplashesDataloader.getSplashText(), -20.0F);
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
