/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

import java.time.LocalDate;
import java.time.Month;
import java.util.Random;
import java.util.TimeZone;

public class PerspectiveLogo {
	// Pride Logos will be re-worked in the near future.
	public static final String[] pride_types = new String[]{"rainbow"};
	private static final int pride_index = new Random().nextInt(pride_types.length);
	public static boolean isPride() {
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "force_pride")) return true;
		else {
			LocalDate date = LocalDate.now(TimeZone.getTimeZone("GMT+12").toZoneId());
			return date.getMonth() == Month.JUNE || date.getMonth() == Month.JULY && date.getDayOfMonth() <= 2;
		}
	}
	private static String getPrideType() {
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "force_pride_type")) {
			return pride_types[(int)ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "force_pride_type_index")];
		} else {
			return pride_types[pride_index];
		}
	}
	public static Logo getLogo(Logo.Type type) {
		return new Logo(new Identifier(Data.VERSION.getID(), (type.equals(Logo.Type.PRIDE) ? "perspective/" + getPrideType() : "perspective")), type);
	}
	public record Logo(Identifier id, Type type) {
		public String getNamespace() {
			return this.id.getNamespace();
		}
		public String getName() {
			return this.id.getPath();
		}
		public Identifier getTexture() {
			return new Identifier(getNamespace(), "textures/gui/logo/" + type().toString() + "/" + getName() + "/" + (Data.VERSION.isDevelopmentBuild() ? "development" : "release") + ".png");
		}
		public enum Type implements StringIdentifiable {
			DEFAULT("default"),
			PRIDE("pride"),
			EXPERIMENTAL("experimental");
			private final String name;
			private Type(String name) {
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
			renderPerspectiveLogo(context);
		}
		private void renderPerspectiveLogo(DrawContext context) {
			Identifier logoIdentifier = getLogo((experimental ? Logo.Type.EXPERIMENTAL : (isPride() ? Logo.Type.PRIDE : Logo.Type.DEFAULT))).getTexture();
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
}
