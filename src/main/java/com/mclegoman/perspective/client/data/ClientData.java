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
	private static boolean finishedInitializingAfterConfig = false;
	public static void setFinishedInitializing(boolean value) {
		finishedInitializing = value;
	}
	public static void setFinishedInitializingAfterConfig(boolean value) {
		finishedInitializingAfterConfig = value;
	}
	public static boolean getFinishedInitializing() {
		return finishedInitializing;
	}
	public static boolean getFinishedInitializingAfterConfig() {
		return finishedInitializingAfterConfig;
	}
	public static boolean isFinishedInitializing() {
		return minecraft.isFinishedLoading() && getFinishedInitializing() && getFinishedInitializingAfterConfig();
	}
}