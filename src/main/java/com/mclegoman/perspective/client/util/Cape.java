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
	public static Identifier getCapeTexture(String CAPE) {
		if (CAPE.contains(":")) {
			String NAMESPACE = CAPE.substring(0, CAPE.indexOf(":"));
			String TEXTURE = CAPE.substring(CAPE.indexOf(":") + 1);
			return new Identifier(NAMESPACE, "textures/entity/player/cape/" + TEXTURE + ".png");
		} else {
			return new Identifier(Data.PERSPECTIVE_VERSION.getID(), "textures/entity/player/cape/" + CAPE + ".png");
		}
	}
}
