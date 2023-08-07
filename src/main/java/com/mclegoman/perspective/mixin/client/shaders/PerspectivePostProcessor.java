/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective

    This class was forked from Souper Secret Settings by Nettakrim.
    Licensed under GNU Lesser General Public License v3.0
    https://github.com/Nettakrim/Souper-Secret-Settings
*/

package com.mclegoman.perspective.mixin.client.shaders;

import com.google.gson.JsonElement;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.shaders.PerspectiveShader;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PostEffectProcessor.class)
public class PerspectivePostProcessor {
    private boolean useDepth;

    @Inject(at = @At(value = "INVOKE", target = "Ljava/lang/String;substring(II)Ljava/lang/String;"), method = "parsePass")
    public void detectDepth(TextureManager textureManager, JsonElement jsonPass, CallbackInfo ci) {
        if (PerspectiveShader.DEPTH_FIX) {
            useDepth = true;
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "render")
    public void fixDepth(float tickDelta, CallbackInfo ci) {
        if (useDepth) {
            PerspectiveClientData.CLIENT.getFramebuffer().copyDepthFrom(PerspectiveShader.DEPTH_FRAME_BUFFER);
        }
    }
}