/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.shaders;

import com.mclegoman.luminance.client.shaders.ShaderRenderEvents;
import com.mclegoman.luminance.client.shaders.UniformHelper;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;

public class SmoothUniforms extends Uniforms {
	public static float prevZoom = (float) Zoom.getMultiplier();
	public static float zoom = (float) Zoom.getMultiplier();
	public static void init() {
		ShaderRenderEvents.ShaderUniform.registerFloat(Data.version.getID(), "zoomMultiplierSmooth", () -> UniformHelper.getSmooth(prevZoom, zoom));
	}
	public static void tick() {
		prevZoom = zoom;
		zoom = (prevZoom + (float) Zoom.getMultiplier()) * 0.5F;
	}
}
