/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.shaders;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;

public class ShaderUniforms {
	public static void tick() {
		updateSmoothFogColor();
		updateSmoothLight();
	}
	// uniform float minecraftFOV;
	public static float getFOV() {
		return ClientData.CLIENT.options.getFov().getValue() != null ? ClientData.CLIENT.options.getFov().getValue() : 70.0F;
	}
	// uniform float minecraftRenderDistance;
	public static float getRenderDistance() {
		return ClientData.CLIENT.options.getViewDistance().getValue() != null ? ClientData.CLIENT.options.getViewDistance().getValue() : 12.0F;
	}
	// uniform float perspectiveZoomMultiplier;
	public static float getZoomMultiplier() {
		return (float) Zoom.zoomMultiplier;
	}
	// uniform float minecraftFogColor;
	public static Vec3d getFogColor() {
		if (ClientData.CLIENT.world != null && ClientData.CLIENT.player != null) {
			return Vec3d.unpackRgb(ClientData.CLIENT.world.getBiome(ClientData.CLIENT.player.getBlockPos()).value().getFogColor());
		}
		return Vec3d.ZERO;
	}
	// uniform float perspectiveSmoothFogColor;
	public static Vec3d smoothFogColor = Vec3d.ZERO;
	public static void updateSmoothFogColor() {
		if (ClientData.CLIENT.world != null && ClientData.CLIENT.player != null) {
			smoothFogColor = new Vec3d((float) MathHelper.lerp(ClientData.CLIENT.getTickDelta(), smoothFogColor.getX(), getFogColor().getX()), (float) MathHelper.lerp(ClientData.CLIENT.getTickDelta(), smoothFogColor.getY(), getFogColor().getY()), (float) MathHelper.lerp(ClientData.CLIENT.getTickDelta(), smoothFogColor.getZ(), getFogColor().getZ()));
		}
	}
	public static Vec3d getSmoothFogColor() {
		return smoothFogColor;
	}
	// uniform float perspectiveLight;
	public static float getLight() {
		if (ClientData.CLIENT.world != null && ClientData.CLIENT.player != null) {
			boolean seeSky = ClientData.CLIENT.world.isSkyVisible(ClientData.CLIENT.player.getBlockPos());
			float blockLightLevel = ClientData.CLIENT.world.getLightLevel(LightType.BLOCK, ClientData.CLIENT.player.getBlockPos());
			float skyLightLevel = ClientData.CLIENT.world.getSkyBrightness(ClientData.CLIENT.getTickDelta()) * 15;
			return Math.min(seeSky ? Math.max(blockLightLevel, skyLightLevel) : blockLightLevel, 15);
		}
		return 15.0F;
	}
	// uniform float perspectiveSmoothLight;
	public static float smoothLight = 15.0F;
	public static void updateSmoothLight() {
		smoothLight = MathHelper.lerp(ClientData.CLIENT.getTickDelta(), smoothLight, getLight());
	}
	public static float getSmoothLight() {
		return smoothLight;
	}
}
