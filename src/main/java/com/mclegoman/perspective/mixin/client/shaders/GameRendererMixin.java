/*
    Perspective
    Contributor(s): MCLegoMan, Nettakrim
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.shaders;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.shaders.ShaderDataLoader;
import com.mclegoman.perspective.client.ui.UIBackground;
import com.mclegoman.perspective.config.ConfigHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(priority = 100, value = GameRenderer.class)
public abstract class GameRendererMixin {
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;beginWrite(Z)V"))
	public void perspective$renderGame(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
		if (!ClientData.CLIENT.gameRenderer.isRenderingPanorama()) {
			if (Shader.shouldRenderShader() && (String.valueOf(ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_mode")).equalsIgnoreCase("game") || Shader.shouldDisableScreenMode())) {
				if (!((boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.EXPERIMENTAL, "improved_shader_renderer") && Shader.useDepth)) {
					Shader.render(Shader.postProcessor, tickDelta, "game");
				}
			}
		}
	}
	@Inject(method = "render", at = @At("TAIL"))
	private void perspective$renderScreen(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
		if (!ClientData.CLIENT.gameRenderer.isRenderingPanorama()) {
			if (Shader.shouldRenderShader() && String.valueOf(ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_mode")).equalsIgnoreCase("screen") && !Shader.shouldDisableScreenMode())
				Shader.render(Shader.postProcessor, tickDelta, "screen");
		}
	}
	@Inject(method = "onResized", at = @At(value = "TAIL"))
	private void perspective$onResized(int width, int height, CallbackInfo ci) {
		if (Shader.postProcessor != null) {
			Shader.postProcessor.setupDimensions(width, height);
		}
		if (Shader.entityPostProcessor != null) {
			for (PostEffectProcessor postProcessor : Shader.entityPostProcessor) {
				postProcessor.setupDimensions(width, height);
			}
		}
		if (UIBackground.Gaussian.postProcessor != null) {
			UIBackground.Gaussian.postProcessor.setupDimensions(width, height);
		}
		if (Shader.depthFramebuffer == null) {
			Shader.depthFramebuffer = new SimpleFramebuffer(width, height, true, MinecraftClient.IS_SYSTEM_MAC);
		} else {
			Shader.depthFramebuffer.resize(width, height, MinecraftClient.IS_SYSTEM_MAC);
		}
		if (Shader.entityDepthFramebuffer == null) {
			Shader.entityDepthFramebuffer = new ArrayList<>();
			for (List<Object> ignored : ShaderDataLoader.entityLinkRegistry) {
				Shader.entityDepthFramebuffer.add(new SimpleFramebuffer(width, height, true, MinecraftClient.IS_SYSTEM_MAC));
			}
		} else {
			for (Framebuffer framebuffer : Shader.entityDepthFramebuffer) {
				framebuffer.resize(width, height, MinecraftClient.IS_SYSTEM_MAC);
			}
		}
	}
}