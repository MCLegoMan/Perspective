/*
    Perspective
    Contributor(s): Nettakrim, MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.shaders;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.config.ConfigHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.render.*;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = WorldRenderer.class)
public abstract class WorldRendererMixin {
	@Inject(at = {
			@At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderLayer(Lnet/minecraft/client/render/RenderLayer;DDDLorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", ordinal = 3),
			@At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderLayer(Lnet/minecraft/client/render/RenderLayer;DDDLorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", ordinal = 5)
	}, method = "render")
	public void perspective$renderLayer(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
		if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.NORMAL, "super_secret_settings_enabled")) {
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
		if (Shader.shouldUseEntityLink()) {
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
}