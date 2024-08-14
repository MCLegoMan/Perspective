/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.ui;

import net.minecraft.util.Identifier;

public class UIBackgroundData {
	private final String id;
	private final UIBackground.Runnable renderWorld;
	private final boolean renderPanorama;;
	private final UIBackground.Runnable renderMenu;
	private final boolean renderShader;
	private final Identifier shaderId;
	public UIBackgroundData(String id, UIBackground.Runnable renderWorld, boolean renderPanorama, UIBackground.Runnable renderMenu, boolean renderShader, Identifier shaderId) {
		this.id = id;
		this.renderWorld = renderWorld;
		this.renderPanorama = renderPanorama;
		this.renderMenu = renderMenu;
		this.renderShader = renderShader;
		this.shaderId = shaderId;
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
}