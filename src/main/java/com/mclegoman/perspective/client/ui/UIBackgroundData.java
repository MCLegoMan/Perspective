/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.ui;

import com.mclegoman.perspective.common.data.Data;
import net.minecraft.util.Identifier;

public class UIBackgroundData {
	private final String id;
	private final UIBackground.Runnable renderWorld;
	private final boolean renderPanorama;;
	private final UIBackground.Runnable renderMenu;
	private final boolean renderShader;
	private final Identifier shaderId;
	private final boolean renderDarkening;
	private UIBackgroundData(String id, UIBackground.Runnable renderWorld, boolean renderPanorama, UIBackground.Runnable renderMenu, boolean renderShader, Identifier shaderId, boolean renderDarkening) {
		this.id = id;
		this.renderWorld = renderWorld;
		this.renderPanorama = renderPanorama;
		this.renderMenu = renderMenu;
		this.renderShader = renderShader;
		this.shaderId = shaderId;
		this.renderDarkening = renderDarkening;
	}
	public String getId() {
		return this.id;
	}
	public UIBackground.Runnable getRenderWorld() {
		return this.renderWorld;
	}
	public boolean getRenderPanorama() {
		return this.renderPanorama;
	}
	public UIBackground.Runnable getRenderMenu() {
		return this.renderMenu;
	}
	public boolean getRenderShader() {
		return this.renderShader;
	}
	public Identifier getShaderId() {
		return this.shaderId;
	}
	public boolean getRenderDarkening() {
		return this.renderDarkening;
	}

	public static class Builder {
		private final Identifier identifier;
		private UIBackground.Runnable renderWorld;
		private boolean renderPanorama;
		private UIBackground.Runnable renderMenu;
		private boolean renderShader;
		private Identifier shaderId;
		private boolean renderDarkening;
		public Builder(Identifier identifier) {
			this.identifier = identifier;
			this.renderWorld = null;
			this.renderPanorama = true;
			this.renderMenu = null;
			this.renderShader = true;
			this.shaderId = null;
			this.renderDarkening = true;
		}
		public static UIBackgroundData getFallback() {
			return new Builder(Identifier.of(Data.version.getID(), "fallback")).build();
		}
		public Builder renderWorld(UIBackground.Runnable renderWorld) {
			this.renderWorld = renderWorld;
			return this;
		}
		public Builder renderPanorama(boolean renderPanorama) {
			this.renderPanorama = renderPanorama;
			return this;
		}
		public Builder renderMenu(UIBackground.Runnable renderMenu) {
			this.renderMenu = renderMenu;
			return this;
		}
		public Builder renderShader(boolean renderShader) {
			this.renderShader = renderShader;
			return this;
		}
		public Builder shaderId(Identifier shaderId) {
			this.shaderId = shaderId;
			return this;
		}
		public Builder renderDarkening(boolean renderDarkening) {
			this.renderDarkening = renderDarkening;
			return this;
		}
		public UIBackgroundData build() {
			return new UIBackgroundData(this.identifier.getPath(), this.renderWorld, this.renderPanorama, this.renderMenu, this.renderShader, this.shaderId, this.renderDarkening);
		}
	}
}