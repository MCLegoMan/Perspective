/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.april_fools_prank.AprilFoolsPrank;
import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hide.Hide;
import com.mclegoman.perspective.client.hud.HUD;
import com.mclegoman.perspective.client.overlays.HUDOverlays;
import com.mclegoman.perspective.client.panorama.Panorama;
import com.mclegoman.perspective.client.perspective.Perspective;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.zoom.Zoom;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class Tick {
    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (ClientData.CLIENT.isFinishedLoading()) {
                ConfigHelper.tick(client);
                HUD.tick();
                AprilFoolsPrank.tick(client);
                Keybindings.tick(client);
                Perspective.tick(client);
                Zoom.tick(client);
                Shader.tick(client);
                Panorama.tick(client);
                HUDOverlays.tick(client);
                Hide.tick(client);
                UpdateChecker.tick(client);
            }
        });
    }
}