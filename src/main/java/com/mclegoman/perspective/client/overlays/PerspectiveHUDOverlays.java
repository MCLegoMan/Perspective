/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.overlays;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class PerspectiveHUDOverlays {
    public static Text MESSAGE;
    public static float REMAINING;
    public static void tick(MinecraftClient client) {
        if (REMAINING > 0) REMAINING -= 1;
    }
    public static void setOverlay(Text text) {
        MESSAGE = text;
        REMAINING = 40;
    }
}
