/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.shaders;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Higher Priority - This should allow other mods (such as Iris Shaders) to override the hand renderer.
@Mixin(priority = 10000, value = GameRenderer.class)
public abstract class GameRendererShaderHandFix {
	@Unique
	private Camera cam;
	@Unique
	private float td;
	@Unique
	private Matrix4f m4f;

	@Redirect(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;renderHand(Lnet/minecraft/client/render/Camera;FLorg/joml/Matrix4f;)V"))
	private void perspective$handRendering(GameRenderer instance, Camera camera, float tickDelta, Matrix4f matrix4f) {
		if ((boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.EXPERIMENTAL, "override_hand_renderer")) {
			cam = camera;
			td = tickDelta;
			m4f = matrix4f;
			if (Shader.shouldRenderShader()) return;
		}
		ClientData.CLIENT.gameRenderer.renderHand(camera, tickDelta, matrix4f);
	}
	// This ideally should be in WorldRenderer. However I am currently having trouble getting the hand to follow the camera.
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;beginWrite(Z)V", shift = At.Shift.AFTER))
	private void perspective$renderHand(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
		if ((boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.EXPERIMENTAL, "override_hand_renderer")) {
			if (Shader.shouldRenderShader()) {
				if (ClientData.CLIENT.gameRenderer.renderHand) {
					if (cam != null && m4f != null) {
						RenderSystem.clear(256, MinecraftClient.IS_SYSTEM_MAC);
						ClientData.CLIENT.gameRenderer.renderHand(cam, td, m4f);
					}
				}
			}
		}
	}
}