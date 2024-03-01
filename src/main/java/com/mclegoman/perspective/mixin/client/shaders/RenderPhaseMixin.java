/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.shaders;

import com.mclegoman.perspective.client.shaders.Shader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = RenderPhase.class, priority = 100)
public abstract class RenderPhaseMixin {
	@Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(Ljava/lang/String;Ljava/lang/Runnable;Ljava/lang/Runnable;)Lnet/minecraft/client/render/RenderPhase$Target;", ordinal = 2))
	private static RenderPhase.Target perspective$overrideTargets(String string, Runnable runnable, Runnable runnable2) {
		switch (string) {
			case ("translucent_target") -> {
				return new RenderPhase.Target("translucent_target", () -> {
					if (MinecraftClient.isFabulousGraphicsOrBetter()) {
						MinecraftClient.getInstance().worldRenderer.getTranslucentFramebuffer().beginWrite(false);
					}
					if (Shader.shouldRenderShader()) {
						if (Shader.translucentFramebuffer != null) Shader.translucentFramebuffer.beginWrite(false);
					}
				}, () -> {
					if (MinecraftClient.isFabulousGraphicsOrBetter() || Shader.shouldRenderShader()) {
						MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
					}
				});
			}
			case ("item_entity_target") -> {
				return new RenderPhase.Target("item_entity_target", () -> {
					if (MinecraftClient.isFabulousGraphicsOrBetter()) {
						MinecraftClient.getInstance().worldRenderer.getEntityFramebuffer().beginWrite(false);
					}
					if (Shader.shouldRenderShader()) {
						if (Shader.entityFramebuffer != null) Shader.entityFramebuffer.beginWrite(false);
					}
				}, () -> {
					if (MinecraftClient.isFabulousGraphicsOrBetter() || Shader.shouldRenderShader()) {
						MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
					}
				});
			}
			case ("particles_target") -> {
				return new RenderPhase.Target("particles_target", () -> {
					if (MinecraftClient.isFabulousGraphicsOrBetter()) {
						MinecraftClient.getInstance().worldRenderer.getParticlesFramebuffer().beginWrite(false);
					}
					if (Shader.shouldRenderShader()) {
						if (Shader.particlesFramebuffer != null) Shader.particlesFramebuffer.beginWrite(false);
					}
				}, () -> {
					if (MinecraftClient.isFabulousGraphicsOrBetter() || Shader.shouldRenderShader()) {
						MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
					}
				});
			}
			case ("weather_target") -> {
				return new RenderPhase.Target("weather_target", () -> {
					if (MinecraftClient.isFabulousGraphicsOrBetter()) {
						MinecraftClient.getInstance().worldRenderer.getWeatherFramebuffer().beginWrite(false);
					}
					if (Shader.shouldRenderShader()) {
						if (Shader.weatherFramebuffer != null) Shader.weatherFramebuffer.beginWrite(false);
					}
				}, () -> {
					if (MinecraftClient.isFabulousGraphicsOrBetter() || Shader.shouldRenderShader()) {
						MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
					}
				});
			}
			case ("clouds_target") -> {
				return new RenderPhase.Target("clouds_target", () -> {
					if (MinecraftClient.isFabulousGraphicsOrBetter()) {
						MinecraftClient.getInstance().worldRenderer.getCloudsFramebuffer().beginWrite(false);
					}
					if (Shader.shouldRenderShader()) {
						if (Shader.cloudsFramebuffer != null) Shader.cloudsFramebuffer.beginWrite(false);
					}
				}, () -> {
					if (MinecraftClient.isFabulousGraphicsOrBetter() || Shader.shouldRenderShader()) {
						MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
					}
				});
			}
			default -> {
				return new RenderPhase.Target(string, runnable, runnable2);
			}
		}
	}
}