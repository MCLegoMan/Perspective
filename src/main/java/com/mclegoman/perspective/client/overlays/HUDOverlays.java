/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.overlays;

import net.minecraft.text.Text;

public class HUDOverlays {
	public static Text MESSAGE;
	public static float REMAINING;
	public static void tick() {
		if (REMAINING > 0) REMAINING -= 1;
	}
	public static void setOverlay(Text text) {
		MESSAGE = text;
		REMAINING = 40;
	}
}