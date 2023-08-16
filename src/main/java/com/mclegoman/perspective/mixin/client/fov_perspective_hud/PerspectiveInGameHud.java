/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.fov_perspective_hud;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = InGameHud.class)
public abstract class PerspectiveInGameHud {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void perspective$renderHUD(DrawContext context, float tickDelta, CallbackInfo ci) {
        try {
            if ((Boolean)PerspectiveConfigHelper.getConfig("hide_hud")) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst hiding the InGameHUD.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
}