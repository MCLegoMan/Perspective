/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.logo;

import com.mclegoman.luminance.client.logo.LogoHelper;
import com.mclegoman.luminance.client.util.CompatHelper;
import com.mclegoman.luminance.common.util.Couple;
import com.mclegoman.luminance.common.util.DateHelper;
import com.mclegoman.luminance.common.util.IdentifierHelper;
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

public class PerspectiveLogo {
	public static void init() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PrideLogoDataLoader());
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SplashesDataloader());
		CompatHelper.addOverrideModMenuIcon(new Couple<>(Data.version.getID(), "pride"), () -> "assets/" + IdentifierHelper.getStringPart(IdentifierHelper.Type.NAMESPACE, IdentifierHelper.stringFromIdentifier(getLogo(Logo.Type.PRIDE).getIconTexture())) + "/" + IdentifierHelper.getStringPart(IdentifierHelper.Type.KEY, IdentifierHelper.stringFromIdentifier(getLogo(Logo.Type.PRIDE).getIconTexture())), PerspectiveLogo::isPride);
		CompatHelper.addLuminanceModMenuBadge(Data.version.getID());
	}
	public static boolean isPerspectiveBirthday() {
		// Perspective's official birthday is June 14th.
		LocalDate date = DateHelper.getDate();
		return date.getMonth() == Month.JUNE && date.getDayOfMonth() >= 14 && date.getDayOfMonth() <= 15;
	}
	public static LogoData getDefaultLogo() {
		return new LogoData("default", "perspective", IdentifierHelper.identifierFromString(getLogoTexture("default", "perspective")), IdentifierHelper.identifierFromString(getLogoTexture("default", "perspective")));
	}
	public static LogoData getExperimentalLogo() {
		return new LogoData("experimental", "perspective", IdentifierHelper.identifierFromString(getLogoTexture("experimental", "perspective")), IdentifierHelper.identifierFromString(getLogoTexture("experimental", "perspective")));
	}
	public static String getLogoTexture(String type, String id) {
		return "perspective:textures/logos/" + type + "/" + id + ".png";
	}
	public static String getIconTexture(String type, String id) {
		return "perspective:textures/icons/" + type + "/" + id + ".png";
	}
	public static boolean isPride() {
		return isActuallyPride() || isForcePride();
	}
	public static boolean isActuallyPride() {
		return DateHelper.isPride();
	}
	public static boolean isForcePride() {
		return (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "force_pride");
	}
	private static LogoData getPrideLogo() {
		if (!ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "force_pride_type").equals("random")) {
			return getPrideLogoFromId((String)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "force_pride_type"));
		} else {
			return PrideLogoDataLoader.getLogo();
		}
	}
	public static LogoData getPrideLogoFromId(String id) {
		for (LogoData logoData : PrideLogoDataLoader.registry) {
			if (id.equals(logoData.getId())) return logoData;
		}
		return getDefaultLogo();
	}
	public static Logo getLogo(Logo.Type type) {
		switch (type) {
			case PRIDE -> {
				return new Logo(getPrideLogo());
			}
			case EXPERIMENTAL -> {
				return new Logo(getExperimentalLogo());
			}
			default -> {
				return new Logo(getDefaultLogo());
			}
		}
	}
	public static Identifier getLogoTexture(boolean experimental) {
		return getLogo((experimental ? Logo.Type.EXPERIMENTAL : (isPride() ? Logo.Type.PRIDE : Logo.Type.DEFAULT))).getLogoTexture();
	}
	public static void renderLogo(DrawContext context, int x, int y, int width, int height, Identifier logoTexture) {
		context.drawTexture(logoTexture, x, y, 0.0F, 0.0F, width, (int) (height * 0.6875), width, height);
		LogoHelper.renderDevelopmentOverlay(context, (int) ((x + ((float) width / 2)) - ((width * 0.75F) / 2)), (int) (y + (height - (height * 0.54F))), width, height, Data.version.isDevelopmentBuild(), 0, 0);
	}
	public record Logo(LogoData data) {
		public Identifier getIconTexture() {
			return data.getIconTexture();
		}
		public Identifier getLogoTexture() {
			return data.getLogoTexture();
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
		public Widget(int x, int y, boolean experimental) {
			super(x, y, 256, 64, Text.empty());
			this.experimental = experimental;
		}
		public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
			renderLogo(context, this.getX(), this.getY(), this.getWidth(), this.getHeight(), getLogoTexture(experimental));
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
