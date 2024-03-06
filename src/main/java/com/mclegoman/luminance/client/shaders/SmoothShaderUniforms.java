/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.shaders;

import com.mclegoman.luminance.client.data.ClientData;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class SmoothShaderUniforms {
	protected static float lu_SmoothBlockLight;
	protected static float lu_SmoothSkyLight;
	protected static Vector3f lu_SmoothFogColor;
	protected static float lu_SmoothSubmergedInWater;
	protected static Vector3f lu_SmoothWaterFogColor;
	protected static void tick() {
		try {
			updateSmoothBlockLight();
			updateSmoothSkyLight();
			updateSmoothFogColor();
			updateSmoothSubmergedInWater();
			updateSmoothWaterFogColor();
		} catch (Exception ignored) {}
	}
	public static void setUniforms(JsonEffectShaderProgram program) {
		try {
			ShaderUniforms.setUniform(program, "lu_SmoothBlockLight", lu_SmoothBlockLight);
			ShaderUniforms.setUniform(program, "lu_SmoothSkyLight", lu_SmoothSkyLight);
			ShaderUniforms.setUniform(program, "lu_SmoothFogColor", lu_SmoothFogColor);
			ShaderUniforms.setUniform(program, "lu_SmoothSubmergedInWater", lu_SmoothSubmergedInWater);
			ShaderUniforms.setUniform(program, "lu_SmoothWaterFogColor", lu_SmoothFogColor);
		} catch (Exception ignored) {}
	}
	protected static void updateSmoothBlockLight() {
		lu_SmoothBlockLight = MathHelper.lerp(ClientData.CLIENT.getTickDelta(), lu_SmoothBlockLight, ShaderUniforms.lu_BlockLight);
	}
	protected static void updateSmoothSkyLight() {
		lu_SmoothSkyLight = MathHelper.lerp(ClientData.CLIENT.getTickDelta(), lu_SmoothSkyLight, ShaderUniforms.lu_SkyLight);
	}
	protected static void updateSmoothFogColor() {
		lu_SmoothFogColor = new Vec3d(MathHelper.lerp(ClientData.CLIENT.getTickDelta(), lu_SmoothFogColor.x, ShaderUniforms.lu_FogColor.x), MathHelper.lerp(ClientData.CLIENT.getTickDelta(), lu_SmoothFogColor.y, ShaderUniforms.lu_FogColor.y), MathHelper.lerp(ClientData.CLIENT.getTickDelta(), lu_SmoothFogColor.z, ShaderUniforms.lu_FogColor.z)).toVector3f();
	}
	protected static void updateSmoothSubmergedInWater() {
		lu_SmoothSubmergedInWater = MathHelper.lerp(ClientData.CLIENT.getTickDelta(), lu_SmoothSubmergedInWater, ShaderUniforms.lu_SubmergedInWater);
	}
	protected static void updateSmoothWaterFogColor() {
		lu_SmoothWaterFogColor = new Vec3d(MathHelper.lerp(ClientData.CLIENT.getTickDelta(), lu_SmoothWaterFogColor.x, ShaderUniforms.lu_WaterFogColor.x), MathHelper.lerp(ClientData.CLIENT.getTickDelta(), lu_SmoothWaterFogColor.y, ShaderUniforms.lu_WaterFogColor.y), MathHelper.lerp(ClientData.CLIENT.getTickDelta(), lu_SmoothWaterFogColor.z, ShaderUniforms.lu_WaterFogColor.z)).toVector3f();
	}
	static {
		lu_SmoothBlockLight = ShaderUniforms.lu_BlockLight;
		lu_SmoothSkyLight = ShaderUniforms.lu_SkyLight;
		lu_SmoothFogColor = ShaderUniforms.lu_FogColor;
		lu_SmoothSubmergedInWater = ShaderUniforms.lu_SubmergedInWater;
		lu_SmoothWaterFogColor = ShaderUniforms.lu_WaterFogColor;
	}
}
