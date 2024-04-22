/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.shaders;

import com.mclegoman.luminance.client.translation.Translation;
import com.mclegoman.luminance.common.data.Data;
import com.mclegoman.luminance.common.util.Couple;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class ShaderRenderEvents {
	public static class BeforeRender {
		public static final List<ShaderRunnable> registry = new ArrayList<>();
		public static void register(ShaderRunnable runnable) {
			try {
				add(runnable);
			} catch (Exception error) {
				Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to register BeforeRender shader event: {}", error));
			}
		}
		private static void add(ShaderRunnable runnable) {
			if (!registry.contains(runnable)) registry.add(runnable);
		}
	}
	public static class ShaderUniform {
		public static final Map<Couple<String, String>, Callable<Float>> registryFloat = new HashMap<>();
		public static final Map<Couple<String, String>, Callable<float[]>> registryFloatArray = new HashMap<>();
		public static final Map<Couple<String, String>, Callable<Vector3f>> registryVector3f = new HashMap<>();
		public static void registerFloat(String modId, String uniform, Callable<Float> callable) {
			Couple<String, String> couple = new Couple<>(modId, uniform);
			try {
				addFloat(couple, callable);
			} catch (Exception error) {
				Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to register shader uniform: {}_{}: {}", couple.getFirst(), couple.getSecond(), error));
			}
		}
		public static void registerFloats(String modId, String uniform, Callable<float[]> callable) {
			Couple<String, String> couple = new Couple<>(modId, uniform);
			try {
				addFloatArray(couple, callable);
			} catch (Exception error) {
				Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to register shader uniform: {}_{}: {}", couple.getFirst(), couple.getSecond(), error));
			}
		}
		public static void registerVector3f(String modId, String uniform, Callable<Vector3f> callable) {
			Couple<String, String> couple = new Couple<>(modId, uniform);
			try {
				addVector3f(couple, callable);
			} catch (Exception error) {
				Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to register shader uniform: {}_{}: {}", couple.getFirst(), couple.getSecond(), error));
			}
		}
		private static void addFloat(Couple<String, String> shader, Callable<Float> callable) {
			if (!registryFloat.containsKey(shader)) registryFloat.put(shader, callable);
		}
		private static void addFloatArray(Couple<String, String> shader, Callable<float[]> callable) {
			if (!registryFloatArray.containsKey(shader)) registryFloatArray.put(shader, callable);
		}
		private static void addVector3f(Couple<String, String> shader, Callable<Vector3f> callable) {
			if (!registryVector3f.containsKey(shader)) registryVector3f.put(shader, callable);
		}
	}
	public interface ShaderRunnable {
		default void run(JsonEffectShaderProgram program) {}
	}
}
