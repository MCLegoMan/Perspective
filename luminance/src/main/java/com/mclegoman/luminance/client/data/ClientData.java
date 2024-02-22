/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.data;

import net.minecraft.client.MinecraftClient;

public class ClientData {
	public static MinecraftClient client;
	static {
		client = MinecraftClient.getInstance();
	}
}
