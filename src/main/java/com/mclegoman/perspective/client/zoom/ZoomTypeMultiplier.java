/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.zoom;

public abstract class ZoomTypeMultiplier {
	protected static float currentMultiplier = 1.0F;
	protected static float getMultiplier() {
		return currentMultiplier;
	}
	protected static void setMultiplier(float multiplier) {
		currentMultiplier = multiplier;
	}
}
