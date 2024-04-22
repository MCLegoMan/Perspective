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
		public static final Map<Couple<String, String>, Callable<Float>> registry = new HashMap<>();
		public static final Map<Couple<String, String>, Callable<float[]>> registryArray = new HashMap<>();
		public static void registerFloat(String modId, String uniform, Callable<Float> callable) {
			Couple<String, String> couple = new Couple<>(modId, uniform);
			try {
				add(couple, callable);
			} catch (Exception error) {
				Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to register shader uniform: {}_{}: {}", couple.getFirst(), couple.getSecond(), error));
			}
		}
		public static void registerFloats(String modId, String uniform, Callable<float[]> callable) {
			Couple<String, String> couple = new Couple<>(modId, uniform);
			try {
				addArray(couple, callable);
			} catch (Exception error) {
				Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to register shader uniform: {}_{}: {}", couple.getFirst(), couple.getSecond(), error));
			}
		}
		private static void add(Couple<String, String> shader, Callable<Float> callable) {
			if (!registry.containsKey(shader)) registry.put(shader, callable);
		}
		private static void addArray(Couple<String, String> shader, Callable<float[]> callable) {
			if (!registryArray.containsKey(shader)) registryArray.put(shader, callable);
		}
	}
	public interface ShaderRunnable {
		default void run(JsonEffectShaderProgram program) {}
	}
}
