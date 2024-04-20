/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/
package com.mclegoman.luminance.client.shaders;

public class Uniforms {
	private static int alpha = 100;
	public static float get() {
		return Math.max(0.0F, Math.min((alpha / 100.0F), 1.0F));
	}
	public static void set(int value) {
		alpha = Math.max(0, Math.min((value), 100));
	}
	public static void reset() {
		set(100);
	}
	public static void adjust(int amount) {
		set(alpha + amount);
	}
}
