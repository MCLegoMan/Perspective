/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.shaders;

import com.mclegoman.luminance.client.data.ClientData;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

public class UniformHelper {
	public static float time = 3455800.0F;
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
	public static Vector3f getSmooth(Vector3f prev, Vector3f current) {
		return new Vector3f(getSmooth(prev.x, current.x), getSmooth(prev.y, current.y), getSmooth(prev.z, current.z));
	}
}
