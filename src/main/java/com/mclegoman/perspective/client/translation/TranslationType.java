/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.translation;

import net.minecraft.util.StringIdentifiable;

public enum TranslationType implements StringIdentifiable {
	ENDISABLE("endisable"),
	ONFF("onff"),
	DISABLE_SCREEN_MODE("disable_screen_mode");
	private final String name;

	TranslationType(String name) {
		this.name = name;
	}

	@Override
	public String asString() {
		return this.name;
	}
}