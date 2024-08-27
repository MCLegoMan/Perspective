/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.contributor;

import net.minecraft.util.Identifier;

import java.util.List;

public class ContributorData {
	private final List<String> ids;
	private final String uuid;
	private final String type;
	private final boolean shouldFlipUpsideDown;
	private final boolean shouldReplaceCape;
	private final Identifier capeTexture;
	private final boolean shouldRenderHeadItem;
	private final Identifier headItem;
	private final boolean shouldRenderOverlay;
	private final Identifier overlayTexture;
	public ContributorData(List<String> ids, String uuid, String type, boolean shouldFlipUpsideDown, boolean shouldReplaceCape, Identifier capeTexture, boolean shouldRenderHeadItem, Identifier headItem, boolean shouldRenderOverlay, Identifier overlayTexture) {
		this.ids = ids;
		this.uuid = uuid;
		this.type = type;
		this.shouldFlipUpsideDown = shouldFlipUpsideDown;
		this.shouldReplaceCape = shouldReplaceCape;
		this.capeTexture = capeTexture;
		this.shouldRenderHeadItem = shouldRenderHeadItem;
		this.headItem = headItem;
		this.shouldRenderOverlay = shouldRenderOverlay;
		this.overlayTexture = overlayTexture;
	}
	public List<String> getIds() {
		return this.ids;
	}
	public String getUuid() {
		return this.uuid;
	}
	public String getType() {
		return this.type;
	}
	public boolean getShouldFlipUpsideDown() {
		return this.shouldFlipUpsideDown;
	}
	public boolean getShouldReplaceCape() {
		return this.shouldReplaceCape;
	}
	public Identifier getCapeTexture() {
		return this.capeTexture;
	}
	public boolean getShouldRenderHeadItem() {
		return this.shouldRenderHeadItem;
	}
	public Identifier getHeadItem() {
		return this.headItem;
	}
	public boolean getShouldRenderOverlay() {
		return this.shouldRenderOverlay;
	}
	public Identifier getOverlayTexture() {
		return this.overlayTexture;
	}
}
