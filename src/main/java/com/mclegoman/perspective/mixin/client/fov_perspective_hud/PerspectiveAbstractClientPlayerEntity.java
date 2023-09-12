/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.fov_perspective_hud;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.client.data.PerspectiveClientData;
import com.mclegoman.perspective.client.zoom.PerspectiveZoom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = AbstractClientPlayerEntity.class)
public abstract class PerspectiveAbstractClientPlayerEntity {
    @ModifyReturnValue(at = @At("RETURN"), method = "getFovMultiplier")
    private float perspective$getFovMultiplier(float getFovMultiplier) {
        return !PerspectiveClientData.CLIENT.gameRenderer.isRenderingPanorama() && PerspectiveZoom.isZooming() && PerspectiveConfigHelper.getConfig("zoom_mode").equals("smooth") ? (float)PerspectiveZoom.zoomFov(getFovMultiplier) : getFovMultiplier;
    }
}