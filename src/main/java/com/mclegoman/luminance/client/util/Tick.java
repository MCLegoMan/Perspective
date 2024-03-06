/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.util;

import com.mclegoman.luminance.client.shaders.ShaderUniforms;
import com.mclegoman.luminance.client.data.ClientData;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class Tick {
	public static void init() {
		ClientTickEvents.END_CLIENT_TICK.register((client) -> {
			if (ClientData.isFinishedInitializing()) {
				ShaderUniforms.tick();
			}
		});
	}
}