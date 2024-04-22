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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ShaderRenderEvents {
	public static class BeforeRender {
		public static final Map<Couple<String, String>, Callable<Float>> registry = new HashMap<>();
		public static final Map<Couple<String, String>, Callable<float[]>> registryArray = new HashMap<>();
		public static void register(String id, String uniform, Callable<?> callable) {
			Couple<String, String> uniformId = new Couple<>(id, uniform);
			try {
				if (callable.call() instanceof float[]) addArray(uniformId, (Callable<float[]>) callable);
				else if (callable.call() instanceof Float) add(uniformId, (Callable<Float>) callable);
			} catch (Exception error) {
				Data.version.sendToLog(Helper.LogType.ERROR, Translation.getString("Failed to register shader uniform: {}: {}", uniformId, error));
			}
		}
		private static void add(Couple<String, String> shader, Callable<Float> callable) {
			if (!registry.containsKey(shader)) registry.put(shader, callable);
		}
		private static void addArray(Couple<String, String> shader, Callable<float[]> callable) {
			if (!registryArray.containsKey(shader)) registryArray.put(shader, callable);
		}
	}
}
