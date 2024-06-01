/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.logo;

import com.mclegoman.luminance.client.logo.LogoHelper;
import com.mclegoman.luminance.common.util.DateHelper;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PerspectiveLogo {
	// Pride Logos will be re-worked soon.
	public static final List<String> prideTypes = new ArrayList<>();
	// Whilst currently there are only four types, there will be more soon as they are re-worked.
	private static int prideIndex;
	public static void init() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SplashesDataloader());
		prideTypes.add("rainbow");
		prideTypes.add("bi");
		prideTypes.add("pan");
		prideTypes.add("trans");
		prideIndex = new Random().nextInt(prideTypes.size());
	}
	public static boolean isPride() {
		return isActuallyPride() || isForcePride();
	}
	public static boolean isActuallyPride() {
		LocalDate date = DateHelper.getDate();
		return (date.getMonth() == Month.JUNE || date.getMonth() == Month.JULY && date.getDayOfMonth() <= 2);
	}
	public static boolean isForcePride() {
		return (boolean) ConfigHelper.getConfig("force_pride");
	}
	private static String getPrideType() {
		if ((boolean) ConfigHelper.getConfig("force_pride_type")) {
			int index = (int)ConfigHelper.getConfig("force_pride_type_index");
			Data.version.sendToLog(LogType.INFO, String.valueOf(prideTypes.size()));
			if (index < prideTypes.size()) return prideTypes.get(index);
		}
		return prideTypes.get(prideIndex);
	}
	public static Logo getLogo(Logo.Type type) {
		return new Logo(new Identifier(Data.version.getID(), (type.equals(Logo.Type.PRIDE) ? getPrideType() : Data.version.getID())), type);
	}
	public static void renderLogo(DrawContext context, int x, int y, int width, int height, boolean experimental) {
		Identifier logoIdentifier = getLogo((experimental ? Logo.Type.EXPERIMENTAL : (isPride() ? Logo.Type.PRIDE : Logo.Type.DEFAULT))).getTexture();
		context.drawTexture(logoIdentifier, x, y, 0.0F, 0.0F, width, (int) (height * 0.6875), width, height);
		LogoHelper.renderDevelopmentOverlay(context, (int) ((x + ((float) width / 2)) - ((width * 0.75F) / 2)), (int) (y + (height - (height * 0.54F))), width, height, Data.version.isDevelopmentBuild());
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
			super(0, 0, 256, 48, Text.empty());
			this.experimental = experimental;
		}
		public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
			renderLogo(context, this.getX(), this.getY(), 256, 64, experimental);
			LogoHelper.createSplashText(context, this.getWidth(), this.getX(), this.getY() + 32, ClientData.minecraft.textRenderer, SplashesDataloader.getSplashText(), -20.0F);
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
