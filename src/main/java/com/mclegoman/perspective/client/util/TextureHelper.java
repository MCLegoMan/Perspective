/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import net.minecraft.util.Identifier;

public class TextureHelper {
	public static Identifier getTexture(Identifier texture, Identifier current) {
		return texture.getPath().equals("none") ? current : Identifier.of(texture.getNamespace(), texture.getPath().endsWith(".png") ? texture.getPath() : texture.getPath() + ".png");
	}
}