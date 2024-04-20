/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.shaders;

import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gl.JsonEffectShaderProgram;

public class Uniforms extends com.mclegoman.luminance.client.shaders.Uniforms {
	public static void update(JsonEffectShaderProgram program) {
		setUniform(program, getUniformName(Data.version.getID(), "zoomMultiplier"), (float) Zoom.getMultiplier());
	}
}
