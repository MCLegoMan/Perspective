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
	private final boolean renderPanorama;
	private final UIBackground.Runnable renderMenu;
	private final boolean renderTitleScreenPanorama;
	private final UIBackground.Runnable renderTitleScreen;
	private final boolean renderShader;
	private final Identifier shaderId;
	private final boolean renderDarkening;
	private UIBackgroundData(String id, UIBackground.Runnable renderWorld, boolean renderPanorama, UIBackground.Runnable renderMenu, boolean renderTitleScreenPanorama, UIBackground.Runnable renderTitleScreen, boolean renderShader, Identifier shaderId, boolean renderDarkening) {
		this.id = id;
		this.renderWorld = renderWorld;
		this.renderPanorama = renderPanorama;
		this.renderMenu = renderMenu;
		this.renderTitleScreenPanorama = renderTitleScreenPanorama;
		this.renderTitleScreen = renderTitleScreen;
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
	public boolean getRenderTitleScreenPanorama() {
		return this.renderTitleScreenPanorama;
	}
	public UIBackground.Runnable getRenderTitleScreen() {
		return this.renderTitleScreen;
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
		private boolean renderTitleScreenPanorama;
		private UIBackground.Runnable renderTitleScreen;
		private boolean renderShader;
		private Identifier shaderId;
		private boolean renderDarkening;
		public Builder(Identifier identifier) {
			this.identifier = identifier;
			this.renderWorld = null;
			this.renderPanorama = true;
			this.renderMenu = null;
			this.renderTitleScreenPanorama = true;
			this.renderTitleScreen = null;
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
		public Builder renderTitleScreenPanorama(boolean renderTitleScreenPanorama) {
			this.renderTitleScreenPanorama = renderTitleScreenPanorama;
			return this;
		}
		public Builder renderTitleScreen(UIBackground.Runnable renderTitleScreen) {
			this.renderTitleScreen = renderTitleScreen;
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
			return new UIBackgroundData(this.identifier.getPath(), this.renderWorld, this.renderPanorama, this.renderMenu, this.renderTitleScreenPanorama, this.renderTitleScreen, this.renderShader, this.shaderId, this.renderDarkening);
		}
	}
}