/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.mixin.client.compat;

import com.mclegoman.perspective.client.util.PerspectiveHideHUD;
import com.mclegoman.perspective.common.data.PerspectiveData;
import mcp.mobius.waila.gui.hud.TooltipRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = TooltipRenderer.class)
public class PerspectiveWTHIT {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private static void perspective$renderWTHIT(DrawContext ctx, float delta, CallbackInfo ci) {
        try {
            if (PerspectiveHideHUD.shouldHideHUD()) ci.cancel();
        } catch (Exception e) {
            PerspectiveData.LOGGER.error(PerspectiveData.PREFIX + "An error occurred whilst trying to HideHUD$renderWTHIT.");
            PerspectiveData.LOGGER.error(e.getLocalizedMessage());
        }
    }
}