/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.data;

import net.minecraft.client.MinecraftClient;

public class ClientData {
	public static final MinecraftClient CLIENT = MinecraftClient.getInstance();
	private static boolean finishedInitializing = false;
	private static boolean finishedInitializingAfterConfig = false;
	public static void finishedInitializing() {
		finishedInitializing = true;
	}
	public static void finishedInitializingAfterConfig() {
		finishedInitializingAfterConfig = true;
	}
	public static boolean isFinishedInitializing() {
		return CLIENT.isFinishedLoading() && finishedInitializing && finishedInitializingAfterConfig;
	}
}