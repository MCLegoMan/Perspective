/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.data;

import net.minecraft.client.MinecraftClient;

public class ClientData {
	public static final MinecraftClient CLIENT = MinecraftClient.getInstance();
	private static boolean finishedInitializing = false;
	public static void finishedInitializing() {
		finishedInitializing = true;
	}
	public static boolean isFinishedInitializing() {
		return finishedInitializing;
	}
}