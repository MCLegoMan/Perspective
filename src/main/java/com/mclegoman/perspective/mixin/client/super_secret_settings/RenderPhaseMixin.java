/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.super_secret_settings;

import com.mclegoman.perspective.client.shaders.Shader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderPhase.class, priority = 100)
public abstract class RenderPhaseMixin {
	@Mutable
	@Shadow @Final public static RenderPhase.Target TRANSLUCENT_TARGET;
	@Mutable
	@Shadow @Final public static RenderPhase.Target ITEM_ENTITY_TARGET;

	@Mutable
	@Shadow @Final public static RenderPhase.Target PARTICLES_TARGET;

	@Mutable
	@Shadow @Final public static RenderPhase.Target WEATHER_TARGET;

	@Mutable
	@Shadow @Final public static RenderPhase.Target CLOUDS_TARGET;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void perspective$overrideTargets(CallbackInfo ci) {
		TRANSLUCENT_TARGET = new RenderPhase.Target("translucent_target", () -> {
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
		ITEM_ENTITY_TARGET = new RenderPhase.Target("item_entity_target", () -> {
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
		PARTICLES_TARGET = new RenderPhase.Target("particles_target", () -> {
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
		WEATHER_TARGET = new RenderPhase.Target("weather_target", () -> {
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
		CLOUDS_TARGET = new RenderPhase.Target("clouds_target", () -> {
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
}