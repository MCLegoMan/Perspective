/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.shaders;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.zoom.Zoom;

public class ShaderUniforms {
	// uniform float minecraftFOV;
	public static float getFOV() {
		return ClientData.CLIENT.options.getFov().getValue() != null ? ClientData.CLIENT.options.getFov().getValue() : 70.0F;
	}
	// uniform float minecraftRenderDistance;
	public static float getRenderDistance() {
		return ClientData.CLIENT.options.getViewDistance().getValue() != null ? ClientData.CLIENT.options.getViewDistance().getValue() : 12.0F;
	}
	// uniform float perspectiveZoomMultiplier;
	public static float getZoomMultiplier() {
		return (float) Zoom.zoomMultiplier;
	}
}
