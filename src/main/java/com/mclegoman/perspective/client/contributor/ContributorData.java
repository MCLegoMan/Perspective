/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.contributor;

import net.minecraft.util.Identifier;

public class ContributorData {
	private final String uuid;
	private final String type;
	private final boolean shouldFlipUpsideDown;
	private final boolean shouldReplaceCape;
	private final Identifier capeTexture;
	private final boolean shouldRenderHeadItem;
	private final Identifier headItem;
	public ContributorData(String uuid, String type, boolean shouldFlipUpsideDown, boolean shouldReplaceCape, Identifier capeTexture, boolean shouldRenderHeadItem, Identifier headItem) {
		this.uuid = uuid;
		this.type = type;
		this.shouldFlipUpsideDown = shouldFlipUpsideDown;
		this.shouldReplaceCape = shouldReplaceCape;
		this.capeTexture = capeTexture;
		this.shouldRenderHeadItem = shouldRenderHeadItem;
		this.headItem = headItem;
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
}
