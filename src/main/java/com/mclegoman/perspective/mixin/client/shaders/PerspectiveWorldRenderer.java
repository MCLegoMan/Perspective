/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective

    This class was forked from Souper Secret Settings by Nettakrim.
    Licensed under GNU Lesser General Public License v3.0
    https://github.com/Nettakrim/Souper-Secret-Settings
*/

package com.mclegoman.perspective.mixin.client.shaders;

import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.shaders.PerspectiveShader;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class PerspectiveWorldRenderer {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/PostEffectProcessor;render(F)V", ordinal = 1), method = "render")
    public void saveDepthFabulous(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
        PerspectiveShader.DEPTH_FRAME_BUFFER.copyDepthFrom(PerspectiveClientData.CLIENT.getFramebuffer());
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderWorldBorder(Lnet/minecraft/client/render/Camera;)V", ordinal = 1), method = "render")
    public void saveDepthFastFancy(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo ci) {
        PerspectiveShader.DEPTH_FRAME_BUFFER.copyDepthFrom(PerspectiveClientData.CLIENT.getFramebuffer());
        PerspectiveClientData.CLIENT.getFramebuffer().beginWrite(false);
    }
}