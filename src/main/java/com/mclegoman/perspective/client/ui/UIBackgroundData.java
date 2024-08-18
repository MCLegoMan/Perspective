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
	private final boolean renderDarkening;
	public UIBackgroundData(String id, UIBackground.Runnable renderWorld, boolean renderPanorama, UIBackground.Runnable renderMenu, boolean renderShader, Identifier shaderId) {
		this(id, renderWorld, renderPanorama, renderMenu, renderShader, shaderId, true);
	}
	public UIBackgroundData(String id, UIBackground.Runnable renderWorld, boolean renderPanorama, UIBackground.Runnable renderMenu, boolean renderShader) {
		this(id, renderWorld, renderPanorama, renderMenu, renderShader, null, true);
	}
	public UIBackgroundData(String id, UIBackground.Runnable renderWorld, boolean renderPanorama, UIBackground.Runnable renderMenu) {
		this(id, renderWorld, renderPanorama, renderMenu, true, null, true);
	}
	public UIBackgroundData(String id, UIBackground.Runnable renderWorld, boolean renderPanorama) {
		this(id, renderWorld, renderPanorama, null, true, null, true);
	}
	public UIBackgroundData(String id, UIBackground.Runnable renderWorld) {
		this(id, renderWorld, true, null, true, null, true);
	}
	public UIBackgroundData(String id) {
		this(id, null, true, null, true, null, true);
	}
	public UIBackgroundData(String id, Identifier shaderId) {
		this(id, null, true, null, true, shaderId, true);
	}
	public UIBackgroundData(String id, boolean renderPanorama, Identifier shaderId, boolean renderDarkening) {
		this(id, null, renderPanorama, null, true, shaderId, renderDarkening);
	}
	public UIBackgroundData(String id, boolean renderPanorama, Identifier shaderId) {
		this(id, null, renderPanorama, null, true, shaderId, true);
	}
	public UIBackgroundData(String id, Identifier shaderId, boolean renderDarkening) {
		this(id, null, true, null, true, shaderId, renderDarkening);
	}
	public UIBackgroundData(String id, boolean renderPanorama, boolean renderDarkening) {
		this(id, null, renderPanorama, null, true, null, renderDarkening);
	}
	public UIBackgroundData(String id, boolean renderDarkening) {
		this(id, null, true, null, true, null, renderDarkening);
	}
	public UIBackgroundData() {
		this("fallback", null, true, null, true, null, true);
	}
	public UIBackgroundData(String id, UIBackground.Runnable renderWorld, boolean renderPanorama, UIBackground.Runnable renderMenu, boolean renderShader, Identifier shaderId, boolean renderDarkening) {
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
}