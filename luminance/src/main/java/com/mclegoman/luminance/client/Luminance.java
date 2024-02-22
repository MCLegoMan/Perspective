/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client;

import com.mclegoman.luminance.client.util.Tick;
import net.fabricmc.api.ClientModInitializer;

public class Luminance implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Tick.init();
	}
}
