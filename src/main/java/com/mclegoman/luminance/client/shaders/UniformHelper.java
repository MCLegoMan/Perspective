/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.shaders;

import com.mclegoman.luminance.client.data.ClientData;
import com.mclegoman.luminance.client.translation.Translation;
import com.mclegoman.luminance.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.Uniform;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

public class UniformHelper {
	public static float time = 3455800.0F;
	public static void init() {
		ShaderRenderEvents.BeforeRender.register(new ShaderRenderEvents.ShaderRunnable() {
			public void run(JsonEffectShaderProgram program) {
				ShaderRenderEvents.ShaderUniform.registryFloat.forEach((uniform, callable) -> {
					try {
						set(program, uniform.first(), uniform.second(), callable.call());
					} catch (Exception error) {
						Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to set shader uniforms: {}", error));
					}
				});
				ShaderRenderEvents.ShaderUniform.registryFloatArray.forEach((uniform, callable) -> {
					try {
						set(program, uniform.first(), uniform.second(), callable.call());
					} catch (Exception error) {
						Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to set shader uniforms: {}", error));
					}
				});
				ShaderRenderEvents.ShaderUniform.registryVector3f.forEach((uniform, callable) -> {
					try {
						set(program, uniform.first(), uniform.second(), callable.call());
					} catch (Exception error) {
						Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to set shader uniforms: {}", error));
					}
				});
			}
		});
	}
	public static void updateTime() {
		// This will get reset every 48 hours to prevent shader stuttering/freezing on some shaders.
		// This may still stutter/freeze on weaker systems.
		// This was tested using i5-11400@2.60GHz/8GB Allocated(of 32GB RAM)/RTX3050(31.0.15.5212).
		time = (time + 1.00F) % 3456000.0F;
	}
	public static float getSmooth(float prev, float current) {
		float tickDelta = ClientData.minecraft.getTickDelta();
		return MathHelper.lerp(tickDelta, prev, current);
	}
	public static float[] getSmooth(float[] prev, float[] current) {
		if (prev.length == current.length) {
			return new float[]{getSmooth(prev[0], current[0]), getSmooth(prev[1], current[1]), getSmooth(prev[2], current[2])};
		}
		return new float[]{};
	}
	public static Uniform getUniform(JsonEffectShaderProgram program, String prefix, String uniformName) {
		return program.getUniformByNameOrDummy(getUniformName(prefix, uniformName));
	}
	public static String getUniformName(String prefix, String uniformName) {
		return prefix + "_" + uniformName;
	}
	public static void set(JsonEffectShaderProgram program, String prefix, String uniformName, float... values) {
		try {
			getUniform(program, prefix, uniformName).set(values);
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to set shader uniform: {}_{}: {}", prefix, uniformName, error));
		}
	}
	public static void set(JsonEffectShaderProgram program, String prefix, String uniformName, Vector3f values) {
		try {
			getUniform(program, prefix, uniformName).set(values);
		} catch (Exception error) {
			Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to set shader uniform: {}_{}: {}", prefix, uniformName, error));
		}
	}
}
