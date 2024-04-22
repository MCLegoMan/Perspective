/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.shaders;

import com.mclegoman.luminance.client.shaders.ShaderRenderEvents;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;

public class Uniforms {
	public static void init() {
		SmoothUniforms.init();
		ShaderRenderEvents.BeforeRender.register(Data.version.getID(), "zoomMultiplier", Uniforms::getZoomMultiplier);
	}
	public static void tick() {
		SmoothUniforms.tick();
	}
	public static float getZoomMultiplier() {
		return (float) Zoom.getMultiplier();
	}
}
