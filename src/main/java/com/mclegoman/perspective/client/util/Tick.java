/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.entity.Entity;
import com.mclegoman.perspective.client.events.AprilFoolsPrank;
import com.mclegoman.perspective.client.events.Halloween;
import com.mclegoman.perspective.client.keybindings.Keybindings;
import com.mclegoman.perspective.client.shaders.Shaders;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hide.Hide;
import com.mclegoman.perspective.client.hud.HUDHelper;
import com.mclegoman.perspective.client.panorama.Panorama;
import com.mclegoman.perspective.client.perspective.Perspective;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.zoom.Zoom;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class Tick {
	public static void init() {
		ClientTickEvents.END_CLIENT_TICK.register((client) -> {
			if (ClientData.isFinishedInitializing()) {
				ConfigHelper.tick();
				HUDHelper.tick();
				AprilFoolsPrank.tick();
				Halloween.tick();
				Keybindings.tick();
				Perspective.tick();
				Zoom.tick();
				Shader.tick();
				Shaders.tick();
				Entity.tick();
				Panorama.tick();
				Hide.tick();
			}
		});
	}
}