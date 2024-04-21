/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.shaders;

import com.mclegoman.luminance.client.data.ClientData;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.util.math.MathHelper;

public class SmoothUniforms extends Uniforms {
	public static float prevZoom = getViewDistance();
	public static float zoom = getViewDistance();
	public static void init() {
	}
	public static void tick() {
		prevZoom = zoom;
		zoom = (prevZoom + getZoomMultiplier()) * 0.5F;
	}
	public static void update(JsonEffectShaderProgram program) {
		float tickDelta = ClientData.minecraft.getTickDelta();
		setUniform(program, Data.version.getID(), "zoomMultiplierSmooth", MathHelper.lerp(tickDelta, prevZoom, zoom));
	}
}
