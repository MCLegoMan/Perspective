/*
    Perspective
    Contributor(s): Nettakrim, MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.shaders;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.shaders.Shader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = WorldRenderer.class)
public abstract class WorldRendererMixin {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/PostEffectProcessor;render(F)V", ordinal = 0), method = "render")
	public void perspective$saveDepthOutlines(float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
		Shader.depthFramebuffer.copyDepthFrom(ClientData.minecraft.getFramebuffer());
		for (Framebuffer framebuffer : Shader.entityDepthFramebuffer) framebuffer.copyDepthFrom(ClientData.minecraft.getFramebuffer());
		ClientData.minecraft.getFramebuffer().beginWrite(false);
	}
	@Inject(at = {
			@At(value = "INVOKE", target = "Lnet/minecraft/client/gl/PostEffectProcessor;render(F)V", ordinal = 1),
			@At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderWorldBorder(Lnet/minecraft/client/render/Camera;)V", ordinal = 1)
	}, method = "render")
	public void perspective$saveDepth(float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
		Shader.depthFramebuffer.copyDepthFrom(ClientData.minecraft.getFramebuffer());
		for (Framebuffer framebuffer : Shader.entityDepthFramebuffer) framebuffer.copyDepthFrom(ClientData.minecraft.getFramebuffer());
		if (ClientData.minecraft.options.getGraphicsMode().getValue().getId() <= GraphicsMode.FANCY.getId()) ClientData.minecraft.getFramebuffer().beginWrite(false);
	}
	@Inject(at = {
			@At(value = "INVOKE", target = "Lnet/minecraft/client/gl/PostEffectProcessor;render(F)V", ordinal = 1, shift = At.Shift.AFTER),
			@At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderWorldBorder(Lnet/minecraft/client/render/Camera;)V", ordinal = 1, shift = At.Shift.AFTER)
	}, method = "render")
	public void perspective$renderGameExperimental(float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
		try {
			if (Shader.shouldUseDepthGameRenderer()) {
				if (!ClientData.minecraft.gameRenderer.isRenderingPanorama()) {
					// This could possibly be changed to have isFirstPerson configurable (first person / all perspective)??.
					if (Shader.shouldRenderEntityLinkShader() && ClientData.minecraft.options.getPerspective().isFirstPerson()) {
						for (PostEffectProcessor postProcessor : Shader.entityPostProcessor) {
							Shader.render(postProcessor, tickDelta);
						}
					}
					if (Shader.shouldRenderShader()) Shader.render(Shader.postProcessor, tickDelta, "world_renderer:game");
				}
			}
		} catch (Exception error) {
		}
	}
	@Inject(at = {
			@At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderLayer(Lnet/minecraft/client/render/RenderLayer;DDDLorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", ordinal = 3),
			@At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderLayer(Lnet/minecraft/client/render/RenderLayer;DDDLorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", ordinal = 5)
	}, method = "render")
	public void perspective$renderLayer(float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
		if (Shader.shouldRenderShader()) {
			if (Shader.translucentFramebuffer != null) {
				Shader.translucentFramebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
				Shader.translucentFramebuffer.copyDepthFrom(ClientData.minecraft.getFramebuffer());
			}
			if (Shader.entityFramebuffer != null) {
				Shader.entityFramebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
				Shader.entityFramebuffer.copyDepthFrom(ClientData.minecraft.getFramebuffer());
			}
			if (Shader.particlesFramebuffer != null) {
				Shader.particlesFramebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
				Shader.particlesFramebuffer.copyDepthFrom(ClientData.minecraft.getFramebuffer());
			}
			if (Shader.weatherFramebuffer != null) {
				Shader.weatherFramebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
				Shader.weatherFramebuffer.copyDepthFrom(ClientData.minecraft.getFramebuffer());
			}
			if (Shader.cloudsFramebuffer != null) {
				Shader.cloudsFramebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
				Shader.cloudsFramebuffer.copyDepthFrom(ClientData.minecraft.getFramebuffer());
			}
		}
		if (Shader.shouldRenderEntityLinkShader()) {
			if (!Shader.entityTranslucentFramebuffer.isEmpty()) {
				for (Framebuffer framebuffer : Shader.entityTranslucentFramebuffer) {
					if (framebuffer != null) {
						framebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
						framebuffer.copyDepthFrom(ClientData.minecraft.getFramebuffer());
					}
				}
			}
			if (!Shader.entityEntityFramebuffer.isEmpty()) {
				for (Framebuffer framebuffer : Shader.entityEntityFramebuffer) {
					if (framebuffer != null) {
						framebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
						framebuffer.copyDepthFrom(ClientData.minecraft.getFramebuffer());
					}
				}
			}
			if (!Shader.entityParticlesFramebuffer.isEmpty()) {
				for (Framebuffer framebuffer : Shader.entityParticlesFramebuffer) {
					if (framebuffer != null) {
						framebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
						framebuffer.copyDepthFrom(ClientData.minecraft.getFramebuffer());
					}
				}
			}
			if (!Shader.entityWeatherFramebuffer.isEmpty()) {
				for (Framebuffer framebuffer : Shader.entityWeatherFramebuffer) {
					if (framebuffer != null) {
						framebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
						framebuffer.copyDepthFrom(ClientData.minecraft.getFramebuffer());
					}
				}
			}
			if (!Shader.entityCloudsFramebuffer.isEmpty()) {
				for (Framebuffer framebuffer : Shader.entityCloudsFramebuffer) {
					if (framebuffer != null) {
						framebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
						framebuffer.copyDepthFrom(ClientData.minecraft.getFramebuffer());
					}
				}
			}
		}
	}
	// Panorama Shader Renderer
	@Inject(method = "render", at = @At(value = "RETURN"))
	private void perspective$renderShaderPanorama(float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
		if (ClientData.minecraft.gameRenderer.isRenderingPanorama()) {
			if (Shader.shouldRenderShader()) {
				Shader.render(Shader.postProcessor, tickDelta, "game:panorama");
			}
		}
	}
}