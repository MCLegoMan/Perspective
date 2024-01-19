/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.common.data.Data;
import net.minecraft.util.Identifier;

public class Cape {
	public static Identifier getCapeTexture(String texture, Identifier current) {
		if (texture.equals("none")) {
			return current;
		} else {
			if (texture.contains(":")) {
				String cape_namespace = texture.substring(0, texture.indexOf(":"));
				String cape_texture = texture.substring(texture.indexOf(":") + 1);
				return new Identifier(cape_namespace, "textures/entity/player/cape/" + cape_texture + ".png");
			} else {
				return new Identifier(Data.PERSPECTIVE_VERSION.getID(), "textures/entity/player/cape/" + texture + ".png");
			}
		}
	}
}