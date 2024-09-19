/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.contributor;

import com.mclegoman.luminance.common.util.IdentifierHelper;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
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
	private final boolean isOverlayEmissive;
	private ContributorData(List<String> ids, String uuid, String type, boolean shouldFlipUpsideDown, boolean shouldReplaceCape, Identifier capeTexture, boolean shouldRenderHeadItem, Identifier headItem, boolean shouldRenderOverlay, Identifier overlayTexture, boolean isOverlayEmissive) {
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
		this.isOverlayEmissive = isOverlayEmissive;
	}
	public static Builder builder(String uuid) {
		return new Builder(uuid);
	}
	public static class Builder {
		private final List<String> ids;
		private final String uuid;
		private String type;
		private boolean shouldFlipUpsideDown;
		private boolean shouldReplaceCape;
		private Identifier capeTexture;
		private boolean shouldRenderHeadItem;
		private Identifier headItem;
		private boolean shouldRenderOverlay;
		private Identifier overlayTexture;
		private boolean isOverlayEmissive;
		public Builder(String uuid) {
			this.ids = new ArrayList<>();
			this.uuid = uuid;
			this.type = Contributor.Type.CONTRIBUTOR.name();
			this.shouldFlipUpsideDown = false;
			this.shouldReplaceCape = false;
			this.capeTexture = IdentifierHelper.identifierFromString("none");
			this.shouldRenderHeadItem = false;
			this.headItem = Identifier.ofVanilla("air");
			this.shouldRenderOverlay = false;
			this.overlayTexture = IdentifierHelper.identifierFromString("none");
			this.isOverlayEmissive = false;
		}
		public Builder id(String... ids) {
			Collections.addAll(this.ids, ids);
			return this;
		}
		public Builder id(List<String> id) {
			this.ids.addAll(id);
			return this;
		}
		public Builder type(String type) {
			this.type = type;
			return this;
		}
		public Builder shouldFlipUpsideDown(boolean shouldFlipUpsideDown) {
			this.shouldFlipUpsideDown = shouldFlipUpsideDown;
			return this;
		}
		public Builder shouldReplaceCape(boolean shouldReplaceCape) {
			this.shouldReplaceCape = shouldReplaceCape;
			return this;
		}
		public Builder capeTexture(Identifier capeTexture) {
			this.capeTexture = capeTexture;
			return this;
		}
		public Builder shouldRenderHeadItem(boolean shouldRenderHeadItem) {
			this.shouldRenderHeadItem = shouldRenderHeadItem;
			return this;
		}
		public Builder headItem(Identifier headItem) {
			this.headItem = headItem;
			return this;
		}
		public Builder shouldRenderOverlay(boolean shouldRenderOverlay) {
			this.shouldRenderOverlay = shouldRenderOverlay;
			return this;
		}
		public Builder overlayTexture(Identifier overlayTexture, boolean emissive) {
			this.overlayTexture = overlayTexture;
			this.isOverlayEmissive = emissive;
			return this;
		}
		public Builder overlayTexture(Identifier overlayTexture) {
			return overlayTexture(overlayTexture, false);
		}
		public ContributorData build() {
			return new ContributorData(this.ids, this.uuid, this.type, this.shouldFlipUpsideDown, this.shouldReplaceCape, this.capeTexture, this.shouldRenderHeadItem, this.headItem, this.shouldRenderOverlay, this.overlayTexture, this.isOverlayEmissive);
		}
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
	public boolean getIsOverlayEmissive() {
		return this.isOverlayEmissive;
	}
}
