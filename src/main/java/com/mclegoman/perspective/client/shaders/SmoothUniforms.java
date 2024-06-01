/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.shaders;

import com.mclegoman.luminance.client.events.Events;
import com.mclegoman.luminance.client.shaders.Shaders;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;

public class SmoothUniforms extends Uniforms {
	public static float prevZoom = (float) Zoom.getMultiplier();
	public static float zoom = (float) Zoom.getMultiplier();
	public static void init() {
		Events.ShaderUniform.registerFloat(Data.version.getID(), "zoomMultiplierSmooth", (tickDelta) -> Shaders.getSmooth(tickDelta, prevZoom, zoom));
	}
	public static void tick() {
		prevZoom = zoom;
		zoom = (prevZoom + (float) Zoom.getMultiplier()) * 0.5F;
	}
}
