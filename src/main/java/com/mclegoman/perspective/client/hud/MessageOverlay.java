/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.hud;

import net.minecraft.text.Text;

public class MessageOverlay {
	public static Text message;
	public static float remaining;

	public static void tick() {
		if (remaining > 0) remaining -= 1;
	}

	public static void setOverlay(Text text) {
		message = text;
		remaining = 40;
	}
}