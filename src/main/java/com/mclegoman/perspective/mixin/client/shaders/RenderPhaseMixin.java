/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.shaders;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.shaders.Shader;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = RenderPhase.class)
public class RenderPhaseMixin {
	@Shadow @Final protected String name;
	@Inject(at = @At(value = "RETURN"), method = "startDrawing")
	public void perspective$startDrawing(CallbackInfo ci) {
		if (this.name.equals("translucent_target")) {
			if (Shader.shouldRenderShader() && Shader.translucentFramebuffer != null) Shader.translucentFramebuffer.beginWrite(false);
			if (Shader.shouldRenderEntityLinkShader() && Shader.entityTranslucentFramebuffer != null) for (Framebuffer framebuffer : Shader.entityTranslucentFramebuffer) if (framebuffer != null) framebuffer.beginWrite(false);
		}
		if (this.name.equals("item_entity_target")) {
			if (Shader.shouldRenderShader() && Shader.entityFramebuffer != null) Shader.translucentFramebuffer.beginWrite(false);
			if (Shader.shouldRenderEntityLinkShader() && Shader.entityEntityFramebuffer != null) for (Framebuffer framebuffer : Shader.entityEntityFramebuffer) if (framebuffer != null) framebuffer.beginWrite(false);
		}
		if (this.name.equals("particles_target")) {
			if (Shader.shouldRenderShader() && Shader.particlesFramebuffer != null) Shader.particlesFramebuffer.beginWrite(false);
			if (Shader.shouldRenderEntityLinkShader() && Shader.entityParticlesFramebuffer != null) for (Framebuffer framebuffer : Shader.entityParticlesFramebuffer) if (framebuffer != null) framebuffer.beginWrite(false);
		}
		if (this.name.equals("weather_target")) {
			if (Shader.shouldRenderShader() && Shader.weatherFramebuffer != null) Shader.weatherFramebuffer.beginWrite(false);
			if (Shader.shouldRenderEntityLinkShader() && Shader.entityWeatherFramebuffer != null) for (Framebuffer framebuffer : Shader.entityWeatherFramebuffer) if (framebuffer != null) framebuffer.beginWrite(false);
		}
		if (this.name.equals("clouds_target")) {
			if (Shader.shouldRenderShader() && Shader.cloudsFramebuffer != null) Shader.cloudsFramebuffer.beginWrite(false);
			if (Shader.shouldRenderEntityLinkShader() && Shader.entityCloudsFramebuffer != null) for (Framebuffer framebuffer : Shader.entityCloudsFramebuffer) if (framebuffer != null) framebuffer.beginWrite(false);
		}
	}
	@Inject(at = @At(value = "RETURN"), method = "endDrawing")
	public void perspective$endDrawing(CallbackInfo ci) {
		if (ClientData.minecraft.getFramebuffer() != null) {
			if (Shader.shouldRenderShader() || Shader.shouldRenderEntityLinkShader()) ClientData.minecraft.getFramebuffer().beginWrite(false);
		}
	}
}