/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.logo;

import net.minecraft.util.Identifier;

public class LogoData {
	private final String type;
	private final String id;
	private final Identifier logoTexture;
	private final Identifier iconTexture;
	public LogoData(String type, String id, Identifier logoTexture, Identifier iconTexture) {
		this.type = type;
		this.id = id;
		this.logoTexture = logoTexture;
		this.iconTexture = iconTexture;
	}
	public String getType() {
		return this.type;
	}
	public String getId() {
		return this.id;
	}
	public Identifier getLogoTexture() {
		return this.logoTexture;
	}
	public Identifier getIconTexture() {
		return this.iconTexture;
	}
}
