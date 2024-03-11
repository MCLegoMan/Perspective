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

import java.util.Objects;

@Mixin(priority = 100, value = RenderPhase.class)
public class RenderPhaseMixin {
	@Shadow @Final protected String name;
	@Inject(at = @At(value = "RETURN"), method = "startDrawing")
	public void perspective$startDrawing(CallbackInfo ci) {
		if (Shader.shouldRenderShader()) {
			if (this.name.equals("translucent_target") && Shader.translucentFramebuffer != null) Shader.translucentFramebuffer.beginWrite(false);
			if (this.name.equals("item_entity_target") && Shader.entityFramebuffer != null) Shader.entityFramebuffer.beginWrite(false);
			if (this.name.equals("particles_target") && Shader.particlesFramebuffer != null) Shader.particlesFramebuffer.beginWrite(false);
			if (this.name.equals("weather_target") && Shader.weatherFramebuffer != null) Shader.weatherFramebuffer.beginWrite(false);
			if (this.name.equals("clouds_target") && Shader.cloudsFramebuffer != null) Shader.cloudsFramebuffer.beginWrite(false);
		}
	}
	@Inject(at = @At(value = "RETURN"), method = "endDrawing")
	public void perspective$endDrawing(CallbackInfo ci) {
		if (Shader.shouldRenderShader()) {
			if (ClientData.CLIENT.getFramebuffer() != null) ClientData.CLIENT.getFramebuffer().beginWrite(false);
		}
	}
}