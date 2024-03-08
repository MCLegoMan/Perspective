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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Higher Priority - This should allow other mods (such as Iris Shaders) to override the hand renderer.
@Mixin(priority = 10000, value = GameRenderer.class)
public abstract class HandRendererFix {
	@Shadow public abstract void renderHand(Camera camera, float tickDelta);
	@Shadow public boolean renderHand;
	@Redirect(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;renderHand(Lnet/minecraft/client/render/Camera;F)V"))
	private void perspective$disableHandRendering(GameRenderer gameRenderer, Camera camera, float tickDelta) {
		if ((boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.EXPERIMENTAL, "override_hand_renderer")) {
			if (Shader.shouldRenderShader()) return;
		}
		this.renderHand(camera, tickDelta);
	}
	// This ideally should be in WorldRenderer. However I am currently having trouble getting the hand to follow the camera.
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;beginWrite(Z)V", shift = At.Shift.AFTER))
	private void perspective$renderHand(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
		if ((boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.EXPERIMENTAL, "override_hand_renderer")) {
			if (Shader.shouldRenderShader()) {
				if (this.renderHand) {
					RenderSystem.clear(256, MinecraftClient.IS_SYSTEM_MAC);
					this.renderHand(ClientData.CLIENT.gameRenderer.getCamera(), tickDelta);
				}
			}
		}
	}
}