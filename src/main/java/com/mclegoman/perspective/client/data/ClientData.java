/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.data;

import net.minecraft.client.MinecraftClient;

public class ClientData {
	public static final MinecraftClient minecraft = MinecraftClient.getInstance();
	private static boolean finishedInitializing = false;
	public static void finishedInitializing() {
		finishedInitializing = true;
	}
	public static boolean isFinishedInitializing() {
		return minecraft.isFinishedLoading() && finishedInitializing;
	}
}