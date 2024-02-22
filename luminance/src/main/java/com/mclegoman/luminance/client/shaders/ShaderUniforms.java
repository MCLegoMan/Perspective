/*
    Luminance
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.luminance.client.shaders;

import com.mclegoman.luminance.client.data.ClientData;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import org.joml.Vector3f;

public class ShaderUniforms {
	protected static float lu_FOV;
	protected static float lu_RenderDistance;
	protected static Vector3f lu_PlayerPos;
	protected static float lu_PlayerPitch;
	protected static float lu_PlayerYaw;
	protected static float lu_BlockLight;
	protected static float lu_SkyLight;
	protected static Vector3f lu_FogColor;
	protected static float lu_SubmergedInWater;
	protected static Vector3f lu_WaterFogColor;
	public static void tick() {
		try {
			updateFOV();
			updateRenderDistance();
			updatePlayerPos();
			updatePlayerPitch();
			updatePlayerYaw();
			updateBlockLight();
			updateSkyLight();
			updateFogColor();
			updateSubmergedInWater();
			updateWaterFogColor();
			SmoothShaderUniforms.tick();
		} catch (Exception ignored) {}
	}
	public static void setUniforms(JsonEffectShaderProgram program) {
		try {
			setUniform(program, "lu_FOV", lu_FOV);
			setUniform(program, "lu_RenderDistance", lu_RenderDistance);
			setUniform(program, "lu_PlayerPos", lu_PlayerPos);
			setUniform(program, "lu_PlayerPitch", lu_PlayerPitch);
			setUniform(program, "lu_PlayerYaw", lu_PlayerYaw);
			setUniform(program, "lu_BlockLight", lu_BlockLight);
			setUniform(program, "lu_SkyLight", lu_SkyLight);
			setUniform(program, "lu_FogColor", lu_FogColor);
			setUniform(program, "lu_SubmergedInWater", lu_SubmergedInWater);
			setUniform(program, "lu_WaterFogColor", lu_WaterFogColor);
			SmoothShaderUniforms.setUniforms(program);
		} catch (Exception ignored) {}
	}
	protected static void updateFOV() {
		lu_FOV = ClientData.client.options.getFov().getValue() != null ? ClientData.client.options.getFov().getValue() : 70.0F;
	}
	protected static void updateRenderDistance() {
		lu_RenderDistance = ClientData.client.options.getViewDistance().getValue() != null ? ClientData.client.options.getViewDistance().getValue() : 12.0F;
	}
	protected static void updatePlayerPos() {
		lu_PlayerPos = ClientData.client.player != null ? ClientData.client.player.getPos().toVector3f() : Vec3d.ZERO.toVector3f();
	}
	protected static void updatePlayerPitch() {
		lu_PlayerPitch = ClientData.client.player != null ? ClientData.client.player.getPitch() : 90.0F;
	}
	protected static void updatePlayerYaw() {
		lu_PlayerYaw = ClientData.client.player != null ? ClientData.client.player.getYaw() : 0.0F;
	}
	protected static void updateBlockLight() {
		lu_BlockLight = ClientData.client.world != null && ClientData.client.player != null ? ClientData.client.world.getLightLevel(LightType.BLOCK, ClientData.client.player.getBlockPos()) : 15.0F;
	}
	protected static void updateSkyLight() {
		lu_SkyLight = ClientData.client.world != null && ClientData.client.player != null ? ClientData.client.world.getLightLevel(LightType.SKY, ClientData.client.player.getBlockPos()) : 15.0F;
	}
	protected static void updateFogColor() {
		lu_FogColor = (ClientData.client.world != null && ClientData.client.player != null) ? Vec3d.unpackRgb(ClientData.client.world.getBiome(ClientData.client.player.getBlockPos()).value().getFogColor()).toVector3f() : Vec3d.ZERO.toVector3f();
	}
	protected static void updateSubmergedInWater() {
		lu_SubmergedInWater = ClientData.client.player != null && ClientData.client.player.isSubmergedInWater() ? 1.0F : 0.0F;
	}
	protected static void updateWaterFogColor() {
		lu_WaterFogColor = (ClientData.client.world != null && ClientData.client.player != null) ? Vec3d.unpackRgb(ClientData.client.world.getBiome(ClientData.client.player.getBlockPos()).value().getWaterFogColor()).toVector3f() : Vec3d.ZERO.toVector3f();
	}
	protected static void setUniform(JsonEffectShaderProgram program, String uniformName, float... value) {
		try {
			program.getUniformByNameOrDummy(uniformName).set(value);
		} catch (Exception ignored) {}
	}
	protected static void setUniform(JsonEffectShaderProgram program, String uniformName, Vector3f value) {
		try {
			program.getUniformByNameOrDummy(uniformName).set(value);
		} catch (Exception ignored) {}
	}
	static {
		lu_FOV = 70.0F;
		lu_RenderDistance = 12.0F;
		lu_PlayerPos = Vec3d.ZERO.toVector3f();
		lu_PlayerPitch = 90.0F;
		lu_PlayerYaw = 0.0F;
		lu_BlockLight = 15.0F;
		lu_SkyLight = 15.0F;
		lu_FogColor = Vec3d.ZERO.toVector3f();
		lu_SubmergedInWater = 0.0F;
		lu_WaterFogColor = Vec3d.ZERO.toVector3f();
	}
}
